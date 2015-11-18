package com.yellowbyte.giovannifallout.gui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.touch.TouchManager;
import com.yellowbyte.giovannifallout.tween.CardAccessor;

public class GUIHand { //The players current hand of cards from their deck.
	
	private Array<Card> cards;
	private Card currCard;
	private TouchManager tm;
	private Vector2 touch;
	
	//TWEENING
	private TweenManager tweenManager;
	private long startTime, delta;
	
	
	public GUIHand(TouchManager tm, Array<Card> startHand) {
		this.tm = tm;
		cards = startHand;
		setCardPositions();
		touch = new Vector2();
		
		Tween.registerAccessor(Card.class, new CardAccessor());
		tweenManager = new TweenManager();
	}

	
	public void setCardPositions() { //Adjusts Card positions on screen according to size of hand.
		Tween.registerAccessor(Card.class, new CardAccessor());
		tweenManager = new TweenManager();
		
		for (int i = 0; i < cards.size; i++) {
			Card c = cards.get(i);
			
			int posY = -150;			
			if(currCard != null) {
				posY = -200;
			}
			
			if(c.equals(currCard)) {
				posY = -100;
			}
			
			
			float xDist = Card.WIDTH;
			float xBalance = 0;
			
			if(cards.size > 4) {
				xDist = Card.WIDTH/2;
				xBalance = 60;
			}
			
			float center = (xDist / 2)* cards.size+xBalance;
			
			
			Tween.to(c, CardAccessor.POS_XY, 4f)
			.target((((MainGame.WIDTH / 2) - center)) + (xDist * i), posY)
			.ease(TweenEquations.easeNone)
			.start(tweenManager); // ** start it

			startTime = TimeUtils.millis();
		}
	}
	
	
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+1000)/1000;
		tweenManager.update(delta); 
		
		if(cards.size > 0) {
			for(int i = cards.size-1; i>=0; i--) {
				if(!cards.get(i).equals(currCard)) {
					cards.get(i).render(sb);
				}
			}
			if(currCard != null) {
				currCard.render(sb);
			}
		}
	}

	public void update() {

		if (Gdx.input.justTouched() && !GameManager.paused && !GUIManager.magnified) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());

			deselectCards();
			for (int i = 0; i < cards.size; i++) { //CHECK CARD TOUCHES.
				Card card = cards.get(cards.size-1-i);
				
				if (card.getBounds().contains(touch)) {
					currCard = card;
				}
			}

			setCardPositions();
			tm.getState().handleCard(currCard);						
		}
	}
	
	public Card getCard() {
		return currCard;
	}
	
	public void setCard(Card card) {
		currCard = card;
	}
	
	public void deselectCards() {
		currCard = null;
		tm.getState().handleCard(currCard);	
	}
}
