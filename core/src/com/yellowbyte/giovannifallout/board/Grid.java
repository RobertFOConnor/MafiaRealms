package com.yellowbyte.giovannifallout.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.MainGame;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.card.EffectCard;
import com.yellowbyte.giovannifallout.touch.TouchManager;


public class Grid {
	
	private TouchManager tm;
	private Vector2 touch;
	
	private Tile[][] tileGrid; //The 3x3 tile area
	private Tower[] towerList; //The list of towers
	
	private static float TowerY = 630;
	private static float TileY = 630;
	private boolean user;
	
	private final float tileScale = 200;
	private final float tileGap = 10;
	

	public Grid(TouchManager tm, boolean isUser) {
		this.tm = tm;
		user = isUser;
		
		float towerX = 120;		
		float tileX = 330;
		
		if(!isUser) {
			towerX = MainGame.WIDTH-(Tower.WIDTH+170);
			tileX = MainGame.WIDTH-330-((tileScale*3)+(tileGap*2));
		}
		
		initTowers(towerX);
		initTiles(tileX);
		
		touch = new Vector2();
	}
	
	public void update() {	
		
		for(int x = 0; x < tileGrid.length; x++) {
			for(int y = 0; y < tileGrid[x].length; y++) {
				tileGrid[x][y].update();
			}
		}
		
		if (Gdx.input.justTouched() && !GameManager.paused) {
			touch = MainGame.camera.unprojectCoordinates(Gdx.input.getX(),
					Gdx.input.getY());
			
			gridloop:
			for(int x = 0; x < tileGrid.length; x++) {
				for(int y = 0; y < tileGrid[x].length; y++) {
					
					if(tileGrid[x][y].checkTouch(touch)) {
						tm.getState().handleTile(tileGrid[x][y]);
						break gridloop;
					}
				}
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		
		towerList[0].render(sb);
		
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				Tile tile = tileGrid[y][x];
				tile.render(sb);
			}
		}
		
		towerList[1].render(sb);
		towerList[2].render(sb);
	}
	
