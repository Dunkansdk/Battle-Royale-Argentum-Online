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
	
	public static void init()
	{
		bodies = new TextureAtlas(Gdx.files.internal("bodies.atlas"));
	}
	
	public static TextureRegion getBody(int bodyIndex)
	{
		return bodies.findRegion(String.valueOf(bodyIndex));
	}
}
