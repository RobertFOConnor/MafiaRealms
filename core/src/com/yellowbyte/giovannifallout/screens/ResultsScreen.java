package com.yellowbyte.giovannifallout.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.google.GoogleConstants;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;
import com.yellowbyte.giovannifallout.tween.SpriteButton;

public class ResultsScreen implements Screen {
	
	private OrthoCamera camera;
	private Vector2 touch;
	
	
	private static Texture dance_sheet = Assets.manager.get(Assets.victory_dance, Texture.class);
	private static TextureRegion VIC1 = new TextureRegion(dance_sheet, 0, 0, 345, 450);
	private static TextureRegion VIC2 = new TextureRegion(dance_sheet, 345, 0, 345, 450);
	private static TextureRegion VIC3 = new TextureRegion(dance_sheet, 690, 0, 345, 450);
	private static Animation VICTORY_ANIM;	
	private static Animation LOSER_ANIM;
	private float guyX = 400;
	
	
	private Animation animation;
	private Texture bg = Assets.manager.get(Assets.results_bg, Texture.class);
	
	private long earnedXP = 0;
	private long earnedGold = 0;
	
	private SpriteButton returnButton;
	private String resultTitle;
	private float stateTime = 0f;
	
	private XPBar xpBar;
	private GoldDisplay goldDisplay;
	private boolean resultsOver = false;
	
	private TweenManager tweenManager;
	private long startTime, delta;
	
	private Music victory = Assets.manager.get(Assets.VICTORY_THEME, Music.class);

	
	public ResultsScreen(boolean win) {
		VICTORY_ANIM = new Animation(1/20f, VIC1, VIC2, VIC3, VIC2);
		VICTORY_ANIM.setPlayMode(Animation.PlayMode.LOOP);
		
		LOSER_ANIM = new Animation(1/20f, new TextureRegion(Assets.manager.get(Assets.LOSER, Texture.class)));
		LOSER_ANIM.setPlayMode(Animation.PlayMode.LOOP);
		
		animation = VICTORY_ANIM;
		MainGame.userStats.addMatchPlayed();
		
		
		
		if(win) {
			resultTitle = Constants.YOU_WIN;
			guyX = 350;
			MainGame.userStats.addPlayerWin();
			
			if (MainGame.googleServices.isSignedIn()) { //UNLOCK ACHIEVEMENTS
				
				int[] matchesPlayedAwards = {1, 5, 10, 50, 100};
				
				for(int i = 0; i < GoogleConstants.NUM_MATCHES_AWARDS.length; i++) {
					if (MainGame.userStats.getPlayerWins() >= matchesPlayedAwards[i]) {
						MainGame.googleServices.unlockAchievementGPGS(GoogleConstants.NUM_MATCHES_AWARDS[i]);
					}
				}
			}	
			
			if(MainGame.settings.isMusicEnabled()) {
				MainGame.GAME_MUSIC.stop();
				victory.play();
			}
			
			earnedXP = (GameManager.towersDestroyed*20)+500;
			earnedGold = 20+(GameManager.towersDestroyed*10)+100;

			if(GameManager.myTurns < 12) {
				earnedXP+=GameManager.damageDone;
                earnedGold+=GameManager.damageDone-10;
			}

		} else {
			resultTitle = Constants.YOU_LOSE;
			animation = LOSER_ANIM;


			if(GameManager.surrender) {
				earnedGold = (GameManager.towersDestroyed*10);
                earnedXP = (GameManager.towersDestroyed*20);
			} else {
				earnedGold = 20+(GameManager.towersDestroyed*10);
                earnedXP = (GameManager.towersDestroyed*20)+100;
			}

		}	
		
		
		MainGame.userStats.setLastMatch(System.nanoTime());
		MainGame.userStats.addXP(earnedXP);
		MainGame.userStats.addGold(earnedGold);
		
		//SUBMIT XP TO LEADERBOARD
		if (MainGame.googleServices.isSignedIn()) {
			MainGame.googleServices.submitScore(MainGame.userStats.getPlayerXP());
		}
		
		MainGame.saveManager.saveDataValue("PLAYER", MainGame.userStats);
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
		xpBar = new XPBar(earnedXP);
	}
	
	
	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		touch = new Vector2(0,0);
		
