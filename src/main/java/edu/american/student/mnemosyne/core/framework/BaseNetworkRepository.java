package edu.american.student.mnemosyne.core.framework;

public class BaseNetworkRepository implements AccumuloTable
{
	private String repositoryName = "BASE_NETWORK";
	private String rawBytesField = "RAW_BYTES";
	private String baseConfiguration = "BASE_CONFIGURATION";
	private String baseError = "BASE_ERROR";
	private String trainData ="TRAIN_DATA";
	private String baseNetwork = "BASE_NETWORK";
	@Override
	public String toString()
	{
		return repositoryName;
	}
	
	public String getRawBytesField()
	{
		return rawBytesField;
	}

	public String baseConfiguration()
	{
		return baseConfiguration;
		
	}
	
	public String baseError()
	{
		return baseError;
	}

	public String trainData()
	{
		return trainData;
	}

	public String baseNetwork()
	{
		return baseNetwork;
	}
}
