package edu.american.student.mnemosyne.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.hadoop.io.Text;
import org.encog.neural.networks.BasicNetwork;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.core.framework.ArtifactRepository;
import edu.american.student.mnemosyne.core.framework.BaseNetworkRepository;

public class AccumuloForeman
{

	private Connector conn;

	public AccumuloForeman()
	{

	}

	@SuppressWarnings("deprecation")
	public boolean connect()
	{
		try
		{
			Instance instance = new ZooKeeperInstance(MnemosyneConstants.getAccumuloInstance(), MnemosyneConstants.getZookeeperInstance());
			conn = new Connector(instance, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes());
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public Connector getConnector()
	{
		return conn;
	}

	public TableOperations getTableOps()
	{
		return conn.tableOperations();
	}

	public boolean deleteTables()
	{
		Map<String, String> tableMap = this.getTableOps().tableIdMap();
		Iterator<Entry<String, String>> it = tableMap.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, String> entry = (it.next());
			if (!entry.getKey().startsWith("!"))
			{
				System.out.println("Deleting " + entry.getKey() + " ... ");
				this.deleteTable(entry.getKey());
			}
		}
		return true;
	}

	public boolean deleteTable(String name)
	{
		TableOperations tableOps = this.getTableOps();
		try
		{
			tableOps.delete(name);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public boolean makeTables()
	{
		System.out.println("MAKING TABLES");
		this.makeTable("CONSUME");
		this.makeTable("TRADINGFLOOR");
		this.makeTable("ACCOUNT");
		this.makeTable("SYMBOLS");
		this.makeTable("ACTIVE");
		return true;
	}

	public BatchWriter getBatchWriter(String tableName) throws TableNotFoundException
	{
		long memBuf = 1000000L; // bytes to store before sending a batch
		long timeout = 1000L; // milliseconds to wait before sending
		int numThreads = 10;

		BatchWriter writer = conn.createBatchWriter(tableName, memBuf, timeout, numThreads);
		return writer;
	}

	public void addBytes(String table, String row, String fam, String qual, byte[] value)
	{
		try
		{
			BatchWriter writer = this.getBatchWriter(table);
			Mutation m = new Mutation(row);
			Value v = new Value();
			v.set(value);
			m.put(fam, qual, new ColumnVisibility(MnemosyneConstants.getDefaultAuths()), System.currentTimeMillis(), v);
			writer.addMutation(m);
			writer.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void add(String table, String row, String fam, String qual, String value)
	{
		this.addBytes(table, row, fam, qual, value.getBytes());

	}

	public void makeTable(String tableName)
	{
		TableOperations tableOps = this.getTableOps();
		try
		{
			tableOps.create(tableName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(tableName + " created ...");

	}

	public List<Entry<Key, Value>> fetchByColumnFamily(String table, String fam) throws TableNotFoundException
	{
		Authorizations auths = new Authorizations(MnemosyneConstants.getDefaultAuths());

		Scanner scan = conn.createScanner(table, auths);

		scan.fetchColumnFamily(new Text(fam));
		List<Entry<Key, Value>> toRet = new ArrayList<Entry<Key, Value>>();
		for (Entry<Key, Value> entry : scan)
		{
			toRet.add(entry);
		}
		return toRet;
	}

	public List<Entry<Key, Value>> fetchByQualifier(String table, String fam, String qual) throws TableNotFoundException
	{
		Authorizations auths = new Authorizations(MnemosyneConstants.getDefaultAuths());
		Scanner scan = conn.createScanner(table, auths);
		scan.fetchColumn(new Text(fam), new Text(qual));
		List<Entry<Key, Value>> toRet = new ArrayList<Entry<Key, Value>>();
		for (Entry<Key, Value> entry : scan)
		{
			toRet.add(entry);
		}
		return toRet;
	}

	public void saveNetwork(String TABLE_TO_SAVE, String FAMILY_NAME, String artifactId, BasicNetwork network, ClassificationNetworkConf conf) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(network);
		out.close();
		byte[] arr = baos.toByteArray();
		this.addBytes(TABLE_TO_SAVE, artifactId, FAMILY_NAME, artifactId, arr);
		
		baos = new ByteArrayOutputStream();
		out = new ObjectOutputStream(baos);
		out.writeObject(conf);
		out.close();
		arr = baos.toByteArray();
		this.addBytes(TABLE_TO_SAVE, artifactId, "BASE_CONFIGURATION", artifactId, arr);
	}

	public BasicNetwork inflateNetwork(String tableName, String fam, String artifactId) throws TableNotFoundException, IOException, ClassNotFoundException
	{
		List<Entry<Key, Value>> rows = this.fetchByQualifier(tableName, fam, artifactId);
		for (Entry<Key, Value> entry : rows)
		{
			byte[] arr = entry.getValue().get();
			ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(arr));
			return (BasicNetwork) objectIn.readObject();
		}
		return null;
	}

	public boolean tableExists(String name)
	{
		return this.getTableOps().exists(name);
	}

	public void assertBaseNetwork(BasicNetwork network, String artifactId, ClassificationNetworkConf conf) throws IOException
	{
		this.saveNetwork(AccumuloForeman.getBaseNetworkRepositoryName(), AccumuloForeman.getBaseNetworkRepository().getRawBytesField(), artifactId,network,conf);
	}

	public BasicNetwork getBaseNetwork(String artifactId) throws TableNotFoundException, IOException, ClassNotFoundException
	{
		return this.inflateNetwork(AccumuloForeman.getBaseNetworkRepositoryName(), AccumuloForeman.getBaseNetworkRepository().getRawBytesField(),artifactId);
	}

	public static String getArtifactRepositoryName()
	{
		return AccumuloForeman.getArtifactRepository().toString();
	}

	public static String getBaseNetworkRepositoryName()
	{
		return AccumuloForeman.getBaseNetworkRepository().toString();
	}

	public static BaseNetworkRepository getBaseNetworkRepository()
	{
		return new BaseNetworkRepository();
	}

	public static ArtifactRepository getArtifactRepository()
	{
		return new ArtifactRepository();
	}

	public ClassificationNetworkConf getBaseNetworkConf(String artifactId) throws Exception
	{
		return this.inflateNetworkConfiguration(artifactId);
	}

	private ClassificationNetworkConf inflateNetworkConfiguration(String artifactId) throws TableNotFoundException, IOException, ClassNotFoundException
	{
		List<Entry<Key, Value>> rows = this.fetchByQualifier(AccumuloForeman.getBaseNetworkRepositoryName(), "BASE_CONFIGURATION", artifactId);
		for (Entry<Key, Value> entry : rows)
		{
			byte[] arr = entry.getValue().get();
			ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(arr));
			return (ClassificationNetworkConf) objectIn.readObject();
		}
		return null;
	}

	public long getBaseNetworkError(String artifactId)
	{
		return this.inflateNetworkError(artifactId);
	}

	private long inflateNetworkError(String artifactId)
	{
		return 1;
	}
}
