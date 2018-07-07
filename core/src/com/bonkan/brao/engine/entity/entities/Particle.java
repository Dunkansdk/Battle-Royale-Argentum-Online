package com.bonkan.brao.engine.entity.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bonkan.brao.engine.entity.Entity;
import com.bonkan.brao.engine.utils.AtlasManager;


public class Particle extends Entity {
	
	public enum ParticleType {
		TEST1("particle.p"), TEST2("particle2.p");

		public String file;

		private ParticleType (String file) {
			this.file = file;
		}
	}
		
	private ParticleEffect effect;

	public Particle(ParticleType particle, int x, int y) {
		super(x, y);
		this.effect = new ParticleEffect();
		this.effect.load(Gdx.files.internal(particle.file), AtlasManager.getParticleAtlas());
		this.effect.start();
		this.effect.setPosition(x, y);
	}

	@Override	
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch) {
		// La concha de tu madre a vos que inventaste esta funcion de mierda y no la polimorfiaste para el update, jodeputa
		effect.draw(batch, Gdx.graphics.getDeltaTime());
	}

	public boolean isComplete() {
		return effect.isComplete();
	}

}
