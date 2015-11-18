package com.yellowbyte.giovannifallout.touch;

import com.yellowbyte.giovannifallout.Player;
import com.yellowbyte.giovannifallout.board.Tile;
import com.yellowbyte.giovannifallout.card.Card;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.SoundManager;

public class NullTouchState implements TouchState {

	private TouchManager context;
	private Player user;
	
	public NullTouchState(TouchManager tm, Player user) {
		context = tm;
		this.user = user;
	}

	
	@Override
	public void handleTile(Tile t) {
		if (t.isOccupied() && (user.isPlacing() || user.isWaiting())) {
			t.setSelected(true);
			SoundManager.play(Assets.TILE_SELECT);
			setState(new TileTouchState(context, t, user));
		}
	}

	@Override
	public void handleCard(Card card) {

		if(card != null) {
			System.out.println("Card Selected");
			setState(new CardTouchState(card, context, user));
		}		
	}
	
	@Override
	public String getString() {
		return "NULL";
	}


	@Override
	public void setState(TouchState state) {
		context.setState(state);
	}
}