		returnButton = new SpriteButton(new TextureRegion(Assets.manager.get(Assets.continue_button, Texture.class)), new Vector2(MainGame.WIDTH, 90));
		
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());

			if (returnButton.checkTouch(touch)) {
				SoundManager.play(Assets.CLICK);
				ScreenManager.setScreen(new MainMenuScreen());		
				
				if(MainGame.settings.isMusicEnabled()) {
					victory.stop();
					MainGame.GAME_MUSIC.play();
				}
			}
		}
		
		xpBar.update();
		
		
		
		if(xpBar.isFinished()) {
			if(goldDisplay == null) {
				goldDisplay = new GoldDisplay();
			}		
			goldDisplay.update();
			
			if(goldDisplay.isFinished()) {
				
				if (!resultsOver) {
					Tween.to(returnButton, SpriteAccessor.POS_XY, 20f)
					.target(1127f, 90f).ease(TweenEquations.easeNone)
					.start(tweenManager); // ** start it

					TweenCallback myCallBack = new TweenCallback(){ 
			            @Override
			            public void onEvent(int type, BaseTween<?> source) {
			                startTime = TimeUtils.millis();
			            }
			        };
					Tween.to(returnButton, SpriteAccessor.SCALE_XY, 30f)
					.target(1.1f, 1.1f).ease(TweenEquations.easeNone)
					.repeatYoyo(Tween.INFINITY, 0)
					.setCallback(myCallBack) 
					.setCallbackTriggers(TweenCallback.END) 
        
					.start(tweenManager); // ** start it

					startTime = TimeUtils.millis();

					resultsOver = true;
				}
			}
		} 
	}

	@Override
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+1000)/1000; // **get time delta **//
		tweenManager.update(delta); //** update sprite1 **//
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(bg, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
		
		
		returnButton.draw(sb);
		
		Fonts.mainTitle.draw(sb, resultTitle, (MainGame.WIDTH/2)-Fonts.mainTitle.getBounds(resultTitle).width/2, MainGame.HEIGHT-100);

		stateTime += Gdx.graphics.getDeltaTime();
		sb.draw(animation.getKeyFrame(stateTime, true), guyX, 200);

		sb.end();
		
		xpBar.render(sb);
		
		if(xpBar.isFinished()) {
			goldDisplay.render(sb);
		}
		sb.end();
	}
	
	private abstract class SlideIn {
		
		protected Sprite posXP;
		protected boolean finished = false;
		
		public SlideIn() {
			posXP = new Sprite();
			posXP.setPosition(MainGame.WIDTH, 0);
			
			Tween.to(posXP, SpriteAccessor.POS_XY, 20f)
			.target(1150f, 0f)
			.ease(TweenEquations.easeNone)
			.start(tweenManager); // ** start it

			startTime = TimeUtils.millis();
		}
		
		public abstract void update();
		public abstract void render(SpriteBatch sb);
		
		public boolean isFinished() {
			return finished;
		}
	}
	
	private class GoldDisplay extends SlideIn {
		
		private Texture goldCoin;
		private long countingGold = 0;
		private String goldEarned;
		
		public GoldDisplay() {
			goldCoin = Assets.manager.get(Assets.gold_coin, Texture.class);
            goldEarned = ""+countingGold;
		}
		
		public void update() {		
			if(!finished) {
				if(countingGold < earnedGold) {
					countingGold++;
					SoundManager.play(Assets.GOLD_BEEP);
					goldEarned = ""+countingGold;
				} else {
					finished = true;
				}
			}
		}
		
		public void render(SpriteBatch sb) {
			sb.draw(goldCoin, posXP.getX()-230, 300);
			Fonts.goldFont.draw(sb, goldEarned, posXP.getX(), 430);
		}	
	}
	
	private class XPBar extends SlideIn {
		
		private ShapeRenderer shapeRenderer;
		private Texture xp;
		private float barWidth = 500;		
		private long matchXP = 0; //XP earned from match
		private float countingXP = 0; //XP 
		private float XPMax = 0;
		private float DISP_XP = 0;	
		private String levelString, XPString;
		private int counterSpeed = 1;
		
		
		public XPBar(long earnedXP) {
			shapeRenderer = new ShapeRenderer();
			xp = Assets.manager.get(Assets.xp, Texture.class);
			matchXP = earnedXP;
			XPMax = MainGame.userStats.getXPNeeded();
			levelString = "Level "+MainGame.userStats.getPlayerLevel();
			XPString = "";		
		}

		public void update() {
			
			if (!finished) {
				
				if(counterSpeed < 12) {
					counterSpeed++;
				}
				
				for (int i = 0; i < counterSpeed; i++) {
					if (countingXP < matchXP) {
						countingXP++;
					} else {
						finished = true;
					}
				}
				SoundManager.play(Assets.XP_BEEP);
				
				DISP_XP = (MainGame.userStats.getPlayerXP()-matchXP)+countingXP;
				
				
				if(DISP_XP >= XPMax) {
					
					MainGame.userStats.setPlayerLevel(MainGame.userStats.getPlayerLevel()+1); //LEVEL UP PLAYER
					MainGame.saveManager.saveDataValue("PLAYER", MainGame.userStats);
					
					
					DISP_XP = (MainGame.userStats.getPlayerXP()-matchXP)+countingXP;
					XPMax = MainGame.userStats.getXPNeeded();
					levelString = "Level "+MainGame.userStats.getPlayerLevel();
				} 	
				
				XPString = (int)DISP_XP+"/"+(int)XPMax;
			}				
		}
		
		public void render(SpriteBatch sb) {			
			
			float percent = DISP_XP/XPMax;
			float loadedX = (barWidth*percent);
			
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.identity();
			shapeRenderer.setColor(Color.GRAY);
			shapeRenderer.rect(posXP.getX(), 600, barWidth, 50);
			shapeRenderer.circle(posXP.getX(), 625, 25);
			shapeRenderer.circle(posXP.getX()+barWidth, 625, 25);
			shapeRenderer.setColor(new Color(.8f, 0, 0, 1));
			shapeRenderer.rect(posXP.getX(), 600, loadedX, 50);
			shapeRenderer.circle(posXP.getX(), 625, 25);
			shapeRenderer.circle(posXP.getX()+loadedX, 625, 25);
			shapeRenderer.end();
			
			sb.begin();
			sb.draw(xp, posXP.getX()-260, 550);
			Fonts.menuFont.draw(sb, levelString, posXP.getX(), 710);
			Fonts.EditFont.draw(sb, XPString, posXP.getX(), 580);
			Fonts.MFont.draw(sb, "+"+(int)countingXP, posXP.getX()+40+loadedX, 638);			
		}
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
