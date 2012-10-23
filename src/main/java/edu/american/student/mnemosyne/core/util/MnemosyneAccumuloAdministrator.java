package edu.american.student.mnemosyne.core.util;

import edu.american.student.mnemosyne.core.exception.RepositoryException;

public class MnemosyneAccumuloAdministrator
{

	public static void main(String[] args) throws RepositoryException
	{
		MnemosyneAccumuloAdministrator.setup();
	}
	public static void setup() throws RepositoryException
	{
		AccumuloForeman aForeman = new AccumuloForeman();
		aForeman.connect();
		aForeman.deleteTables();
		aForeman.makeTable(AccumuloForeman.getBaseNetworkRepositoryName());
		aForeman.makeTable(AccumuloForeman.getArtifactRepositoryName());
	}
}
