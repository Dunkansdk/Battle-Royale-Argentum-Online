package com.bonkan.brao.engine.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.entities.Human.PlayerState;

public class CommonAnimator {

	private Animation<TextureRegion> rightAnimation;
	private Animation<TextureRegion> leftAnimation;
	private Animation<TextureRegion> upAnimation;
	private Animation<TextureRegion> downAnimation;
	
	private TextureRegion sheet;
	
	// A variable for tracking elapsed time for the animation
	private float stateTime;
	private PlayerState lastState;
		
	private int numCols;
	private int numRows;
	
	public CommonAnimator(int numCols, int numRows) 
	{
		this.numCols = numCols;
		this.numRows = numRows;
		sheet = null;
	}
	
	/**
	 * <p>Crea las animaciones según la textura</p>
	 * @param texture	&emsp;{@link com.badlogic.gdx.graphics.g2d.TextureRegion TextureRegion} si bien es un TextureRegion, es la textura ENTERA de la animación
	 */
	public void setTexture(TextureRegion texture, PlayerState currState)
	{
		sheet = texture;
		lastState = currState;

		if(sheet != null)
		{
			TextureRegion[][] tmp = sheet.split( 
					sheet.getRegionWidth() / numCols,
					sheet.getRegionHeight() / numRows);

			TextureRegion[] walkFrames = new TextureRegion[numCols];
			TextureRegion[] walkFrames2 = new TextureRegion[numCols];
			TextureRegion[] walkSidewayFrames = new TextureRegion[numCols - 1];
			TextureRegion[] walkSidewayFrames2 = new TextureRegion[numCols - 1];
			
			int index = 0;
			for (int j = 0; j < numCols; j++) {
			walkFrames[index++] = tmp[0][j];
			}		
			downAnimation = new Animation<TextureRegion>(0.075f, walkFrames);
			
			index = 0;
			for (int j = 0; j < numCols; j++) {
			walkFrames2[index++] = tmp[1][j];
			}
			upAnimation = new Animation<TextureRegion>(0.075f, walkFrames2);
			
			index = 0;
			for (int j = 0; j < numCols - 1; j++) {
			walkSidewayFrames[index++] = tmp[3][j];
			}
			rightAnimation = new Animation<TextureRegion>(0.075f, walkSidewayFrames);
			
			index = 0;
			for (int j = 0; j < numCols - 1; j++) {
			walkSidewayFrames2[index++] = tmp[2][j];
			}
			leftAnimation = new Animation<TextureRegion>(0.075f, walkSidewayFrames2);
		}
		
	}
	
	/**
	 * <p>Dibuja la animación que corresponde según el estado</p>
	 * @param batch		&emsp;{@link com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch} el batch que dibuja
	 * @param x			&emsp;<b>float</b> posición X destino
	 * @param y			&emsp;<b>float</b> posición Y destino
	 * @param state		&emsp;<b>playerState (<i>enum</i> público de la clase {@link com.bonkan.brao.engine.entity.entities.human.Player Player} )</b> el estado de la entidad
	 */
	public void render(SpriteBatch batch, float x, float y, PlayerState state) {
		
		if(sheet != null)
		{
			stateTime += Gdx.graphics.getDeltaTime();
			
			TextureRegion currentFrame = null;
					
			// Get current frame of animation for the current stateTime
			if(state == PlayerState.MOVE_UP || state == PlayerState.MOVE_LEFT_UP || state == PlayerState.MOVE_RIGHT_UP) currentFrame = upAnimation.getKeyFrame(stateTime, true);
			if(state == PlayerState.MOVE_DOWN || state == PlayerState.MOVE_LEFT_DOWN || state == PlayerState.MOVE_RIGHT_DOWN) currentFrame = downAnimation.getKeyFrame(stateTime, true);
			if(state == PlayerState.MOVE_RIGHT) currentFrame = rightAnimation.getKeyFrame(stateTime, true);
			if(state == PlayerState.MOVE_LEFT) currentFrame = leftAnimation.getKeyFrame(stateTime, true);
			
			if(state == PlayerState.NONE) {
				if(lastState == PlayerState.MOVE_UP || lastState == PlayerState.MOVE_LEFT_UP || lastState == PlayerState.MOVE_RIGHT_UP) currentFrame = upAnimation.getKeyFrame(0, false);
				if(lastState == PlayerState.MOVE_DOWN || lastState == PlayerState.MOVE_LEFT_DOWN || lastState == PlayerState.MOVE_RIGHT_DOWN) currentFrame = downAnimation.getKeyFrame(0, false);
				if(lastState == PlayerState.MOVE_RIGHT) currentFrame = rightAnimation.getKeyFrame(0, false);
				if(lastState == PlayerState.MOVE_LEFT) currentFrame = leftAnimation.getKeyFrame(0, false);
				if(lastState == PlayerState.NONE) currentFrame = downAnimation.getKeyFrame(0, false); // StateRecienArranco
			}
			
			if(!lastState.equals(state)) {
				if(state != PlayerState.NONE) lastState = state;
				stateTime = 0.0f;
			}	

			batch.draw(currentFrame, x, y);
		}
	}
	
}
