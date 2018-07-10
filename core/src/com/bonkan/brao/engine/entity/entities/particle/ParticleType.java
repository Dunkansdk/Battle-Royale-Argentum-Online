package com.bonkan.brao.engine.entity.entities.particle;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.bonkan.brao.engine.utils.AssetsManager;

public enum ParticleType {
	
	TEST1("particle.p"), 
	TEST2("particle2.p");

	private ParticleEffect effect;
	
	private ParticleType (String file) {
		this.effect = AssetsManager.getParticle(file);
		this.effect.loadEmitterImages(AssetsManager.getParticlesAtlas());
	}
	
	public ParticleEffect obtain() {
		return effect;
	}
}
