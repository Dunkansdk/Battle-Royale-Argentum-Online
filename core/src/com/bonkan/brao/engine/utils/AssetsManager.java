package com.bonkan.brao.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetsManager {

	private static AssetManager am;
	
	public static void init()
	{
		am = new AssetManager();
	}
	
	public static void loadAssets()
	{
		// ATLASES
		am.load("bodies.atlas", TextureAtlas.class);
		am.load("items.atlas", TextureAtlas.class);
		am.load("heads.atlas", TextureAtlas.class);
		am.load("world.atlas", TextureAtlas.class);
		am.load("weapons.atlas", TextureAtlas.class);
		am.load("shields.atlas", TextureAtlas.class);
		am.load("particles.atlas", TextureAtlas.class);
		
		// TEXTURAS AUXILIARES (podria hacer un atlas para esto tambien pero alta fiaca)
		am.load("slot.png", Texture.class);
		am.load("tooltip.png", Texture.class);
		am.load("hpBar.png", Texture.class);
		am.load("manaBar.png", Texture.class);
		am.load("barOutline.png", Texture.class);
		
		// FUENTES
		FileHandleResolver resolver = new InternalFileHandleResolver();
		am.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		am.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter font = new FreeTypeFontLoaderParameter();
		font.fontFileName = "segoeui.ttf";
		font.fontParameters.size = 14;
		am.load("segoeui.ttf", BitmapFont.class, font);

		// SKIN
		// cargo la fuente de nuevo para crear la skin porque probablemente la fuente anterior todavia no se haya cargado
		BitmapFont auxFont;
		FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("segoeui.ttf"));
 		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
 		param.size = 14;
 		auxFont = freeTypeFontGenerator.generateFont(param);
		
		ObjectMap<String, Object> fontMap = new ObjectMap<String, Object>();
		fontMap.put("default-font", auxFont);

		SkinParameter parameter = new SkinParameter(fontMap);

		am.load("uiskin.json", Skin.class, parameter);

		// PARTICULAS
		ParticleEffectParameter particleParam = new ParticleEffectLoader.ParticleEffectParameter();
		particleParam.atlasFile = "particles.atlas";
		am.load("particle.p", ParticleEffect.class, particleParam);
		
		// aca la particula 2 se esta cargando con el mismo atlas de la particula 1, no se si se usa o no, cualquier cosa lo cambiamos
		am.load("particle2.p", ParticleEffect.class, particleParam);
	}
	
	public static boolean update()
	{
		return am.update();
	}
	
	public static float getProgress()
	{
		return am.getProgress();
	}
	
	public static TextureRegion getBody(int id)
	{
		return ((TextureAtlas) am.get("bodies.atlas")).findRegion(String.valueOf(id));
	}
	
	public static TextureRegion getItem(String id)
	{
		return ((TextureAtlas) am.get("items.atlas")).findRegion(id);
	}
	
	public static TextureRegion getWeapon(String id)
	{
		return ((TextureAtlas) am.get("weapons.atlas")).findRegion(id);
	}
	
	public static TextureRegion getShield(String id)
	{
		return ((TextureAtlas) am.get("shields.atlas")).findRegion(id);
	}
	
	public static TextureRegion getHead(int id)
	{
		return ((TextureAtlas) am.get("heads.atlas")).findRegion(String.valueOf(id));
	}
	
	public static TextureRegion getWorldSprite(String id)
	{
		return ((TextureAtlas) am.get("world.atlas")).findRegion(id);
	}
	
	public static TextureAtlas getParticlesAtlas()
	{
		return am.get("particles.atlas");
	}
	
	public static Texture getTexture(String texture)
	{
		return am.get(texture);
	}
	
	public static BitmapFont getDefaultFont()
	{
		return am.get("segoeui.ttf", BitmapFont.class);
	}
	
	public static Skin getDefaultSkin()
	{
		return am.get("uiskin.json");
	}
	
	public static ParticleEffect getParticle(String id)
	{
		return am.get(id);
	}
	
	public static void dispose()
	{
		am.dispose();
	}
}
