package com.bonkan.brao.engine.entity;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.animation.BodyAnimator;
import com.bonkan.brao.engine.entity.animation.HeadAnimator;
import com.bonkan.brao.engine.map.factory.BodyFactory;
import com.bonkan.brao.engine.utils.AtlasManager;
import com.bonkan.brao.engine.utils.Constants;

public class Human extends Entity {
	
	public enum playerState {
		NONE,
		MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN
	}
	
	private UUID id;
	private playerState state;
	private String userName;
	private BitmapFont defaultFont;
	
	// Texturas del player
	private BodyAnimator bodyAnimator;
	private HeadAnimator headAnimator;
	
	private int bodyIndex;
	private int headIndex;

	public Human(int bodyIndex, int headIndex, UUID id, String userName, final World world) {
		super(AtlasManager.getBody(bodyIndex));
		this.bodyIndex = bodyIndex;
		this.headIndex = headIndex;
		this.userName = userName;
		this.id = id;
		this.state = playerState.NONE;
		this.headAnimator = new HeadAnimator(AtlasManager.getHeads(headIndex));
		this.bodyAnimator = new BodyAnimator(texture);
		
		FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("segoeui.ttf"));
 		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
 		parameter.size = 14;
 		this.defaultFont = freeTypeFontGenerator.generateFont(parameter);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		bodyAnimator.render(batch, body.getPosition().x - Constants.BODY_WIDTH / 2, body.getPosition().y - Constants.BODY_HEIGHT / 2, state);
		headAnimator.render(batch, body.getPosition().x - 8, body.getPosition().y + Constants.BODY_HEIGHT / 2 - 3, state);
		defaultFont.draw(batch, userName, body.getPosition().x - (userName.length() / 2 * 14) / 2, body.getPosition().y - Constants.BODY_HEIGHT / 2);
	}

	@Override
	public void dispose() {
		
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
	
	public int getHeadIndex()
	{
		return headIndex;
	}
	
	public String getUserName()
	{
		return userName;
	}
}
