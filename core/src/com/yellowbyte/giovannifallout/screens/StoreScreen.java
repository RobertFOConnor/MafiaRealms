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
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.card.CardCreator;
import com.yellowbyte.giovannifallout.gui.Button;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;
import com.yellowbyte.giovannifallout.tween.SpriteButton;
import com.yellowbyte.giovannifallout.tween.SpriteText;

public class StoreScreen implements Screen {

	private OrthoCamera camera;
	private Vector2 touch;	
	private Button backButton;
	private BackgroundManager bgManager;
	
	private SpriteButton randomCardButton;
	private final long randomCardCost = 100;
	
	private SpriteButton rareCardButton;
	private final long rareCardCost = 1000;
	
	private Card purchasedCard;
	private boolean showingCard = false;
	private Sprite cardAnimSprite;
	private SpriteText unlockedText;
	private Texture alpha;
	private String currGold;
	
	private TweenManager tweenManager;
	private long startTime, delta;
	

	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		touch = new Vector2(0,0);
		TextureRegion back = new TextureRegion(Assets.manager.get(Assets.guisheet, Texture.class), 0, 270, 150, 150);
		backButton = new Button(back, new Vector2((back.getRegionWidth()/3), MainGame.HEIGHT-(back.getRegionHeight()+(back.getRegionWidth()/3))));
		bgManager = new BackgroundManager();
		
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
		cardAnimSprite = new Sprite();
		cardAnimSprite.setCenter(MainGame.WIDTH/2, MainGame.HEIGHT/2);
		cardAnimSprite.setScale(0.001f);
		
		alpha = Assets.manager.get(Assets.alpha, Texture.class);
		randomCardButton = new SpriteButton(new TextureRegion(Assets.manager.get(Assets.random_card, Texture.class)), new Vector2(MainGame.WIDTH/4-250, 300));
		rareCardButton = new SpriteButton(new TextureRegion(Assets.manager.get(Assets.rare_card, Texture.class)), new Vector2(MainGame.WIDTH-MainGame.WIDTH/4-250, 300));
		
		
		unlockedText = new SpriteText("You Have Unlocked", Fonts.menuFont);
		unlockedText.setPosition(MainGame.WIDTH/2-Fonts.menuFont.getBounds(unlockedText.getText()).width/2, MainGame.HEIGHT-100);
		
		currGold = "Gold: "+MainGame.userStats.getPlayerGold();
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());
			
			if (showingCard) {

				TweenCallback myCallBack = new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						purchasedCard = null;
						showingCard = false;
					}
				};

				Tween.to(cardAnimSprite, SpriteAccessor.SCALE_XY, 10f)
						.target(0.001f, 0.001f).ease(TweenEquations.easeNone)
						.setCallback(myCallBack)
						.setCallbackTriggers(TweenCallback.END)

						.start(tweenManager);
				startTime = TimeUtils.millis();

			} else {

				if (backButton.checkTouch(touch)) {
					SoundManager.play(Assets.CLICK);
					ScreenManager.setScreen(new MainMenuScreen());

				} else if (randomCardButton.checkTouch(touch)) {

					long myGold = MainGame.userStats.getPlayerGold();

					if (myGold >= randomCardCost) {
						MainGame.userStats.addGold(-randomCardCost);

						purchasedCard = CardCreator.createCard((int) (Math
								.random() * MainGame.numberOfCards + 1));
						MainGame.userStats.getPlayerCardList().insert(0,
								purchasedCard.getID());
						MainGame.saveManager.saveDataValue("PLAYER",
								MainGame.userStats);

						currGold = "Gold: "
								+ MainGame.userStats.getPlayerGold();
						Tween.to(cardAnimSprite, SpriteAccessor.SCALE_XY, 10f)
								.target(1.5f, 1.5f)
								.ease(TweenEquations.easeNone)
								.start(tweenManager);
						startTime = TimeUtils.millis();
						showingCard = true;
					}
				} else if (rareCardButton.checkTouch(touch)) {

					long myGold = MainGame.userStats.getPlayerGold();

					if (myGold >= rareCardCost) {
						MainGame.userStats.addGold(-rareCardCost);

						purchasedCard = CardCreator.createCard((int) (Math
								.random() * MainGame.numberOfCards + 1));
						MainGame.userStats.getPlayerCardList().insert(0,
								purchasedCard.getID());
						MainGame.saveManager.saveDataValue("PLAYER",
								MainGame.userStats);

						currGold = "Gold: "
								+ MainGame.userStats.getPlayerGold();
						Tween.to(cardAnimSprite, SpriteAccessor.SCALE_XY, 10f)
								.target(1.5f, 1.5f)
								.ease(TweenEquations.easeNone)
								.start(tweenManager);
						startTime = TimeUtils.millis();
						showingCard = true;
					}
				}
			}
		}
		bgManager.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+700)/1000; 
		tweenManager.update(delta); 
		bgManager.render(sb);
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		backButton.render(sb);
		randomCardButton.draw(sb);
		rareCardButton.draw(sb);
		
		Fonts.MFont.draw(sb, "Cost: "+randomCardCost, randomCardButton.getX(), randomCardButton.getY()-40);
		Fonts.MFont.draw(sb, "Cost: "+rareCardCost, rareCardButton.getX(), rareCardButton.getY()-40);
		
		Fonts.MFont.draw(sb, currGold, MainGame.WIDTH-Fonts.MFont.getBounds(currGold).width-40, MainGame.HEIGHT-50);
		
		if(showingCard) {
			sb.draw(alpha, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
			purchasedCard.render(new Vector2(MainGame.WIDTH/2-(Card.WIDTH/2*cardAnimSprite.getScaleX()),  MainGame.HEIGHT/2-(Card.HEIGHT/2*cardAnimSprite.getScaleX())), cardAnimSprite.getScaleX(), sb);
			unlockedText.draw(sb);
		}		
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
