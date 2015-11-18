package com.yellowbyte.giovannifallout.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.TextButton;
import com.yellowbyte.giovannifallout.box.DeckSelectionBox;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.card.CardCreator;
import com.yellowbyte.giovannifallout.card.Deck;
import com.yellowbyte.giovannifallout.card.UnitCard;
import com.yellowbyte.giovannifallout.gui.Button;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;
import com.yellowbyte.giovannifallout.tween.CardAccessor;

public class CardScreen implements Screen {

	private OrthoCamera camera;
	private Vector2 touch;
	private Button leftAll, rightAll;
	
	private Texture bg = Assets.manager.get(Assets.CARDSCREEN_BG, Texture.class);
	
	private Array<Integer> allCards;
	private Array<Card> visibleCards;
	private Array<Card> tableCards;
	private Card selectedCard;
	private int lastCard = 7;
	private float cardScale = 0.5f;
	private int maxCards = 30;
	private static int cardScreenPos = 0;
	
	
	private Vector2 unitPreviewPos = new Vector2(1580, 60);
	private Vector2 cardPreviewPos = new Vector2(1470, 400);
	private float cardPreviewScale = 1.4f;
	
	private TextButton backButton;
	private TextButton clearTableButton;
	private TextButton loadDeckButton;
	private TextButton saveDeckButton;
	private boolean saving = false;
	
	private static final float deckButtonsY = 1035;	
	private static final float allCardsY = 20;
	
	
	private SaveBox saveBox;
	private DeckSelectionBox loadBox;
	
	private TweenManager tweenManager;
	private long startTime, delta;
	
	private String deckSize; // 0/20
	
	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		touch = new Vector2(0,0);
		
		Texture guiSheet = Assets.manager.get(Assets.guisheet, Texture.class);
		TextureRegion left = new TextureRegion(guiSheet, 0, 270, 150, 150);
		leftAll = new Button(left, new Vector2((left.getRegionWidth()/3), 50));
		
		TextureRegion right = new TextureRegion(guiSheet, 150, 270, -150, 150);
		rightAll = new Button(right, new Vector2(1150, 50));
		
		
		resetCardArrays();
		
		lastCard = cardScreenPos+lastCard;
		
		for(int i = cardScreenPos; i < lastCard; i++) {
			
			Card card = CardCreator.createCard(allCards.get(i));
			card.setPos(new Vector2(250+((Card.WIDTH*cardScale)*visibleCards.size), allCardsY));
			card.setScaleFactor(cardScale);
			
			visibleCards.add(card);
		}
		selectedCard = visibleCards.get(3);
		
		backButton = new TextButton("GO BACK", Fonts.MFont, new Vector2(50, deckButtonsY));
		clearTableButton = new TextButton("CLEAR TABLE", Fonts.MFont, new Vector2(360, deckButtonsY));
		loadDeckButton = new TextButton("LOAD DECK", Fonts.MFont, new Vector2(750, deckButtonsY));
		saveDeckButton = new TextButton("SAVE DECK", Fonts.MFont, new Vector2(1120, deckButtonsY));
		deckSize = tableCards.size+"/20";
		
		saveBox = new SaveBox();	
		loadBox = new DeckSelectionBox();	
		
		
		
		Tween.registerAccessor(Card.class, new CardAccessor());
		tweenManager = new TweenManager();	
		
		startTime = TimeUtils.millis();
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {

			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());
			
