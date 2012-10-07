package edu.american.student.mnemosyne.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
		System.out.println(tableName+" created ...");

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

	public void saveNetwork(String TABLE_TO_SAVE,String FAMILY_NAME,Serializable network,int networkId) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(network);
		out.close();
		byte[] arr = baos.toByteArray();
		System.out.println(arr.length);
		this.addBytes(TABLE_TO_SAVE, MnemosyneConstants.getNeuralNetworkRowName(), FAMILY_NAME, networkId+"", arr);
	}

	public BasicNetwork inflateNetwork(String tableName,String fam) throws TableNotFoundException, IOException, ClassNotFoundException
	{
		List<Entry<Key, Value>> rows = 	this.fetchByColumnFamily(tableName, fam);
		for (Entry<Key, Value> entry : rows)
		{
			byte[] arr = entry.getValue().get();
			ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(arr));
			return (BasicNetwork)objectIn.readObject();
		}
		return null;
	}

	public boolean tableExists(String name)
	{
		return this.getTableOps().exists(name);
	}

	public void assertBaseNetwork(BasicNetwork network) throws IOException
	{
		//TODO remove hard coded strings
		this.saveNetwork("BASE_NETWORK", "RAW_BYTES", network, 0);
	}

	public BasicNetwork getBaseNetwork() throws TableNotFoundException, IOException, ClassNotFoundException
	{
		return this.inflateNetwork("BASE_NETWORK","RAW_BYTES");
	}

}
