package com.yellowbyte.giovannifallout.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.TextButton;
import com.yellowbyte.giovannifallout.UserStatObject;
import com.yellowbyte.giovannifallout.appwarp.WarpController;
import com.yellowbyte.giovannifallout.box.AlertBox;
import com.yellowbyte.giovannifallout.box.DeckSelectionBox;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.gui.Button;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;
import com.yellowbyte.giovannifallout.tween.ButtonTween;

public class MainMenuScreen implements Screen {

	private OrthoCamera camera;
	private Vector2 touch;
	private TextButton singleButton, multiButton, cardsButton, howToButton;
	private Array<TextButton> textButtonArray;
	private Button achievementButton, scoreButton, settingsButton, exitButton;
	private BackgroundManager bgManager;
	private String[] playerStats;
	private DeckSelectionBox loadBox = new DeckSelectionBox();
	private AlertBox ab;
	private int gameType = 0; // 1 = 1Player | 2 = 2Player
	private TweenManager tweenManager;
	private long startTime;
    private long delta;

	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		touch = new Vector2(0,0);
		bgManager = new BackgroundManager();
		
		float gapMargin = 200;
		float topMargin = MainGame.HEIGHT - 220;
		
		singleButton = new TextButton(Constants.SINGLE_PLAYER, Fonts.menuFont, new Vector2(0, -40));
		singleButton.center();
		multiButton = new TextButton(Constants.MULTIPLAYER, Fonts.menuFont, new Vector2(0, -40));
		multiButton.center();
		cardsButton = new TextButton(Constants.VIEW_CARDS, Fonts.menuFont, new Vector2(0, -40));
		cardsButton.center();
		howToButton = new TextButton(Constants.HOWTOPLAY, Fonts.menuFont, new Vector2(0, -40));
		howToButton.center();
		
		Texture iconSheet = Assets.manager.get(Assets.menu_icons, Texture.class);
		
		TextureRegion achievements = new TextureRegion(iconSheet, 0, 0, 100, 100);
		achievementButton = new Button(achievements, new Vector2(MainGame.WIDTH-400, 40));
		
		TextureRegion leaderboard = new TextureRegion(iconSheet, 100, 0, 100, 100);
		scoreButton = new Button(leaderboard, new Vector2(MainGame.WIDTH-280, 40));
		
		TextureRegion settings = new TextureRegion(iconSheet, 200, 0, 100, 100);
		settingsButton = new Button(settings, new Vector2(MainGame.WIDTH-140, 40));

		exitButton = new Button(new TextureRegion(Assets.manager.get(Assets.gui_sheet, Texture.class), 0, 150, 100, 100), new Vector2(MainGame.WIDTH-150, (MainGame.HEIGHT-150)));
		
		playerStats = new String[4];
        UserStatObject stats = MainGame.userStats;

		playerStats[0] = "Player: "+stats.getPlayerName();
		playerStats[1] = "Level: "+stats.getPlayerLevel();
		playerStats[2] = "XP: "+stats.getPlayerXP();
		playerStats[3] = "Gold: "+stats.getPlayerGold();
		
		
		Tween.registerAccessor(TextButton.class, new ButtonTween());
		tweenManager = new TweenManager();
		
		textButtonArray = new Array<TextButton>();
		textButtonArray.add(singleButton);
		textButtonArray.add(multiButton);
		textButtonArray.add(cardsButton);
		textButtonArray.add(howToButton);
		
		for(int i = 0; i < textButtonArray.size; i++) {
			Tween.to(textButtonArray.get(i), ButtonTween.POSITION_Y, 30f+(10f*i)) 
	        .target(topMargin-(gapMargin*i)) 
	        .ease(TweenEquations.easeOutElastic) 
	        .start(tweenManager); 
			
		}
		
		startTime = TimeUtils.millis();
	}

	@Override
	public void update() {
		
		if (Gdx.input.justTouched()) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());

			if (!loadBox.isShowing()) {
				
				if(ab != null) {
					ab = null;
				} else if (singleButton.checkTouch(touch)) {
					SoundManager.play(Assets.CLICK);
                    displayDecks(1);

				} else if (multiButton.checkTouch(touch)) {
                    SoundManager.play(Assets.CLICK);
                    displayDecks(2);
					
				} else if (cardsButton.checkTouch(touch)) {
					changeScreen(new CardScreen());

				} else if (howToButton.checkTouch(touch)) {
					changeScreen(new HowToScreen());

				} else if (scoreButton.checkTouch(touch)) {
					SoundManager.play(Assets.CLICK);
					//MainGame.googleServices.showScores(); //Show leaderboard.
					MainGame.googleServices.getAchievementsGPGS(); // Show
																	// achievements.
					
				} else if (achievementButton.checkTouch(touch)) {
					//SoundManager.play(Assets.CLICK);
					//MainGame.googleServices.rateGame();
					changeScreen(new StoreScreen());

				} else if (settingsButton.checkTouch(touch)) {
					changeScreen(new SettingsScreen());

				} else if (exitButton.checkTouch(touch)) {
                    Gdx.app.exit();
                }
				
			} else {

				int result = loadBox.update(touch);
				
				if(result != -1) {
					
					MainGame.chosenDeck = loadBox.getDeck(result);
					
					if(gameType == 1) {
						changeScreen(new SingleGameScreen());
					} else if(gameType == 2) {
						WarpController.getInstance().startApp(MainGame.userStats.getPlayerName());
						changeScreen(new MatchmakingScreen());
					} 
				}
			}

		}
		bgManager.update();
	}

    private void displayDecks(int gameType) {
        if(MainGame.cd.getDeckNames().size <=0) {
            ab = new AlertBox("You have no saved decks!");
        } else {
            this.gameType = gameType;
            loadBox = new DeckSelectionBox();
            loadBox.display();
        }
    }

    @Override
	public void render(SpriteBatch sb) {
		
		delta = (TimeUtils.millis()-startTime+700)/1000; // **get time delta **//
		tweenManager.update(delta); //** update sprite1 **//
		
		bgManager.render(sb);
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(Assets.manager.get(Assets.alpha, Texture.class), (MainGame.WIDTH/2)-250, 0, 500, MainGame.HEIGHT);
		
		for(TextButton tb : textButtonArray) {
			tb.render(sb);
		}

		scoreButton.render(sb);
		achievementButton.render(sb);
		settingsButton.render(sb);
        exitButton.render(sb);
		
		for(int i = 0; i < playerStats.length; i++) {
			
			Fonts.MFont.draw(sb, playerStats[i], 40,MainGame.HEIGHT-(60*(i+1)));
		}
		
		loadBox.render(sb);
		
		if(ab != null) {
			ab.render(sb);
		}
		
		sb.end();
	}
	
	public void changeScreen(Screen s) {
		SoundManager.play(Assets.CLICK);
		ScreenManager.setScreen(s);
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