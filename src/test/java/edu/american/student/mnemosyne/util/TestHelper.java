package edu.american.student.mnemosyne.util;

import java.io.IOException;

import org.apache.accumulo.core.client.TableNotFoundException;
import org.encog.engine.network.activation.ActivationSigmoid;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.NNProcessorFactory;

public class TestHelper
{

	public static void constructTestClassificationNetwork() throws IOException, TableNotFoundException, ClassNotFoundException
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
		NNProcessor processor = NNProcessorFactory.getProcessorBean(conf);
		processor.constructNetworks();
		
	}

	
	public static void ingestTestArtifacts() throws Exception
	{
		AccumuloForeman aForeman = new AccumuloForeman();
		aForeman.connect();
		aForeman.add("ARTIFACT_TABLE", "ROW", "FAM", "QUAL", "VALUE");
	}
	
}
