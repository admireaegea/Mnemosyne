package edu.american.student.mnemosyne.conf;

public class CongressNetworkConf  extends NetworkConf implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7668921475431018909L;

	private int numOfNeurons =0;
	private int numOfInputs = 0;
	
	public void setNumberOfInputs(int x)
	{
		this.numOfInputs = x;
	}
	
	public void setNumberOfNeurons(int x)
	{
		this.numOfNeurons = x;
	}
	
	public int getNumberOfNeurons()
	{
		return numOfNeurons	;
	}

	public int getNumberOfInputs()
	{
		return numOfInputs;
	}

}
