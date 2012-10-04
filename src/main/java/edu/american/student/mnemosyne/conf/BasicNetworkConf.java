package edu.american.student.mnemosyne.conf;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;

public class BasicNetworkConf extends NetworkConf
{
	private ActivationFunction inputActivation = null;
	private ActivationFunction hiddenActivation = new ActivationSigmoid();
	private ActivationFunction outputActivation = new ActivationSigmoid();
	private boolean inputBias = true;
	private boolean hiddenBias = true;
	private boolean outputBias = true;
	private int inputNeuronCount = 1;
	private int hiddenNeuronCount = 3;
	private int outputNeuronCount = 1;
	private double[][] basicMLInput;
	private double[][] basicIdealOutput;
	private double error=.000005;
	
	public ActivationFunction getInputActivation()
	{
		return inputActivation;
	}

	public boolean getInputBias()
	{
		return inputBias;
	}

	public int getInputNeuronCount()
	{
		return inputNeuronCount;
	}

	public ActivationFunction getHiddenActivation()
	{
		return hiddenActivation;
	}

	public boolean getHiddenBias()
	{
		return hiddenBias;
	}

	public int getHiddenNeuronCount()
	{
		return hiddenNeuronCount;
	}

	public ActivationFunction getOutputActivation()
	{
		return outputActivation;
	}

	public boolean getOutputBias()
	{
		return this.outputBias;
	}

	public int getOutputNeuronCount()
	{
		return this.outputNeuronCount;
	}

	public double[][] getBasicMLInput()
	{
		return this.basicMLInput;
	}

	public double[][] getBasicIdealOutput()
	{
		return this.basicIdealOutput;
	}

	public double getErrorBound()
	{
		return this.error;
	}

	public void setInputBias(boolean b)
	{
		this.inputBias =b;
		
	}

	public void setHiddenBias(boolean b)
	{
		this.hiddenBias = b;
		
	}

	public void setOutputActivation(ActivationFunction activation)
	{
		this.outputActivation = activation;
		
	}

	public void setHiddenActiviation(ActivationFunction activation)
	{
		this.hiddenActivation = activation;
		
	}

	public void setInputActivation(ActivationFunction activation)
	{
		this.inputActivation = activation;
	}

	public void setInputNeuronCount(int i)
	{
		this.inputNeuronCount = i;
		
	}

	public void setHiddenNeuronCount(int i)
	{
		this.hiddenNeuronCount = i;
		
	}

	public void setOutputNeuronCount(int i)
	{
		this.outputNeuronCount = i;
		
	}

	public void setBasicMLInput(double[][] ds)
	{
		this.basicMLInput = ds;
	}

	public void setBasicIdealMLOutput(double[][] ds)
	{
		this.basicIdealOutput = ds;
		
	}
	
	

}
