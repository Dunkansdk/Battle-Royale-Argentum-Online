package com.bonkan.brao.server.utils;

/**
 * <p>Clase utilitaria que maneja una posición bidimensional.</p>
 */
public class Position {

	private int x, y;
	
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
