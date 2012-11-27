package edu.american.student.mnemosyne.core;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.core.util.foreman.MnemosyneAccumuloAdministrator;
import edu.american.student.mnemosyne.util.TestHelper;

public class VerifyProcessTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		MnemosyneAccumuloAdministrator.setup();
		TestHelper.ingestTestArtifacts();
		TestHelper.buildArtifacts();
		TestHelper.constructCongress();
		TestHelper.trainCongress();
//		TestHelper.constructBaseClassificationNetwork();
//		TestHelper.trainNetworks();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test() throws Exception
	{
//		VerifyProcess pro = new VerifyProcess();
//		pro.setup();
//		pro.process();
	}

}
