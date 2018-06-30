package com.bonkan.brao.engine.entity;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.animation.BodyAnimator;
import com.bonkan.brao.engine.utils.BodyFactory;

public class Player extends Entity {

	private UUID id;
	private Body body;
	private playerState state;
	private BodyAnimator bodyAnimator;
	
	public enum playerState {
		NONE,
		MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN
	}

	public Player(TextureRegion texture, UUID id, World world) {
		super(texture);
		this.id = id;
		this.body = BodyFactory.createBox(world, 0, 0, texture.getRegionWidth(), texture.getRegionHeight(), false, true);
		this.state = playerState.NONE;
		this.bodyAnimator = new BodyAnimator(new Texture(Gdx.files.internal("body.png")));
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		bodyAnimator.render(batch, body.getPosition().x - texture.getRegionWidth() / 2, body.getPosition().y - texture.getRegionHeight() / 2, state);
	}

	public Body getBody() {
		return body;
	}

	public UUID getID() {
		return id; 
	}
	
	public void setState(playerState state) {
		this.state = state;
	}
	
	public playerState getState() {
		return state;
	}

	@Override
	void dispose() {
		bodyAnimator.dispose();
	}
}
