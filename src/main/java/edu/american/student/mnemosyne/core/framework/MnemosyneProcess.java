package edu.american.student.mnemosyne.core.framework;

import java.io.IOException;

import edu.american.student.mnemosyne.core.util.AccumuloForeman;

public interface MnemosyneProcess
{
	AccumuloForeman aForeman = new AccumuloForeman();
	//TODO figure out the interface 
	public void process() throws IOException, InterruptedException, ClassNotFoundException;
	
	public void setup() throws Exception;

}
