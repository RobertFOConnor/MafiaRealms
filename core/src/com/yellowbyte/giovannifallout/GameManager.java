package com.yellowbyte.giovannifallout;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbyte.giovannifallout.appwarp.MyWarpListener;
import com.yellowbyte.giovannifallout.appwarp.WarpController;
import com.yellowbyte.giovannifallout.appwarp.WarpListener;
import com.yellowbyte.giovannifallout.appwarp.WarpSender;
import com.yellowbyte.giovannifallout.board.BoardManager;
import com.yellowbyte.giovannifallout.gui.GUIManager;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Constants;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.screens.ResultsScreen;
import com.yellowbyte.giovannifallout.screens.ScreenManager;
import com.yellowbyte.giovannifallout.touch.NullTouchState;
import com.yellowbyte.giovannifallout.touch.TouchManager;

public class GameManager implements WarpListener {


    //Managers.
	private GUIManager gm;
	private BoardManager bm;
	private TouchManager tm;
    public static ParticleManager pm;


    //Players.
    private Player user, opponent;
    private com.yellowbyte.giovannifallout.AI.AIOpponent ai;


    //Game Statistics.
	public static int myTurns, oppTurns = 0;
    public static long damageDone, damageTaken;
	public static int towersDestroyed = 0;
    public static boolean surrender = false;
	

    //Pause Menu.
    private PauseMenu pauseMenu;
	public static boolean paused = false;
	public static boolean multiplayer;
	
	
	public GameManager(boolean multiplayer) {
		this.multiplayer = multiplayer;
		paused = false;
		tm = new TouchManager();
		bm = new BoardManager(tm);	//Needs touch manager for initializing Grids.
		
		setupPlayers();
		
		gm = new GUIManager(tm, user, opponent);
		
		tm.setState(new NullTouchState(tm, user));
	
		myTurns = 0;
		oppTurns = 0;
		damageDone = 0;
		damageTaken = 0;
        towersDestroyed = 0;
        surrender = false;

        if (multiplayer) {
            playersTurn(WarpController.getInstance().isMyTurn());
        } else {
            playersTurn((int) (Math.random()*2) == 1);
        }

        pauseMenu = new PauseMenu();
		pm = new ParticleManager();		
		
		WarpController.getInstance().setListener(this);
	}

    private void playersTurn(boolean is_my_turn) {
        if(is_my_turn) {
            opponent.setState(Player.WAITING);
            gm.createMessage(Constants.YOUR_TURN);
            myTurns++;
            user.startTurn();
            gm.startTurn(true);
        } else {
            user.setState(Player.WAITING);
            gm.createMessage(Constants.OPPONENTS_TURN);
            oppTurns++;
			opponent.startTurn();
            gm.startTurn(false);
        }
    }

    private void setupPlayers() {
        String[] playerNames;
        if (multiplayer) {
            playerNames = WarpController.getInstance().getUsers();
        } else {
            playerNames = new String[]{MainGame.userStats.getPlayerName(), "AI"};
        }

        user = new Player(playerNames[0], MainGame.chosenDeck, bm, true);
        opponent = new Player(playerNames[1], MainGame.chosenDeck, bm, false);
        ai = new com.yellowbyte.giovannifallout.AI.AIOpponent(opponent, user);
    }


    public void update() {

		bm.update();
		gm.update();
		
		if(opponent.isAttacking()) {
			if(bm.isFinishedAttack()) { //End opponents turn.
				tm.setState(new NullTouchState(tm, user));
				bm.deselectTiles();
                playersTurn(true);
            }
		} else if(opponent.isPlacing()) {
			if(!multiplayer) {
				ai.update();
			}
		}
		
		if(user.isAttacking()) {
			if(bm.isFinishedAttack()) { //End players turn.
                playersTurn(false);
            }
		}
		
		pauseMenu.update();
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();	
		bm.render(sb);
		gm.render(sb);
		pauseMenu.render(sb);
		sb.end();

		pm.render(sb);
	}


