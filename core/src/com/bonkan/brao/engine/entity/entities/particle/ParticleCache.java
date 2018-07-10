package com.bonkan.brao.engine.entity.entities.particle;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

/**
 * <p>Se encarga de almacenar los efectos</p>
 */
public class ParticleCache {
	
	private HashMap<ParticleType, ParticleEffectPool> pool;
	
	/**
	 * <p>Cache de efectos para hacer lo que libgdx quiere que haga</p>
	 */
	public ParticleCache() {
		pool = new HashMap<ParticleType, ParticleEffectPool>();
	}
	
	/**
	 * <p>Agrega un efecto nuevo al pool de efectos del juego</p>
	 * @param particle
	 */
	public void create(ParticleType particle) {
		if(!pool.containsKey(particle)) {
			pool.put(particle, new ParticleEffectPool(particle.obtain(), 0, 1));
		}
	}
	
	/**
	 * <p>Obtiene el {@link com.badlogic.gdx.graphics.g2d.ParticleEffect} de un pool de efectos determinado</p>
	 * @param particle {@link com.bonkan.brao.engine.entity.entities.particle.ParticleType}
	 * @return {@link com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect}
	 */
	public PooledEffect getPooledEffect(ParticleType particle) {
		return pool.get(particle).obtain();	
	}

}
