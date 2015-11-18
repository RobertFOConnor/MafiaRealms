package com.yellowbyte.giovannifallout.board;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yellowbyte.giovannifallout.Entity;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.ParticleManager;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;

public class Tower extends Entity {
	
	public static final float WIDTH = 150;
	
	private int baseHealth;
	private int currHealth;
	private boolean owner;
	private boolean destroyed;
	
	
	public Tower(boolean owner, Vector2 pos, int baseHealth) {
		super(new TextureRegion(Assets.manager.get(Assets.tower_sheet, Texture.class), 0, 0, 200, 283), pos);
		this.owner = owner;
		this.baseHealth = baseHealth;
		currHealth = baseHealth;
		destroyed = false;
	}

	public int getCurrHealth() {
		return currHealth;
	}

	public void setCurrHealth(int currHealth) {		
		
		if(destroyed == false) {
			this.currHealth = currHealth;
			checkHealth();
		} else {
			this.currHealth = 0;
		}
	}

	
	public void checkHealth() {
		if(currHealth <= 5) {
			texture = new TextureRegion(Assets.manager.get(Assets.tower_sheet, Texture.class), 200, 0, 200,283); //HALF
			
			if(currHealth <= 0) {
				
				destroy();
			} 
		}	
	}
	
	public void destroy() {

		if(!owner) {

			GameManager.towersDestroyed++;

			if (MainGame.googleServices.isSignedIn()) {
				MainGame.googleServices.unlockAchievementGPGS("CgkIsM6Q5OIdEAIQCg");
			}
		}
		
		texture = new TextureRegion(Assets.manager.get(Assets.tower_sheet, Texture.class), 400, 0, 200,283); //DESTROYED
		currHealth = 0;
		destroyed = true;
		GameManager.pm.addEffect(ParticleManager.EffectType.DESTROY, pos.x+Tile.WIDTH/2, pos.y+20);
		SoundManager.play(Assets.TOWERGONE);
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}


	@Override
	public void render(SpriteBatch sb) {
		if(owner) {
			sb.draw(texture, pos.x, pos.y);
		} else {
			sb.draw(texture, pos.x+texture.getRegionWidth(), pos.y, -texture.getRegionWidth(), texture.getRegionHeight());
		}
		

		Fonts.MFont.draw(sb, currHealth + "/" + baseHealth, pos.x+50, pos.y+220);
	}

	@Override
	public void update() {
		
	}
}
