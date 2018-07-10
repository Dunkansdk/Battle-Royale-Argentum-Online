package com.bonkan.brao.engine.entity.entities.particle;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticlePool {
	
	private ParticleCache cache;
	private ArrayList<PooledEffect> pool;
	
	public ParticlePool() {
		pool = new ArrayList<PooledEffect>();
		cache = new ParticleCache();
		cache.create(ParticleType.TEST1);
		cache.create(ParticleType.TEST2);
	}
	
	public void create(ParticleType particle, int x, int y) {
		PooledEffect effect = cache.getPooledEffect(particle);
		effect.setPosition(x, y);
		pool.add(effect);
	}

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
