package edu.american.student.mnemosyne.conf;

import org.apache.accumulo.core.security.Authorizations;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import edu.american.student.mnemosyne.core.util.MnemosyneConstants;

public class HadoopJobConfiguration
{

	private String jobName = "DefaultJobName";
	private Class<? extends Mapper<?, ?, ?, ?>> mapperClass;
	private Class<? extends Reducer<?,?,?,?>> reducerClass;
	private Class<?> inputClass;
	private Class<?> outputClass;
	private Class<?> outputKeyClass;
	private Class<?> outputValueClass;
	private int numOfReduceTasks = 0;
	private String defaultTable = MnemosyneConstants.getDefaultTable();
	private Authorizations defaultAuths = new Authorizations(MnemosyneConstants.getDefaultAuths());
	private Path pathToProcess = null;
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

	public Class<?> getInputFormatClass()
	{
		return inputClass;
	}

	public void setInputFormatClass(Class<?> inputClass)
	{
		this.inputClass = inputClass;
	}

	public Class<?> getOutputFormatClass()
	{
		return this.outputClass;
	}

	public void setOutputFormatClass(@SuppressWarnings("rawtypes") Class class1)
	{
		this.outputClass = class1;
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

	public static String buildJobName(Class<?> className)
	{
		return className.getSimpleName();
	}

	public void overridePathToProcess(Path path)
	{
		this.pathToProcess = path;
	}
	
	public Path getPathToProcess()
	{
		return this.pathToProcess;
	}

}
