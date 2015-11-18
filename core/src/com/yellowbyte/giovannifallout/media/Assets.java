package com.yellowbyte.giovannifallout.media;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	
	public static AssetManager manager = new AssetManager();
	
	public static final String MENU_BG_NEAR = "images/bg_menu_buildings.png";
	public static final String MENU_BG_FAR = "images/bg_menu_faraway.png";
	public static final String GAME_BG = "images/game_bg.png";
	public static final String CARDSCREEN_BG = "images/bg_cardscreen.png";
	public static final String BUTTON_BG = "images/bg_button.png";
	
	public static final String load_wheel = "images/load_wheel.png";
	public static final String gui_sheet = "images/gui_sheet.png";
	public static final String guisheet = "images/guisheet.png";
	public static final String profile_sheet = "images/profile_sheet.png";
	public static final String card_sheet = "images/card_sheet.png";
	public static final String card_types = "images/card_types.png";
	public static final String unit_sheet = "images/unit_sheet.png";
	public static final String deck_button = "images/deck_button.png";
	public static final String delete_deck = "images/delete_deck.png";
	public static final String victory_dance = "images/victory_dance.png";
	public static final String stat_back = "images/stat_back.png";
	public static final String alpha = "images/alpha.png";
	public static final String page = "images/page.png";
	public static final String card_selected = "images/card_select.png";
	public static final String black = "images/black.png";
	public static final String LOSER = "images/loser.png";
	public static final String menu_icons = "images/menuscreen_icons.png";
	public static final String attack_arrow = "images/attack_arrow.png";
	public static final String attack_wind = "images/attack_wind.png";
	public static final String tilesheet_blue = "images/tilesheet_blue.png";
	public static final String tilesheet_red = "images/tilesheet_red.png";
	public static final String tower_sheet = "images/tower_sheet.png";
	public static final String tile_selected = "images/tile_selected.png";
	public static final String sac_sheet = "images/sac_sheet.png";
	public static final String name_bar = "images/name_bar.png";
	public static final String event_button = "images/event_button.png";
	public static final String mag_button = "images/mag.png";
	public static final String time_box = "images/timebox.png";
	public static final String hourglass = "images/hourglass.png";
	public static final String start_button = "images/start_button.png";
	
	//RESULTS SCREEN
	public static final String results_bg = "results/bg.png";
	public static final String continue_button = "results/continue.png";
	public static final String gold_coin = "results/gold.png";
	public static final String xp = "results/xp.png";
	
	
	//STORE SCREEN
	public static final String random_card = "store/random_card.png";
	public static final String rare_card = "store/rare_card.png";
	

	//Sounds
	public static final String MAIN_THEME = "sounds/main_theme.mp3";
	public static final String VICTORY_THEME = "sounds/victorymusic.mp3";
	
    public static final String CARD_SELECT = "sounds/cardSelect.wav";
    public static final String SUMMON = "sounds/summon.wav";
    public static final String HIT = "sounds/punchHit.wav";
    public static final String TOWERHIT = "sounds/towerHit.wav";
    public static final String TOWERGONE = "sounds/towerGone.wav";
    public static final String CLICK = "sounds/click.wav";
    public static final String SAC_RESOURCE = "sounds/cashRegister.wav";
    public static final String SAC_CARDS = "sounds/cardDeal.wav";
    public static final String ACTION = "sounds/action.wav";
    public static final String CURSE_WORD = "sounds/curse_word.wav";
    public static final String TILE_SELECT = "sounds/tileSelect.wav";
    public static final String TILE_MOVE = "sounds/tileMove.wav";
    public static final String XP_BEEP = "sounds/xpBeep.wav";
    public static final String GOLD_BEEP = "sounds/goldBeep.wav";


	public static void load () {

		manager = new AssetManager();
        manager.load(MENU_BG_NEAR, Texture.class);
        manager.load(MENU_BG_FAR, Texture.class);
        manager.load(GAME_BG, Texture.class);
        manager.load(CARDSCREEN_BG, Texture.class);    
        manager.load(BUTTON_BG, Texture.class);  
        manager.load(load_wheel, Texture.class);
        manager.load(gui_sheet, Texture.class);
        manager.load(guisheet, Texture.class);
        manager.load(profile_sheet, Texture.class);
        manager.load(card_sheet, Texture.class);
        manager.load(card_types, Texture.class);
        manager.load(unit_sheet, Texture.class);
        manager.load(deck_button, Texture.class);
        manager.load(delete_deck, Texture.class);
        manager.load(victory_dance, Texture.class);
        manager.load(stat_back, Texture.class);
        manager.load(LOSER, Texture.class);
        manager.load(menu_icons, Texture.class);
        manager.load(attack_arrow, Texture.class);
        manager.load(attack_wind, Texture.class);
        manager.load(tilesheet_blue, Texture.class);
        manager.load(tilesheet_red, Texture.class);
        manager.load(tile_selected, Texture.class);
        manager.load(tower_sheet, Texture.class);
        manager.load(sac_sheet, Texture.class);
        manager.load(name_bar, Texture.class);
        manager.load(event_button, Texture.class);
        manager.load(mag_button, Texture.class);
        manager.load(time_box, Texture.class);
        manager.load(hourglass, Texture.class);
        manager.load(start_button, Texture.class);
        
        //RESULTS SCREEN
        manager.load(results_bg, Texture.class);
        manager.load(continue_button, Texture.class);
        manager.load(gold_coin, Texture.class);
        manager.load(xp, Texture.class);
        
        
        //STORE SCREEN
        manager.load(random_card, Texture.class);
        manager.load(rare_card, Texture.class);
        
        //OVERLAY TEXTURES
        manager.load(alpha, Texture.class);
        manager.load(page, Texture.class);
        manager.load(card_selected, Texture.class);
        manager.load(black, Texture.class);

        
        //LOAD MUSIC
        manager.load(MAIN_THEME, Music.class);
        manager.load(VICTORY_THEME, Music.class);
        
        
        //LOAD SOUNDS
        manager.load(CARD_SELECT, Sound.class);
        manager.load(SUMMON, Sound.class);
        manager.load(HIT, Sound.class);
        manager.load(TOWERHIT, Sound.class);
        manager.load(TOWERGONE, Sound.class);
        manager.load(CLICK, Sound.class);
        manager.load(SAC_RESOURCE, Sound.class);
        manager.load(SAC_CARDS, Sound.class);
        manager.load(ACTION, Sound.class);
        manager.load(CURSE_WORD, Sound.class);
        manager.load(TILE_SELECT, Sound.class);
        manager.load(TILE_MOVE, Sound.class);
        manager.load(XP_BEEP, Sound.class);
        manager.load(GOLD_BEEP, Sound.class);
	}
	public static void dispose() {
    	manager.dispose();
    }

    public static boolean update() {
        return manager.update();
    }
}
