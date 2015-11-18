package com.yellowbyte.giovannifallout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	
	protected TextureRegion texture;
	protected Vector2 pos;
	
	
	public Entity(TextureRegion texture, Vector2 pos) {
		this.texture = texture;
		this.pos = pos;
	}

	public abstract void update();
	
	public void render(SpriteBatch sb) {
		sb.draw(texture, pos.x, pos.y);
	}
	
	public Vector2 getPosition() {
		return pos;
	}
	
	public Vector2 getMidPoint() {
		return new Vector2(pos.x+(texture.getRegionWidth()/2), pos.y+(texture.getRegionHeight()/2));
	}
	
	public Rectangle getBounds() {
		return new Rectangle(pos.x, pos.y, texture.getRegionWidth(), texture.getRegionHeight());
	}
	
	public boolean checkTouch(Vector2 touch) {
		if(getBounds().contains(touch)) {
			return true;
		}
		return false;
	}
	
	public void setTexture(TextureRegion t) {
		texture = t;
	}
}
