package com.bonkan.brao.engine.entity.entities;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bonkan.brao.engine.entity.Entity;
import com.bonkan.brao.engine.entity.entities.particle.ParticlePool;
import com.bonkan.brao.engine.entity.entities.particle.ParticleType;

public class Spell extends Entity {
	
	private ParticlePool pool;
	private PooledEffect effect;
	private boolean explosion;
	private Vector2 target;
	private double angle;
	
	private static final float SPEED = 3.5f;

	public Spell(float x, float y, float dest_x, float dest_y, ParticleType effect) {
		super((int)x, (int)y);
		this.pool = new ParticlePool();
		this.effect = pool.createPooled(effect, (int)location.x, (int)location.y);
		this.target = new Vector2(dest_x, dest_y);
		this.angle = Math.atan2(dest_y - y, dest_x - x);
		this.explosion = false;
	}

	@Override
	public void update(float delta) {
		if(!target.epsilonEquals(location, 20)) {
			location.x = (int) (location.x + (Math.sin(angle + 90 * Math.PI / 180)) * SPEED);
			location.y = (int) (location.y - (Math.cos(angle + 90 * Math.PI / 180)) * SPEED);
			pool.update(effect, (int)location.x, (int)location.y);
		} else {
			if(!explosion) pool.create(ParticleType.TEST1, (int)target.x, (int)target.y);
			effect.free();
			explosion = true;
		}

	}
	
	public boolean isComplete() {
		return target.epsilonEquals(location, 20);
	}

	@Override
	public void render(SpriteBatch batch) {
		pool.render(batch, Gdx.graphics.getDeltaTime());
	}

}
