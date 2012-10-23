package edu.american.student.mnemosyne.core.util;

import edu.american.student.mnemosyne.core.exception.DataspaceException;

public class MnemosyneAccumuloAdministrator
{

	public static void main(String[] args) throws DataspaceException
	{
		MnemosyneAccumuloAdministrator.setup();
	}
	public static void setup() throws DataspaceException
	{
		AccumuloForeman aForeman = new AccumuloForeman();
		aForeman.connect();
		aForeman.deleteTables();
		aForeman.makeTable(AccumuloForeman.getBaseNetworkRepositoryName());
		aForeman.makeTable(AccumuloForeman.getArtifactRepositoryName());
	}
}
