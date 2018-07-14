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
	
	private static final float SPEED = 4f;

	/**
	 * Crea un hechizo!
	 * @param x &emsp;<b>(float)</b> initial x
	 * @param y &emsp;<b>(float)</b> initial y
	 * @param dest_x &emsp;<b>(float)</b> target x
	 * @param dest_y &emsp;<b>(float)</b> target y
	 * @param effect &emsp;<b>(ParticleType)</b> {@link com.bonkan.brao.engine.entity.entities.particle.ParticleType}
	 */
	public Spell(float x, float y, float dest_x, float dest_y, ParticleType effect) {
		super((int)x, (int)y);
		// Iniciamos un nuevo pool de particulas para evitar bugs
		this.pool = new ParticlePool();
		// Creamos la particula que va a viajar
		this.effect = pool.createPooled(effect, (int)location.x, (int)location.y);
		this.target = new Vector2(dest_x, dest_y);
		this.angle = Math.atan2(dest_y - y, dest_x - x);
		this.explosion = false;
	}

	@Override
	public void update(float delta) {
		// Le damos un poco de chanwin para que el rango de collision sea mayor
		if(!target.epsilonEquals(location, 5) && !explosion) {
			location.x = (float) (location.x + (Math.sin(angle + 90 * Math.PI / 180)) * SPEED);
			location.y = (float) (location.y - (Math.cos(angle + 90 * Math.PI / 180)) * SPEED);
			pool.update(effect, (int)location.x, (int)location.y);
		} else {
			// Llego a destino
			if(!explosion) pool.createPooled(ParticleType.EXPLOSION, (int)target.x, (int)target.y);
			effect.free(); // Liberamos la particula viajera, dejamos la explosion
			explosion = true;
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		pool.render(batch, Gdx.graphics.getDeltaTime());
	}
	
	/**
	 * Collisiona con algun player?! Hacela explotar
	 * @return
	 */
	public void hit() {
		this.explosion = true;
	}

}
