package com.valkyrie.db.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SequenceFileToValkyrieDb extends Configured implements Tool {

	public static int main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new SequenceFileToValkyrieDb(), args);
		return res;
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();

		Job job = new Job(conf, "sequence-file-to-valkyrie-db");
		job.setJarByClass(getClass());

		job.setMapperClass(Mapper.class);
		job.setReducerClass(LastValueReducer.class);
		job.setPartitionerClass(FNVHadoopPartitioner.class);

		// This needs to match valkyrie.partitions.count.
		// Hopefully this is set on the command line: -Dmapred.reduce.tasks=12
		// job.setNumReduceTasks(12);

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(BytesWritable.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(BytesWritable.class);

		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(ValkyrieDbOutputFormat.class);

		FileInputFormat.addInputPaths(job, args[0]);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class LastValueReducer extends Reducer<IntWritable, BytesWritable, IntWritable, BytesWritable> {
		public void reduce(IntWritable key, Iterable<BytesWritable> values, Context context)
				throws InterruptedException, IOException {
			BytesWritable bw = null;
			for (BytesWritable value : values) {
				bw = value;
			}
			// pick the last!
			context.write(key, bw);
		}
	}
}
