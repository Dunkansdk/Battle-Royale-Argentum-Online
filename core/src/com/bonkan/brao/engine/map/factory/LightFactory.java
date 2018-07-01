package com.bonkan.brao.engine.map.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightFactory {
	
	public static PointLight createPointLight(RayHandler rayHandler, Body body, Color c, float dist) {
        PointLight pl = new PointLight(rayHandler, 120, c, dist, 0, 0);
        pl.setSoftnessLength(0f);
        pl.attachToBody(body);
        pl.setXray(false);
        return pl;
    }
    public static PointLight createPointLight(RayHandler rayHandler, float x, float y, Color c, float dist) {
        PointLight pl = new PointLight(rayHandler, 120, c, dist, x, y);
        pl.setSoftnessLength(0f);
        pl.setXray(false);
        return pl;
    }

    /* Cone Lights */
    public static ConeLight createConeLight(RayHandler rayHandler, Body body, Color c, float dist, float dir, float cone) {
        ConeLight cl = new ConeLight(rayHandler, 120, c, dist, 0, 0, dir, cone);
        cl.setSoftnessLength(0f);
        cl.attachToBody(body);
        cl.setXray(false);
        return cl;
    }
    public static ConeLight createConeLight(RayHandler rayHandler, float x, float y, Color c, float dist, float dir, float cone) {
        ConeLight cl = new ConeLight(rayHandler, 120, c, dist, x, y, dir, cone);
        cl.setSoftnessLength(0f);
        cl.setXray(false);
        return cl;
    }

}
