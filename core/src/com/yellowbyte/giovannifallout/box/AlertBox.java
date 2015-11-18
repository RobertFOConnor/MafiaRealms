package com.yellowbyte.giovannifallout.box;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.media.Fonts;

public class AlertBox extends Box {

	private String message;
	private static float boundX = 80;
	
	public AlertBox(String message) {
		super(Fonts.alertFont.getBounds(message).width+boundX, Fonts.alertFont.getBounds(message).height+boundX);
		this.message = message;
	}
	
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+1000)/1000; 
		tweenManager.update(delta); 
		
		boxSprite.draw(sb);
		Fonts.alertFont.draw(sb, message, MainGame.WIDTH/2-Fonts.alertFont.getBounds(message).width/2, 
											boxSprite.getY()+80);
	} 
}
