package edu.american.student.mnemosyne.conf;

import org.apache.accumulo.core.security.Authorizations;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Reducer;

import edu.american.student.mnemosyne.core.util.MnemosyneConstants;

public class HadoopJobConfiguration
{

	private String jobName = "DefaultJobName";
	private Class<? extends Mapper<?, ?, ?, ?>> mapperClass;
	private Class<? extends Reducer<?,?,?,?>> reducerClass;
	private Class<? extends InputFormat<?, ?>> inputClass;
	private Class<? extends OutputFormat<?, ?>> outputClass;
	private Class<?> outputKeyClass;
	private Class<?> outputValueClass;
	private int numOfReduceTasks = 0;
	private String defaultTable = MnemosyneConstants.getDefaultTable();
	private Authorizations defaultAuths = new Authorizations(MnemosyneConstants.getDefaultAuths());

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

	public void overrideDefaultTable(String defaultTable)
	{
		this.defaultTable = defaultTable;
	}
	
	public String getDefaultTable()
	{
		return this.defaultTable;
	}

	public void setDefaultAuths(Authorizations auths)
	{
		this.defaultAuths = auths;
	}
	public Authorizations getDefaultAuths()
	{
		return this.defaultAuths;
	}

	public Class<? extends Reducer<?,?,?,?>> getReducerClass()
	{
		return this.reducerClass;
	}
	
	public void setReducerClass(Class<? extends Reducer<?,?,?,?>> reducerClass)
	{
		this.reducerClass = reducerClass;
	}

}
