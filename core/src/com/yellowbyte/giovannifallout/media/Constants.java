package com.yellowbyte.giovannifallout.media;

public class Constants {

	public static final String TITLE = "Mafia Realms";
	public static final String ENTER_USERNAME = "Please Enter Your Username";
	public static final String START_GAME = "START";
	public static final String CHOOSE_AVATAR = "Choose Your Avatar";
	public static final String VERSION_CODE = "7.0";
	
	
	public static final String NEW_GAME = "New Game";
	public static final String SINGLE_PLAYER = "Single Player";
	public static final String MULTIPLAYER = "Multiplayer";
	public static final String VIEW_CARDS = "View Cards";
	public static final String HOWTOPLAY = "How To Play";
	
	public static final String CONNECTING_TO_APPWARP = "...Connecting to AppWarp...";
	public static final String WAITING_FOR_USER = "...Waiting for other user...";
	public static final String FINDING_MATCH = "Finding Match";
	public static final String ERROR_IN_CONNECTION = "Error in Connection!";
	
	public static final String SAC_RESOURCE = "Sacrificed for Cash!";
	public static final String SAC_CARDS = "Sacrificed for Cards!";
	public static final String TIME_UP = "Time's Up!";
	
	public static final String YOUR_TURN = "Your Turn!";
	public static final String OPPONENTS_TURN = "Opponents Turn!";
	
	public static final String YOU_WIN = "You Win!";
	public static final String WIN_SUB = "The family is yours.";
	public static final String YOU_LOSE = "You Lose!";
	public static final String LOSE_SUB = "Sorry kid, not today!";
	public static final String RETURN_TO_MENU = "Return to Menu";
	
	public static final String TURNS_TAKEN = "Turns Taken: ";
	public static final String DAMAGE_DEALT = "Damage Dealt: ";
	public static final String DAMAGE_TAKEN = "Damage Taken: ";
	public static final String XP_EARNED = "XP Earned: ";
	
	public static final String SURRENDER = "Surrender";
	public static final String RETURN_TO_GAME = "Return To Game";
	
	
	//Player States
	public static final String WAITING = "WAITING";
	public static final String PLACING = "PLACING";
	public static final String ATTACKING = "ATTACKING";
	
	public static final String CASH = "Cash: $";
	
	//How To Play Strings
	public static final String SUMMARY_1 = "The main objective of the game is to destroy 2 enemy buildings.";
	public static final String SUMMARY_2 = "You must use your cards to place units and attack the enemy.";
	public static final String SUMMARY_3 = "Sacrifice a card each turn to earn money for cards, or get more cards.";
	public static final String SUMMARY_4 = "If the opponent destroys your towers first, you fail.";	
	public static final String[] SUMMARY_PAR = {SUMMARY_1, SUMMARY_2, SUMMARY_3, SUMMARY_4};
	
	
	public static final String CARDS_1 = "Each card you play costs money, so look in the top corner to make sure you can afford it!";
	public static final String CARDS_2 = "Unit cards can be played on an empty tile, but Action cards must be played on a unit.";
	public static final String CARDS_3 = "Action & Event cards can be used to improve, delay or destroy existing units on the board.";
	public static final String CARDS_4 = "N.B. Yellow: Unit Cards    Blue: Action Cards    Purple: Event Cards";	
	public static final String[] CARDS_PAR = {CARDS_1, CARDS_2, CARDS_3, CARDS_4};
	
	
	public static final String TILES_1 = "You may only place units on your own tiles, on the left side of the board.";
	public static final String TILES_2 = "If a card can be placed on a tile, it will show a glowing symbol.";
	public static final String TILES_3 = "The frontmost units in a row will always attack first.";
	public static final String TILES_4 = "Action & event cards can be played on enemy units.";	
	public static final String[] TILES_PAR = {TILES_1, TILES_2, TILES_3, TILES_4};
	
	public static final String UNITS_1 = "Unit stats are written as A, C, H : Attack, Countdown and Health";
	public static final String UNITS_2 = "A unit will only attack once its countdown has reached 0. This value decrements each turn.";
	public static final String UNITS_3 = "Units attack straight across, and only do damage to buildings if no units defend it.";
	public static final String UNITS_4 = "The frontmost units in a row will always attack first.";	
	public static final String[] UNITS_PAR = {UNITS_1, UNITS_2, UNITS_3, UNITS_4};
	
	public static final String[] CURSE_WORDS = { "fuck", "shit", "bitch",
			"cunt", "nigger", "nigga", "damn", "whore", "jizz", "cock",
			"pussy", "arse", "prostitute", "wank", "faggot", "retard", "blowjob"};
	
	public static boolean hasCurseWords(String s) {
		for(String curse : CURSE_WORDS) {
			if(s.contains(curse)) {
				SoundManager.play(Assets.CURSE_WORD);
				return true;
			}
		}
		return false;
	}
}
