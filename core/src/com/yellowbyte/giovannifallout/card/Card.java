package com.yellowbyte.giovannifallout.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;

public abstract class Card {

	private static Texture cardSheet = Assets.manager.get(Assets.card_types, Texture.class);
	private static TextureRegion CARD_BG = new TextureRegion(cardSheet, 0, 0, 500, 900);
	private static TextureRegion CARD_BG_2 = new TextureRegion(cardSheet, 500, 0, 500, 900);
	private static TextureRegion CARD_BG_3 = new TextureRegion(cardSheet, 1000, 0, 500, 900);
	
	
	private TextureRegion back = CARD_BG;
	public static float WIDTH = 250;
	public static float HEIGHT = 450;
	public static float UNIT_WIDTH = 125;
	public static float UNIT_HEIGHT = 200;
	
	
	//W/H OF PREVIEW IMAGE ON CARD
	public static Vector2 PREV_SIZE = new Vector2(0.96f, 0.4f);
	public static Vector2 PREV_POS = new Vector2(0.021f, 0.4445f);	
	public static final float TITLE_Y = 0.95f;
	
	public static final float ATT_STAT_X = 0.245f;
	public static final float CD_STAT_X = 0.55f;
	public static final float HP_STAT_X = 0.85f;
	public static final float STAT_Y = 0.405f;
	public static final float COST_X = 0.76f;
	public static final float COST_Y = 0.82f;
	public static final float DESC_Y = 0.3f;
	
	private int ID;
	private String name;
	private Array<String> description;
	private int cost;
	private int baseAttack;
	private int baseHealth;
	private int baseCountdown;
	private TextureRegion preview;
	private Vector2 pos;
	
	private String type;
	private int drawBonus;
	private int cashBonus;
	private String rarity;
    
	
	private float scaleFactor = 1f;
	
	public static final String MONSTER = "MON";
	public static final String ACTION = "EFF";
	public static final String EVENT = "ALL";

	
	public Card(int ID, String name, String description, int cost, int attack, int health, int countdown,
                        String type, int drawBonus, int cashBonus, TextureRegion preview, String rarity) {


		this.ID = ID;
		this.name = name;
		this.description = new Array<String>();
		
		int maxChar = 20;		
		int spacePos = 0;
		String remainderString = description;
		
		while(remainderString.length() > 0) {
			if(remainderString.length() > maxChar) {
				String sub = remainderString.substring(0, maxChar);
				spacePos = sub.lastIndexOf(" ");
				remainderString = remainderString.substring(spacePos, remainderString.length());
				this.description.add(sub.substring(0, spacePos));
			} else {
				this.description.add(remainderString.substring(0, remainderString.length()));
				remainderString = "";
			}
		}
		
		
		this.cost = cost;
		this.baseAttack = attack;
		this.baseHealth = health;
		this.baseCountdown = countdown;
		this.type = type;
		this.drawBonus = drawBonus;
		this.cashBonus = cashBonus;
		this.rarity = rarity;
		
		if(type.equals(MONSTER)) {
			back = CARD_BG;
		} else if(type.equals(ACTION)) {
			back = CARD_BG_2;
		} else {
			back = CARD_BG_3;
		}
		
		pos = new Vector2(0, 0);
		
		this.preview = preview;
	}
	
	//Standard render method for drawing scaled down cards in hand GUI.
	public void render(SpriteBatch sb) {
		render(pos, scaleFactor, sb);
	}
	
	// Renders the card with the specified dimensions.
	public void render(Vector2 position, float scale, SpriteBatch sb) {

		drawCardImages(sb, position, scale);

		drawCardText(sb, position, scale);
	}
	
	
	private void drawCardImages(SpriteBatch sb, Vector2 pos, float scale) {
		float width = WIDTH*scale;
		float height = HEIGHT*scale;
		
		sb.draw(preview, pos.x + (PREV_POS.x * width), pos.y + (PREV_POS.y * height), PREV_SIZE.x * width, PREV_SIZE.y * height);
		sb.draw(back, pos.x, pos.y, width, height);		
	}
	
	private void drawCardText(SpriteBatch sb, Vector2 pos, float scale) {
		float width = WIDTH*scale;
		float height = HEIGHT*scale;
		
		Fonts.scaleCardFonts(width/WIDTH);

        if(rarity.equals("rare")) {
            Fonts.c_Title.setColor(Color.YELLOW);
        }
		
		Fonts.c_Title.draw(sb, name, (pos.x+(width/2))-Fonts.c_Title.getBounds(name).width/2, pos.y+height*TITLE_Y);
		
		float descY = STAT_Y;
        float statY = pos.y+(height*STAT_Y);
		if(type.equals(MONSTER)) {
			descY = DESC_Y;
			Fonts.c_Stat.draw(sb, ""+baseAttack, (pos.x+width*ATT_STAT_X), statY);
			Fonts.c_Stat.draw(sb, ""+baseCountdown, (pos.x+width*CD_STAT_X), statY);
			Fonts.c_Stat.draw(sb, ""+baseHealth, (pos.x+width*HP_STAT_X), statY);
		} 
		
		for(int i = 0; i < description.size; i++) {
			String descLine = description.get(i);
			
			Fonts.c_Description.draw(sb, descLine, (pos.x+(width/2))-Fonts.c_Description.getBounds(descLine).width/2, (pos.y+height*descY)-(i*height*0.06f));
			
		}
		Fonts.c_Cost.draw(sb, "$"+cost, (pos.x+(width*COST_X)), pos.y+height*COST_Y);
        Fonts.scaleCardFonts(1f);
        Fonts.c_Title.setColor(Color.BLACK);
	}
	
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}

	public int getBaseAttack() {
		return baseAttack;
	}

	public int getBaseHealth() {
		return baseHealth;
	}

	public int getBaseCountdown() {
		return baseCountdown;
	}

	public Vector2 getPos() {
		return pos;
	}
	
	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(pos.x, pos.y, WIDTH*scaleFactor, HEIGHT*scaleFactor);
	}

	public String getType() {
		return type;
	}
	
	public int getDrawBonus() {
		return drawBonus;
	}

	public int getCashBonus() {
		return cashBonus;
	}
	
	public float getScaleFactor() {
		return scaleFactor;
	}
	
	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

    public abstract Unit getUnit();
}
