package edu.american.student.mnemosyne.core;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.encog.neural.networks.BasicNetwork;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.foreman.ArtifactForeman;
import edu.american.student.mnemosyne.core.util.foreman.MnemosyneAccumuloAdministrator;
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
		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		for(Artifact artifact:artifacts)
		{
			System.out.println(artifact.getArtifactId());
			BasicNetwork network =aForeman.getBaseNetwork(artifact.getArtifactId());
			// train the neural network
			ClassificationNetworkConf conf= aForeman.getBaseNetworkConf(artifact.getArtifactId());
			assertNotNull("conf is null",conf);
			assertNotNull(network);
		
		}
	}
			


}
