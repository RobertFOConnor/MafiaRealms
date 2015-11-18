package com.yellowbyte.giovannifallout.touch;

import com.yellowbyte.giovannifallout.board.Tile;
import com.yellowbyte.giovannifallout.card.Card;


public interface TouchState {//NONE / TILE / CARD
	
	
	public void setState(TouchState state);

	public void handleTile(Tile t);

	public void handleCard(Card card);	
	
	public String getString();
}
