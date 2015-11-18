package com.yellowbyte.giovannifallout.card;

import com.badlogic.gdx.utils.Array;

public class Deck {

	private Array<Integer> cardList;
	
	public Deck(Array<Integer> cardList) {
		this.cardList = cardList;
	}

	public Deck() {
		this.cardList = new Array<Integer>();
	}
	
	public Array<Integer> getCardList() {
		return cardList;
	}
}
