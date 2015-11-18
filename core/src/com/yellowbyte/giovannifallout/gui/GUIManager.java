package com.yellowbyte.giovannifallout.gui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.Player;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;
import com.yellowbyte.giovannifallout.touch.TouchManager;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;
import com.yellowbyte.giovannifallout.tween.SpriteButton;

public class GUIManager {
	
	private Vector2 touch;
	private Array<Button> guiButtons;
	private Button exitButton, magButton, endTurnButton;
	private Player user, opponent;	
	private GUIHand hand;
	private static GUIMessage message;	
	public static boolean magnified = false;
	
	private static Texture guiSheet = Assets.manager.get(Assets.gui_sheet, Texture.class);
	
	private static TextureRegion CARD_BORDER = new TextureRegion(guiSheet, 0, 250, 1150, 100);
	private static TextureRegion END_TURN = new TextureRegion(guiSheet, 0, 350, 280, 280);
	private static TextureRegion END_TURN_GRAYED = new TextureRegion(guiSheet, 280, 350, 280, 280);
	private Texture alpha = Assets.manager.get(Assets.alpha, Texture.class);
	private Sprite timeBox = new Sprite(Assets.manager.get(Assets.time_box, Texture.class));
	
	
	//SACRIFICING BUTTONS  --* CASH OR CARDS *--
	private Array<SpriteButton> sacButtons;
	private SpriteButton sacRes, sacCards;
	private static Texture sac_sheet = Assets.manager.get(Assets.sac_sheet, Texture.class);
	private static TextureRegion SAC_RES = new TextureRegion(sac_sheet, 0, 0, 120, 139);
	private static TextureRegion SAC_CARDS = new TextureRegion(sac_sheet, 120, 0, 120, 139);
	private boolean showingSacrificeOptions = false;
	
	
	private TextureRegion PROFILE_PIC, PROFILE_PIC_2;
	private TextureRegion NAME_BAR_N = new TextureRegion(Assets.manager.get(Assets.name_bar, Texture.class));
	
	private TextureRegion nameBar = NAME_BAR_N;
	private TextureRegion oppNameBar = NAME_BAR_N;
	private Sprite hourGlass;
	
	
	// MEASUREMENTS
	private static float P1NAME_X = 180;
	private static float P2NAME_X;
	private static float NAME_Y = MainGame.HEIGHT-15;
	private Sprite magSprite;
	public static final Vector2 magCardPos = new Vector2((MainGame.WIDTH/2)-Card.WIDTH,100);
	private Vector2 magButtonPos;
	
	// TIMER
	private long startTurnTime; // Timer variables.
	private long duration = 91000;
	private long timeElapsed;
	private String timer;
	
	private TweenManager tweenManager;
	private long startTime, delta;
	
	
	public GUIManager(TouchManager tm, Player user, Player opponent) {
		this.user = user;
		this.opponent = opponent;		
		Texture profileSheet = Assets.manager.get(Assets.profile_sheet, Texture.class);
		PROFILE_PIC = new TextureRegion(profileSheet, 150*MainGame.userStats.getProfilePic(), 0, 150, 150);		
		int oppProfilePic = 1;
		if(!GameManager.multiplayer) {
			oppProfilePic = 4;
		} 
		PROFILE_PIC_2 = new TextureRegion(profileSheet, 150*oppProfilePic, 0, 150, 150);
		
		
		P2NAME_X = (MainGame.WIDTH-P1NAME_X) - Fonts.MFont.getBounds(opponent.getName()).width;	
		initButtons();		
		touch = new Vector2(0,0);	
		hand = new GUIHand(tm, user.getHand());
		
		//Initialize Timer
		startTurnTime = System.nanoTime();
		setEndButtonActive(true);
		
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
	}
	
	private void initButtons() {
		exitButton = new Button(new TextureRegion(guiSheet, 0, 150, 100, 100), new Vector2(MainGame.WIDTH/2+200, (MainGame.HEIGHT-110)));
		magButton = new Button(new TextureRegion(Assets.manager.get(Assets.mag_button, Texture.class)), new Vector2(MainGame.WIDTH/2-300, MainGame.HEIGHT-110));
		endTurnButton = new Button(END_TURN_GRAYED, new Vector2(MainGame.WIDTH-END_TURN_GRAYED.getRegionWidth(), 0));	
		
		sacRes = new SpriteButton(SAC_RES, new Vector2(40,-139));
		sacCards = new SpriteButton(SAC_CARDS, new Vector2(220,-139));
		sacButtons = new Array<SpriteButton>();
		sacButtons.add(sacRes);
		sacButtons.add(sacCards);
		magButtonPos = new Vector2();
		magButton.setPosition(magButtonPos);
		guiButtons = new Array<Button>();
		guiButtons.add(exitButton);
		guiButtons.add(endTurnButton);	
		hourGlass = new Sprite(Assets.manager.get(Assets.hourglass, Texture.class));
		hourGlass.setPosition(80, 40);
		magSprite = new Sprite();


        timeBox.setPosition(MainGame.WIDTH/2-timeBox.getWidth()/2, MainGame.HEIGHT-timeBox.getHeight());
	}

