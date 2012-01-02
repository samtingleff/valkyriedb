package com.valkyrie.db.hadoop.example;

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.valkyrie.db.hadoop.Murmur3HadoopPartitioner;
import com.valkyrie.db.hadoop.ValkyrieDbOutputFormat;

public class RandomDataCreatingHadoopJob extends Configured implements Tool {

	public static int main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new RandomDataCreatingHadoopJob(), args);
		return res;
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();

		Job job = new Job(conf, "example-job");
		job.setJarByClass(getClass());
		job.setMapperClass(ExampleMapper.class);
		job.setReducerClass(ExampleReducer.class);
		job.setPartitionerClass(Murmur3HadoopPartitioner.class);
		job.setNumReduceTasks(12); // should match valkyrie.partitions.count

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(BytesWritable.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(ValkyrieDbOutputFormat.class);

		FileInputFormat.addInputPaths(job, args[0]);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class ExampleMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
		private Random random = new Random();

		private int numKeys = 10;

		public void setup(Context context) throws InterruptedException, IOException {
			numKeys = context.getConfiguration().getInt("num.keys", 10000);
			for (int i = 0; i < numKeys; ++i) {
				context.write(new IntWritable(i), new IntWritable(random.nextInt(1000)));
			}
		}

		public void map(LongWritable key, Text value, Context context)
				throws InterruptedException, IOException {
		}
	}

	public static class ExampleReducer extends Reducer<IntWritable, IntWritable, IntWritable, BytesWritable> {
		public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
				throws InterruptedException, IOException {
			long sum = 0;
			for (IntWritable v : values) {
				sum += v.get();
			}
			context.write(key, new BytesWritable(Long.toString(sum).getBytes()));
		}
	}
}
