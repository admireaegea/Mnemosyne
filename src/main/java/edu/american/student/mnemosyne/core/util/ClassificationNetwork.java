package edu.american.student.mnemosyne.core.util;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.neural.flat.FlatNetwork;
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

	public static BasicNetwork addLayerToNetwork(BasicNetwork network, BasicLayer layer)
	{
		FlatNetwork toCopy = network.getStructure().getFlat();
		FlatNetwork toInstall = new FlatNetwork();
		
		//these properties stay the same
		toInstall.setConnectionLimit(toCopy.getConnectionLimit());
		toInstall.setHasContext(toCopy.getHasContext());
		toInstall.setInputCount(toCopy.getInputCount());
		toInstall.setOutputCount(toCopy.getOutputCount());
		toInstall.setBeginTraining(toCopy.getBeginTraining());
		
		
		//Add new stuff before the output layer
		ActivationFunction[] functions =toCopy.getActivationFunctions();
		ActivationFunction[] toInstallFunc = new ActivationFunction[functions.length+1];
		
		double[] toCopyBiasActivations = toCopy.getBiasActivation();
		double[] toInstallBiasActivations = new double[toCopyBiasActivations.length+1];
		
		int[] toCopyTargetOffset = toCopy.getContextTargetOffset();
		int[] toInstallTargetOffset = new int[toCopyTargetOffset.length+1];
		
		int[] toCopyTargetSize = toCopy.getContextTargetSize();
		int[] toInstallTargetSize = new int[toCopyTargetSize.length+1];
		
		int toCopyEndTraining = toCopy.getEndTraining();
		int toInstallEndTraining = toCopyEndTraining+1;
		
		int[] toCopyLayerContextCount = toCopy.getLayerContextCount();
		int[] toInstallLayerContextCount = new int[toCopyLayerContextCount.length+1];
		
		int[] toCopyLayerCounts = toCopy.getLayerCounts();
		int[] toInstallLayerCounts = new int[toCopyLayerCounts.length+1];
		
		//TODO
		toCopy.getLayerFeedCounts();
		toCopy.getLayerIndex();
		toCopy.getLayerSums();
		toCopy.getLayerOutput();
		toCopy.getNeuronCount();
		toCopy.getWeightIndex();
		toCopy.getWeights();
		
		for(int i=0;i<functions.length-1;i++)
		{
			toInstallFunc[i] = functions[i];
			toInstallBiasActivations[i] = toCopyBiasActivations[i];
			toInstallTargetOffset[i] = toCopyTargetOffset[i];
			toInstallTargetSize[i] = toCopyTargetSize[i];
			toInstallLayerContextCount[i] = toCopyLayerContextCount[i];
			toInstallLayerCounts[i] = toCopyLayerCounts[i];
		}
		
		toInstallFunc[functions.length-1] = layer.getActivation();
		toInstallFunc[functions.length] = functions[functions.length-1];
		
		toInstallBiasActivations[toCopyBiasActivations.length-1] = layer.getBiasActivation();
		toInstallBiasActivations[toCopyBiasActivations.length] = toCopyBiasActivations[toCopyBiasActivations.length-1];
		
		toInstallTargetOffset[toCopyTargetOffset.length-1] = toCopyTargetOffset[toCopyTargetOffset.length-2];
		toInstallTargetOffset[toCopyTargetOffset.length] = toCopyTargetOffset[toCopyTargetOffset.length-1];
		
		toInstallTargetSize[toCopyTargetSize.length-1] = toCopyTargetSize[toCopyTargetSize.length-2];
		toInstallTargetSize[toCopyTargetSize.length] = toCopyTargetSize[toCopyTargetSize.length-1];
		
		toInstallLayerContextCount[toCopyLayerContextCount.length-1] = toCopyLayerContextCount[toCopyLayerContextCount.length-2];
		toInstallLayerContextCount[toCopyLayerContextCount.length] = toCopyLayerContextCount[toCopyLayerContextCount.length-1];
		
		
		toInstallLayerCounts[toCopyBiasActivations.length-1] =layer.getContextCount();
		toInstallLayerCounts[toCopyBiasActivations.length] = toCopyLayerContextCount[toCopyLayerContextCount.length-1];
		
		
		toInstall.setBiasActivation(toInstallBiasActivations);
		toInstall.setActivationFunctions(toInstallFunc);
		toInstall.setContextTargetOffset(toInstallTargetOffset);
		toInstall.setContextTargetSize(toInstallTargetSize);
		toInstall.setEndTraining(toInstallEndTraining);
		toInstall.setLayerContextCount(toInstallLayerContextCount);

		
		compare(toCopy,toInstall);
		return network;
		
	}

	/**
	 * XXX: for debug
	 * @param toCopy
	 * @param toInstall
	 */
	private static void compare(FlatNetwork toCopy, FlatNetwork toInstall)
	{
		// TODO Auto-generated method stub
		
	}
}
