package edu.american.student.mnemosyne.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.core.util.MnemosyneAccumuloAdministrator;
import edu.american.student.mnemosyne.util.TestHelper;

public class ArtifactBuilderProcessTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		MnemosyneAccumuloAdministrator.setup();
		TestHelper.constructTestClassificationNetwork();
		TestHelper.ingestTestArtifacts();
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
		ArtifactBuilderProcess pro = new ArtifactBuilderProcess();
		pro.setup();
		pro.process();
	}

}
