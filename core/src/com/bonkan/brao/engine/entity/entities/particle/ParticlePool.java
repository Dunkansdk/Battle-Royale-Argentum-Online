package com.bonkan.brao.engine.entity.entities.particle;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticlePool {
	
	private ParticleCache cache;
	private ArrayList<PooledEffect> pool;
	
	/**
	 * <p>Controla los efectos que existen en el screen</p>
	 */
	public ParticlePool() {
		pool = new ArrayList<PooledEffect>();
		cache = new ParticleCache();
		cache.create(ParticleType.TEST1);
		cache.create(ParticleType.TEST2);
		cache.create(ParticleType.EXPLOSION);
	}
	
	/**
	 * <p>Crea un nuevo {@link com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect}</p>
	 * @param particle {@link com.bonkan.brao.engine.entity.entities.particle.ParticleType}
	 * @param x
	 * @param y
	 */
	public void create(ParticleType particle, int x, int y) {
		PooledEffect effect = cache.getPooledEffect(particle);
		effect.setPosition(x, y);
		pool.add(effect);
	}
	
	/**
	 * <p>Crea un nuevo {@link com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect}</p>
	 * @param particle {@link com.bonkan.brao.engine.entity.entities.particle.ParticleType}
	 * @param x
	 * @param y
	 */
	public PooledEffect createPooled(ParticleType particle, int x, int y) {
		PooledEffect effect = cache.getPooledEffect(particle);
		effect.setPosition(x, y);
		pool.add(effect);
		return effect;
	}

	public void update(PooledEffect effect, int x, int y) {
		effect.setPosition(x, y);
	}

	/**
	 * <p>Renderiza, que va a hacer.</p>
	 * @param batch
	 * @param delta
	 */
	public void render(SpriteBatch batch, float delta) {
		Iterator<PooledEffect> it = pool.iterator();
	    while (it.hasNext()) {
	      PooledEffect effect = it.next();
	      effect.draw(batch, delta);
	      if (effect.isComplete()) {
				effect.free();
				it.remove();
			}
	    }
	}

}
