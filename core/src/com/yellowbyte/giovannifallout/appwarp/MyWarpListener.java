package com.yellowbyte.giovannifallout.appwarp;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.screens.MultiplayerGameScreen;
import com.yellowbyte.giovannifallout.screens.ScreenManager;

public class MyWarpListener implements WarpListener {

	private final String[] game_win = {"Congrats You Win!", "Enemy Defeated"};
	private final String[] game_loose = {"Oops You Loose!","Target Achieved","By Enemy"};
	private final String[] enemy_left = {"Congrats You Win!", "Enemy Left the Game"};
	
	
	
	
	private String msg = Constants.CONNECTING_TO_APPWARP;
	
	
	@Override
	public void onError (String message) {
		this.msg = Constants.ERROR_IN_CONNECTION;
	}

	@Override
	public void onGameStarted (String message) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run () {
				System.out.println("Players: "+WarpController.getInstance().getUsers()[0]+" VS "+WarpController.getInstance().getUsers()[1]);
				
				ScreenManager.setScreen(new MultiplayerGameScreen());
			}
		});
		
	}

	@Override
	public void onGameFinished (int code, boolean isRemote) {

		WarpController.getInstance().handleLeave();
	}

	@Override
	public void onGameUpdateReceived (String message) {//recieves info from other device if grid is updated

	}

	@Override
	public void onWaitingStarted(String message) {
		
		this.msg = Constants.WAITING_FOR_USER;
	}

	public String getMessage() {
		return msg;
	}
	
	public boolean isMyTurn() {
		return WarpController.getInstance().isMyTurn();
	}
}
