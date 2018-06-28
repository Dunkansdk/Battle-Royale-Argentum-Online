package com.bonkan.brao.engine.entity;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends Entity {
	
	private int health;
	private int mana;
	private UUID id;
	private Body body;

	public Player(TextureRegion texture, int mana, int health, UUID id, Body body) {
		super(texture);
		this.health = health;
		this.mana = mana;
		this.id = id;
		this.body = body;

	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, body.getPosition().x - texture.getRegionWidth() / 2, body.getPosition().y - texture.getRegionHeight() / 2);
	}

	public Body getBody() {
		return body;
	}

}