	public void update() {
		
		if (Gdx.input.justTouched() && !GameManager.paused) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());

			if (magnified) {
				magnifyCard(false);

				startTime = TimeUtils.millis();
			} else {

				// CHECK FOR BUTTON TOUCHES.

				if (magButton.checkTouch(touch)) {
					if (!magnified && hand.getCard() != null) {
						magnified = true;
						
						magSprite.setPosition(hand.getCard().getPos().x, hand.getCard().getPos().y);
						magSprite.setScale(1f);
						
						magnifyCard(true);
					}

				} else if (exitButton.checkTouch(touch)) {
					GameManager.paused = true;
				}

				if (user.isPlacing()) {

					if (!user.hasSacrificed() && hand.getCard() != null) {
						
						if (sacRes.checkTouch(touch)) {
								sacrificeResource();

						} else if (sacCards.checkTouch(touch)) {

							if (user.getHand().size < 8) {
								sacrificeCards();
							}
						}
					}

					if (endTurnButton.checkTouch(touch)) {
						endTurn();
					}
				}
			}
		}
		hand.update(); // Display hand.
		if (hand.getCard() != null) {
			magButtonPos = new Vector2(hand.getCard().getPos().x + Card.WIDTH / 2 - 62, hand.getCard().getPos().y + 140);
			magButton.setPosition(magButtonPos);
		}

		updateTimer(); // Increment Timer!

		if (message != null) { // UPDATE MESSAGE.
			message.update();

			if (message.isFinished()) {
				message = null;
			}
		}
	}

	private void updateTimer() {
		
		if(!user.isAttacking() && !opponent.isAttacking()) {
			timeElapsed = duration - ((System.nanoTime() - startTurnTime) / 1000000);

			// Check if time is up.
			if (timeElapsed < 0) {
				
				if(user.isPlacing()) {  // Your time is up!!!
					message = new GUIMessage(Constants.TIME_UP);
					endTurn();
				}
				
				timeElapsed = 0; // His time is up...
			}
		}
	}

	
	public void render(SpriteBatch sb) {
		
		// DRAW NAMES AND TIMER
		delta = (TimeUtils.millis()-startTime+1000)/1000; 
		tweenManager.update(delta); 
		
		
		//P1
		sb.draw(PROFILE_PIC, 0, MainGame.HEIGHT-150);
		
		if(!user.isPlacing()) {
			sb.draw(alpha, 0, MainGame.HEIGHT-150, 150, 150);
		}		
		sb.draw(nameBar, 150, MainGame.HEIGHT-60);
		Fonts.moneyFont.draw(sb, "$"+user.getCurrRes()+" / $"+user.getBaseRes(), P1NAME_X, NAME_Y-80);
		
		
		//P2
		sb.draw(PROFILE_PIC_2, MainGame.WIDTH, MainGame.HEIGHT-150, -150, 150);
		if(!opponent.isPlacing()) {
			sb.draw(alpha, MainGame.WIDTH-150, MainGame.HEIGHT-150, 150, 150);
		}	
		sb.draw(oppNameBar, MainGame.WIDTH-150, MainGame.HEIGHT-60, -oppNameBar.getRegionWidth(), oppNameBar.getRegionHeight());	
		String enemyRes = "$"+opponent.getCurrRes()+" / $"+opponent.getBaseRes();	
		Fonts.moneyFont.draw(sb, enemyRes, MainGame.WIDTH-P1NAME_X-Fonts.moneyFont.getBounds(enemyRes).width, NAME_Y-80);
		
		
		sb.draw(CARD_BORDER, MainGame.WIDTH/2-CARD_BORDER.getRegionWidth()/2, 0);
		
		Fonts.MFont.setColor(Color.BLACK);
		Fonts.MFont.draw(sb, user.getName(), P1NAME_X, NAME_Y);
		Fonts.MFont.draw(sb, opponent.getName(), P2NAME_X, NAME_Y);		
		Fonts.MFont.setColor(Color.WHITE);

        timeBox.draw(sb);
		timer = ""+(timeElapsed/1000);
		if(timeElapsed/1000 < 10) {
			Fonts.menuFont.setColor(Color.RED);
		}
		Fonts.menuFont.draw(sb, timer, ((MainGame.WIDTH/2)-Fonts.menuFont.getBounds(timer).width/2), MainGame.HEIGHT-63);
		Fonts.menuFont.setColor(Color.WHITE);

		
		hand.render(sb);
		
		// DRAW BUTTONS
		for (Button button : guiButtons) {
			button.render(sb);
		}

		if (hand.getCard() != null) {
			magButton.render(sb);
		}
		
		if (user.isPlacing() && !user.hasSacrificed()) {
			if (!showingSacrificeOptions && hand.getCard() != null) {
				showSacrificeOptions(true);
			}
			
		} else if(opponent.isPlacing()) {
			hourGlass.rotate(-6f);
			hourGlass.draw(sb);
		}
		
		if(showingSacrificeOptions && hand.getCard() == null) {
			showSacrificeOptions(false);
		}
		
		if(sacRes.getY() > -138f) {
			sacRes.draw(sb);
			sacCards.draw(sb);
		}
		

		if(magnified) {
			sb.draw(alpha, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
			hand.getCard().render(new Vector2(magSprite.getX(), magSprite.getY()), magSprite.getScaleX(), sb);
		}
		
		if(message != null) {
			message.render(sb);
		}
	}
	

	private void resetTimer() {
		startTurnTime = System.nanoTime();	
	}
	
	public void createMessage(String text) {
		message = new GUIMessage(text);
	}
	
	public void setEndButtonActive(boolean active) {
		if(active) {
			endTurnButton.setTexture(END_TURN);
		} else {
			endTurnButton.setTexture(END_TURN_GRAYED);
		}
	}

	public void startTurn(boolean isUser) {	
		hand.setCardPositions();
		setEndButtonActive(isUser);
		resetTimer();
	}
	
	public void endTurn() {
		user.endTurn();
		magnified = false;
		hand.deselectCards();
		opponent.setCurrRes(opponent.getBaseRes());
		setEndButtonActive(false);
		resetTimer();
	}
	
	private void sacrificeResource() {		
		user.sacrificeResource(hand.getCard());
		message = new GUIMessage(Constants.SAC_RESOURCE);		
		SoundManager.play(Assets.SAC_RESOURCE);	
		showSacrificeOptions(false);
	}
	

	private void sacrificeCards() {
		user.sacrificeCards(hand.getCard());
		message = new GUIMessage(Constants.SAC_CARDS);		
		SoundManager.play(Assets.SAC_CARDS);		
		showSacrificeOptions(false);
	}

	private void showSacrificeOptions(boolean show) {
		float target = 40f;
		
		if(!show) {
			target = -170f;
		}
		
		for (SpriteButton sb : sacButtons) {
			Tween.to(sb, SpriteAccessor.POS_XY, 20f)
					.target(sb.getX(), target)
					.ease(TweenEquations.easeOutBounce)
					.start(tweenManager);
		}
		showingSacrificeOptions = show;
		startTime = TimeUtils.millis();
	}
	
	private void magnifyCard(boolean mag) {
		TweenCallback myCallBack = new TweenCallback(){ 
            @Override
            public void onEvent(int type, BaseTween<?> source) {
            	magnified = false;
            }
        };
        
        float xTarget = hand.getCard().getPos().x;
        float yTarget = hand.getCard().getPos().y;
        float scaleTarget = 1f;
        
        if(mag) {
        	xTarget = magCardPos.x;
            yTarget = magCardPos.y;
            scaleTarget = 2f;
            
            myCallBack = new TweenCallback(){ 
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                }
            };
        }
		
		Tween.to(magSprite, SpriteAccessor.POS_XY, 10f)
		.target(xTarget, yTarget)
		.ease(TweenEquations.easeNone)
		.start(tweenManager);
		
		Tween.to(magSprite, SpriteAccessor.SCALE_XY, 10f)
		.target(scaleTarget, scaleTarget)
		.setCallback(myCallBack)
		.setCallbackTriggers(TweenCallback.END)
		.ease(TweenEquations.easeNone)
		.start(tweenManager);

		startTime = TimeUtils.millis();
	}
}
