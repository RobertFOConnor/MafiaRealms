package com.yellowbyte.giovannifallout.touch;

import com.badlogic.gdx.utils.Array;
import com.yellowbyte.giovannifallout.Player;
import com.yellowbyte.giovannifallout.board.Tile;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.card.EffectCard;
import com.yellowbyte.giovannifallout.card.UnitCard;
import com.yellowbyte.giovannifallout.gui.GUIManager;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.SoundManager;


public class CardTouchState implements TouchState {

	private TouchManager context;
	private Player user;
	private Card card;
	private Array<Tile> availableTiles = new Array<Tile>();
	
	public CardTouchState(Card card, TouchManager tm, Player user) {
		context = tm;
		this.user = user;
		
		setCard(card);
	}
	
	private void setCard(Card card) {
		this.card = card;
		SoundManager.play(Assets.CARD_SELECT);
		availableTiles = this.user.getAvailableTiles(this.card);
	}


	@Override
	public void handleTile(Tile t) {

		if (availableTiles.contains(t, false) && !GUIManager.magnified) {

			if (card instanceof UnitCard) { // Place CREATURE CARD
				UnitCard unitCard = (UnitCard) card;
				user.placeCard(t, unitCard);

				System.out.println("card placed");
			} else if (card.getType().equals(Card.ACTION)) { // Apply ACTION
																// CARD
				user.applyCard(t, (EffectCard) card);
			} else /* if(card.getType().equals(Card.EVENT)) */{ // Apply ACTION
																	// CARD
				user.applyEventCard((EffectCard) card);
			}
			resetAvailableTiles();
			setState(new NullTouchState(context, user));
		}
	}

	@Override
	public void handleCard(Card card) {
		resetAvailableTiles();
		
		if(card == null) {
			setState(new NullTouchState(context, user));
			
		} else if(!card.equals(this.card)){
			setCard(card);
		}		
	}
	
	@Override
	public String getString() {
		return "CARD";		
	}

	@Override
	public void setState(TouchState state) {
		context.setState(state);		
	}
	
	private void resetAvailableTiles() {
		for(Tile tile : availableTiles) {
			tile.setAvailable(false);
		}
	}
}
