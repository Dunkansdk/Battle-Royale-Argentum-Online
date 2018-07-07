package com.bonkan.brao.engine.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * <p>Simple clase estática que carga todos los atlases y provee getters para acceder
 * a sus texturas.</p>
 */
public class AtlasManager {

	private static TextureAtlas bodies;
	private static TextureAtlas heads;
	private static TextureAtlas world;
	private static TextureAtlas particles;
	private static TextureAtlas items;
	
	public static void init()
	{
		bodies = AssetsManager.getBodies();
		heads = AssetsManager.getHeads();
		world = AssetsManager.getWorldAtlas();
		particles = AssetsManager.getParticlesAtlas();
		items = AssetsManager.getItems();
	}
	
	public static TextureRegion getBody(int bodyIndex)
	{
		return bodies.findRegion(String.valueOf(bodyIndex));
	}
	
	public static TextureRegion getHeads(int headIndex)
	{
		return heads.findRegion(String.valueOf(headIndex));
	}
	
	public static TextureRegion getWorldSprite(String id) 
	{
		return world.findRegion(id);
	}
	
	public static TextureRegion getItem(String id)
	{
		return items.findRegion(id);
	}

	public static TextureAtlas getParticleAtlas() 
	{
		return particles;
	}

	public static void dispose() 
	{
		/*bodies.dispose();
		heads.dispose();
		world.dispose();
		particles.dispose();*/
	}
}
