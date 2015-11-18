package com.yellowbyte.giovannifallout.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.TextButton;
import com.yellowbyte.giovannifallout.board.BoardManager;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.gui.Button;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;
import com.yellowbyte.giovannifallout.touch.TouchManager;

public class HowToScreen implements Screen {
	private OrthoCamera camera;
	private Vector2 touch;	
	private Texture bg;
	private BoardManager bm;
	
	private Button backButton;
	private TextButton cardsButton, tilesButton, unitsButton;
	private String[] currParagraph;

	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		touch = new Vector2(0,0);
		
		bg = Assets.manager.get(Assets.GAME_BG, Texture.class);
		
		bm = new BoardManager(new TouchManager());
		
		TextureRegion back = new TextureRegion(Assets.manager.get(Assets.guisheet, Texture.class), 0, 270, 150, 150);
		backButton = new Button(back, new Vector2((back.getRegionWidth()/3), MainGame.HEIGHT-(back.getRegionHeight()+(back.getRegionWidth()/3))));
		
		currParagraph = Constants.SUMMARY_PAR;
		
		cardsButton = new TextButton("Cards", Fonts.menuFont, new Vector2(MainGame.WIDTH/4, 180));
		tilesButton = new TextButton("Tiles", Fonts.menuFont, new Vector2((MainGame.WIDTH/4)*2, 180));
		unitsButton = new TextButton("Units", Fonts.menuFont, new Vector2((MainGame.WIDTH/4)*3, 180));
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());
				if (backButton.checkTouch(touch)) {
					SoundManager.play(Assets.CLICK);
					ScreenManager.setScreen(new MainMenuScreen());
				} else if(cardsButton.checkTouch(touch)) {
					currParagraph = Constants.CARDS_PAR;
				} else if(tilesButton.checkTouch(touch)) {
					currParagraph = Constants.TILES_PAR;
				} else if(unitsButton.checkTouch(touch)) {
					currParagraph = Constants.UNITS_PAR;
				}
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();	
		sb.draw(bg, 0, 0);
		bm.render(sb);
		sb.draw(Assets.manager.get(Assets.alpha, Texture.class), 0, 100, MainGame.WIDTH, MainGame.HEIGHT-200);
		Fonts.menuFont.draw(sb, Constants.HOWTOPLAY, MainGame.WIDTH/2-Fonts.menuFont.getBounds(Constants.HOWTOPLAY).width/2, MainGame.HEIGHT-120);
		
		for(int i = 0; i < currParagraph.length; i++) {
			Fonts.MFont.draw(sb, currParagraph[i], MainGame.WIDTH/2-Fonts.MFont.getBounds(currParagraph[i]).width/2, MainGame.HEIGHT-(250+(i*100)));
		}
		
		cardsButton.render(sb);
		tilesButton.render(sb);
		unitsButton.render(sb);
		
		backButton.render(sb);
		sb.end();
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
