package com.bonkan.brao.engine.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component {
    public Vector3 position;
    public Vector2 scale;
    public float rotation;
    public boolean isHidden;
    
    /**
     * Empty default constructor
     */
    public TransformComponent() {
    	this.position = new Vector3();
    	this.scale = new Vector2(1.0f, 1.0f);
    	this.rotation = 0.0f;
    	this.isHidden = false;
    }
    
    /**
     * Set position
     */
    public TransformComponent(Vector3 position) {
    	this.position = position;
    	this.scale = new Vector2(1.0f, 1.0f);
    	this.rotation = 0.0f;
    	this.isHidden = false;
    }
    
    /**
     * Set position with z
     */
    public TransformComponent(float x, float y, int width, int height) {
    	this.position = new Vector3(x, y, y + height);
    	this.scale = new Vector2(1.0f, 1.0f);
    	this.rotation = 0.0f;
    	this.isHidden = false;
    }
}