    private class PauseMenu {

        private Texture bgImg = Assets.manager.get(Assets.alpha, Texture.class);
        private TextButton surrender, back;
        private Vector2 touch;

        public PauseMenu() {
            surrender = new TextButton(Constants.SURRENDER, Fonts.menuFont, new Vector2(0, MainGame.HEIGHT/2+200));
            back = new TextButton(Constants.RETURN_TO_GAME, Fonts.menuFont, new Vector2(0, MainGame.HEIGHT/2-200));
            surrender.center();
            back.center();
        }


        public void update() {
            if (Gdx.input.justTouched()) {
                touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
                        Gdx.input.getY());

                if(paused) {
                    if(surrender.checkTouch(touch)) {
                        user.surrender();
                        GameManager.surrender = true;

                        WarpController.getInstance().handleLeave();
                        ScreenManager.setScreen(new ResultsScreen(false));

                    } else if(back.checkTouch(touch)) {
                        paused = false;
                    }
                }
            }
        }

        public void render(SpriteBatch sb) {
            if(paused) {
                sb.draw(bgImg, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
                surrender.render(sb);
                back.render(sb);
            }
        }
    }
	
	@Override
	public void onWaitingStarted(String message) {

		
	}

	@Override
	public void onError(String message) {
		
	}

	@Override
	public void onGameStarted(String message) {
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		gm.createMessage(opponent.getName() + " has disconnected.");
	}

	@Override
	public void onGameUpdateReceived(String message) {

		try {
			JSONObject data = new JSONObject(message);
			int type = data.getInt("updateType");
			boolean myGrid = false;
			int row = 0;
			int col = 0;
			int cardID = 0;

			switch (type) {
			case WarpSender.ENDED_TURN:
				opponent.startAttack();

				break;
			case WarpSender.SAC_RESOURCE:
				gm.createMessage(opponent.getName() + " sacrificed for Resources!");
				opponent.addResource();

				break;
			case WarpSender.SAC_CARDS:
				gm.createMessage(opponent.getName() + " sacrificed for Cards!");
				break;
			case WarpSender.SUMMONED_UNIT:
				row = (int) data.getInt("row");
				col = (int) data.getInt("col");
				cardID = (int) data.getInt("cardID");
				opponent.placeRecievedCard(row, col, cardID);

				// System.out.println("Put on: "+col+" "+row);

				break;
			case WarpSender.PLAYED_ACTION:
				row = (int) data.getInt("row");
				col = (int) data.getInt("col");
				cardID = (int) data.getInt("cardID");
				myGrid = (boolean) data.getBoolean("myGrid");

				if (myGrid) {
					user.applyRecievedCard(row, col, cardID);
				} else {
					opponent.applyRecievedCard(row, col, cardID);
				}

				//System.out.println("Recieved Action Card");

				break;
			case WarpSender.PLAYED_EVENT:
				cardID = (int) data.getInt("cardID");
				opponent.applyRecievedEventCard(cardID);

				//System.out.println("Recieved Event Card");

				break;
			case WarpSender.SURRENDER:
				bm.destroyTowers(false);
				break;
			case WarpSender.MOVED_UNIT:
				int oldRow = (int) data.getInt("oldRow");
				int oldCol = (int) data.getInt("oldCol");
				row = (int) data.getInt("row");
				col = (int) data.getInt("col");
				opponent.moveRecievedCard(oldRow, oldCol, row, col);
				break;
			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void endGame(boolean you_won) {
		if(you_won) {
			System.out.println("YOU WIN!");
			WarpController.getInstance().handleLeave();
			WarpController.getInstance().setListener(new MyWarpListener());
			ScreenManager.setScreen(new ResultsScreen(true));
		} else {
			System.out.println("YOU LOSE!");
			WarpController.getInstance().handleLeave();
			WarpController.getInstance().setListener(new MyWarpListener());
			ScreenManager.setScreen(new ResultsScreen(false));
		}
		
	}
}
