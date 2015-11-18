package com.yellowbyte.giovannifallout.board;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.SoundManager;


public class Strike { //Represents an attack on a unit / tower.

	private Tile attacker;	
	private Tile cardPrey;
	private Tower towerPrey;
	private boolean towerStrike;
	private boolean started = false;
	private boolean isFinished = false;
	private boolean calDone = false;
	private ScoreMessage sMessage;
	private int damage = 0;
	
	
	public Strike(Tile attacker) {
		this.attacker = attacker;
	}

	
	public void start(Tile prey) { // ATTACKS UNIT.
		towerStrike = false;
		cardPrey = prey;
		started = true;
	}


	public void start(Tower tower) { //ATTACKS TOWER.
		towerStrike = true;
		towerPrey = tower;
		started = true;
	}


	public Tile getAttacker() {
		return attacker;
	}


	public boolean isFinished() {
		return isFinished;
	}


	public boolean hasStarted() {
		return started;
	}


	public void update() {
		
		if(!calDone) {
			
			String damageTaken;
			damage = attacker.getCard().getCurrAttack();
			damageTaken = ""+damage;
			
			if(towerStrike) {                       //Tower Attack.
				if(!towerPrey.isDestroyed()) {
					sMessage = new ScoreMessage(damageTaken, towerPrey.getMidPoint().cpy());
					towerPrey.setCurrHealth(towerPrey.getCurrHealth()-attacker.getCard().getCurrAttack());
					SoundManager.play(Assets.TOWERHIT);
				} else {
					isFinished = true;
				}			
				resetCountdown();
                attacker.getCard().onTowerAttack();
				
			} else {			
				sMessage = new ScoreMessage(damageTaken, cardPrey.getMidPoint().cpy());
				cardPrey.getCard().setCurrHealth(
						cardPrey.getCard().getCurrHealth()
								- attacker.getCard().getCurrAttack());
				cardPrey.getCard().getUnit().hitUnit();

				SoundManager.play(Assets.HIT);
				
				if (cardPrey.getCard().getCurrHealth() <= 0) {
					cardPrey.destroyCard();
				} else {
                    cardPrey.getCard().onHit();
                }

				resetCountdown();
			}
            attacker.getCard().onAttack();
			calDone = true;
		}

		//Check if time is up.
		if (!isFinished) {
			if(!sMessage.isAnimating()) {
				isFinished = true;  //TEMP!!!
			}
		}	
	}
	
	public void render(SpriteBatch sb) {
		if(sMessage != null) {
			sMessage.update();
			sMessage.render(sb);
			
		}
	}
	
	
	private void resetCountdown() {
		attacker.getCard().setCurrCountdown(attacker.getCard().getBaseCountdown());
		attacker.setTileImage();
	}

	public int getDamage() {
		return damage;
	}
}
