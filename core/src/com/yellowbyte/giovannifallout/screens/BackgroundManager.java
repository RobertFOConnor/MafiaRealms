package com.yellowbyte.giovannifallout.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.media.Assets;

public class BackgroundManager {

	private ShapeRenderer shapeRenderer;
	private Texture bg = Assets.manager.get(Assets.MENU_BG_NEAR, Texture.class);
	private Texture bgfar = Assets.manager.get(Assets.MENU_BG_FAR, Texture.class);
	private static float bgX = 0;
	private static float bgfarX = 0;
	
	public BackgroundManager() {
		shapeRenderer = new ShapeRenderer();
	}
	
	
	public void update() {
		bgX-=5;
		bgfarX -=2;
		
		if(bgX <= -MainGame.WIDTH) {
			bgX = 0;
		}
		
		if(bgfarX <= -MainGame.WIDTH) {
			bgfarX = 0;
		}
	}
	
	public void render(SpriteBatch sb) {
		shapeRenderer.setProjectionMatrix(MainGame.camera.combined);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.identity();
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(0f, 0f,(float) MainGame.WIDTH, (float) MainGame.HEIGHT, Color.ORANGE, Color.ORANGE, Color.PURPLE, Color.PURPLE);
		shapeRenderer.end();
		
		sb.setProjectionMatrix(MainGame.camera.combined);
		sb.begin();
		sb.draw(bgfar, bgfarX, 0);
		sb.draw(bgfar, bgfarX+MainGame.WIDTH, 0);
		sb.draw(bg, bgX, 0);
		sb.draw(bg, bgX+MainGame.WIDTH, 0);
		sb.end();
	}
}
