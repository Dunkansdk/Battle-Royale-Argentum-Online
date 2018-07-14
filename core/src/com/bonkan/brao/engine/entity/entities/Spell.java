package com.bonkan.brao.engine.entity.entities;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

import java.awt.Rectangle;
import java.util.UUID;

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
	private UUID castedBy;
	private boolean earlyCollision;
	
	private static final float SPEED = 4f;

	/**
	 * Crea un hechizo!
	 * @param x &emsp;<b>(float)</b> initial x
	 * @param y &emsp;<b>(float)</b> initial y
	 * @param destX &emsp;<b>(float)</b> target x
	 * @param destY &emsp;<b>(float)</b> target y
	 * @param effect &emsp;<b>(ParticleType)</b> {@link com.bonkan.brao.engine.entity.entities.particle.ParticleType}
	 */
	public Spell(float x, float y, float destX, float destY, ParticleType effect, UUID castedBy) {
		super((int)x, (int)y);
		// Iniciamos un nuevo pool de particulas para evitar bugs
		this.pool = new ParticlePool();
		// Creamos la particula que va a viajar
		this.effect = pool.createPooled(effect, (int)location.x, (int)location.y);
		this.target = new Vector2(destX, destY);
		this.angle = Math.atan2(destY - y, destX - x);
		this.explosion = false;
		this.castedBy = castedBy;
		this.earlyCollision = false;
	}

	@Override
	public void update(float delta) {
		// Le damos un poco de chanwin para que el rango de collision sea mayor
		if(!target.epsilonEquals(location, 5) && !explosion && !earlyCollision) {
			location.x = (float) (location.x + (Math.sin(angle + 90 * Math.PI / 180)) * SPEED);
			location.y = (float) (location.y - (Math.cos(angle + 90 * Math.PI / 180)) * SPEED);
			pool.update(effect, (int)location.x, (int)location.y);
		} else {
			// Llego a destino o exploto antes
			if(!explosion) pool.createPooled(ParticleType.EXPLOSION, (int)location.x, (int)location.y);
			effect.free(); // Liberamos la particula viajera, dejamos la explosion
			explosion = true;
			earlyCollision = false;
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
		this.earlyCollision = true;
	}

	public Rectangle getRect()
	{
		return new Rectangle((int) location.x, (int) location.y - 10, 10, 10);
	}
	
	public UUID getCastedBy()
	{
		return castedBy;
	}
}
