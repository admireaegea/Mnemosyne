package edu.american.student.mnemosyne.core;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import edu.american.student.mnemosyne.conf.BasicNetworkConf;

public class Test
{
	/**
	 * The input necessary for XOR.
	 */
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for XOR.
	 */
	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };
	public static void main(String[] args)
	{
		BasicNetworkConf conf = new BasicNetworkConf();
		conf.setInputActivation(null);
		conf.setInputNeuronCount(2);
		conf.setInputBias(true);
		conf.setHiddenActiviation(new ActivationSigmoid());
		conf.setHiddenNeuronCount(4);
		conf.setHiddenBias(true);
		conf.setOutputActivation(new ActivationSigmoid());
		conf.setOutputNeuronCount(1);
	
		
		conf.setBasicMLInput(XOR_INPUT);
		conf.setBasicIdealMLOutput(XOR_IDEAL);
		new Test(conf);
	}
	public Test(BasicNetworkConf conf)
	{
		BasicNetwork network = new BasicNetwork();
		BasicLayer input = new BasicLayer(conf.getInputActivation(), conf.getInputBias(), conf.getInputNeuronCount());
		BasicLayer hidden = new BasicLayer(conf.getHiddenActivation(), conf.getHiddenBias(), conf.getHiddenNeuronCount());
		BasicLayer output = new BasicLayer(conf.getOutputActivation(), conf.getOutputBias(), conf.getOutputNeuronCount());

		network.addLayer(input);// input layer
		network.addLayer(hidden);// hidden layer
		network.addLayer(output);// output layer
		// input.setContextFedBy(hidden);
		network.getStructure().finalizeStructure();
		network.reset();

		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(conf.getBasicMLInput(), conf.getBasicIdealOutput());

		// train the neural network
		final Propagation train = new ResilientPropagation(network, trainingSet);

		int epoch = 1;

		do
		{
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		}
		while(train.getError() > conf.getErrorBound());
//		while (train.getError() > 0.2204e-10);

		// test the neural network
		System.out.println("Neural Network Results:");
		for (MLDataPair pair : trainingSet)
		{
			final MLData outputD = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1) + ", actual=" + outputD.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		}
		for (int i = 0; i < conf.getBasicMLInput().length; i++)
		{
			System.out.println("for input " + print(conf.getBasicMLInput()[i]));
			MLData result = network.compute(new BasicMLData(conf.getBasicMLInput()[i]));
			System.out.println("it found this result " + Math.round(result.getData(0)));
		}
		Encog.getInstance().shutdown();
	}
	
	private static String print(double[] ds)
	{
		StringBuilder b = new StringBuilder();
		for(int i=0;i<ds.length;i++)
		{
			b.append(ds[i]+" ");
		}
		return b.toString();
	}
}
