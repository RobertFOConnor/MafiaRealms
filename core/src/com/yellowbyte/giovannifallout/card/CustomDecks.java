package com.yellowbyte.giovannifallout.card;

import com.badlogic.gdx.utils.Array;

public class CustomDecks {

	private Array<String> deckList;
	
	public CustomDecks() {
		deckList = new Array<String>();
	}
	
	public void addDeck(String name) {
		deckList.add(name);
	}
	
	public Array<String> getDeckNames() {
		return deckList;
	}
}
