package com.bonkan.brao.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * <p>Simple clase estática que carga todos los atlases y provee getters para acceder
 * a sus texturas.</p>
 */
public class AtlasManager {

	private static TextureAtlas bodies;
	private static TextureAtlas heads;
	
	public static void init()
	{
		bodies = new TextureAtlas(Gdx.files.internal("bodies.atlas"));
		heads = new TextureAtlas(Gdx.files.internal("heads.atlas"));
	}
	
	public static TextureRegion getBody(int bodyIndex)
	{
		return bodies.findRegion(String.valueOf(bodyIndex));
	}
	
	public static TextureRegion getHeads(int headIndex)
	{
		return heads.findRegion(String.valueOf(headIndex));
	}
}
