package edu.american.student.mnemosyne.core.util;

public class MnemosyneAccumuloAdministrator
{

	public static void main(String[] args)
	{
		MnemosyneAccumuloAdministrator.setup();
	}
	public static void setup()
	{
		AccumuloForeman aForeman = new AccumuloForeman();
		aForeman.connect();
		aForeman.deleteTables();
		aForeman.makeTable("BASE_NETWORK");
		aForeman.makeTable("ARTIFACT_TABLE");
	}
}
