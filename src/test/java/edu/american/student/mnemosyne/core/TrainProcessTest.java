package edu.american.student.mnemosyne.core;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.core.util.MnemosyneAccumuloAdministrator;
import edu.american.student.mnemosyne.util.TestHelper;

public class TrainProcessTest
{
	
	@BeforeClass
	public static void beforeClass() throws Exception
	{
		MnemosyneAccumuloAdministrator.setup();
		TestHelper.constructTestClassificationNetwork();
		TestHelper.ingestTestArtifacts();
		TestHelper.buildArtifacts();
	}
	@Test
	public void test() throws Exception
	{
		TrainProcess pro = new TrainProcess();
		pro.process();
	}


}
