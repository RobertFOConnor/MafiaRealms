package com.yellowbyte.giovannifallout;

import com.badlogic.gdx.utils.Array;
import com.yellowbyte.giovannifallout.appwarp.WarpSender;
import com.yellowbyte.giovannifallout.board.BoardManager;
import com.yellowbyte.giovannifallout.board.Tile;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.card.CardCreator;
import com.yellowbyte.giovannifallout.card.Deck;
import com.yellowbyte.giovannifallout.card.EffectCard;
import com.yellowbyte.giovannifallout.card.UnitCard;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.SoundManager;

public class Player {

	
	private String name;
	private boolean sacrificed = false;
	private boolean isUser = false;
	private int baseRes = 0;
	private int currRes = 0;
	private Array<Integer> deck;
	private Array<Card> currHand;
	
	private BoardManager bm;
	private WarpSender sender;
	
	public static final String WAITING = "WAITING";
	public static final String PLACING = "PLACING";
	public static final String ATTACKING = "ATTACKING";
	private String state = PLACING;

	
	//Where in the deck to draw the card from
	private int drawPosition = 0;
	
	public Player(String name, Deck chosenDeck, BoardManager bm, boolean isUser) {
		this.name = name;
		this.bm = bm;
		this.isUser = isUser;
		
		
		deck = chosenDeck.getCardList();
		deck.shuffle();
		
		currHand = new Array<Card>();
		for(int i = 0; i <= 3; i++) {
			drawCard();
		}
		
		sender = new WarpSender();
	}	
	

	public void drawCard() {
		Card c = CardCreator.createCard(deck.get(drawPosition));
		currHand.add(c);
		drawPosition++;
		
		if(drawPosition >= deck.size) {
			//Full cycle of deck!
			drawPosition = 0;
		}
	}


	public String getName() {
		return name;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Array<Card> getHand() {
		return currHand;
	}

	public int getBaseRes() {
		return baseRes;
	}

	public int getCurrRes() {
		return currRes;
	}

	public void setCurrRes(int currRes) {
		this.currRes = currRes;
	}
	
	public boolean hasSacrificed() {
		return sacrificed;
	}
	
	public void addResource() {
		baseRes++;
		currRes++;
	}

	public void sacrificeResource(Card card) {		
		currHand.removeValue(card, false);
		sacrificed = true;
		addResource();
		
		if(GameManager.multiplayer) {
			if(isUser) {
				sender.sendResourceSacrifice();
			}
		}	
	}
	
	public void sacrificeCards(Card card) {		
		currHand.removeValue(card, false);
		sacrificed = true;
		drawCard();
		drawCard();
		
		if (GameManager.multiplayer) {
			if (isUser) {
				sender.sendCardSacrifice();
			}
		}
	}
	
	public void startTurn() {
		this.state = PLACING;
		bm.startTurn(isUser);
		sacrificed = false;
		currRes = baseRes;
		
		if(currHand.size < 7) {
			drawCard();
		}	
	}
	
	public void endTurn() {	
		startAttack();
		
		if (GameManager.multiplayer) {
			if (isUser) {
				sender.sendTurn();
			}
		}
	}

	/* SEND & RECIEVE MONSTER CARD */

	public void placeRecievedCard(int row, int col, int cardID) { //Called by opponent

		UnitCard card = (UnitCard) CardCreator.createCard(cardID);
		card.onSummon();
		
		bm.placeUnit(isUser, row, col, card);
		SoundManager.play(Assets.SUMMON);
		consumeCard(card);		
	}
	
	public void placeCard(Tile t, UnitCard card) { //Called by local player.
		card.onSummon();
		t.addCard(card);	
		giveCardsandCashBonus(card);
		SoundManager.play(Assets.SUMMON);
		consumeCard(card);
		bm.deselectTiles();
		if (GameManager.multiplayer) {
			sender.sendSummon(t.getRow(), t.getCol(), card.getID());
		}
	}
	
	public void moveRecievedCard(int oldRow, int oldCol, int newRow, int newCol) { //Called by local player.
		bm.moveUnit(isUser, oldRow, oldCol, newRow, newCol);
	}
	
	
	public void moveCard(Tile currTile, Tile newTile) { //Called by local player.
		currTile.getCard().onMove();
		currTile.moveCard(newTile);
		if(GameManager.multiplayer) {
			sender.sendMove(currTile.getRow(), currTile.getCol(), newTile.getRow(), newTile.getCol(), newTile.getCard().getID());
		}
	}
	
	
	/* SEND & RECIEVE ACTION CARD */	
	public void applyRecievedCard(int row, int col, int cardID) { //Called by opponent
		
		EffectCard card = (EffectCard) CardCreator.createCard(cardID);
		bm.applyCard(isUser, row, col, card);	
		consumeCard(card);
		
		SoundManager.play(Assets.ACTION);
	}
	
	
	public void applyCard(Tile t, EffectCard card) { //Called by local player.
		t.applyCard(card);
		consumeCard(card);	
		bm.deselectTiles();
		if(GameManager.multiplayer) {
			sender.sendApplyCard(t.getRow(), t.getCol(), card.getID(), !t.isOwner());
		}
		SoundManager.play(Assets.ACTION);
	}
	
	/* SEND & RECIEVE EVENT CARD */

	public void applyRecievedEventCard(int cardID) { //Called by opponent

		EffectCard card = (EffectCard) CardCreator.createCard(cardID);
		if(card.getDrawBonus()!=0 || card.getCashBonus()!=0) {
			giveCardsandCashBonus(card);
		} else {
			bm.applyEventCard(card, false);	
		}
		consumeCard(card);
		
		SoundManager.play(Assets.ACTION);
	}
	
	public void applyEventCard(EffectCard card) { //Called by local player.
		
		if(card.getDrawBonus()!=0 || card.getCashBonus()!=0) {
			giveCardsandCashBonus(card);
		} else {
			bm.applyEventCard(card, true);
		}
		
		consumeCard(card);
		
		if(GameManager.multiplayer) {
			sender.sendApplyEventCard(card.getID());
		}
		
		SoundManager.play(Assets.ACTION);
	}
	
	private void giveCardsandCashBonus(Card card) {
		//ADD CARDS
		for(int i = 0; i < card.getDrawBonus(); i++) {
			drawCard();
		}
		
		//ADD RESOURCES
		int unitNum;
		if(card.getType().equals("HIS")){
			unitNum = bm.getUnitCount(!isUser);
		} else {
			unitNum = bm.getUnitCount(isUser);
		}
		currRes += unitNum* card.getCashBonus();
	}


	private void consumeCard(Card card) {
		setCurrRes(currRes- card.getCost());
		currHand.removeValue(card, false);		
	}
	
	
	public void startAttack() {
		bm.startAttack(isUser);
		this.state = ATTACKING;
	}
	
	public void surrender() {
		if(GameManager.multiplayer) {
			sender.sendSurrender();
		}
	}

    public boolean isAttacking() {
        return state.equals(Player.ATTACKING);
    }

	public boolean isWaiting() {
		return state.equals(Player.WAITING);
    }

    public boolean isPlacing() {
		return state.equals(Player.PLACING);
	}

	public Array<Tile> getOccupiedTiles() {
		return bm.getOccupiedTiles(isUser);
	}
	
	public Array<Tile> getEmptyTiles() {
		return bm.getEmptyTiles(isUser);
	}


	public Array<Tile> getAvailableTiles(Card card) {
		if (card.getCost() <= currRes && state.equals(PLACING)) { //Can Player afford card?
			return bm.getAvailableTiles(card, currRes, isUser);
		}
		return new Array<Tile>();
	}
}
