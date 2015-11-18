package com.yellowbyte.giovannifallout.box;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.TextButton;
import com.yellowbyte.giovannifallout.media.Fonts;

public class ChoiceBox extends Box {

	private TextButton cancelButton, OKButton;
	private String title = "Delete this deck?";

	
	public ChoiceBox() {
		super(400, 200);
		float buttonY = MainGame.HEIGHT / 2 - 60;
		cancelButton = new TextButton("Cancel", Fonts.MFont, new Vector2((MainGame.WIDTH/2) -50-Fonts.MFont.getBounds("Cancel").width, buttonY));
		OKButton = new TextButton("OK", Fonts.MFont, new Vector2((MainGame.WIDTH/2) +80, buttonY));
	}
	
	public void display() {
		showing = true;
	}

	public int update(Vector2 touch) {
		if (cancelButton.checkTouch(touch)) {
			showing = false;
			return 0;
		} else if(OKButton.checkTouch(touch)) {
			showing = false;
			return 1;
		}
		
		return -1;
	}

	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+1000)/1000; 
		tweenManager.update(delta); 

		if (showing) {
			boxSprite.draw(sb);
			Fonts.MFont.draw(sb, title, MainGame.WIDTH / 2 - Fonts.MFont.getBounds(title).width / 2,boxSprite.getY()+160);
			
			cancelButton.getPosition().set((MainGame.WIDTH/2) -50-Fonts.MFont.getBounds("Cancel").width, boxSprite.getY()+40);
			OKButton.getPosition().set((MainGame.WIDTH/2) +80, boxSprite.getY()+40);
			
			cancelButton.render(sb);
			OKButton.render(sb);
		}
	}
}
