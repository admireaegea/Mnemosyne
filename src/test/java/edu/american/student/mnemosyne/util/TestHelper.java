package edu.american.student.mnemosyne.util;

import edu.american.student.mnemosyne.core.ArtifactBuilderProcess;
import edu.american.student.mnemosyne.core.BaseNetworkBuilderProcess;
import edu.american.student.mnemosyne.core.IngestProcess;

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
}
