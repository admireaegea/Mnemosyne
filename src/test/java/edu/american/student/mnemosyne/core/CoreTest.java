package edu.american.student.mnemosyne.core;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.NNProcessorBeanFactory;

public class CoreTest
{

	private static AccumuloForeman aForeman;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		aForeman = new AccumuloForeman();
		aForeman.connect();
		aForeman.deleteTables();
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
		int inputNeuronCount =2;
		int num = 4;
		
		ClassificationNetworkConf conf = new ClassificationNetworkConf();
		conf.setInputActivation(null);
		conf.setInputBias(true);
		conf.setInputNeuronCount(inputNeuronCount);
		
		conf.setHiddenActiviation(new ActivationSigmoid());
		conf.setHiddenBias(true);
		conf.setHiddenNeuronCount(2^num);
		
		conf.setOutputActivation(new ActivationSigmoid());
		conf.setOutputNeuronCount(2);
		
		conf.setNumberOfCategories(num);
		
		NNProcessor processor = NNProcessorBeanFactory.getProcessorBean(conf);
		processor.constructNetworks();
		//((ClassificationNNProcessor)processor).testRun();
		
	}

}
