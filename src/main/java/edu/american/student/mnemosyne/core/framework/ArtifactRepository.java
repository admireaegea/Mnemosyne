package edu.american.student.mnemosyne.core.framework;

public class ArtifactRepository implements AccumuloTable
{

	private String repositoryName = "ARTIFACT_TABLE";

	@Override
	public String toString()
	{
		return repositoryName;
	}
}
