package com.bonkan.brao.engine.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.Human.PlayerState;

/**
 * <p>Clase encargada de la animación de cuerpos. Todas las {@link com.bonkan.brao.engine.entity.Entity entidades}
 * que representan un jugador tienen un BodyAnimator.</p>
 */
public class BodyAnimator {
	
	private static final int FRAME_COLS = 6, FRAME_ROWS = 4;
	
	private Animation<TextureRegion> rightAnimation;
	private Animation<TextureRegion> leftAnimation;
	private Animation<TextureRegion> upAnimation;
	private Animation<TextureRegion> downAnimation;
	
	private TextureRegion walkSheet;
	
	// A variable for tracking elapsed time for the animation
	private float stateTime;
	private PlayerState lastState;
		
	/**
	 * <p>Crea las animaciones según la textura</p>
	 * @param texture	&emsp;{@link com.badlogic.gdx.graphics.g2d.TextureRegion TextureRegion} si bien es un TextureRegion, es la textura ENTERA de la animación
	 */
	public BodyAnimator(TextureRegion texture) {
		
		walkSheet = texture;
		lastState = PlayerState.NONE;

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
	
	/**
	 * <p>Dibuja la animación que corresponde según el estado</p>
	 * @param batch		&emsp;{@link com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch} el batch que dibuja
	 * @param x			&emsp;<b>float</b> posición X destino
	 * @param y			&emsp;<b>float</b> posición Y destino
	 * @param state		&emsp;<b>playerState (<i>enum</i> público de la clase {@link com.bonkan.brao.engine.entity.humans.Player Player} )</b> el estado de la entidad
	 */
	public void render(SpriteBatch batch, float x, float y, PlayerState state) {
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		
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

		batch.draw(currentFrame, x, y); // Draw current frame at (50, 50)
	}
	
}
