package com.yellowbyte.giovannifallout.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class CardCreator {

	
	public static Card createCard(int id) {
		Json json = new Json();
		CardFactory f = json.fromJson(CardFactory.class,
				Gdx.files.internal("cards/cards.json"));
		return f.createCard(id);
	}
	
}
