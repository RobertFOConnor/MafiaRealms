package com.yellowbyte.giovannifallout.media;

import com.badlogic.gdx.audio.Sound;
import com.yellowbyte.giovannifallout.MainGame;

public class SoundManager {

	public static void play(String s) {
		if(MainGame.settings.isSoundFXEnabled()) {
			Assets.manager.get(s, Sound.class).play();
		}		
	}
}
