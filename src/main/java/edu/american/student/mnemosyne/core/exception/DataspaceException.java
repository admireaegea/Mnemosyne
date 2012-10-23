package edu.american.student.mnemosyne.core.exception;

public class DataspaceException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7547386405340454597L;

	public DataspaceException(String message)
	{
		super(message);
	}

	public DataspaceException(String message, Throwable throwable)
	{
		super(message, throwable);
	}

}
