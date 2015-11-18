package com.yellowbyte.giovannifallout.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.google.IGoogleServices;

public class AndroidLauncher extends AndroidApplication implements IGoogleServices {
	
	private GameHelper gameHelper;
	private final static int REQUEST_CODE_UNUSED = 9002;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInSucceeded() {
			}

			@Override
			public void onSignInFailed() {
			}
		};

		gameHelper.setup(gameHelperListener);
		gameHelper.setMaxAutoSignInAttempts(0);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainGame(this), config);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop(){
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
			//showAds(false);
		}		
		
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				// @Override
				public void run() {
					gameHelper.signOut();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}	
	}

	@Override
	public void rateGame() {
		// Replace the end of the URL with the package of your game
		String str = "https://play.google.com/store/apps/details?id=com.yellowbyte.giovannifallout.android&hl=en";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));

	}

	@Override
	public void submitScore(long score) {
		if (isSignedIn() == true) {
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(R.string.leaderboard_id), score);
		} else {
			// Maybe sign in here then redirect to submitting score?
		}
		
	}

	@Override
	public void showScores() {
		if (isSignedIn() == true)
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
					gameHelper.getApiClient(),
					getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		else {
			signIn();
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
					gameHelper.getApiClient(),
					getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		}
		
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
		
	}

	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(gameHelper
							.getApiClient()), 101);
		} else if (!gameHelper.isConnecting()) {
			signIn();
		}
	}

	@Override
	public void showAds(boolean show) {
		// TODO Auto-generated method stub
		
	}
}
