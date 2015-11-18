package com.yellowbyte.giovannifallout.touch;

import com.yellowbyte.giovannifallout.Player;
import com.yellowbyte.giovannifallout.board.Tile;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.SoundManager;

public class TileTouchState implements TouchState {

	private TouchManager context;
	private Player user;
	private Tile tile;
	
	public TileTouchState(TouchManager tm, Tile tile, Player user) {
		context = tm;
		this.user = user;
		this.tile = tile;
	}
	

	@Override
	public void handleTile(Tile t) {
		
		if((user.isPlacing() || user.isWaiting()) && tile.getCard() != null) {
			
			if (user.isPlacing() && !tile.equals(t)
					&& t.isOwner() && tile.isOwner() && !t.isOccupied()
					&& tile.getCard().getCurrMove() > 0 && tile.isAdjacent(t.getRow(), t.getCol())) {
				user.moveCard(tile, t); // Player uses currTile and newTile
				System.out.println("Card moved.");
				tile.setSelected(false);

			} else {
				tile.setSelected(false);
				
				if(t.isOccupied() && !t.equals(tile)) {			
					t.setSelected(true);
					SoundManager.play(Assets.TILE_SELECT);
					setState(new TileTouchState(context, t, user));			
				} else {	
					setState(new NullTouchState(context, user));
				}
			}			
			
		} else {
			tile.setSelected(false);
			setState(new NullTouchState(context, user));
		}
		
		
		
	}

	@Override
	public void handleCard(Card card) {

		if(card != null) {
			tile.setSelected(false);
			setState(new CardTouchState(card, context, user));
		}
	}
	
	@Override
	public String getString() {
		return "TILE";		
	}

	@Override
	public void setState(TouchState state) {
		context.setState(state);		
	}

}
