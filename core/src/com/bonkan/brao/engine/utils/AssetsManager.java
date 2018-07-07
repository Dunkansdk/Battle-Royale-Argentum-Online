package com.bonkan.brao.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class AssetsManager {

	private static AssetManager am;
	private static BitmapFont defaultFont;
	
	public static void init()
	{
		am = new AssetManager();
	}
	
	public static void loadAssets()
	{
		FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("segoeui.ttf"));
 		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
 		parameter.size = 14;
 		defaultFont = freeTypeFontGenerator.generateFont(parameter);
		
 		// LA FUENTE SE PUEDE CARGAR MEJOR, HAY QUE CHUSMEAR LA DOC DE LIBGDX
 		// TAMBIEN FALTARIA CARGAR LA SKIN DEFAULT ACA
 		// Y VOLAR EL ATLASMANAGER
 		
		am.load("bodies.atlas", TextureAtlas.class);
		am.load("items.atlas", TextureAtlas.class);
		am.load("heads.atlas", TextureAtlas.class);
		am.load("world.atlas", TextureAtlas.class);
		am.load("particles.atlas", TextureAtlas.class);
	}
	
	public static boolean update()
	{
		return am.update();
	}
	
	public static float getProgress()
	{
		return am.getProgress();
	}
	
	public static TextureAtlas getBodies()
	{
		return am.get("bodies.atlas");
	}
	
	public static TextureAtlas getItems()
	{
		return am.get("items.atlas");
	}
	
	public static TextureAtlas getHeads()
	{
		return am.get("heads.atlas");
	}
	
	public static TextureAtlas getWorldAtlas()
	{
		return am.get("world.atlas");
	}
	
	public static TextureAtlas getParticlesAtlas()
	{
		return am.get("particles.atlas");
	}
	
	public static BitmapFont getDefaultFont()
	{
		return defaultFont;
	}
}
