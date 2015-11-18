package com.yellowbyte.giovannifallout.box;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class Box {

	private Texture bg = Assets.manager.get(Assets.page, Texture.class);
	protected float width, height;
	protected Sprite boxSprite;
	protected boolean showing = false;
	
	protected TweenManager tweenManager;
	protected long startTime, delta;
	
	public Box(float width, float height) {
		this.width = width;
		this.height = height;
		
		boxSprite = new Sprite(bg);
		boxSprite.setSize(width, height);
        boxSprite.setCenter(MainGame.WIDTH / 2, MainGame.HEIGHT+height);
        
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
		
		Tween.to(boxSprite, SpriteAccessor.POS_XY, 20f)
        .target(boxSprite.getX(), MainGame.HEIGHT/2-height/2)
        .ease(TweenEquations.easeNone) 
        .start(tweenManager); 
        
        startTime = TimeUtils.millis();
	}
	
	public boolean isShowing() {
		return showing;
	}
}
