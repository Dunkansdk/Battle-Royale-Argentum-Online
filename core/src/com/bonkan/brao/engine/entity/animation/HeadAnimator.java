package com.bonkan.brao.engine.entity.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.entities.Human.PlayerState;

public class HeadAnimator {
	
	private static final int HEAD_FRAMES = 4;
	
	private TextureRegion headSheet;
	private TextureRegion[] headFrames;
	
	private PlayerState lastState;
	
	public HeadAnimator(TextureRegion texture) {
		headSheet = texture;
		
		if(headSheet != null)
		{
			TextureRegion[][] tmp = headSheet.split(headSheet.getRegionWidth() / HEAD_FRAMES, headSheet.getRegionHeight());
			headFrames = new TextureRegion[HEAD_FRAMES];
			
			for(int i = 0; i < HEAD_FRAMES; i++) {
				headFrames[i] = tmp[0][i];
			}
			
			lastState = PlayerState.NONE;
		}

	}
	
	public void setTexture(TextureRegion texture, PlayerState state)
	{
		headSheet = texture;
		lastState = state;
		
		if(texture != null)
		{
			TextureRegion[][] tmp = headSheet.split(headSheet.getRegionWidth() / HEAD_FRAMES, headSheet.getRegionHeight());
			headFrames = new TextureRegion[HEAD_FRAMES];
			
			for(int i = 0; i < HEAD_FRAMES; i++) {
				headFrames[i] = tmp[0][i];
			}
		}

	}
	
	public void render(SpriteBatch batch, float x, float y, PlayerState state) {
		
		TextureRegion currentFrame = null;

		if(headSheet != null)
		{
			if(state == PlayerState.MOVE_UP || state == PlayerState.MOVE_LEFT_UP || state == PlayerState.MOVE_RIGHT_UP) currentFrame = headFrames[3];
			if(state == PlayerState.MOVE_DOWN || state == PlayerState.MOVE_LEFT_DOWN || state == PlayerState.MOVE_RIGHT_DOWN) currentFrame = headFrames[0];
			if(state == PlayerState.MOVE_RIGHT) currentFrame = headFrames[1];
			if(state == PlayerState.MOVE_LEFT) currentFrame = headFrames[2];
			
			if(state == PlayerState.NONE) {
				if(lastState == PlayerState.MOVE_UP || lastState == PlayerState.MOVE_LEFT_UP || lastState == PlayerState.MOVE_RIGHT_UP) currentFrame = headFrames[3];
				if(lastState == PlayerState.MOVE_DOWN || lastState == PlayerState.MOVE_LEFT_DOWN || lastState == PlayerState.MOVE_RIGHT_DOWN) currentFrame = headFrames[0];
				if(lastState == PlayerState.MOVE_RIGHT) currentFrame = headFrames[1];
				if(lastState == PlayerState.MOVE_LEFT) currentFrame = headFrames[2];
				if(lastState == PlayerState.NONE) currentFrame = headFrames[0]; // StateRecienArranco
			}
			
			if(!lastState.equals(state)) {
				if(state != PlayerState.NONE) lastState = state;
			}

			batch.draw(currentFrame, x, y); 
		}
	}

}
