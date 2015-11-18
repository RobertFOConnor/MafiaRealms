package com.yellowbyte.giovannifallout.media;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

public class Fonts {

	//Menu fonts
	public static BitmapFont mainTitle, menuFont, MFont, EditFont, DeckFont, statBoxFont, goldFont;
	
	//Menu fonts
	public static BitmapFont strikeFont, moneyFont;
	
	//Box fonts
	public static BitmapFont alertFont;
	
	//Card fonts
	public static BitmapFont c_Title, c_Cost, c_Stat, c_Description;
	
	public static int modifier = -12;

    public static boolean loaded = false;

    public static FreeTypeFontGenerator generator;
    public static FreeTypeFontParameter parameter;

	
	public static synchronized void initFonts() {

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        parameter = new FreeTypeFontParameter();


        parameter.size = 250+modifier;
        mainTitle = generator.generateFont(parameter);
        parameter.size = 85+modifier;
        menuFont = generator.generateFont(parameter);
        parameter.size = 55+modifier;
        MFont = generator.generateFont(parameter);
        parameter.size = 58+modifier;
        DeckFont = generator.generateFont(parameter);
        parameter.size = 60+modifier;
        EditFont = generator.generateFont(parameter);
        parameter.size = 48+modifier;
        statBoxFont = generator.generateFont(parameter);
        parameter.size = 180+modifier;
        goldFont = generator.generateFont(parameter);

        parameter.size = 85+modifier;
        alertFont = generator.generateFont(parameter);

        //CARD FONTS


        parameter.size = 43+modifier;
        c_Title = generator.generateFont(parameter);
        c_Title.setColor(new Color(0f, 0f, 0f, 1));
        parameter.size = 55+modifier;
        c_Cost = generator.generateFont(parameter);
        parameter.size = 50+modifier;
        c_Stat = generator.generateFont(parameter);
        parameter.size = 35+modifier;
        c_Description = generator.generateFont(parameter);
        parameter.size = 75+modifier;
        moneyFont = generator.generateFont(parameter);
        moneyFont.setColor(Color.WHITE);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/statfont.ttf"));
        FreeTypeFontParameter parameter2 = new FreeTypeFontParameter();
        parameter2.size = 80;
        strikeFont = generator2.generateFont(parameter2);
        strikeFont.setColor(Color.RED);

        generator2.dispose();


        loaded = true;
	}


	public static void scaleCardFonts(float scale) {
		Fonts.c_Title.setScale(scale);
		Fonts.c_Cost.setScale(scale);
		Fonts.c_Stat.setScale(scale);
		Fonts.c_Description.setScale(scale);
	}
}
