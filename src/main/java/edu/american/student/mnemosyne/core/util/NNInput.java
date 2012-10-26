package edu.american.student.mnemosyne.core.util;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;


public class NNInput
{

	public static double[] inflate(ClassificationNetworkConf conf, String value)
	{
		String doubles = value.split("\\)")[0].replace("(", "");
		String[] inputs = doubles.split(",");
		double[] toReturn = new double[inputs.length];
		for(int i=0;i<inputs.length;i++)
		{
		
			toReturn[i]=Double.parseDouble(inputs[i].replace("(", ""));
		}
		
		return BinaryUtils.toBinary(conf,toReturn,true);
	}

}
