package edu.american.student.mnemosyne.core.util;

public enum CLIConstants
{

	START("analyze"),
	SETUP("setup"),
	BUILD("build"),
	CONSTRUCT("construct"),
	TRAIN("train"),
	ERROR("calculateError"),
	SAVE("save"),
	INFLATE("inflate"),
	INGEST("ingest");
	
	String title;
	CLIConstants(String title)
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public static CLIConstants getTitle(String passed)
	{
		CLIConstants[] constants = CLIConstants.class.getEnumConstants();
		for(CLIConstants constant: constants)
		{
			if(constant.getTitle().equals(passed))
			{
				return constant;
			}
		}
		return null;
	}
}