	public void renderStatBoxes(SpriteBatch sb) {
		
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				Tile tile = tileGrid[y][x];
				if(tile.isSelected() && tile.getCard() != null) 
				tile.getCard().getUnit().getStatBox().render(sb, tile.isOwner());
			}
		}
		
	}
	
	public void initTiles(float tileMargin) {//opponent row is mirrored
		
		
		tileGrid = new Tile[3][3];
		float OFFSET = 105;
		if(user) {
			OFFSET = -105;
		}
		
		for(int col = 0; col < tileGrid.length; col++) {
			for(int row = 0; row < tileGrid[col].length; row++) {
				
				if(row == 1) {
					
					tileGrid[col][row] = new Tile(new Vector2(tileMargin+OFFSET+((tileScale+tileGap) * col), TileY - ((Tower.WIDTH-10) * row)), user);
					
				} else {
					tileGrid[col][row] = new Tile(new Vector2(tileMargin+((tileScale+tileGap) * col), TileY - ((Tower.WIDTH-10) * row)), user);
					
				}
				
				if(!user) {
					tileGrid[col][row].setCol(2-col);
				} else {
					tileGrid[col][row].setCol(col);
				}
				tileGrid[col][row].setRow(row);
				
			}
		}
	}
	
	public void initTowers(float towerMargin) {
		towerList = new Tower[3];
		for (int i = 0; i < 3; i++) {
			float mar = towerMargin;
			if(i == 1) {
				if(user) {
					mar-= 100;
				} else {
					mar+=100;
				}
				
			} 
			
			towerList[i] = new Tower(user, new Vector2(mar, TowerY - ((Tower.WIDTH-10) * i)), 10);
		}
	}

	public boolean isUser() {
		return user;
	}
	
	public Array<Tile> getAttackers(boolean isUser) { //Returns units attacking this turn.
		Array<Tile> attackers = new Array<Tile>();
		
		for(int col = 0; col < tileGrid.length; col++) {
			for(int row = 0; row < tileGrid[col].length; row++) {
				Tile t;
				if(isUser) {
					t = tileGrid[2-col][row];
				} else {
					t = tileGrid[col][row];
				}
				
				if(t.isOccupied()) {
					if(t.getCard().getCurrCountdown() <= 0) {
						attackers.add(t);
					}
				}
			}
		}
		return attackers;
	}
	
	public Tile getTarget(int row, boolean isUser) { //Returns front unit being targeted. Returns null if no units are on that row. 
		
		for(int y = 0; y < tileGrid.length; y++) {
			Tile t = tileGrid[2-y][row];
			if(isUser) {
				t = tileGrid[y][row];
			}
			if(t.isOccupied()) {
				return t;
			}
		}
		return null;
	} 


	public void startTurn() {
		for(int x = 0; x < tileGrid.length; x++) {
			for(int y = 0; y < tileGrid[x].length; y++) {
				if(tileGrid[x][y].isOccupied()) {
					tileGrid[x][y].getCard().onStartTurn();
				}
			}
		}
	}
	
	
	public void applyToAll(EffectCard card) {
		for(int x = 0; x < tileGrid.length; x++) {
			for(int y = 0; y < tileGrid[x].length; y++) {
				if(tileGrid[x][y].isOccupied()) {
					tileGrid[x][y].applyCard(card);
				}
			}
		}
	}	
	
	
	public Tile getTile(int row, int col) {
		for(int c = 0; c < tileGrid.length; c++) {
			for(int r = 0; r < tileGrid[c].length; r++) {
				if(tileGrid[c][r].getRow() == row && tileGrid[c][r].getCol() == col) {
					return tileGrid[c][r];
				}			
			}
		}
		return null;
	}


	public Tower getTower(int row) {
		return towerList[row];
	}

	public boolean hasTowersStanding() {
		int towerCount = 0;
		
		for(Tower tower : towerList) {
			if(!tower.isDestroyed()) {
				towerCount++;
			}
		}
		
		if(towerCount > 1) {
			return true;
		}
		return false;
	}
	
	public void destroyTowers() {
		for(Tower tower : towerList) {
			tower.destroy();
		}
	}

	public int getUnitCount() {		
		int count = 0;		
		
		for(int x = 0; x < tileGrid.length; x++) {
			for(int y = 0; y < tileGrid[x].length; y++) {
				if(tileGrid[x][y].isOccupied()) {
					count++;
				}
			}
		}
		return count;
	}

	public Array<Tile> getEmptyTiles() {
		Array<Tile> emptyTiles = new Array<Tile>();
		
		for(int c = 0; c < tileGrid.length; c++) {
			for(int r = 0; r < tileGrid[c].length; r++) {
				if(!tileGrid[c][r].isOccupied()) {
					emptyTiles.add(tileGrid[c][r]);
				}
			}
		}
		return emptyTiles;
	}
	
	public Array<Tile> getOccupiedTiles() {
		Array<Tile> occTiles = new Array<Tile>();
		
		for(int c = 0; c < tileGrid.length; c++) {
			for(int r = 0; r < tileGrid[c].length; r++) {
				if(tileGrid[c][r].isOccupied()) {
					occTiles.add(tileGrid[c][r]);
				}
			}
		}
		return occTiles;
	}
	
	public Array<Tile> getAllTiles() {
		Array<Tile> allTiles = new Array<Tile>();
		
		for(int c = 0; c < tileGrid.length; c++) {
			for(int r = 0; r < tileGrid[c].length; r++) {
				allTiles.add(tileGrid[c][r]);
			}
		}
		return allTiles;
	}

	public void deselectTiles() {
		for(int c = 0; c < tileGrid.length; c++) {
			for(int r = 0; r < tileGrid[c].length; r++) {
				if(tileGrid[c][r].isSelected()) {
					tileGrid[c][r].setSelected(false);
				}
				tileGrid[c][r].setTileImage();
			}
		}
	}
}