			if (!saving && !loadBox.isShowing()) {

				if (backButton.checkTouch(touch)) {
					SoundManager.play(Assets.CLICK);
					ScreenManager.setScreen(new MainMenuScreen());


				} else if (leftAll.checkTouch(touch)) {

					if (cardScreenPos > 0) {
						
						moveCards(true);
						
						visibleCards.insert(0, getNextCard(allCards.get(cardScreenPos - 1), 0));
						lastCard--;
						cardScreenPos--;
					}
				} else if (rightAll.checkTouch(touch)) {

					if (lastCard < allCards.size) { //HAVE YOU REACHED END OF DECK?
						
						moveCards(false);
						
						visibleCards.add(getNextCard(allCards.get(lastCard), visibleCards.size));
						lastCard++;
						cardScreenPos++;
					}
				} else if (clearTableButton.checkTouch(touch)) {
					
					resetCardArrays();
					resetVisibleScroller();
					
					SoundManager.play(Assets.SAC_CARDS);
					
				} else if (saveDeckButton.checkTouch(touch) && MainGame.cd.getDeckNames().size < 4) {
					
					if(tableCards.size >= 20) { // SHOW SAVE BOX.
						saveBox.display();
					}
					
				} else if (loadDeckButton.checkTouch(touch)) {
					loadBox = new DeckSelectionBox();	
					loadBox.display();
				}
				
				for (int i = 0; i < visibleCards.size; i++) { //CHECK CARD TOUCHES.
					Card card = visibleCards.get(visibleCards.size-1-i);
					
					if (card.getBounds().contains(touch)) {
						
						if(selectedCard != card) {
							selectedCard = card;
							
							
						} else if(tableCards.size < maxCards) { // ADD CARD TO TABLE!

							int numOfCardTypeInDeck = 0;
							for(Card c : tableCards) {
								if(c.getID() == selectedCard.getID()) { // CARD IS ALREADY IN DECK
									numOfCardTypeInDeck++;
								}
							}

							if(numOfCardTypeInDeck < 3) { //IF THERE'S LESS THAN THREE ALREADY IN THE DECK, ADD IT.
								addCardToTable(selectedCard);
								resetVisibleScroller();
							}
						}
					}
				}

				for (int i = 0; i < tableCards.size; i++) { // CHECK CARD TOUCHES.
					Card card = tableCards.get(tableCards.size - 1 - i);

					if (card.getBounds().contains(touch)) {
						selectedCard = card;
					}
				}
				
				
			} else if(saving) {
				saveBox.update(touch);
			} else if(loadBox.isShowing()) {
				
				int result = loadBox.update(touch);
				
				if(result != -1) { // LOAD NEW DECK.
					
					resetCardArrays();
					
					for(int ID : loadBox.getDeck(result).getCardList()) {
						addCardToTable(CardCreator.createCard(ID));
					}
					
					cardScreenPos = 0;
					lastCard = 7;
					resetVisibleScroller();
					
					SoundManager.play(Assets.SAC_CARDS);
				}
			}
		}
	}
	
	@Override
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+850)/1000; // **get time delta **//
		tweenManager.update(delta); //** update sprite1 **//
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(bg, 0, 0);
		
		drawCards(sb, visibleCards);
		drawCards(sb, tableCards);
		
		deckSize = tableCards.size+"/20";
		Fonts.MFont.draw(sb, deckSize, 1200, 350);
		
		backButton.render(sb);	
		clearTableButton.render(sb);
		loadDeckButton.render(sb);
		saveDeckButton.render(sb);
		
		renderPreviews(sb);
		
		leftAll.render(sb);
		rightAll.render(sb);
		
		if(saving) {
			saveBox.render(sb);
		} 
		loadBox.render(sb);
		sb.end();
	}

	private void addCardToTable(Card card) {
		
		int multPos = tableCards.size;
		
		if(multPos > 19) {
			multPos = multPos - 20;
		} else if(multPos > 9) {
			multPos = multPos - 10;
		}

		startTime = TimeUtils.millis();
		
		Tween.to(card, CardAccessor.POS_XY, 30f) //** tween POSITION_Y for a duration **//
        .target(50+((Card.WIDTH*cardScale)*multPos), 320+(Card.HEIGHT*cardScale)*(tableCards.size/10)) // ** final POSITION_Y **//
        .ease(TweenEquations.easeOutQuint) //** easing equation **//
        .start(tweenManager); //** start it
		
		
		card.setScaleFactor(cardScale);
		tableCards.add(card);
		
		visibleCards.removeValue(card, false);
		allCards.removeValue(card.getID(), false);
		
	}
	


	private void drawCards(SpriteBatch sb, Array<Card> cardArray) {
		for(int i = 0; i < cardArray.size; i++) {			
			Card card = cardArray.get(i);
			card.render(sb);
			checkSelected(sb, card);
		}
		
	}

	private void checkSelected(SpriteBatch sb, Card card) {
		if(selectedCard.equals(card)) {
			sb.draw(Assets.manager.get(Assets.card_selected, Texture.class), card.getPos().x, card.getPos().y, Card.WIDTH*card.getScaleFactor(), Card.HEIGHT*card.getScaleFactor());
		}	
	}

	//RENDERS THE CARD AND UNIT PREVIEWS ON THE RIGHT OF THE SCREEN. 
	private void renderPreviews(SpriteBatch sb) {
		
		if(selectedCard != null) {
			selectedCard.render(cardPreviewPos, cardPreviewScale, sb);
			
			if(selectedCard.getType().equals(Card.MONSTER)) {
				selectedCard.getUnit().render(sb, false, unitPreviewPos);
			}
		}	
	}	

	private Card getNextCard(int cardID, int cardPos) {
		Card card = CardCreator.createCard(cardID);
		card.setPos(new Vector2(250+((Card.WIDTH*cardScale)*cardPos), allCardsY));
		card.setScaleFactor(cardScale);
		return card;
	}
	
	private void moveCards(boolean left) {
		
		float dir = -(Card.WIDTH*cardScale);
		int removePos = 0;
		
		if(left) {
			dir = Card.WIDTH*cardScale;
			removePos = visibleCards.size - 1;
		}
		
		for(int i = 0; i < visibleCards.size; i++) {			
			Card card = visibleCards.get(i);
			card.setPos(card.getPos().cpy().add(dir, 0));
		}
		
		visibleCards.removeIndex(removePos);
		
		SoundManager.play(Assets.CARD_SELECT);
	}
	
	private void resetCardArrays() {
		allCards = new Array<Integer>();
		for(Integer card : MainGame.userStats.getPlayerCardList()) {
			allCards.add(card);
		}
		
		visibleCards = new Array<Card>();
		tableCards = new Array<Card>(); // RESET THE TABLE.
	}
	
	private void resetVisibleScroller() {
		visibleCards = new Array<Card>();
		
		if(cardScreenPos+7 >= allCards.size) {
			cardScreenPos--;
			lastCard--;
		}
		
		for(int j = cardScreenPos; j < lastCard; j++) {
			
			visibleCards.add(getNextCard(allCards.get(j), visibleCards.size));
		}
	}
	
	
	private class SaveBox {

		private Texture bg = Assets.manager.get(Assets.page, Texture.class);
		private String title = "Name Your New Deck: ";
		private TextButton saveButton, cancelButton;
		private Stage stage;
		private TextField tf;

		public SaveBox() {
			
			float buttonY = MainGame.HEIGHT/2-120;
			saveButton = new TextButton("Save", Fonts.menuFont, new Vector2(MainGame.WIDTH / 2 + 130, buttonY));
			cancelButton = new TextButton("Cancel", Fonts.menuFont, new Vector2(MainGame.WIDTH / 2 - 250, buttonY));

			stage = new Stage(camera.getViewPort());
			Gdx.input.setInputProcessor(stage);

			Skin skin = new Skin(Gdx.files.internal("ui\\uiskin.json"));

			TextFieldStyle tStyle = new TextFieldStyle();
			tStyle.font = Fonts.EditFont;// here i get the font
			tStyle.fontColor = Color.WHITE;
			tStyle.background = skin.getDrawable("textfield");
			tStyle.cursor = skin.newDrawable("cursor", Color.WHITE);
			tStyle.cursor.setMinWidth(8f);
			tStyle.selection = skin.newDrawable("textfield", 0.5f, 0.5f, 0.5f,
					0.5f);

			tf = new TextField("", tStyle);
			tf.setPosition(MainGame.WIDTH / 2 - 250, 500);
			tf.setSize(500, 80);
			tf.setText("");
			tf.setVisible(false);
			tf.setMaxLength(14);

			stage.addActor(tf);
		}

		public void display() {
			saving = true;
			tf.setVisible(true);
			tf.setText("");
		}

		public void update(Vector2 touch) {
			
			if (saveButton.checkTouch(touch) && tf.getText().length() > 3) {
				
				if (Constants.hasCurseWords(tf.getText().toLowerCase())) {
					
					tf.setText("");
					
				} else {
					Array<Integer> saveArray = new Array<Integer>();

					for (Card c : tableCards) {
						saveArray.add(c.getID());
					}

					String deckName = tf.getText();
					Deck deck = new Deck(saveArray);

					// IF THE DECK ALREADY EXISTS.
					if (!MainGame.cd.getDeckNames().contains(deckName, false)) { 
						MainGame.cd.addDeck(deckName);
					}
					MainGame.saveManager.saveDataValue("DECKS", MainGame.cd);
					MainGame.saveManager.saveDataValue(deckName, deck);

					saving = false;
					tf.setVisible(false);
				}
				
			} else if (cancelButton.checkTouch(touch)) {
				saving = false;
				tf.setVisible(false);
			}
		}

		public void render(SpriteBatch sb) {

			sb.draw(bg, (MainGame.WIDTH / 2) - 450, MainGame.HEIGHT / 2 - 200,
					900, 400);
			Fonts.menuFont.draw(sb, title,
					MainGame.WIDTH / 2 - Fonts.menuFont.getBounds(title).width
							/ 2, 700);
			saveButton.render(sb);
			cancelButton.render(sb);

			sb.end();
			stage.draw();
			sb.begin();
		}
	}
	
	@Override
	public void resize(int w, int h) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void goBack() {
		ScreenManager.setScreen(new MainMenuScreen());
		
	}
}
