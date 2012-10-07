package edu.american.student.mnemosyne.conf;

import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;

public class HadoopJobConfiguration
{

	private String jobName = "DefaultJobName";
	private Class<? extends Mapper<?, ?, ?, ?>> mapperClass;
	private Class<? extends InputFormat<?, ?>> inputClass;
	private Class<? extends OutputFormat<?, ?>> outputClass;
	private Class<?> outputKeyClass;
	private Class<?> outputValueClass;
	private int numOfReduceTasks = 0;

	public String getJobName()
	{
		return jobName;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	public Class<? extends Mapper<?, ?, ?, ?>> getMapperClass()
	{
		return mapperClass;
	}

	public void setMapperClass(Class<? extends Mapper<?, ?, ?, ?>> mapperClass)
	{
		this.mapperClass = mapperClass;
	}

	public Class<? extends InputFormat<?, ?>> getInputFormatClass()
	{
		return inputClass;
	}

	public void setInputFormatClass(Class<? extends InputFormat<?, ?>> inputClass)
	{
		this.inputClass = inputClass;
	}

	public Class<? extends OutputFormat<?, ?>> getOutputFormatClass()
	{
		return this.outputClass;
	}

	public void setOutputFormatClass(Class<? extends OutputFormat<?, ?>> outputClass)
	{
		this.outputClass = outputClass;
	}

	public Class<?> getOutputKeyClass()
	{
		return outputKeyClass;
	}

	public void setOutputKeyClass(Class<?> outputKeyClass)
	{
		this.outputKeyClass = outputKeyClass;
	}

	public Class<?> getOutputValueClass()
	{
		return outputValueClass;
	}

	public void setOutputValueCLass(Class<?> outputValueClass)
	{
		this.outputValueClass = outputValueClass;
	}

	public void setNumReduceTasks(int i)
	{
		this.numOfReduceTasks = i;
	}

	public int getNumReduceTasks()
	{
		return this.numOfReduceTasks;
	}

}
