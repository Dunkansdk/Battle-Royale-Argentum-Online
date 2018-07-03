package com.bonkan.brao.engine.map.factory;

public class Sensor {
	
	private int direccion;
	private boolean isColliding;
	private boolean isCollidingWithEnemy;
	
	public Sensor(int direccion) {
		this.direccion = direccion;
		this.isColliding = false;
		this.isCollidingWithEnemy = false;
	}
	
	public boolean isColliding() {
		return isColliding;
	}
	
	public void setColliding(boolean set) {
		isColliding = set;
	}
	
	public void setCollidingWithEnemy(boolean set) {
		isCollidingWithEnemy = set;
	}
	
	public int getDireccion() {
		return direccion;
	}
	
	public boolean getCollidingWithEnemy()
	{
		return isCollidingWithEnemy;
	}

}
