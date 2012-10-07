package edu.american.student.mnemosyne.core.util;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;

public class ClassificationNetwork
{

	public static BasicNetwork constructNetworks(ClassificationNetworkConf conf)
	{
		BasicNetwork network = new BasicNetwork();
		BasicLayer input = new BasicLayer(conf.getInputActivation(), conf.getInputBias(), conf.getInputNeuronCount());
		BasicLayer hidden = new BasicLayer(conf.getHiddenActivation(), conf.getHiddenBias(), conf.getNumberOfCategories());
		BasicLayer output = new BasicLayer(conf.getOutputActivation(), conf.getOutputBias(), conf.getOutputNeuronCount());

		network.addLayer(input);// input layer
		network.addLayer(hidden);// hidden layer
		network.addLayer(output);// output layer;
		network.getStructure().finalizeStructure();
		network.reset();

		return network;
	}

}
