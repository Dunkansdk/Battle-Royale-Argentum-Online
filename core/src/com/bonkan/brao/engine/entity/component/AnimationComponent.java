package com.bonkan.brao.engine.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

public class AnimationComponent implements Component {
    @SuppressWarnings("rawtypes")
	public IntMap<Animation> animations = new IntMap<Animation>();
}