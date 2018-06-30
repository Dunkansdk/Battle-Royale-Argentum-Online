package com.bonkan.brao.engine.exception;

/**
 * Manejo de excepciones del cliente
 */
public class BRAOException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BRAOException(String message) {
		super("BRAOException: " + message);
	}

}
