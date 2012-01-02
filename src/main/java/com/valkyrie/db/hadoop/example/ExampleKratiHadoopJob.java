package com.valkyrie.db.hadoop.example;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
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

public class ExampleKratiHadoopJob extends Configured implements Tool {

	public static int main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new ExampleKratiHadoopJob(), args);
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

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BytesWritable.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(ValkyrieDbOutputFormat.class);

		FileInputFormat.addInputPaths(job, args[0]);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class ExampleMapper extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context)
				throws InterruptedException, IOException {
			String[] split = value.toString().split("\t");
			context.write(new Text(split[0]), new Text(split[1]));
		}
	}

	public static class ExampleReducer extends Reducer<Text, Text, Text, BytesWritable> {
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws InterruptedException, IOException {
			long count = 0;
			for (Text text : values) {
				++count;
			}
			context.write(key, new BytesWritable(Long.toString(count).getBytes()));
		}
	}
}
