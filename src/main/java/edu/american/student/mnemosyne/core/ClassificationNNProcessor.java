package edu.american.student.mnemosyne.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.accumulo.core.client.TableNotFoundException;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.model.Node;
import edu.american.student.mnemosyne.core.util.MnemosyneConstants;

public class ClassificationNNProcessor extends NNProcessor
{

	private ClassificationNetworkConf conf;
	private List<Node> nodes;
	private double[][] interInput ={{.4,.4},{.6,.4},{.4,.6},{.6,.6}};
	public ClassificationNNProcessor(ClassificationNetworkConf conf2)
	{
		this.conf = conf2;
	}

	@Override
	public void constructNetworks() throws IOException, TableNotFoundException, ClassNotFoundException
	{
		int numOfNodes = MnemosyneConstants.getNumberOfNodes();
		nodes = new ArrayList<Node>();
		for(int i=0;i<numOfNodes;i++)
		{
			Node toAdd = new Node();
			toAdd.setConfigType(conf.getClass().getSimpleName());
			toAdd.setConfig(conf);
			toAdd.constructNeuralNetworks(conf);
			nodes.add(toAdd);
		}
		
	}
//
//	public void testRun() throws TableNotFoundException, IOException, ClassNotFoundException
//	{
//		AccumuloForeman aForeman = new AccumuloForeman();
//		aForeman.connect();
//		int id =0;
//		Node lastNode = null;
//		for(Node node: nodes)
//		{
//			List<BasicNetwork> networks =aForeman.inflateNetworks(node);
//			System.out.println("### NODE #"+node.getName());
//			long start = System.currentTimeMillis();
//			long elapsed = System.currentTimeMillis() -start;
//			int epoch = 1;
//			while(epoch <20000000)
//			{
//				elapsed = System.currentTimeMillis() -start;
//				networks =aForeman.inflateNetworks(node);
//				for(BasicNetwork network:networks)
//				{
//					MLDataSet trainingSet =null;
//					
//					if(lastNode == null)
//					{
//						trainingSet= new BasicMLDataSet(MnemosyneConstants.getTestInput(),interInput);
//						lastNode = node;
//					}
//					else
//					{
//						trainingSet = new BasicMLDataSet(edit(epoch),MnemosyneConstants.getTestIdeal());
//					}
//					// train the neural network
//					ResilientPropagation train = new ResilientPropagation(network, trainingSet);
//
//					
//					do
//					{
//						train.iteration();
//						System.out.println("Epoch #" + epoch + " Error:" + train.getError());
//						if((1.0/epoch)< train.getError())
//						{
//		
//							trainingSet = new BasicMLDataSet(interInput,MnemosyneConstants.getTestIdeal());
//							train.setTraining(trainingSet);
//						}
//						epoch++;
//					}
//					while (train.getError() >  .000005/epoch);
//					aForeman.saveNetwork(node, network, id);
//					// test the neural network
//					System.out.println("Neural Network Results:");
//					for (MLDataPair pair : trainingSet)
//					{
//						final MLData outputD = network.compute(pair.getInput());
//						System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1) + ", actual=" + outputD.getData(0) +" "+outputD.getData(1)+ ",ideal=" + pair.getIdeal().getData(0)+ " "+pair.getIdeal().getData(1) );
//					}
//					id++;
//				}
//			}
//			
//		}
//		
//	}
//	private double[][] edit(int epoch)
//	{
//		for(int i=0;i<interInput.length;i++)
//		{
//			for(int j=0;j<interInput[i].length;j++)
//			{
//				double random = new Random().nextDouble()/epoch;
//				if(interInput[i][j]+random >=1 && interInput[i][j]-random >0 )
//				{
//					interInput[i][j]-=random;
//				}
//				else if (interInput[i][j]+random < 1)
//				{
//					interInput[i][j]+=random;
//				}
//			}
//		}
//		return interInput;
//	}

	@Override
	public void assignNodes()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process()
	{
		// TODO Auto-generated method stub
	}

}
