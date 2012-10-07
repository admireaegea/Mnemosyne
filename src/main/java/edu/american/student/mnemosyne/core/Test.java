package edu.american.student.mnemosyne.core;
import java.io.IOException;

import org.apache.accumulo.core.client.TableNotFoundException;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import edu.american.student.mnemosyne.core.model.Node;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;

public class Test
{
//
//	public static double XOR_INPUT[][] =
//	{
//	{ 0.0, 0.0, 0.0 },
//	{ 0.0, 0.0, 1.0 },
//	{ 0.0, 1.0, 0.0 },
//	{ 0.0, 1.0, 1.0 },
//	{ 1.0, 0.0, 0.0 },
//	{ 1.0, 0.0, 1.0 },
//	{ 1.0, 1.0, 0.0 },
//	{ 1.0, 1.0, 1.0 } };
//
//	/**
//	 * The ideal data necessary for XOR.
//	 */
//	public static double XOR_IDEAL[][] =
//	// 0 1 1 0 1 0 0 1
//	{
//	{ 1.0 },
//	{ 1.0 },
//	{ 1.0 },
//	{ 1.0 },
//	{ 1.0 },
//	{ 1.0 },
//	{ 1.0 },
//	{ 0.0 } };
//
//	public static void main(String[] args) throws IOException, TableNotFoundException, ClassNotFoundException
//	{
//		BasicNetwork network = new BasicNetwork();
//		BasicLayer input = new BasicLayer(null, true, 3);
//		BasicLayer hidden = new BasicLayer(new ActivationSigmoid(), true, 16);
//		BasicLayer output = new BasicLayer(new ActivationSigmoid(), false, 1);
//
//		network.addLayer(input);// input layer
//		network.addLayer(hidden);// hidden layer
//		network.addLayer(output);// output layer
//		// input.setContextFedBy(hidden);
//		network.getStructure().finalizeStructure();
//		network.reset();
//
//		// create training data
//		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
//
//		// train the neural network
//		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
//
//		int epoch = 1;
//
//		do
//		{
//			train.iteration();
//			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
//			epoch++;
//		}
//		while (train.getError() >  0.2204e-10);
//
//		// test the neural network
//		System.out.println("Neural Network Results:");
//		for (MLDataPair pair : trainingSet)
//		{
//			final MLData outputD = network.compute(pair.getInput());
//			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1) + ", actual=" + outputD.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
//		}
//		for(int i=0;i<XOR_INPUT.length;i++)
//		{
//			System.out.println("for input "+print(XOR_INPUT[i]));
//			MLData result =network.compute(new BasicMLData(XOR_INPUT[i]));
//			System.out.println("it found this result "+Math.round(result.getData(0)));
//		}
//		AccumuloForeman aForeman = new AccumuloForeman();
//		aForeman.connect();
//		Node exampleNode = new Node();
//		exampleNode.setConfigType("basic");
//		
//		aForeman.deleteTables();
//		aForeman.makeTable(exampleNode.getName());
//		aForeman.saveNetwork(exampleNode, network,0);
//		
//		network = aForeman.inflateNetworks(exampleNode);
//		for(int i=0;i<XOR_INPUT.length;i++)
//		{
//			System.out.println("for input "+print(XOR_INPUT[i]));
//			MLData result =network.compute(new BasicMLData(XOR_INPUT[i]));
//			System.out.println("it found this result "+Math.round(result.getData(0)));
//		}
//		Encog.getInstance().shutdown();
//
//	}
//
//	private static String print(double[] ds)
//	{
//		StringBuilder b = new StringBuilder();
//		for(int i=0;i<ds.length;i++)
//		{
//			b.append(ds[i]+" ");
//		}
//		return b.toString();
//	}
}
