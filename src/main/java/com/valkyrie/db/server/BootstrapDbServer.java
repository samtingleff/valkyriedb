package com.valkyrie.db.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.mtbaker.client.Configuration;
import com.mtbaker.client.provider.properties.PropertiesConfigurationClient;
import com.valkyrie.db.util.FileUtils;
import com.valkyrie.db.util.KeyPartitionerFactory;
import com.valkyrie.db.util.SimpleKeyPartitioner;

public class BootstrapDbServer extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new org.apache.hadoop.conf.Configuration(), new BootstrapDbServer(), args);
		//return res;
	}

	private Configuration conf;

	private FileSystem fs;

	@Override
	public int run(String[] args) throws Exception {
		initConfiguration();
		load(args[0]);
		return 0;
	}

	protected void load(String root) throws Exception {
		List<Integer> partitions = getMyPartitionList();
		fs = FileSystem.get(getConf());
		Path rootPath = new Path(root);
		FileStatus[] files = fs.listStatus(rootPath);
		File localTempDir = new File(conf.getString("valkyrie.server.temp.dir", "/mnt/valkyriedb/tmp"));
		localTempDir.mkdirs();
		List<String> localdirs = conf.getStringList("valkyrie.server.dirs", Collections.singletonList("/mnt/valkyriedb"));
		int dirIndex = 0;
		for (FileStatus f : files) {
			Path p = f.getPath();
			Matcher m = Pattern.compile(".*-([0-9]+).krati.tar.gz").matcher(p.getName());
			if (m.matches()) {
				int partition = Integer.parseInt(m.group(1));
				if (partitions.contains(new Integer(partition))) {
					File tempFile = File.createTempFile("partition-" + partition, ".valkyriedb.tar.gz", localTempDir);
					File localDir = new File(localdirs.get(dirIndex));
					File partitionLocalDir = new File(localDir, Integer.toString(partition));
					partitionLocalDir.mkdirs();
					download(p, tempFile);
					extract(tempFile, partitionLocalDir);
					tempFile.delete();
					swapLocalDataStore(partitionLocalDir);
					++dirIndex;
					if (dirIndex == localdirs.size())
						dirIndex = 0;
				}
			}
		}
	}

	protected void download(Path p, File output) throws IOException {
		Path dst = new Path(output.getAbsolutePath());
		fs.copyToLocalFile(p, dst);
	}

	protected void extract(File f, File localdir) throws IOException, InterruptedException {
		Runtime r = Runtime.getRuntime();
		r.exec(new String[] { "tar", "xzf", f.getAbsolutePath()}, null, localdir).waitFor();
	}

	protected void swapLocalDataStore(File parent) {
		try {
			File current = new File(parent, "index.current");
			File old = new File(parent, "index.old");
			try {
				FileUtils.rmrf(old);
			} catch(Exception e) { }
			current.renameTo(old);
		} catch(Exception e) { }
		File child = new File(parent, "index.new");
		child.renameTo(new File(parent, "index.current"));
	}

	protected List<Integer> getMyPartitionList() throws Exception {
		SimpleKeyPartitioner partitioner = KeyPartitionerFactory.createKeyPartitioner(this.conf);
		return partitioner.getPartitionList(InetAddress.getLocalHost().getHostName());
	}

	protected void initConfiguration() throws IOException {
		Properties props = new Properties();
		InputStream is = new FileInputStream("/etc/valkyrie.server.properties");
		try {
			props.load(is);
			PropertiesConfigurationClient client = new PropertiesConfigurationClient(
					props);
			conf = client.getConfiguration(null, 10);
		} finally {
			is.close();
		}
	}
}
