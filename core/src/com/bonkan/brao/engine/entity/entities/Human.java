package com.bonkan.brao.engine.entity.entities;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Entity;
import com.bonkan.brao.engine.entity.animation.CommonAnimator;
import com.bonkan.brao.engine.entity.animation.HeadAnimator;
import com.bonkan.brao.engine.map.factory.BodyFactory;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.engine.utils.Constants;

public abstract class Human extends Entity {
	
	public enum PlayerState {
		NONE,
		MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN,
		MOVE_LEFT_DOWN, MOVE_LEFT_UP, MOVE_RIGHT_DOWN, MOVE_RIGHT_UP
	}
	
	protected TextureRegion texture;
	
	protected UUID id;
	protected PlayerState state;
	protected PlayerState lastValidState;// ultimo state distinto de NONE (para chequear orientacion)
	protected String userName;
	protected BitmapFont nameFont;
		
	// Texturas del player
	protected CommonAnimator bodyAnimator;
	protected HeadAnimator headAnimator;
	protected CommonAnimator weaponAnimator;
	protected CommonAnimator shieldAnimator;
	
	protected int bodyIndex;
	protected int headIndex;
	
	protected Body body;

	public Human(int x, int y, int bodyIndex, int headIndex, UUID id, String userName, World world) {
		super(x, y);
		this.bodyIndex = bodyIndex;
		this.headIndex = headIndex;
		this.userName = userName;
		this.id = id;
		this.state = PlayerState.NONE;
		this.headAnimator = new HeadAnimator(AssetsManager.getHead(headIndex));
		this.bodyAnimator = new CommonAnimator(6, 4);
		this.bodyAnimator.setTexture(AssetsManager.getBody(bodyIndex), state);
		this.weaponAnimator = new CommonAnimator(6, 4);
		this.shieldAnimator = new CommonAnimator(6, 4);
		this.body = BodyFactory.createPlayerBox(world, x, y, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
		this.nameFont = AssetsManager.getDefaultFont();
	}

	@Override
	public void render(SpriteBatch batch) {
		bodyAnimator.render(batch, location.x - Constants.BODY_WIDTH / 2, location.y - Constants.BODY_HEIGHT / 2, state);
		weaponAnimator.render(batch, location.x - Constants.BODY_WIDTH / 2, location.y - Constants.BODY_HEIGHT / 2, state);
		shieldAnimator.render(batch, location.x - Constants.BODY_WIDTH / 2, location.y - Constants.BODY_HEIGHT / 2, state);
		headAnimator.render(batch, location.x - 8, location.y + Constants.BODY_HEIGHT / 2 - 3, state);
		nameFont.draw(batch, userName, location.x - (userName.length() / 2 * 14) / 2, location.y - Constants.BODY_HEIGHT / 2);
	}
	
	@Override
	public void update(float delta) {
		body.setTransform(location, 0.0f);
	}
	
	public void setLocation(int x, int y)
	{
		location.x = x;
		location.y = y;
	}

	public void setWeapon(String weaponID)
	{
		weaponAnimator.setTexture(AssetsManager.getWeapon(weaponID), lastValidState);
	}
	
	public void setShield(String shieldID)
	{
		shieldAnimator.setTexture(AssetsManager.getShield(shieldID), lastValidState);
	}
	
	public void setState(PlayerState state) 
	{
		this.state = state;
	}
	
	public void setLastValidState(PlayerState state)
	{
		lastValidState = state;
	}
	
	public PlayerState getState() 
	{
		return state;
	}
	
	public PlayerState getLastValidState()
	{
		return lastValidState;
	}

	public int getBodyIndex()
	{
		return bodyIndex;
	}
	
	public int getHeadIndex()
	{
		return headIndex;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public UUID getID()
	{
		return id;
	}
}
