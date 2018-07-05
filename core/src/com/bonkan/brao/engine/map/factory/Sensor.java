package com.bonkan.brao.engine.map.factory;

public class Sensor {
	
	private int direccion;
	private boolean isColliding;
	
	public Sensor(int direccion) {
		this.direccion = direccion;
		this.isColliding = false;
	}
	
	public void setColliding(boolean set) {
		isColliding = set;
	}
	
	public boolean isColliding() {
		return isColliding;
	}
	
	public int getDireccion() {
		return direccion;
	}
}
