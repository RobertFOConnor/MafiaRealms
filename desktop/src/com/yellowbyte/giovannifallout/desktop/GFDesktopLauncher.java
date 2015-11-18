package com.yellowbyte.giovannifallout.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.google.DesktopGoogleServices;
import com.yellowbyte.giovannifallout.media.Constants;

public class GFDesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 540;
		config.title = Constants.TITLE;
		new LwjglApplication(new MainGame(new DesktopGoogleServices()), config);
		//new LwjglApplication(new MyTweenGame(), config);
	}
}
