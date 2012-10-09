package edu.american.student.mnemosyne.core.util;

import java.io.IOException;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;

public class HadoopForeman
{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Job getHadoopJob(HadoopJobConfiguration conf) throws IOException, InterruptedException, ClassNotFoundException
	{
		Job job = new Job();
		job.setJobName(conf.getJobName());


		job.setMapperClass(conf.getMapperClass());
		job.setInputFormatClass((Class<? extends InputFormat>) conf.getInputFormatClass());
		
		if(conf.getOutputFormatClass() != null)
		{
			job.setOutputFormatClass((Class<? extends OutputFormat>) conf.getOutputFormatClass());
		}
		if(conf.getOutputKeyClass() != null)
		{
			job.setOutputKeyClass(conf.getOutputKeyClass());
		}
		if(conf.getOutputValueClass() != null)
		{
			job.setOutputValueClass(conf.getOutputValueClass());
		}
		if(conf.getReducerClass() !=null)
		{
			job.setReducerClass(conf.getReducerClass());
		}
		
		job.setNumReduceTasks(conf.getNumReduceTasks());
		Configuration conf1 = job.getConfiguration();
		if(conf.getInputFormatClass() == AccumuloInputFormat.class)
		{
			AccumuloInputFormat.setInputInfo(conf1, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes(),  conf.getDefaultTable(), conf.getDefaultAuths());
			AccumuloInputFormat.setZooKeeperInstance(conf1,MnemosyneConstants.getZookeeperInstanceName(), MnemosyneConstants.getZookeeperInstance());
		
		}
		else if(conf.getInputFormatClass() == TextInputFormat.class)
		{
			if(conf.getPathToProcess() != null)
			{
				FileInputFormat.setInputPaths(job, conf.getPathToProcess());
			}
		}
		if(conf.getOutputFormatClass() == AccumuloOutputFormat.class)
		{
			AccumuloOutputFormat.setOutputInfo(conf1, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes(), true, conf.getDefaultTable());
			AccumuloOutputFormat.setZooKeeperInstance(conf1,MnemosyneConstants.getZookeeperInstanceName(), MnemosyneConstants.getZookeeperInstance());
			
		}
		return job;
	}

	public boolean runJob(HadoopJobConfiguration hConfig) throws IOException, InterruptedException, ClassNotFoundException
	{
		return this.getHadoopJob(hConfig).waitForCompletion(true);
		
	}
}
