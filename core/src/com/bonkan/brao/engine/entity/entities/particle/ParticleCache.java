package com.bonkan.brao.engine.entity.entities.particle;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

public class ParticleCache {
	
	private HashMap<ParticleType, ParticleEffectPool> pool;

	public ParticleCache() {
		pool = new HashMap<ParticleType, ParticleEffectPool>();
	}
	
	public void create(ParticleType particle) {
		if(!pool.containsKey(particle)) {
			pool.put(particle, new ParticleEffectPool(particle.obtain(), 0, 1));
		}
	}
	
	public PooledEffect getPooledEffect(ParticleType particle) {
		return pool.get(particle).obtain();	
	}

}
