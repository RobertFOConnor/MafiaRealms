package com.yellowbyte.giovannifallout;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.card.CustomDecks;
import com.yellowbyte.giovannifallout.card.Deck;
import com.yellowbyte.giovannifallout.google.IGoogleServices;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SaveManager;
import com.yellowbyte.giovannifallout.media.Settings;
import com.yellowbyte.giovannifallout.screens.ScreenManager;
import com.yellowbyte.giovannifallout.screens.SplashScreen;

public class MainGame extends ApplicationAdapter {
	
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;	
	
	public static int numberOfCards = 26;
	public static OrthoCamera camera;	
	private SpriteBatch sb;	
	
	public static UserStatObject userStats;
	public static Settings settings;
	public static boolean firstTime = false; //First time playing the game.
	public static Music GAME_MUSIC;	

	
	public static IGoogleServices googleServices;	
	public static SaveManager saveManager;
	public static CustomDecks cd;
	public static Deck chosenDeck;

	public static final float STEP = 1 / 60f;
	private float accum;
	
	
	public MainGame(IGoogleServices googleServices) {
		super();
		MainGame.googleServices = googleServices;
	}
	
	
	@Override
	public void create () {
		Gdx.input.setCatchBackKey(true);
		sb = new SpriteBatch();
		camera = new OrthoCamera();
		camera.resize();
		
		
		saveManager = new SaveManager(true);
		
		userStats = new UserStatObject();
		cd = new CustomDecks();
		settings = new Settings();
		
		if(saveManager.loadDataValue("PLAYER", UserStatObject.class) != null) {
			userStats = saveManager.loadDataValue("PLAYER", UserStatObject.class);
		} else {
			firstTime = true;
		}

		
		if(saveManager.loadDataValue("DECKS", CustomDecks.class) != null) {
			cd = saveManager.loadDataValue("DECKS", CustomDecks.class);
		} 

		
		if(saveManager.loadDataValue("SETTINGS", Settings.class) != null) {
			settings = saveManager.loadDataValue("SETTINGS", Settings.class);
		} 
		
		ScreenManager.setScreen(new SplashScreen());
	}


	@Override
	public void render() {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (ScreenManager.getCurrentScreen() != null) {
            ScreenManager.getCurrentScreen().update();
            ScreenManager.getCurrentScreen().render(sb);
        }

        if (Gdx.input.isKeyPressed(Keys.BACK)) {
            ScreenManager.getCurrentScreen().goBack();
        }
	}
	
	@Override
	public void resize(int w, int h) {
		
		if(ScreenManager.getCurrentScreen()!=null) 
			ScreenManager.getCurrentScreen().resize(w, h);
	}
	
	@Override
	public void dispose() {
		
		if(ScreenManager.getCurrentScreen()!=null)
			ScreenManager.getCurrentScreen().dispose();
		
		sb.dispose();	
		
		Assets.manager.dispose();
		Assets.manager = null;
	}


	@Override
	public void pause() {
		
		if(ScreenManager.getCurrentScreen()!=null)
			ScreenManager.getCurrentScreen().pause();
	}
	
	@Override
	public void resume() {
		Assets.load();
		while(!Assets.manager.update()){
	      }
		if(ScreenManager.getCurrentScreen()!=null) 
			ScreenManager.getCurrentScreen().resume();
	}
}