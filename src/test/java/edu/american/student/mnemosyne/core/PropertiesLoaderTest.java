package edu.american.student.mnemosyne.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.MnemosyneConstants;

public class PropertiesLoaderTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		System.out.println(MnemosyneConstants.getAccumuloInstance()+" "+MnemosyneConstants.getAccumuloPassword());
		AccumuloForeman aForeman = new AccumuloForeman();
		aForeman.connect();
		
		
	}

}
