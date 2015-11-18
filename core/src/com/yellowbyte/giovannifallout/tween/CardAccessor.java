package com.yellowbyte.giovannifallout.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.math.Vector2;
import com.yellowbyte.giovannifallout.card.Card;

public class CardAccessor implements TweenAccessor<Card> {
	public static final int POS_XY = 1;
	
	@Override
	public int getValues(Card target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case POS_XY:
			returnValues[0] = target.getPos().x;
			returnValues[1] = target.getPos().y;
			return 2;

		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Card target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case POS_XY:
			target.setPos(new Vector2(newValues[0], newValues[1]));
			break;

		default:
			assert false;
		}
	}
}
