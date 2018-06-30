package com.bonkan.brao.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
