package edu.american.student.mnemosyne.core.util;

import java.io.IOException;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;

public class HadoopForeman
{

	public Job getHadoopJob(HadoopJobConfiguration conf) throws IOException, InterruptedException, ClassNotFoundException
	{
		Job job = new Job();
		job.setJobName(conf.getJobName());


		job.setMapperClass(conf.getMapperClass());
		job.setInputFormatClass(conf.getInputFormatClass());
		
		job.setOutputFormatClass(conf.getOutputFormatClass());
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
