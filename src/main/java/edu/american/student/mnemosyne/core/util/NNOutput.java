package edu.american.student.mnemosyne.core.util;

import java.util.List;

public class NNOutput
{

	public static double[] inflate(String value)
	{
		String doubles = value.split("\\)")[1].replace("(", "");
		String[] outputs = doubles.split(",");
		double[] toReturn = new double[outputs.length];
		for(int i=0;i<outputs.length;i++)
		{
			toReturn[i]=Double.parseDouble(outputs[i]);
		}
		return BinaryUtils.toBinary(toReturn);
	}

	public static String combine(double[] results, NNMetadata data)
	{
		String binaryString = "";
		for(double res: results)
		{
			if(res > .001)
			{
				binaryString+=1;
			}
			else
			{
				binaryString+=0;
			}
		}
		List<String> outputFields = data.getOutputNameFields();
		List<String> outputValues = data.getOutputValueFields();
		int result = Integer.parseInt(binaryString, 2);
		for(int i=0;i<outputValues.size();i++ )
		{
			String value = outputValues.get(i);
			if(value.equals(result+""))
			{
				return outputFields.get(i);
			}
		}
		return null;
	}

}
