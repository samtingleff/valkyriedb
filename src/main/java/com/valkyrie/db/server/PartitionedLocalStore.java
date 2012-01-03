package com.valkyrie.db.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mtbaker.client.Configuration;
import com.mtbaker.client.ConfigurationClient;
import com.mtbaker.client.annotations.Configurable;
import com.mtbaker.client.annotations.ConfigurableField;
import com.mtbaker.client.annotations.ConfigurationInjector;

@Configurable("krati")
public class PartitionedLocalStore {
	private Log log = LogFactory.getLog(getClass());

	@ConfigurableField(value="dirs", type=String.class)
	private List<String> stringDirs;

	private ConfigurationClient conf;

	private Map<Integer, KratiLocalPartition> partitions;

	public PartitionedLocalStore(ConfigurationClient conf) {
		this.conf = conf;
	}

	public void init() throws Exception {
		initStorageBackend();
	}

	public void close() {
		log.trace("close()");
		for (Map.Entry<Integer, KratiLocalPartition> e : partitions.entrySet()) {
			try {
				e.getValue().store.close();
			} catch (IOException e1) {
				log.warn("IOException calling store.close()", e1);
			}
		}
	}

	public KratiLocalStore getPartition(Integer partitionId) {
		KratiLocalPartition kp = partitions.get(partitionId);
		return (kp == null) ? null : kp.store;
	}

	protected void initStorageBackend() throws Exception {
		log.trace("initStorageBackend()");
		partitions = new HashMap<Integer, KratiLocalPartition>(stringDirs
				.size());
		for (String dir : stringDirs) {
			File f = new File(dir);
			List<KratiLocalPartition> localPartitions = initDir(f);
			for (KratiLocalPartition p : localPartitions) {
				partitions.put(p.partition, p);
			}
		}
	}

	protected List<KratiLocalPartition> initDir(File dir) throws Exception {
		log.trace("initDir()");
		File[] children = dir.listFiles();
		List<KratiLocalPartition> result = new ArrayList<KratiLocalPartition>(
				children.length);
		for (File child : children) {
			if (!child.isDirectory())
				continue;
			File indexDir = new File(child, "index.current");
			File partitionProperties = new File(indexDir,
					"partition.properties");
			FileInputStream fis = new FileInputStream(partitionProperties);
			Properties props = new Properties();
			try {
				props.load(fis);
				Integer partitionid = new Integer(Integer.parseInt(props
						.getProperty("partition.id")));
				KratiLocalStore ks = new KratiLocalStore();
				ks.setDir(indexDir.getAbsolutePath());
				inject(ks);
				ks.init();
				result.add(new KratiLocalPartition(ks, partitionid));
			} finally {
				fis.close();
			}
		}
		return result;
	}

	private void inject(KratiLocalStore ks) throws IOException {
		ConfigurationInjector injector = new ConfigurationInjector(this.conf);
		injector.inject(ks);
	}

	private static class KratiLocalPartition {

		protected KratiLocalStore store;

		protected Integer partition;

		public KratiLocalPartition(KratiLocalStore store, Integer partition) {
			this.store = store;
			this.partition = partition;
		}
	}
}
