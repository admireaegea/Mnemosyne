package edu.american.student.mnemosyne.util;

import edu.american.student.mnemosyne.core.ArtifactBuilderProcess;
import edu.american.student.mnemosyne.core.BaseNetworkBuilderProcess;
import edu.american.student.mnemosyne.core.CongressBuilderProcess;
import edu.american.student.mnemosyne.core.CongressTrainProcess;
import edu.american.student.mnemosyne.core.IngestProcess;
import edu.american.student.mnemosyne.core.TrainProcess;
import edu.american.student.mnemosyne.core.VerifyProcess;

public class TestHelper
{

	public static void constructBaseClassificationNetwork() throws Exception
	{
		BaseNetworkBuilderProcess pro = new BaseNetworkBuilderProcess();
		pro.setup();
		pro.process();
	}
	
	public static void ingestTestArtifacts() throws Exception
	{
		IngestProcess pro = new IngestProcess();
		pro.setup();
		pro.process();
	}
	
	
	public static void buildArtifacts() throws Exception
	{
		ArtifactBuilderProcess pro = new ArtifactBuilderProcess();
		pro.setup();
		pro.process();
	}

	public static void trainNetworks() throws Exception
	{
		TrainProcess pro = new TrainProcess();
		pro.setup();
		pro.process();
		
	}

	public static void constructCongress() throws Exception
	{
		CongressBuilderProcess pro = new CongressBuilderProcess();
		pro.setup();
		pro.process();
		
	}

	public static void trainCongress() throws Exception
	{
		CongressTrainProcess pro = new CongressTrainProcess();
		pro.setup();
		pro.process();
		
	}

	public static void verifyCongress() throws Exception
	{
		VerifyProcess pro = new VerifyProcess();
		pro.setup();
		pro.process();
	}
}
