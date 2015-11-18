package com.yellowbyte.giovannifallout.board;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;

public class AttackPhase {

	private Array<Strike> strikers = new Array<Strike>();
	private Grid targetGrid;
	private Strike currStrike;
	private boolean imAttacking;
	private boolean finished = false;
	
	private Sprite arrow;
	private TweenManager tweenManager;
	private long startTime, delta;
	
	
	public AttackPhase(Array<Tile> attackers, Grid targetGrid, boolean imAttacking) {
		this.targetGrid = targetGrid;
		this.imAttacking = imAttacking;


		for (Tile t : attackers) {
			strikers.add(new Strike(t));
		}			
		nextStrike();	
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		tweenManager = new TweenManager();
		
		Tile t = strikers.get(0).getAttacker();
		arrow = new Sprite(Assets.manager.get(Assets.attack_arrow, Texture.class));
		arrow.setPosition(t.getMidPoint().x-arrow.getWidth()/2, t.getPosition().y+Card.UNIT_HEIGHT+20);
	}
	
	
	public void update() {// UPDATE ATTACK PHASE
		
		if(currStrike != null) {

			if(!currStrike.hasStarted()) { // SETUP STRIKE
				startStrike();
			} else {
				currStrike.update(); // UPDATE STRIKE
			}
			
			if(currStrike.isFinished()) { // NEXT STRIKE
				if(imAttacking) {
					GameManager.damageDone += currStrike.getDamage();
				} else {
					GameManager.damageTaken += currStrike.getDamage();
				}
				
				strikers.removeIndex(0);
				nextStrike();
			}
		}
	}
	
	public void render(SpriteBatch sb) {// RENDER ATTACK PHASE
		delta = (TimeUtils.millis()-startTime+1000)/1000; 
		tweenManager.update(delta); 
		
		
		if(currStrike != null) {
			currStrike.render(sb);
			arrow.draw(sb);
		}
	}
		
	
	private void startStrike() {
		Tile attacker = currStrike.getAttacker();
		Tile target = targetGrid.getTarget(attacker.getRow(), imAttacking);		
		
		if (target != null) {
			currStrike.start(target); //Tile Strike!
		} else {
			currStrike.start(targetGrid.getTower(attacker.getRow())); //Tower Strike!
		}	
		
		Tile t = strikers.get(0).getAttacker();
		
		Tween.to(arrow, SpriteAccessor.POS_XY, 20f)
		.target(t.getMidPoint().x-arrow.getWidth()/2, t.getPosition().y+Card.UNIT_HEIGHT+20)
		.ease(TweenEquations.easeOutBounce)
		.start(tweenManager);
		startTime = TimeUtils.millis();
	}

	
	public void nextStrike() {
		
		if(strikers.size <= 0) {
			finished = true;
			
		} else {
			currStrike = strikers.get(0);
		}
	}

	public boolean isFinished() {
		return finished;
	}	
}
