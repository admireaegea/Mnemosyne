package edu.american.student.mnemosyne.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class MnemosyneConstants
{
	//TODO remove hardcoded shit
	public static String mnemosyneSite  = "conf.mnemosyne-site";
	
	public static String getAccumuloInstance()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("accumuloInstance");
		return toReturn;
	}
	
	public static String getAccumuloPassword()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("accumuloPassword");
		return toReturn;
	}
	
	public static String getDefaultTable()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("defaultTable");
		return toReturn;
	}
	
	public static String getZookeeperInstance()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("zookeeperInstance");
		return toReturn;
	}

	public static String getZookeeperInstanceName()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("zookeeperInstanceName");
		return toReturn;
	}
	public static String getAccumuloUser()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("accumuloUser");
		return toReturn;
	}

	public static String getNeuralNetworkRowName()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("networkRowName");
		return toReturn;
	}

	public static int getNetworksPerNode()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("networksPerNode");
		return Integer.parseInt(toReturn);
	}

	public static int getNumberOfNodes()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("numberOfNodes");
		return Integer.parseInt(toReturn);
	}

	public static double[][] getTestInput()
	{
		double[][] toReturn = {{0,0},{0,1},{1,0},{1,1}};
		return toReturn;
	}

	public static double[][] getTestIdeal()
	{
		double[][] toReturn = {{0,0},{1,0},{0,1},{1,1}};
		return toReturn;
	}

	public static double[][] getRandomIdeal(int id)
	{
		init();
		return randomIdeal.get(id);
	}

	static Map<Integer,double[][]> randomIdeal= new HashMap<Integer,double[][]>();
	static double x1 =.4; static double y1=.4;
	static double x2 = .6;static double y2=.4;
	static double x3 = .4;static double y3=.6;
	static double x4= .6; static double y4=.6;
	private static void init()
	{
		int id = 0;
		//{{0,0},{1,0},{0,1},{1,1}};
		double[][] value = {{.4,.4},{.6,.4},{.4,.6},{.6,.6}};
		if(randomIdeal.get(id) != null)
		{
			randomIdeal.put(id, value);
		}
	}

	public static String getDefaultAuths()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("defaultAuths");
		return toReturn;
	}
}
