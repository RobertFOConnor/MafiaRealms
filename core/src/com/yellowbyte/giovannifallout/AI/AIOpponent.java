package com.yellowbyte.giovannifallout.AI;

import com.badlogic.gdx.utils.Array;
import com.yellowbyte.giovannifallout.Player;
import com.yellowbyte.giovannifallout.board.BoardManager;
import com.yellowbyte.giovannifallout.board.Tile;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.card.EffectCard;
import com.yellowbyte.giovannifallout.card.UnitCard;

public class AIOpponent {

	private Player opponent, user;
	private boolean sacrificed, placed, moved, waiting = false;
	private long startTime;
	private long waitTime;
	
	public AIOpponent(Player opponent, Player user) {
		this.opponent = opponent;
		this.user = user;
	}
	
	public void update() {
		
		if(waiting) {
			long timeElapsed = waitTime - ((System.nanoTime() - startTime) / 1000000);

			waiting = timeElapsed >= 0;

		} else {
			
			if(!sacrificed) {
				makeSacrifice();
			} else if(!placed) {
				placeCards();
			} else if(!moved) {
				moveUnits();
			} else {
				endTurn();
			}
		}
	}
	

	public void makeSacrifice() {
		if(opponent.getBaseRes() > 8 || opponent.getHand().size < 2) {
			opponent.sacrificeCards(opponent.getHand().get(0));
		} else {
			opponent.sacrificeResource(opponent.getHand().get(0));
		}
		
		sacrificed = true;
		startWait(500);
	}
	
	
	private void placeCards() {



		for (Card card : opponent.getHand()) {

			if (card.getType().equals(Card.MONSTER)) { //Place Monster card.
				Array<Tile> availTiles = opponent.getAvailableTiles(card);

				if (availTiles.size > 0) { //Are tiles available?

					Tile tile = availTiles.get((int) (Math.random() * availTiles.size));

					if(user.getOccupiedTiles().size > 0) { //Does the user have units in play?

                        int row = user.getOccupiedTiles().get(0).getRow();

                        for(Tile t : availTiles) { //If there's room, place unit opposite enemy.
                            if(t.getRow() == row) {
                                tile = t;
                            }
                        }
					}
					
					opponent.placeCard(tile, (UnitCard) card);
				}
			} else if (card.getType().equals(Card.ACTION)) {
				Array<Tile> availTiles = opponent.getAvailableTiles(card);
				if (availTiles.size > 0) {
					
					for(int j = 0; j < availTiles.size; j++) {
						Tile tile = availTiles.get(j);
						
						if(tile.isOwner()) {
							if(card.getBaseHealth() < 0 || card.getBaseAttack() < 0 || card.getBaseCountdown() > 1) {
								opponent.applyCard(tile, (EffectCard) card);
								break;
							}
						} else {
							if(card.getBaseAttack() > 0 || card.getBaseHealth() > 0) {
								opponent.applyCard(tile, (EffectCard) card);
								break;
							}
						}
					}
				}
			}
		}
		placed = true;
		startWait(1500);
	}
	
	private void moveUnits() {
		Array<Tile> AIUnits = opponent.getOccupiedTiles();

		for (Tile t : AIUnits) {
			Array<Tile> desTiles = opponent.getEmptyTiles();
			if (desTiles.size > 0) {
				Tile destTile = desTiles.get((int) (Math.random() * desTiles.size));

                if(user.getOccupiedTiles().size > 0) { //Does the user have units in play?

                    int row = user.getOccupiedTiles().get(0).getRow();

                    for(Tile emptyTile : desTiles) { //If there's room, place unit opposite enemy.
						if (emptyTile.getRow() == row && t.isAdjacent(emptyTile.getRow(), emptyTile.getCol()) && t.getCard().getCurrCountdown()<1) {
							destTile = emptyTile;
						}
					}
                }
				if(t.isAdjacent(destTile.getRow(), destTile.getCol())) {
					t.moveCard(destTile);
				}
			}
		}
		moved = true;
		startWait(1000);
	}
	
	private void endTurn() {
		sacrificed = false;
		placed = false;
		moved = false;
		
		opponent.endTurn();
	}

	
	private void startWait(long dur) {
		waiting = true;
		waitTime = dur;
		startTime = System.nanoTime();
	}
}
