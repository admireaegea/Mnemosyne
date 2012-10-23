package edu.american.student.mnemosyne.core.util;

import java.util.ArrayList;

public class BinaryUtils
{

	public static double[] toBinary(double[] toReturn)
	{
		ArrayList<Double> binaryDoubles = new ArrayList<Double>();
		for(double value: toReturn)
		{
			//TODO FIXME
			String binaryString = String.format("%5s", Long.toBinaryString((long)value)).replace(' ', '0');
			char[] binaryArr =binaryString.toCharArray();
			for(char chara: binaryArr)
			{
				binaryDoubles.add(Double.parseDouble(chara+""));
			}
		}
		Double[] dubs = new Double[binaryDoubles.size()];
		binaryDoubles.toArray(dubs);
		double[] ret = new double[binaryDoubles.size()];
		for(int i=0;i<binaryDoubles.size();i++)
		{
			ret[i]=dubs[i];
		}
		return ret;
	}

}
