package com.bonkan.brao.entity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.entity.Entity;

public class Character extends Entity {
	
	private TextureRegion texture; // TODO: Animaciones!
	private int health;
	private int mana;
	private int attack;
	private int armor;

    public Character(float x, float y, TextureRegion texture)
    {
        super(x, y);
        this.texture = texture;
    }

    @Override
    public void render(SpriteBatch batch) {
    	batch.draw(texture,  x,  y);
    }
    
    @Override
    public void update(float delta) {
    	
    }

}
