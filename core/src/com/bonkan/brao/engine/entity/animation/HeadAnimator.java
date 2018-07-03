package com.bonkan.brao.engine.entity.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.Human.playerState;

public class HeadAnimator {
	
	private static final int HEAD_FRAMES = 4;
	
	private TextureRegion headSheet;
	private TextureRegion[] headFrames;
	
	private playerState lastState;
	
	public HeadAnimator(TextureRegion texture) {
		headSheet = texture;
		
		TextureRegion[][] tmp = headSheet.split(headSheet.getRegionWidth() / HEAD_FRAMES, headSheet.getRegionHeight());
		headFrames = new TextureRegion[HEAD_FRAMES];
		
		for(int i = 0; i < HEAD_FRAMES; i++) {
			headFrames[i] = tmp[0][i];
		}
		
		lastState = playerState.NONE;

	}
	
	public void render(SpriteBatch batch, float x, float y, playerState state) {
		
		TextureRegion currentFrame = null;
		
		if(state == playerState.MOVE_UP || state == playerState.MOVE_LEFT_UP || state == playerState.MOVE_RIGHT_UP) currentFrame = headFrames[3];
		if(state == playerState.MOVE_DOWN || state == playerState.MOVE_LEFT_DOWN || state == playerState.MOVE_RIGHT_DOWN) currentFrame = headFrames[0];
		if(state == playerState.MOVE_RIGHT) currentFrame = headFrames[1];
		if(state == playerState.MOVE_LEFT) currentFrame = headFrames[2];
		
		if(state == playerState.NONE) {
			if(lastState == playerState.MOVE_UP || lastState == playerState.MOVE_LEFT_UP || lastState == playerState.MOVE_RIGHT_UP) currentFrame = headFrames[3];
			if(lastState == playerState.MOVE_DOWN || lastState == playerState.MOVE_LEFT_DOWN || lastState == playerState.MOVE_RIGHT_DOWN) currentFrame = headFrames[0];
			if(lastState == playerState.MOVE_RIGHT) currentFrame = headFrames[1];
			if(lastState == playerState.MOVE_LEFT) currentFrame = headFrames[2];
			if(lastState == playerState.NONE) currentFrame = headFrames[0]; // StateRecienArranco
		}
		
		if(!lastState.equals(state)) {
			if(state != playerState.NONE) lastState = state;
		}

		batch.draw(currentFrame, x, y); // Draw current frame at (50, 50)
	}

}
