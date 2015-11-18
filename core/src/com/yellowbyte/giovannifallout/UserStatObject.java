package com.yellowbyte.giovannifallout;

import com.badlogic.gdx.utils.Array;
import com.yellowbyte.giovannifallout.card.DeckManager;

public class UserStatObject {
	
	private static final Integer[] STARTING_DECK = {1,1, 4,4,4, 5,5,5, 7,7, 10,10,10, 15,15, 17,17, 20,20, 21,21,21, 22,22, 24,24};
	
	private String playerName = "";
	private int profilePic = 0;
	private int matchCount = 0;
	private int playerWins = 0;
	private int playerLevel = 1;
	private long playerXP = 0;	
	private long playerGold = 0;
	
	private long cardsOwned = 0;
	private long uniqueCardsOwned = 0;
	private long lastMatch = 0;
	private Array<Integer> playerCardList = new Array<Integer>();
	
	
	public UserStatObject() {
		playerCardList = DeckManager.load("all_cards");
		/*for(Integer card : STARTING_DECK) {
			playerCardList.add(card);
		}*/
	}
	
	public UserStatObject(String playerName, int profilePic) {
		this.playerName = playerName;
		this.profilePic = profilePic;
		
		playerCardList = DeckManager.load("all_cards");
		/*for(Integer card : STARTING_DECK) {
			playerCardList.add(card);
		}*/
	}


	public String getPlayerName() {
		return playerName;
	}


	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	public int getProfilePic() {
		return profilePic;
	}


	public void setProfilePic(int profilePic) {
		this.profilePic = profilePic;
	}


	public int getMatchCount() {
		return matchCount;
	}


	public void addMatchPlayed() {
		this.matchCount++;
	}


	public int getPlayerWins() {
		return playerWins;
	}


	public void addPlayerWin() {
		this.playerWins++;
	}


	public int getPlayerLevel() {
		return playerLevel;
	}


	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public long getXPNeeded() {
		return XPCalculator(this.playerLevel);
	}
	
	private long XPCalculator(int level) {
		
		if(level <= 1) {
			return level*1500;
		}
		
		return (level*1500)+XPCalculator(level-1);
	}

	public long getPlayerXP() {
		return playerXP;
	}


	public void addXP(long XP) {
		this.playerXP+=XP;
	}

	public long getPlayerGold() {
		return playerGold;
	}

	public void setPlayerGold(long playerGold) {
		this.playerGold = playerGold;
	}
	
	public void addGold(long earnedGold) {
		this.playerGold += earnedGold;
	}

	public long getCardsOwned() {
		return cardsOwned;
	}

	public void setCardsOwned(long cardsOwned) {
		this.cardsOwned = cardsOwned;
	}

	public long getUniqueCardsOwned() {
		return uniqueCardsOwned;
	}

	public void setUniqueCardsOwned(long uniqueCardsOwned) {
		this.uniqueCardsOwned = uniqueCardsOwned;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public void setPlayerWins(int playerWins) {
		this.playerWins = playerWins;
	}

	public void setPlayerXP(long playerXP) {
		this.playerXP = playerXP;
	}

	public long getLastMatch() {
		return lastMatch;
	}

	public void setLastMatch(long lastMatch) {
		this.lastMatch = lastMatch;
	}

	public Array<Integer> getPlayerCardList() {
		return playerCardList;
	}
}
