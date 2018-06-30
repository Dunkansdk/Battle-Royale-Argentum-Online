package com.bonkan.brao.engine.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.Player.playerState;

public class BodyAnimator {
	
	private static final int FRAME_COLS = 6, FRAME_ROWS = 4;
	
	private Animation<TextureRegion> rightAnimation;
	private Animation<TextureRegion> leftAnimation;
	private Animation<TextureRegion> upAnimation;
	private Animation<TextureRegion> downAnimation;
	
	private TextureRegion walkSheet;
	
	// A variable for tracking elapsed time for the animation
	private float stateTime;
	private playerState lastState;
		
	public BodyAnimator(TextureRegion texture) {
		
		walkSheet = texture;
		lastState = playerState.NONE;

		TextureRegion[][] tmp = walkSheet.split( 
				walkSheet.getRegionWidth() / FRAME_COLS,
				walkSheet.getRegionHeight() / FRAME_ROWS);
		
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS];
		TextureRegion[] walkFrames2 = new TextureRegion[FRAME_COLS];
		TextureRegion[] walkSidewayFrames = new TextureRegion[FRAME_COLS - 1];
		TextureRegion[] walkSidewayFrames2 = new TextureRegion[FRAME_COLS - 1];
		
		int index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkFrames[index++] = tmp[0][j];
		}		
		downAnimation = new Animation<TextureRegion>(0.075f, walkFrames);
		
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkFrames2[index++] = tmp[1][j];
		}
		upAnimation = new Animation<TextureRegion>(0.075f, walkFrames2);
		
		index = 0;
		for (int j = 0; j < FRAME_COLS - 1; j++) {
			walkSidewayFrames[index++] = tmp[3][j];
		}
		rightAnimation = new Animation<TextureRegion>(0.075f, walkSidewayFrames);
		
		index = 0;
		for (int j = 0; j < FRAME_COLS - 1; j++) {
			walkSidewayFrames2[index++] = tmp[2][j];
		}
		leftAnimation = new Animation<TextureRegion>(0.075f, walkSidewayFrames2);
		
	}
	
	public void render(SpriteBatch batch, float x, float y, playerState state) {
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		
		TextureRegion currentFrame = null;
				
		// Get current frame of animation for the current stateTime
		if(state == playerState.MOVE_UP) currentFrame = upAnimation.getKeyFrame(stateTime, true);
		if(state == playerState.MOVE_DOWN) currentFrame = downAnimation.getKeyFrame(stateTime, true);
		if(state == playerState.MOVE_RIGHT) currentFrame = rightAnimation.getKeyFrame(stateTime, true);
		if(state == playerState.MOVE_LEFT) currentFrame = leftAnimation.getKeyFrame(stateTime, true);
		
		if(state == playerState.NONE) {
			if(lastState == playerState.MOVE_UP) currentFrame = upAnimation.getKeyFrame(0, false);
			if(lastState == playerState.MOVE_DOWN) currentFrame = downAnimation.getKeyFrame(0, false);
			if(lastState == playerState.MOVE_RIGHT) currentFrame = rightAnimation.getKeyFrame(0, false);
			if(lastState == playerState.MOVE_LEFT) currentFrame = leftAnimation.getKeyFrame(0, false);
			if(lastState == playerState.NONE) currentFrame = downAnimation.getKeyFrame(0, false); // StateRecienArranco
		}
		
		if(!lastState.equals(state)) {
			if(state != playerState.NONE) lastState = state;
			stateTime = 0.0f;
		}	

		batch.draw(currentFrame, x, y); // Draw current frame at (50, 50)

	}
	
}
