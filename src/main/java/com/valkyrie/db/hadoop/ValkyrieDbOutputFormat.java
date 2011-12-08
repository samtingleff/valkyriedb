package com.valkyrie.db.hadoop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.valkyrie.db.server.KratiLocalStore;
import com.valkyrie.db.util.FileUtils;

public class ValkyrieDbOutputFormat<K> extends
		FileOutputFormat<K, BytesWritable> {

	@Override
	public RecordWriter<K, BytesWritable> getRecordWriter(
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		Path path = getDefaultWorkFile(context, ".krati.tar.gz");
		try {
			return new KratiRecordWriter(context, path);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private class KratiRecordWriter extends RecordWriter<K, BytesWritable> {

		protected TaskAttemptContext context;

		protected Path output;

		protected KratiLocalStore krati;

		protected File localParentDir;

		protected File localDataStoreDir;

		public KratiRecordWriter(TaskAttemptContext context, Path output)
				throws Exception {
			this.context = context;
			this.output = output;
			initLocalStore();
			writePartitionId();
		}

		@Override
		public void write(K key, BytesWritable value) throws IOException,
				InterruptedException {
			try {
				writeToLocalPersistence(key, value);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			try {
				closeLocalStore();
				writeLocalStoreToDFS();
			} finally {
				deleteLocalStore();
			}
		}

		protected void writeToLocalPersistence(K key, BytesWritable value)
				throws Exception {
			byte[] serializedValue = (value == null) ? new byte[0] : value.getBytes();
			krati.set(key.toString().getBytes(), serializedValue);
		}

		protected void initLocalStore() throws Exception {
			String localTempDir = context.getConfiguration().get("valkyrie.hadoop.tmp.dir");
			if (localTempDir == null)
				localTempDir = context.getConfiguration().get("hadoop.tmp.dir");
			this.localParentDir = FileUtils.createTempDir(
					new File(localTempDir),
					"krati-temp",
					".krati");
			this.localDataStoreDir = new File(localParentDir, "index.new");
			krati = new KratiLocalStore(localDataStoreDir, context.getConfiguration());
		}

		protected void writePartitionId() throws Exception {
			int id = context.getTaskAttemptID().getTaskID().getId();
			Properties props = new Properties();
			props.setProperty("partition.id", new Integer(id).toString());
			File f = new File(this.localDataStoreDir, "partition.properties");
			FileOutputStream fos = new FileOutputStream(f);
			try {
				props.store(fos, null);
			} finally {
				fos.close();
			}
		}

		protected void closeLocalStore() throws IOException {
			// this will call sync() on the underlying data store
			krati.close();
		}

		protected void writeLocalStoreToDFS() throws IOException {
			FileSystem fs = FileSystem.get(this.context.getConfiguration());
			FSDataOutputStream out = fs.create(output, false);
			FileUtils.writeTarGz(this.localDataStoreDir, out);
		}

		protected void deleteLocalStore() throws IOException {
			FileUtils.rmrf(this.localParentDir);
		}
	}
}
