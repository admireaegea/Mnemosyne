package edu.american.student.mnemosyne.core.util.copy;

import java.util.Properties;


public class MnemosyneConstants
{

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
	
	public static String getZookeeperInstance()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("zookeeperInstance");
		return toReturn;
	}

	public static String getAccumuloUser()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String)properties.get("accumuloUser");
		return toReturn;
	}
}
