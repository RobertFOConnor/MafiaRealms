package com.yellowbyte.giovannifallout.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yellowbyte.giovannifallout.Entity;
import com.yellowbyte.giovannifallout.GameManager;
import com.yellowbyte.giovannifallout.ParticleManager;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.card.EffectCard;
import com.yellowbyte.giovannifallout.card.UnitCard;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.SoundManager;


public class Tile extends Entity { //A tile in the grid, can be occupied by a unit.
	
	private Texture tileSheet;
	private TextureRegion TILE;
	private TextureRegion TILE_S;
	private TextureRegion TILE_READY;
	private TextureRegion TILE_AVAIL;
	private TextureRegion TILE_EMPTY;
	private Vector2 UNIT_POS;
	public final static float WIDTH = 200;
	
	private boolean occupied, owner, selected, moving = false;
	private int row, col;
	private UnitCard card;

	
	public Tile(Vector2 pos, boolean owner) {
		super(new TextureRegion(Assets.manager.get(Assets.tile_selected, Texture.class)), pos);
		this.owner = owner;
		
		
		if(!owner) {
			tileSheet = Assets.manager.get(Assets.tilesheet_red, Texture.class);
		} else {
			tileSheet = Assets.manager.get(Assets.tilesheet_blue, Texture.class);
		}
		
		TILE = 		 new TextureRegion(tileSheet, 0,   0, 200, 172);
		TILE_AVAIL = new TextureRegion(tileSheet, 200, 0, 200, 172);
		TILE_READY = new TextureRegion(tileSheet, 400, 0, 200, 172);
		TILE_EMPTY = new TextureRegion(tileSheet, 600, 0, 200, 172);
		
		TILE_S = new TextureRegion(Assets.manager.get(Assets.tile_selected, Texture.class));
		setTexture(TILE_EMPTY);
		
		UNIT_POS = new Vector2(pos.x+((Tile.WIDTH/2)-(Card.UNIT_WIDTH/2)), pos.y+30);
	}
	
	@Override
	public void update() {
		if(moving) {
			
			//destination
			Vector2 minusVec = new Vector2((UNIT_POS.x - card.getPos().x)/10,(UNIT_POS.y - card.getPos().y)/10);	
			
			card.setPos(card.getPos().cpy().add(minusVec));
			
			if(Math.sqrt(Math.pow((UNIT_POS.x-card.getPos().x), 2)+Math.pow((UNIT_POS.y-card.getPos().y), 2)) < 1) {
				card.setPos(UNIT_POS);
				moving = false;
			}
		}
	}
	
	@Override
	public void render(SpriteBatch sb) {
		sb.draw(texture, pos.x, pos.y);
		
		if(occupied) {
			card.getUnit().render(sb, owner);
		}
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		if(selected) {
			this.setTexture(TILE_S);
			
			card.getUnit().showStatBox();
			
		} else {
			setTileImage();
		}
		this.selected = selected;
	}
	

	
	public void addCard(UnitCard card) {
		this.card = card;
		card.setPos(UNIT_POS);
		setTileImage();
		occupied = true;
		
		GameManager.pm.addEffect(ParticleManager.EffectType.SPAWN, pos.x+Tile.WIDTH/2, pos.y+20);
	}
	
	public void recieveCard(UnitCard card) {
		this.card = card;
		moving = true;
		setTileImage();
		card.setCurrMove(card.getCurrMove() - 1);
		occupied = true;
	}
	
	
	public void moveCard(Tile t) {
		t.recieveCard(card);
		removeCard();
		SoundManager.play(Assets.TILE_MOVE);
	}
	
	public void destroyCard() {
		card.onDeath();
		removeCard();
		GameManager.pm.addEffect(ParticleManager.EffectType.DESTROY, pos.x + Tile.WIDTH / 2, pos.y + 20);
	}

	private void removeCard() {
		card = null;
		occupied = false;
		setTileImage();
	}

	public UnitCard getCard() {
		return card;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public String toString() {
		return "("+row+","+col+")";
	}

	public boolean isOwner() {
		return owner;
	}

	public void applyCard(EffectCard actionCard) {

		card.addEffect(actionCard);
		
		setTileImage();
		
		
		if(card.getCurrHealth() <= 0) {
			destroyCard();
			
		} else { //Only show effect if unit is not killed.
			GameManager.pm.addEffect(ParticleManager.EffectType.EFFECT, pos.x + Tile.WIDTH / 2, pos.y + 20);
		}
	}


	public void setTileImage() {
		
		setTexture(TILE_EMPTY);	
		
		int cdNum = 1;
		if(owner) {
			cdNum = 0;
		}
		
		if(card != null) {
			setTexture(TILE);
			if(card.getCurrCountdown() <= cdNum) {
				setTexture(TILE_READY);
			}
		}
	}


	public void setAvailable(boolean isAvailable) {
		if(isAvailable) {
			setTexture(TILE_AVAIL);
		} else {
			setTileImage();
		}
	}

	public boolean isAdjacent(int newRow, int newCol) {

        int rowDiff = Math.abs(newRow-row);
        int colDiff = Math.abs(newCol-col);


		if(rowDiff == 0) { //Sideways Move
            if (colDiff == 1) {
                return true;
            }

        } else if(rowDiff == 1) { //Vertical Move
            if (colDiff == 0) {
                return true;
            } else if (colDiff == 1) { //Diagonal

                if ((row == 0 || row == 2)) {
                    if (col == 2) {
                        return false;
                    } else if (col == 1) {
                        if (newCol > col) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                } else { //Middle Row
                    if (col == 1) {
                        if (newCol < col) {
                            return true;
                        }
                    } else if(col == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
	}
}
