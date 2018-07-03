package com.bonkan.brao.server.utils;

/**
 * <p>Clase utilitaria que maneja una posición bidimensional.</p>
 */
public class Position {

	private float x, y;
	
	public Position(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
}
