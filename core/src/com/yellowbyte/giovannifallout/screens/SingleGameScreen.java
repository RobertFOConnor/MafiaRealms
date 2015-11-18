package com.yellowbyte.giovannifallout.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.media.Assets;

public class SingleGameScreen implements Screen {

	private OrthoCamera camera;
	private GameManager gameManager;
	
	private Texture bg = Assets.manager.get(Assets.GAME_BG, Texture.class);
	

	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		
		gameManager = new GameManager(false);
	}

	@Override
	public void update() {

		gameManager.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(bg, 0, 0);
		sb.end();
		gameManager.render(sb);
		
	}

	@Override
	public void resize(int w, int h) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void goBack() {
		GameManager.paused = true;
		
	}
}
