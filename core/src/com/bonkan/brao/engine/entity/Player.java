package com.bonkan.brao.engine.entity;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.animation.BodyAnimator;
import com.bonkan.brao.engine.utils.AtlasManager;
import com.bonkan.brao.engine.utils.BodyFactory;
import com.bonkan.brao.engine.utils.Constants;

public class Player extends Entity {

	private UUID id;
	private Body body;
	private playerState state;
	private BodyAnimator bodyAnimator;
	private int bodyIndex;
	private String userName;
	
	public enum playerState {
		NONE,
		MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN
	}

	public Player(int bodyIndex, UUID id, String userName, World world) {
		super(AtlasManager.getBody(bodyIndex));
		this.bodyIndex = bodyIndex;
		this.userName = userName;
		this.id = id;
		this.body = BodyFactory.createBox(world, 0, 0, Constants.BODY_WIDTH, Constants.BODY_HEIGHT, false, true);
		this.state = playerState.NONE;
		this.bodyAnimator = new BodyAnimator(texture);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		//bodyAnimator.render(batch, body.getPosition().x - texture.getRegionWidth() / 2, body.getPosition().y - texture.getRegionHeight() / 2, state);
		bodyAnimator.render(batch, body.getPosition().x - Constants.BODY_WIDTH / 2, body.getPosition().y - Constants.BODY_HEIGHT / 2, state);
	}

	public Body getBody() 
	{
		return body;
	}

	public UUID getID() 
	{
		return id; 
	}
	
	public void setState(playerState state) 
	{
		this.state = state;
	}
	
	public playerState getState() 
	{
		return state;
	}

	public int getBodyIndex()
	{
		return bodyIndex;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	@Override
	void dispose() {
	}
}
