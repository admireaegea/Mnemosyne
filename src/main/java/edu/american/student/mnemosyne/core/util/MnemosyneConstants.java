package edu.american.student.mnemosyne.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.hadoop.fs.Path;

public class MnemosyneConstants
{
	public static String mnemosyneSite = "conf.mnemosyne-site";

	public static String getAccumuloInstance()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("accumuloInstance");
		return toReturn;
	}

	public static String getAccumuloPassword()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("accumuloPassword");
		return toReturn;
	}

	public static String getDefaultTable()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("defaultTable");
		return toReturn;
	}

	public static String getZookeeperInstance()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("zookeeperInstance");
		return toReturn;
	}

	public static String getZookeeperInstanceName()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("zookeeperInstanceName");
		return toReturn;
	}

	public static String getAccumuloUser()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("accumuloUser");
		return toReturn;
	}

	public static String getNeuralNetworkRowName()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("networkRowName");
		return toReturn;
	}

	public static int getNetworksPerNode()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("networksPerNode");
		return Integer.parseInt(toReturn);
	}

	public static int getNumberOfNodes()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("numberOfNodes");
		return Integer.parseInt(toReturn);
	}

	public static double[][] getTestInput()
	{
		double[][] toReturn =
		{
		{ 0, 0 },
		{ 0, 1 },
		{ 1, 0 },
		{ 1, 1 } };
		return toReturn;
	}

	public static double[][] getTestIdeal()
	{
		double[][] toReturn =
		{
		{ 0, 0 },
		{ 1, 0 },
		{ 0, 1 },
		{ 1, 1 } };
		return toReturn;
	}

	public static String getDefaultAuths()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("defaultAuths");
		return toReturn;
	}

	public static String getMnemosyneHome()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("mnemosyneHome");
		return toReturn;
	}

	public static String getIngestDirectory()
	{
		return MnemosyneConstants.getMnemosyneHome() + "ingest/";
	}

	public static Path[] getAllIngestableFiles()
	{
		ArrayList<Path> paths = walk(new File(MnemosyneConstants.getIngestDirectory()));
		return paths.toArray(new Path[paths.size()]);
	}

	static ArrayList<Path> toReturn = new ArrayList<Path>();

	private static ArrayList<Path> walk(File dir)
	{
		String pattern = ".xml";
		File listFile[] = dir.listFiles();
		if (listFile != null)
		{
			for (int i = 0; i < listFile.length; i++)
			{
				if (listFile[i].isDirectory())
				{
					walk(listFile[i]);
				}
				else
				{
					if (listFile[i].getName().endsWith(pattern))
					{
						System.out.println(listFile[i].getPath());
						toReturn.add(new Path(listFile[i].getPath()));
					}
				}
			}
		}
		return toReturn;
	}

	public static String getArtifactTableName()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("artifactTable");
		return toReturn;
	}

	public static String getArtifactTableRawBytes()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("artifactTableRawBytes");
		return toReturn;
	}

	public static String getArtifactTableArtifactEntry()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("artifactTableArtifactEntry");
		return toReturn;
	}

	public static String getBaseNetworkRepositoryName()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("baseNetworkTable");
		return toReturn;
	}

	public static String getBaseNetworkTableRawBytes()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("baseNetworkTableRawBytes");
		return toReturn;
	}

	public static String getBaseNetworkTableConfiguration()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("baseNetworkTableConfiguration");
		return toReturn;
	}

	public static String getBaseNetworkTableError()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("baseNetworkTableError");
		return toReturn;
	}

	public static String getBaseNetworkTableTrainData()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("baseNetworkTableTrainData");
		return toReturn;
	}

	public static String getBaseNetworkTableBaseNetwork()
	{
		Properties properties = PropertyLoader.loadProperties(mnemosyneSite);
		String toReturn = (String) properties.get("baseNetworkTableBaseNetwork");
		return toReturn;
	}
}
