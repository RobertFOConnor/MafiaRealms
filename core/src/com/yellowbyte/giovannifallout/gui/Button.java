package com.yellowbyte.giovannifallout.gui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yellowbyte.giovannifallout.Entity;

public class Button extends Entity{

	public Button(TextureRegion texture, Vector2 pos) {
		super(texture, pos);
	}

	@Override
	public void update() {
		
	}
	
	public void setPosition(Vector2 pos) {
		this.pos = pos;
	}
}
