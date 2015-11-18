package com.yellowbyte.giovannifallout.board;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;

public class ScoreMessage {

	private Sprite damageMessage;
	private String message;
	private TweenManager tweenManager;
	private long startTime, delta;
	private boolean animating = true;
	
	
	public ScoreMessage(String message, Vector2 pos) {
        this.message = message;
		
        damageMessage = new Sprite();
        damageMessage.setPosition(pos.x, pos.y+40);
        damageMessage.setColor(1, 1, 0, 1);
        damageMessage.setScale(0.01f);
		

		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
		
		TweenCallback myCallBack = new TweenCallback(){ 
            @Override
            public void onEvent(int type, BaseTween<?> source) {
            	animating = false;
            }
        };
		
		Tween.to(damageMessage, SpriteAccessor.POS_XY, 200f) //** tween POSITION_Y for a duration **//
        .target(damageMessage.getX(), damageMessage.getY()+250) // ** final POSITION_Y **//
        .ease(TweenEquations.easeOutQuart) //** easing equation **//
        .setCallback(myCallBack)
		.setCallbackTriggers(TweenCallback.END)
        .start(tweenManager); //** start it
		
		Tween.to(damageMessage, SpriteAccessor.TINT, 20f)
		.delay(30f)
        .target(1f, 0f, 0f)
        .ease(TweenEquations.easeNone) 
        .start(tweenManager); 
		
		Tween.to(damageMessage, SpriteAccessor.SCALE_XY, 50f)
        .target(1.2f) 
        .ease(TweenEquations.easeOutElastic)
        .start(tweenManager); 
		
		startTime = TimeUtils.millis();
	}

	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+800)/1000; 
		tweenManager.update(delta); 
		
		Fonts.strikeFont.setScale(damageMessage.getScaleX());
		Fonts.strikeFont.setColor(damageMessage.getColor());
		Fonts.strikeFont.draw(sb, message, damageMessage.getX()-Fonts.strikeFont.getBounds(message).width/2, damageMessage.getY());
	}
	
	public void update() {
	
	}
	
	
	public boolean isAnimating() {
		return animating;
	}
}
