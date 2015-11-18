package com.yellowbyte.giovannifallout.gui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;

public class GUIMessage {
	
	private String message;
	private Sprite textSprite;
	
	private boolean finished = false;
	
	private TweenManager tweenManager;
	private long startTime, delta;
	
	
	public GUIMessage(String message) {
		this.message = message;
		
		textSprite = new Sprite();
		textSprite.setCenter(MainGame.WIDTH/2-Fonts.menuFont.getBounds(message).width/2, (MainGame.HEIGHT/2)+100);
		textSprite.setScale(0.01f);
		
		startTime = System.nanoTime();
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
		
		TweenCallback myCallBack = new TweenCallback(){ 
            @Override
            public void onEvent(int type, BaseTween<?> source) {
            	finished = true;
            }
        };
		
		Timeline.createSequence()
				.push(Tween.to(textSprite, SpriteAccessor.SCALE_XY, 50f)
						.target(1f, 1f).ease(TweenEquations.easeOutElastic))
				.pushPause(2.0f)
				.push(Tween.to(textSprite, SpriteAccessor.SCALE_XY, 30f)
						.target(0.01f, 0.01f).ease(TweenEquations.easeOutQuint)
						.setCallback(myCallBack)
						.setCallbackTriggers(TweenCallback.END))
				.start(tweenManager);
		
		startTime = TimeUtils.millis();
	}
	
	public void update() {
		
	}
	
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+750)/1000; // **get time delta **//
		tweenManager.update(delta); //** update sprite1 **//
		
		Fonts.menuFont.setScale(textSprite.getScaleX());
		Fonts.menuFont.draw(sb, message, (MainGame.WIDTH/2)-Fonts.menuFont.getBounds(message).width/2, 
										 (MainGame.HEIGHT/2)-Fonts.menuFont.getBounds(message).height/2+(100*textSprite.getScaleX()));
		Fonts.menuFont.setScale(1f);
	}
	
	public boolean isFinished() {
		return finished;
	}
}
