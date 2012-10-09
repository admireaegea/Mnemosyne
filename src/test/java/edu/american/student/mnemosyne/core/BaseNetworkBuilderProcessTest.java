package edu.american.student.mnemosyne.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ArtifactForeman;
import edu.american.student.mnemosyne.core.util.MnemosyneAccumuloAdministrator;
import edu.american.student.mnemosyne.util.TestHelper;

public class BaseNetworkBuilderProcessTest
{
	static AccumuloForeman aForeman = new AccumuloForeman();
	static ArtifactForeman artifactForeman = new ArtifactForeman();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		MnemosyneAccumuloAdministrator.setup();
		TestHelper.ingestTestArtifacts();
		TestHelper.buildArtifacts();
		TestHelper.constructBaseClassificationNetwork();
		aForeman.connect();
		artifactForeman.connect();

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
		String artifactId =aForeman.fetchByColumnFamily(AccumuloForeman.getArtifactRepositoryName(), "RAW_BYTES").get(0).getKey().getRow().toString();
		assertNotNull(aForeman.getBaseNetwork(artifactId));
	}

}
