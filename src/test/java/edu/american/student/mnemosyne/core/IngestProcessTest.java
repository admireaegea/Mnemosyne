package edu.american.student.mnemosyne.core;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.core.util.MnemosyneAccumuloAdministrator;

public class IngestProcessTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		MnemosyneAccumuloAdministrator.setup();
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
		IngestProcess pro = new IngestProcess();
		try
		{
			pro.setup();
			pro.process();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}

}
