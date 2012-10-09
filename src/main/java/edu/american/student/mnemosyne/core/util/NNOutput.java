package edu.american.student.mnemosyne.core.util;

public class NNOutput
{

	public static double[] inflate(String value)
	{
		String doubles = value.split(")")[1].replace("(", "");
		String[] outputs = doubles.split(",");
		double[] toReturn = new double[outputs.length];
		for(int i=0;i<outputs.length;i++)
		{
			toReturn[i]=Double.parseDouble(outputs[i]);
		}
		return toReturn;
	}

}
