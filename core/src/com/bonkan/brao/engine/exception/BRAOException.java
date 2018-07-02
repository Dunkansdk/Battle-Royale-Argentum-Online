package com.bonkan.brao.engine.exception;

/**
 * <p>Manejo de excepciones del cliente.</p>
 */
public class BRAOException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BRAOException(String message) 
	{
		super("BRAOException: " + message);
	}

}
