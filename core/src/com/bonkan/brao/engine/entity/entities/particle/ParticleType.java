package com.bonkan.brao.engine.entity.entities.particle;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.bonkan.brao.engine.utils.AssetsManager;

/**
 * Maneja los archivos y encapsula los effectos de las particulas para acceder mas facil
 */
public enum ParticleType {
	
	TEST1("particle.p"), 
	TEST2("particle2.p");

	private ParticleEffect effect;
	
	/**
	 * <p>Constructor</p>
	 * @param file &emsp;<b>(String)</b> archivo donde se almacenan los datos de la particula
	 */
	private ParticleType (String file) {
		this.effect = AssetsManager.getParticle(file);
		this.effect.loadEmitterImages(AssetsManager.getParticlesAtlas());
	}
	
	/**
	 * Retorna el ParticleEffect del ParticleType correspondiente
	 * @return {@link com.badlogic.gdx.graphics.g2d.ParticleEffect}
	 */
	public ParticleEffect obtain() {
		return effect;
	}
}
