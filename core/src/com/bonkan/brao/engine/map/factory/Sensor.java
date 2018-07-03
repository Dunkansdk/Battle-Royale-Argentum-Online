package com.bonkan.brao.engine.map.factory;

public class Sensor {
	
	private int direccion;
	private boolean isColliding;
	
	public Sensor(int direccion) {
		this.direccion = direccion;
		this.isColliding = false;
	}
	
	public boolean isColliding() {
		return isColliding;
	}
	
	public void setColliding(boolean set) {
		isColliding = set;
	}
	
	public int getDireccion() {
		return direccion;
	}

}
