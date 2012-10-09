package edu.american.student.mnemosyne.core.framework;

public class BaseNetworkRepository implements AccumuloTable
{
	private String repositoryName = "BASE_NETWORK";
	private String rawBytesField = "RAW_BYTES";
	@Override
	public String toString()
	{
		return repositoryName;
	}
	
	public String getRawBytesField()
	{
		return rawBytesField;
	}
}
