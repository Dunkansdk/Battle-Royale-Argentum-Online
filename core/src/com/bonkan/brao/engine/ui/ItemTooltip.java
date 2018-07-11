package com.bonkan.brao.engine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bonkan.brao.engine.utils.AssetsManager;

public class ItemTooltip {

	// es todo estático porque en todo momento siempre va a haber una sola instancia de tooltip
	private static Vector2 position;
	private static Texture texture;
	private static String title;
	private static String desc;
	private static boolean visible;
	private static long lastVisibleFrame;
	private static GlyphLayout glyphLayout;
	private static BitmapFontCache font;
	private static Color fontColor;
	private static float alphaValue;
	
	private static final int MAX_TOOLTIP_TIME = 200;
	
	public static void init()
	{
		position = new Vector2();
		texture = AssetsManager.getTexture("tooltip.png");
		visible = false;
		lastVisibleFrame = System.currentTimeMillis();
		glyphLayout = new GlyphLayout();
		BitmapFont auxFont = AssetsManager.getDefaultFont();
		font = auxFont.getCache();
		fontColor = Color.WHITE;
		alphaValue = 255;
	}
	
	public static void setPosition(int x, int y)
	{
		position.x = x;
		position.y = y;
	}
	
	public static void setTitle(String text)
	{
		title = text;
	}
	
	public static void setDesc(String text)
	{
		desc = text;
	}
	
	public static void setVisible(boolean val)
	{
		visible = val;

		if(val)
		{
			lastVisibleFrame = System.currentTimeMillis();
			alphaValue = 255;
		}
	}
	
	public static void setColor(Color c)
	{
		fontColor = c;
	}
	
	public static void render(SpriteBatch batch)
	{
		// cada MAX_TOOLTIP_TIME milisegundos desaparece
		if(lastVisibleFrame + MAX_TOOLTIP_TIME < System.currentTimeMillis())
			visible = false;
		else
		{
			alphaValue -= 20;
			if(alphaValue < 0) alphaValue = 0;
		}
		
		if(visible)
		{
			// habilitamos alpha blending para efecto de "fade"
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			batch.begin();
				// guardamos el color del batch para aplicarle el alpha
				Color batchColor = batch.getColor();
				
				// aplicamos el alpha
				batch.setColor(batchColor.r, batchColor.g, batchColor.b, alphaValue/255f);
				
				// dibujamos el tooltip
				batch.draw(texture, position.x, position.y);
				
				// aplicamos el alpha a la fuente
				fontColor.set(fontColor.r, fontColor.g, fontColor.b, alphaValue/255f);
				font.setColor(fontColor);
				
				// mostramos el texto
				glyphLayout.setText(AssetsManager.getDefaultFont(), title);
				AssetsManager.getDefaultFont().draw(batch, title, position.x + texture.getWidth() / 2 - glyphLayout.width / 2, position.y + texture.getHeight() - 3 - 10);
				glyphLayout.setText(AssetsManager.getDefaultFont(), desc);
				AssetsManager.getDefaultFont().draw(batch, desc, position.x + texture.getWidth() / 2 - glyphLayout.width / 2, position.y + texture.getHeight() / 2 + glyphLayout.height / 2 - 12);
				
				// reseteamos el alpha y el color de la fuente para los otros draws (los del PlayState)
				font.setColor(255/255f, 255/255f, 255/255f, 255/255f);
			batch.end();
			
			// reseteamos el color del batch para los otros draws
			batch.setColor(batchColor);
			
			// deshabilitamos alpha blending
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}
}
