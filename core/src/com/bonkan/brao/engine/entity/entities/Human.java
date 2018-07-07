package com.bonkan.brao.engine.entity.entities;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Entity;
import com.bonkan.brao.engine.entity.animation.BodyAnimator;
import com.bonkan.brao.engine.entity.animation.HeadAnimator;
import com.bonkan.brao.engine.map.factory.BodyFactory;
import com.bonkan.brao.engine.utils.AtlasManager;
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
	protected String userName;
	protected BitmapFont defaultFont;
		
	// Texturas del player
	protected BodyAnimator bodyAnimator;
	protected HeadAnimator headAnimator;
	
	protected int bodyIndex;
	protected int headIndex;
	
	protected Body body;

	public Human(int x, int y, int bodyIndex, int headIndex, UUID id, String userName, World world) {
		super(x, y);
		this.texture = AtlasManager.getBody(bodyIndex);
		this.bodyIndex = bodyIndex;
		this.headIndex = headIndex;
		this.userName = userName;
		this.id = id;
		this.state = PlayerState.NONE;
		this.headAnimator = new HeadAnimator(AtlasManager.getHeads(headIndex));
		this.bodyAnimator = new BodyAnimator(texture);
		this.body = BodyFactory.createPlayerBox(world, x, y, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
		
		FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("segoeui.ttf"));
 		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
 		parameter.size = 14;
 		this.defaultFont = freeTypeFontGenerator.generateFont(parameter);
	}

	@Override
	public void render(SpriteBatch batch) {
		bodyAnimator.render(batch, location.x - Constants.BODY_WIDTH / 2, location.y - Constants.BODY_HEIGHT / 2, state);
		headAnimator.render(batch, location.x - 8, location.y + Constants.BODY_HEIGHT / 2 - 3, state);
		defaultFont.draw(batch, userName, location.x - (userName.length() / 2 * 14) / 2, location.y - Constants.BODY_HEIGHT / 2);
	}
	
	@Override
	public void update(float delta) {
		body.setTransform(location, 0.0f);
	}
	
	@Override
	public void dispose() {
		
	}
	
	public void setLocation(int x, int y)
	{
		location.x = x;
		location.y = y;
	}

	public UUID getID() 
	{
		return id; 
	}
	
	public void setState(PlayerState state) 
	{
		this.state = state;
	}
	
	public PlayerState getState() 
	{
		return state;
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
	
	public TextureRegion getTexture() {
		return texture;
	}
	
}
