package com.yellowbyte.giovannifallout.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.card.EffectCard;
import com.yellowbyte.giovannifallout.card.UnitCard;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.touch.TouchManager;

public class BoardManager {

	private Grid userGrid, oppGrid;
	private AttackPhase ap;
	private boolean isFinishedAttack = true;

	// ATTACK WIND
	private Texture attackWind = Assets.manager.get(Assets.attack_wind, Texture.class);
	private float windX = 0;
	private float windY = 0;

	public BoardManager(TouchManager tm) {
		userGrid = new Grid(tm,true);
		oppGrid = new Grid(tm,false);
	}
	
	public void update() {
		
		updateAttackPhase();		
		userGrid.update();
		oppGrid.update();
	}

	private void updateAttackPhase() {
		if(ap != null) {
			if(!ap.isFinished()) {
				ap.update();

			} else {
				ap = null;
				isFinishedAttack = true;

				checkGameOver();
			}
		}
	}

	public void render(SpriteBatch sb) {
		userGrid.render(sb);
		oppGrid.render(sb);
		
		userGrid.renderStatBoxes(sb);
		oppGrid.renderStatBoxes(sb);
		
		if(ap != null) {
			ap.render(sb);
			
			if(windY < attackWind.getHeight()) {
				windY += ((attackWind.getHeight()-windY)/10);
				
				if(attackWind.getHeight()-windY < 1) {
					windY = attackWind.getHeight();
				}
			}
		} else {
			if(windY > 0) {
				windY-= 25;
			}
		}
		
		
		if (windY > 0) {
			sb.draw(attackWind, windX, MainGame.HEIGHT - windY);
			sb.draw(attackWind, windX + MainGame.WIDTH, MainGame.HEIGHT
					- windY);

			
			float incScale = 1.3f;
			
			sb.draw(attackWind, 0 - windX, windY*incScale,
					attackWind.getWidth(), -(attackWind.getHeight()*incScale));
			sb.draw(attackWind, (0 - windX) - MainGame.WIDTH,
					windY*incScale, attackWind.getWidth(),
					-(attackWind.getHeight()*incScale));
			windX -= 25;
			if (windX < -MainGame.WIDTH) {
				windX = 0;
			}
		}
	}

	public void startAttack(boolean isUser) {	
		
		Grid attGrid = userGrid; //IM ATTACKING
		Grid targetGrid = oppGrid;

		if (!isUser) { //HE'S ATTACKING
			attGrid = oppGrid;
			targetGrid = userGrid;
		}
		
		attGrid.deselectTiles();
		oppGrid.deselectTiles();
		
		Array<Tile> attackers = attGrid.getAttackers(isUser);
		
		if(attackers.size != 0) {
			ap = new AttackPhase(attackers, targetGrid, isUser);
			isFinishedAttack = false;
		}
	}

	public void startTurn(boolean isUser) {
		Grid g = findGrid(isUser);
		g.startTurn();
	}

	public void placeUnit(boolean isUser, int row, int col, UnitCard card) {
		Grid g = findGrid(isUser);		
		g.getTile(row, col).addCard(card);	
		
		
	}
	
	public void moveUnit(boolean isUser, int oldRow, int oldCol, int newRow, int newCol) {
		Grid g = findGrid(isUser);		
		g.getTile(oldRow, oldCol).moveCard(g.getTile(newRow, newCol));	
		
	}
	
	public void applyCard(boolean isUser, int row, int col, EffectCard card) {
		Grid g = findGrid(isUser);		
		g.getTile(row, col).applyCard(card);
	}
	
	
	public void applyEventCard(EffectCard card, boolean sender) { //Applies Event card to his, mine or all units.
		
		if(sender) {
			if(card.getType().equals("HIS")) {
				oppGrid.applyToAll(card);
			} else if(card.getType().equals("MINE")) {
				userGrid.applyToAll(card);
			}
		} else {
			if(card.getType().equals("HIS")) {
				userGrid.applyToAll(card);
			} else if(card.getType().equals("MINE")) {
				oppGrid.applyToAll(card);
			}
		}	
		
		if(card.getType().equals("ALL")) {
			oppGrid.applyToAll(card);
			userGrid.applyToAll(card);
		}
	}
	
	public Grid findGrid(boolean isUser) {
		if(isUser) {
			return userGrid;
		}
		return oppGrid;
	}
	
	public boolean isFinishedAttack() {
		return isFinishedAttack;
	}
	
	public void checkGameOver() {
		if(!userGrid.hasTowersStanding()) {
			// YOU LOSE
			GameManager.endGame(false);	
			
		} else if(!oppGrid.hasTowersStanding()) {
			//YOU WIN!
			GameManager.endGame(true);	
		}
	}
	
	public void destroyTowers(boolean myTowers) {
		Grid g = userGrid;
		if(!myTowers) {
			g = oppGrid;
		}
		
		g.destroyTowers();
		
		System.out.println("Towers destroyed!");
		checkGameOver();
	}

	public int getUnitCount(boolean isUser) {
		Grid g = findGrid(isUser);
		return g.getUnitCount();
		
	}
	
	
	public Array<Tile> getOccupiedTiles(boolean isUser) {
		Grid g = findGrid(isUser);
		return g.getOccupiedTiles();	
	}
	
	public Array<Tile> getEmptyTiles(boolean isUser) {
		Grid g = findGrid(isUser);
		return g.getEmptyTiles();	
	}
	

	public Array<Tile> getAvailableTiles(Card card, int playerResources, boolean isUser) {
		Array<Tile> avail = new Array<Tile>();	
		Grid userGrid = findGrid(isUser);		
		Grid oppGrid = findGrid(!isUser);		
		
		
			if(card.getType().equals(Card.MONSTER)) { //Place CREATURE CARD
				avail = userGrid.getEmptyTiles();
			} else if(card.getType().equals(Card.ACTION)) { //Apply ACTION CARD
				
				avail.addAll(userGrid.getOccupiedTiles());
				avail.addAll(oppGrid.getOccupiedTiles());
			}  else /*if(card.getType().equals(Card.EVENT))*/ { //Apply EVENT CARD
				avail.addAll(userGrid.getAllTiles());
				avail.addAll(oppGrid.getAllTiles());
			}	
			
			for(Tile tile : avail) {
				tile.setAvailable(true);
			}
			
		return avail;
	}
	
	public void deselectTiles() {
		userGrid.deselectTiles();
		oppGrid.deselectTiles();
	}
}
