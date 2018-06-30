package com.bonkan.brao.engine.utils;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

public final class Physics {

    public static final float PIXELS_PER_UNIT = 1f;
    
    public static float toUnits(float pixels) {
        return pixels / PIXELS_PER_UNIT;
    }
    
    public static Vector toUnits(Vector2 pixels) {
        return new Vector2(toUnits(pixels.x), toUnits(pixels.y));
    }

    public static float toPixels(float units) {
        return units * PIXELS_PER_UNIT;
    }

    public static Vector2 toPixels(Vector2 units) {
        return new Vector2(toPixels(units.x), toPixels(units.y));
    }
}
