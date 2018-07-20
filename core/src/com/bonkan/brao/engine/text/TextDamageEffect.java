package com.bonkan.brao.engine.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bonkan.brao.engine.utils.AssetsManager;

public class TextDamageEffect {

	private String text;
	private long showTime;
	private Vector2 pos;
	private Color fontColor;
	private float alphaValue;
	private boolean alive;
	private BitmapFont font;
	
	public static final int FADE_START = 500; // medio segundo despues de que aparece, empieza a desaparecer
	private static final int MAX_DURATION = 3000; // 3 segundos
	
	public TextDamageEffect(String text, float x, float y)
	{
		this.text = text;
		this.showTime = System.currentTimeMillis();
		this.pos = new Vector2(x, y);
		this.fontColor = Color.RED;
		this.font = AssetsManager.getDefaultFont();
		this.alphaValue = 255;
		this.alive = true;
	}
	
	public void update()
	{
		// en cada frame subimos el Y en 0,5
		pos.y++;
		
		// una vez pasados 3 segundos, empezamos a bajar el alphavalue
		if(System.currentTimeMillis() - showTime > FADE_START)
			alphaValue -= 2;
		
		if(System.currentTimeMillis() - showTime > MAX_DURATION)
			alive = false;
	}
	
	public void render(SpriteBatch batch)
	{
		// seteamos el color de la fuente
		fontColor.set(fontColor.r, fontColor.g, fontColor.b, alphaValue / 255f);
		font.setColor(fontColor);
		
		// mostramos el texto
		AssetsManager.getDefaultFont().draw(batch, text, pos.x, pos.y);
		
		// reseteamos el alpha y el color de la fuente para los otros draws (los del PlayState)
		font.setColor(255 / 255f, 255 / 255f, 255 / 255f, 255 / 255f);
		
	}
	
	public boolean getAlive()
	{
		return alive;
	}

}
