package com.yellowbyte.giovannifallout.box;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.TextButton;
import com.yellowbyte.giovannifallout.card.Deck;
import com.yellowbyte.giovannifallout.gui.Button;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;

public class DeckSelectionBox extends Box {

	private Array<DeckButton> savedDecks;
	private TextButton cancelButton;
	private Texture deck_button = Assets.manager.get(Assets.deck_button, Texture.class);
	private Texture alpha = Assets.manager.get(Assets.alpha, Texture.class);
	private String title = "Choose a deck";
	private ChoiceBox choiceBox;
	private DeckButton chosenDeckButton;


	public DeckSelectionBox() {
		super(600, 800);
		cancelButton = new TextButton("Cancel", Fonts.menuFont, new Vector2(0,0));
		cancelButton.center();
		savedDecks = new Array<DeckButton>();
		choiceBox = new ChoiceBox();
	}

	public void display() {
		showing = true;

		savedDecks = new Array<DeckButton>();
		Array<String> deckNames = MainGame.cd.getDeckNames();

		for (int i = 0; i < deckNames.size; i++) {
			DeckButton deckButton = new DeckButton(new Vector2(MainGame.WIDTH/2-250, 700 - (130 * i)), deckNames.get(i));
			savedDecks.add(deckButton);
		}
	}

	public int update(Vector2 touch) {
		
		if (!choiceBox.isShowing()) {

			if (cancelButton.checkTouch(touch)) {
				showing = false;
				return -1;
			}

			for (DeckButton deckButton : savedDecks) { // DECK CHOSEN.
				if (deckButton.checkTouch(touch)) {

					if (deckButton.checkDeleteTouch(touch)) { // DELETE DECK?
						choiceBox = new ChoiceBox();
						choiceBox.display();
						chosenDeckButton = deckButton;
						return -1;
					}
					showing = false;
					return savedDecks.indexOf(deckButton, false);
				}
			}
			return -1;
		} else {
			int result = choiceBox.update(touch);
			
			if(result == 1) {
				
				MainGame.cd.getDeckNames().removeIndex(
						savedDecks.indexOf(chosenDeckButton, false));
				MainGame.saveManager
						.saveDataValue("DECKS", MainGame.cd);

				display();
				
				return -1;
			}
		}
		return -1;
	}

	public void render(SpriteBatch sb) {

		delta = (TimeUtils.millis()-startTime+1000)/1000; 
		tweenManager.update(delta); 
		
		
		if (showing) {
			sb.draw(alpha, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
			boxSprite.draw(sb);
			Fonts.MFont.draw(sb, title,
					MainGame.WIDTH / 2 - Fonts.MFont.getBounds(title).width / 2, boxSprite.getY()+750);
			
			cancelButton.getPosition().set(cancelButton.getPosition().x, boxSprite.getY()+70);
			cancelButton.render(sb);
			
			for (int i = 0; i<savedDecks.size; i++) {
				DeckButton deckButton = savedDecks.get(i);
				deckButton.getPosition().set(deckButton.getPosition().x, boxSprite.getY()+550 - (130 * i));
				deckButton.render(sb);				
			}
			
			choiceBox.render(sb);
		}
	}

	public Deck getDeck(int result) {
		return MainGame.saveManager.loadDataValue(savedDecks.get(result).getText(), Deck.class);
	}
	
	private class DeckButton extends Button {

		private String deckName;
		private Button deleteButton;
		
		public DeckButton(Vector2 pos, String deckName) {
			super(new TextureRegion(deck_button), pos);
			this.deckName = deckName;
			deleteButton = new Button(new TextureRegion(Assets.manager.get(Assets.delete_deck, Texture.class)), new Vector2(pos.x+435, pos.y+51));
		}

		
		
		public boolean checkDeleteTouch(Vector2 touch) {
			if(deleteButton.checkTouch(touch)) {
				return true;
			}
			return false;
		}



		public void render(SpriteBatch sb) {
			sb.draw(texture, pos.x, pos.y);
			Fonts.DeckFont.draw(sb, deckName, pos.x+130, pos.y+75);
			
			deleteButton.getPosition().set(deleteButton.getPosition().x, pos.y+51);
			deleteButton.render(sb);
		}
		
		public String getText() {
			return deckName;
		}
	}
}