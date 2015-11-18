package com.yellowbyte.giovannifallout.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.TextButton;
import com.yellowbyte.giovannifallout.appwarp.MyWarpListener;
import com.yellowbyte.giovannifallout.appwarp.WarpController;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.gui.Button;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;

public class MatchmakingScreen implements Screen {

	private OrthoCamera camera;
	private Vector2 touch;
	private Button backButton;
	private TextButton settingsButton;
	private Sprite load_wheel;
	private MyWarpListener warpListener;	
	private BackgroundManager bgManager;
	
	
	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		touch = new Vector2(0,0);
		
		bgManager = new BackgroundManager();
		
		TextureRegion back = new TextureRegion(Assets.manager.get(Assets.guisheet, Texture.class), 0, 270, 150, 150);
		backButton = new Button(back, new Vector2((back.getRegionWidth()/3), MainGame.HEIGHT-(back.getRegionHeight()+(back.getRegionWidth()/3))));
		
		warpListener = new MyWarpListener();
		settingsButton = new TextButton(warpListener.getMessage(), Fonts.menuFont, new Vector2(0,MainGame.HEIGHT/2-100));
		settingsButton.center();
		
		
		//INIT LOAD WHEEL
		load_wheel = new Sprite(Assets.manager.get(Assets.load_wheel, Texture.class));
		load_wheel.setPosition(MainGame.WIDTH/2-load_wheel.getWidth()/2, MainGame.HEIGHT/2);
		
		WarpController.getInstance().setListener(warpListener);
	}

	@Override
	public void update() {
		load_wheel.rotate(-6f);
		
		if (Gdx.input.justTouched()) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());

			if (backButton.checkTouch(touch)) {
				WarpController.getInstance().handleLeave();
				ScreenManager.setScreen(new MainMenuScreen());	
				
			}
		}
		settingsButton.setMessage(warpListener.getMessage());
		bgManager.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		bgManager.render(sb);
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		backButton.render(sb);
		settingsButton.render(sb);
		load_wheel.draw(sb);
		sb.end();
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
		ScreenManager.setScreen(new MainMenuScreen());
		
	}
}
