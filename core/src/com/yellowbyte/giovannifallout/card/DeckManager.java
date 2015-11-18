package com.yellowbyte.giovannifallout.card;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class DeckManager {


	public static Array<Integer> load (String deckName) {
		Array<Integer> deck = new Array<Integer>();
		
		BufferedReader reader = null;
		
		try {
			FileHandle file = Gdx.files.internal("cards/"+deckName+".deck");
			reader = new BufferedReader(file.reader());
			String line = reader.readLine();

			while (line != null) {
				deck.add(Integer.parseInt(line));
				line = reader.readLine();
			}
		} catch (Throwable e) {

		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException e) {
			}
		}
		return deck;
	}
}
