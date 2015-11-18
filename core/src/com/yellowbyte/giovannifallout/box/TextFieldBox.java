package com.yellowbyte.giovannifallout.box;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.TextButton;
import com.yellowbyte.giovannifallout.camera.OrthoCamera;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.media.SoundManager;

public class TextFieldBox extends Box {
	
	private OrthoCamera camera;
	private Stage stage;
	private TextField tf;
	
	private TextButton confirmButton;
	private String title = Constants.ENTER_USERNAME;
	private String warning = "Warning: This cannot be changed!";
	
	
	public TextFieldBox() {
		super(1100, 400);
		
		camera = new OrthoCamera();
		camera.resize();
		stage = new Stage(camera.getViewPort());		
		
		Skin skin = new Skin(Gdx.files.internal("ui\\uiskin.json"));
		
		TextFieldStyle tStyle = new TextFieldStyle();
		tStyle.font = Fonts.EditFont;//here i get the font
		tStyle.fontColor = Color.WHITE;
		tStyle.background = skin.getDrawable("textfield");
		tStyle.cursor = skin.newDrawable("cursor", Color.WHITE);
		tStyle.cursor.setMinWidth(9f);
		tStyle.selection = skin.newDrawable("textfield", 0.5f, 0.5f, 0.5f,
		        0.5f);
		
		
		tf = new TextField("", tStyle);
		tf.setPosition(MainGame.WIDTH/2-450, 500);
		tf.setSize(900, 80);
		tf.setText("");
		tf.setMaxLength(22);
		stage.addActor(tf);
		
		
		float buttonY = MainGame.HEIGHT / 2 - 140;
		confirmButton = new TextButton("OK", Fonts.menuFont, new Vector2((MainGame.WIDTH/2) +400, buttonY));
	}
	
	public void display() {
		Gdx.input.setInputProcessor(stage);
		showing = true;
	}
	
	public int update(Vector2 touch) {
		
		if (confirmButton.checkTouch(touch)) {
			
			if(Constants.hasCurseWords(tf.getText().toLowerCase())) {
				SoundManager.play(Assets.CURSE_WORD);
				tf.setText("");
				return 0;
			} else if(tf.getText().length()<3) {
				tf.setText("");
				return 0;
			} else {
				MainGame.userStats.setPlayerName(tf.getText());
				MainGame.saveManager.saveDataValue("PLAYER", MainGame.userStats);
				
				stage.dispose();
				Gdx.input.setInputProcessor(null);
				SoundManager.play(Assets.CLICK);
				showing = false;
				return 1;
			}
		} 	
		return -1;
	}
	
	public void render(SpriteBatch sb) {
		delta = (TimeUtils.millis()-startTime+1000)/1000; 
		tweenManager.update(delta); 
		
		if (showing) {
			boxSprite.draw(sb);
			Fonts.menuFont.draw(sb, title, MainGame.WIDTH / 2 - Fonts.menuFont.getBounds(title).width / 2, boxSprite.getY()+350);
			Fonts.MFont.draw(sb, warning, MainGame.WIDTH / 2 - Fonts.MFont.getBounds(warning).width / 2, boxSprite.getY()+140);
			confirmButton.getPosition().set((MainGame.WIDTH/2) +400, boxSprite.getY()+50);
			confirmButton.render(sb);
			
			sb.end();
			tf.setPosition(MainGame.WIDTH/2-450, boxSprite.getY()+180);
			stage.draw();
			sb.begin();
		}
		
	}
}
