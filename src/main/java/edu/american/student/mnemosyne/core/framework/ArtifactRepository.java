package edu.american.student.mnemosyne.core.framework;

import edu.american.student.mnemosyne.core.util.MnemosyneConstants;

public class ArtifactRepository implements AccumuloTable
{

	private String repositoryName = MnemosyneConstants.getArtifactTableName();
	private String rawBytes = MnemosyneConstants.getArtifactTableRawBytes();
	private String artifactEntry = MnemosyneConstants.getArtifactTableArtifactEntry();
	
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
