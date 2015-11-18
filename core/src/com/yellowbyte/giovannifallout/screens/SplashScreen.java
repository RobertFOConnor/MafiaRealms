package com.yellowbyte.giovannifallout.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;

public class SplashScreen implements Screen {

	private OrthoCamera camera;
	private ShapeRenderer shapeRenderer;
	private Sprite loadingImage, loadWheel;
	private Texture black;
	private float alpha = 1;
	private TweenManager tweenManager;
	private long startTime;
    private long delta;
	private boolean fontsLoaded = false;

	
	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();	
		shapeRenderer = new ShapeRenderer();
		
		loadingImage = new Sprite(new Texture(Gdx.files.internal("images/logo.png")));
		loadingImage.setCenter(MainGame.WIDTH / 2, MainGame.HEIGHT / 2 - 50);
        loadingImage.setScale(0f);

        loadWheel = new Sprite(new Texture(Gdx.files.internal("images/small_load_wheel.png")));
        loadWheel.setPosition(30,30);

		black = new Texture(Gdx.files.internal("images/black.png"));

		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
		Tween.to(loadingImage, SpriteAccessor.SCALE_XY, 70f)
				.target(1f, 1f)
				.ease(TweenEquations.easeOutElastic)
				.start(tweenManager); // ** start it

		startTime = TimeUtils.millis();

		Assets.load();
	}

	@Override
	public void update() {
		
		if(!Assets.update()) {
			alpha-=0.1;
			if(alpha <= 0) {
				alpha = 0;
			}

			if(Assets.manager.getProgress() > 0.7f && !fontsLoaded) {
				Fonts.initFonts();
				fontsLoaded = true;
			}
		}
		
		if(Assets.update()){

			if(alpha >= 1) {
				ScreenManager.setScreen(new TitleScreen());

				
				MainGame.GAME_MUSIC = Assets.manager.get(Assets.MAIN_THEME, Music.class);
				MainGame.GAME_MUSIC.setLooping(true);
				
				if(MainGame.settings.isMusicEnabled()) {
					MainGame.GAME_MUSIC.play();
				}
				
			} else {
				alpha+=0.1;
			}			
		}
        loadWheel.rotate(-20f);
	}

	@Override
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+1000)/1000; // **get time delta **//
		tweenManager.update(delta); //** update sprite1 **//
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		loadingImage.draw(sb);
        //loadWheel.draw(sb);
		sb.setColor(1, 1, 1, alpha);	
		sb.draw(black, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
		sb.setColor(1, 1, 1, 1);
		sb.end();
		
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.identity();
		shapeRenderer.setColor(Color.LIGHT_GRAY);
		shapeRenderer.rect(0, MainGame.HEIGHT-50, MainGame.WIDTH, 50);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(0, MainGame.HEIGHT-50, (Assets.manager.getProgress()*MainGame.WIDTH), 50);
		shapeRenderer.end();
	}


	@Override
	public void resize(int w, int h) {
		
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
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

	}
}
