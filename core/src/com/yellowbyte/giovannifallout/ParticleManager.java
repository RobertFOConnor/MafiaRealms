package com.yellowbyte.giovannifallout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Array;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;

public class ParticleManager {

	//Particle Objects
	private ParticleEffectPool yellowfeatherPool, spawnPool, effectPool, destroyPool;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	
	private OrthoCamera camera;
	
	public enum EffectType { 
    	Yellow,
    	SPAWN,
    	EFFECT,
    	DESTROY,
    	Blue
   }
	
	public ParticleManager() {
		camera = new OrthoCamera();
		camera.resize();
		
		ParticleEffect particleEffect = new ParticleEffect();
		ParticleEffect particleEffect1 = new ParticleEffect();
		ParticleEffect particleEffect2 = new ParticleEffect();

		
		particleEffect.load(Gdx.files.internal("effects/shot.p"), Gdx.files.internal("effects"));
		yellowfeatherPool = new ParticleEffectPool(particleEffect, 1, 2);
		
		particleEffect.load(Gdx.files.internal("effects/spawn.p"), Gdx.files.internal("effects"));
		spawnPool = new ParticleEffectPool(particleEffect, 1, 2);
		
		particleEffect1.load(Gdx.files.internal("effects/effect.p"), Gdx.files.internal("effects"));
		effectPool = new ParticleEffectPool(particleEffect1, 1, 2);
		
		particleEffect2.load(Gdx.files.internal("effects/destroy.p"), Gdx.files.internal("effects"));
		destroyPool = new ParticleEffectPool(particleEffect2, 1, 2);
	}
	
	public void addEffect(EffectType type, float x, float y) {
		PooledEffect effect;
		
		switch (type) {
		case Yellow:
			effect = yellowfeatherPool.obtain();
			break;
		case SPAWN:
			effect = spawnPool.obtain();
			break;
		case EFFECT:
			effect = effectPool.obtain();
			break;
		case DESTROY:
			effect = destroyPool.obtain();
			break;
		default:
			effect = yellowfeatherPool.obtain();
			break;
		}
		effect.setPosition(x, y);
		effects.add(effect);
	}
	
	public void render(SpriteBatch sb) {
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    effect.draw(sb, Gdx.graphics.getDeltaTime());
		    if (effect.isComplete()) {
		        effect.free();
		        effects.removeIndex(i);
		        effect.dispose();
		    }
		}
		
		sb.end();
	}
	
	public void clearAll() {
		effects = new Array<PooledEffect>();
	}
}
