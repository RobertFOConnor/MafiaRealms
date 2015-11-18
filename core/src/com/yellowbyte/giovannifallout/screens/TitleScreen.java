package com.yellowbyte.giovannifallout.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.box.TextFieldBox;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;
import com.yellowbyte.giovannifallout.tween.SpriteButton;
import com.yellowbyte.giovannifallout.tween.SpriteText;

public class TitleScreen implements Screen {

	private OrthoCamera camera;
	private Vector2 touch;	
	private SpriteButton startButton;
	private float TITLE_X = MainGame.WIDTH/2-Fonts.mainTitle.getBounds(Constants.TITLE).width/2;
	private float TITLE_Y = MainGame.HEIGHT-150;
	private BackgroundManager bgManager;
	private TextFieldBox nameBox;
	
	private SpriteText welcomeMessage;
	private TweenManager tweenManager;
	private long startTime, delta;

	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		touch = new Vector2(0,0);
		
		bgManager = new BackgroundManager();
		startButton = new SpriteButton(new TextureRegion(Assets.manager.get(Assets.start_button, Texture.class)), new Vector2(MainGame.WIDTH/2-200,200));
		nameBox = new TextFieldBox();
		
		welcomeMessage = new SpriteText("", Fonts.EditFont);		
		if(!MainGame.firstTime) {
			welcomeMessage = new SpriteText("Welcome back, "+MainGame.userStats.getPlayerName()+"!", Fonts.EditFont);
		}		
		welcomeMessage.setPosition(MainGame.WIDTH/2-Fonts.EditFont.getBounds(welcomeMessage.getText()).width/2, 120);
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
		
		TweenCallback myCallBack = new TweenCallback(){ 
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                startTime = TimeUtils.millis();
            }
        };
		Tween.to(startButton, SpriteAccessor.SCALE_XY, 30f)
		.target(0.9f, 0.9f).ease(TweenEquations.easeNone)
		.repeatYoyo(Tween.INFINITY, 0)
		.setCallback(myCallBack) 
		.setCallbackTriggers(TweenCallback.END) 

		.start(tweenManager); // ** start it

		startTime = TimeUtils.millis();
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());

			if (startButton.checkTouch(touch) && !nameBox.isShowing()) {
				if (startButton.checkTouch(touch) && MainGame.firstTime) {
					nameBox = new TextFieldBox();
					nameBox.display();
				} else {
					ScreenManager.setScreen(new MainMenuScreen());
				}

			} else if (nameBox.isShowing()) {
				int result = nameBox.update(touch);

				if (result == 1) {
					ScreenManager.setScreen(new MainMenuScreen());
				}
			}
		}
		bgManager.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+1000)/1000; // **get time delta **//
		tweenManager.update(delta); //** update sprite1 **//
		
		bgManager.render(sb);
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		startButton.draw(sb);
		Fonts.menuFont.draw(sb, Constants.START_GAME, MainGame.WIDTH/2-Fonts.menuFont.getBounds(Constants.START_GAME).width/2, startButton.getY()+270);
		Fonts.mainTitle.draw(sb, Constants.TITLE, TITLE_X, TITLE_Y);		
		welcomeMessage.draw(sb);
		Fonts.MFont.draw(sb, Constants.VERSION_CODE, MainGame.WIDTH-100, 60);		
		nameBox.render(sb);
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
		
	}
}