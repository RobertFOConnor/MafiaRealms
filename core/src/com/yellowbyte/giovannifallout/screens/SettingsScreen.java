package com.yellowbyte.giovannifallout.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.TextButton;
import com.yellowbyte.giovannifallout.UserStatObject;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.gui.Button;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;

public class SettingsScreen implements Screen {

	private OrthoCamera camera;
	private Vector2 touch;
	private Button backButton;
	
	private TextButton settingsButton;
	private TextButton musicButton, soundButton, resetButton;
	private String title = "Settings";
	private BackgroundManager bgManager;
	
	
	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		touch = new Vector2(0,0);
		
		bgManager = new BackgroundManager();
		
		TextureRegion back = new TextureRegion(Assets.manager.get(Assets.guisheet, Texture.class), 0, 270, 150, 150);
		backButton = new Button(back, new Vector2((back.getRegionWidth()/3), MainGame.HEIGHT-(back.getRegionHeight()+(back.getRegionWidth()/3))));
		
		
		settingsButton = new TextButton(title, Fonts.menuFont, new Vector2(0,MainGame.HEIGHT-80));
		settingsButton.center();
		
		String musicSetting = "ON";
		String soundSetting = "ON";
		
		if(!MainGame.settings.isMusicEnabled()) {
			musicSetting = "OFF";
		}
		
		if(!MainGame.settings.isSoundFXEnabled()) {
			soundSetting = "OFF";
		}
		
		musicButton = new TextButton(musicSetting, Fonts.menuFont, new Vector2(MainGame.WIDTH-300, MainGame.HEIGHT/2));
		soundButton = new TextButton(soundSetting, Fonts.menuFont, new Vector2(MainGame.WIDTH-300, MainGame.HEIGHT/2-200));
		resetButton = new TextButton("Clear Data", Fonts.MFont, new Vector2(300, MainGame.HEIGHT/2-400));
		
	}
	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());

			if (backButton.checkTouch(touch)) {
				SoundManager.play(Assets.CLICK);
				ScreenManager.setScreen(new MainMenuScreen());
				
			} else if (musicButton.checkTouch(touch)) {
				SoundManager.play(Assets.CLICK);
				MainGame.settings.setMusicEnabled(!MainGame.settings.isMusicEnabled());
				if (MainGame.settings.isMusicEnabled()) {
					musicButton.setMessage("ON");
					MainGame.GAME_MUSIC.play();
				} else {
					musicButton.setMessage("OFF");
					MainGame.GAME_MUSIC.pause();
				}
				MainGame.saveManager.saveDataValue("SETTINGS", MainGame.settings);
				
			} else if (soundButton.checkTouch(touch)) {
				SoundManager.play(Assets.CLICK);
				MainGame.settings.setSoundFXEnabled(!MainGame.settings.isSoundFXEnabled());
				if (MainGame.settings.isSoundFXEnabled()) {
					soundButton.setMessage("ON");
				} else {
					soundButton.setMessage("OFF");
				}
				MainGame.saveManager.saveDataValue("SETTINGS", MainGame.settings);
			} else if (resetButton.checkTouch(touch)) {
				MainGame.userStats = new UserStatObject();
				MainGame.firstTime = true;
				
				SoundManager.play(Assets.CLICK);
				ScreenManager.setScreen(new TitleScreen());
			}
			
			
		}
		bgManager.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		bgManager.render(sb);
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		backButton.render(sb);
		settingsButton.render(sb);
		musicButton.render(sb);
		soundButton.render(sb);
		resetButton.render(sb);
		
		Fonts.menuFont.draw(sb, "Music: ", 300, MainGame.HEIGHT/2);
		Fonts.menuFont.draw(sb, "Sound FX: ", 300, MainGame.HEIGHT/2-200);
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
