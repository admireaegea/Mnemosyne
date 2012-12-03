package edu.american.student.mnemosyne.core.model;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.american.student.mnemosyne.conf.NeuronConf;


public class Neuron implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double threshold = 0.0;
	private double learningRate = 0.0;
	private double error =1;
	private List<Double> weights = new Vector<Double>();
	private static final Logger log = LoggerFactory.getLogger(Neuron.class);
	private String hash = "";
	
	public Neuron(NeuronConf conf)
	{
		this.threshold = conf.getThreshold();
		this.learningRate = conf.getLearningRate();
		hash = new String((System.currentTimeMillis()+UUID.randomUUID().toString()));
	}
	
	public String getHash()
	{
		return hash;
	}
	public void setup(int numberOfInputs)
	{
		for(int i=0;i<numberOfInputs;i++)
		{
			weights.add(new Random().nextDouble());
		}
	}
	public void train(InputSet<Integer> is, OutputSet<Integer> os, double aerror)
	{
		log.info("Training");
		int timesCalculated = 1;
		while(error > aerror)
		{
			List<List<Integer>> iSets = is.getInputSets();
			
			for(int i=0;i<iSets.size();i++)
			{
				List<Integer> expected = os.getOutputSets().get(i);
				log.debug("Expecting:"+expected);
				List<Integer> inputs = iSets.get(i);
				double sum = 0.0;
				for(int j=0;j<inputs.size();j++)
				{
					int in = inputs.get(j);
					sum += in * weights.get(j);
				}
				log.debug("Sum = "+sum);
				int result =0;
				if(sum >= threshold)
				{
					result =1;
				}
				log.debug("Result = "+result);
				double inerror= expected.get(0) - result;
				error = ((error * timesCalculated)+Math.abs(inerror))/(timesCalculated+1);
				log.info("Error = "+error);
				double correction = learningRate * inerror;
				log.debug("Correction = "+correction);
				for(int k=0;k<weights.size();k++)
				{
					log.debug("Setting weight "+k+" from "+weights.get(k)+" to "+weights.get(k)+correction);
					weights.set(k, weights.get(k)+correction);
				}
				timesCalculated++;
				
			} 
		}
		
	}

	public void compute(InputSet<Integer> is )
	{
		List<List<Integer>> iSets = is.getInputSets();
		for(int i=0;i<iSets.size();i++)
		{
			List<Integer> inputs = iSets.get(i);
			String inputString = "";
			for(Integer in: inputs)
			{
				inputString +=in+" ";
			}
			log.info("For input "+inputString+" ...");
			double sum = 0.0;
			for(int j=0;j<inputs.size();j++)
			{
				int in = inputs.get(j);
				sum += in * weights.get(j);
			}
			int result =0;
			if(sum >= threshold)
			{
				result =1;
			}
			log.info("Result = "+result);
		}
		
	}
}
