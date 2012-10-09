package edu.american.student.mnemosyne.core.framework;

import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ArtifactForeman;

public interface MnemosyneProcess
{
	AccumuloForeman aForeman = new AccumuloForeman();
	ArtifactForeman artifactForeman = new ArtifactForeman();
	//TODO figure out the interface 
	public void process() throws  Exception;
	
	public void setup() throws Exception;

}
