package edu.american.student.mnemosyne.core.framework;

public class ArtifactRepository implements AccumuloTable
{

	private String repositoryName = "ARTIFACT_TABLE";
	private String rawBytes = "RAW_BYTES";
	private String artifactEntry = "ARTIFACT_ENTRY";
	
	@Override
	public String toString()
	{
		return repositoryName;
	}

	public String rawBytes()
	{
		return rawBytes;
	}

	public String artifactEntry()
	{
		return artifactEntry;
	}
}
