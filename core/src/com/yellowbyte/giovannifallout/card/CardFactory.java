package com.yellowbyte.giovannifallout.card;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yellowbyte.giovannifallout.media.Assets;

public class CardFactory {

	private ArrayList<CardPrototype> prototypes;

	   public Card createCard(int id) {

	     CardPrototype p = null;
	     
	     for(int i = 0; i < prototypes.size(); i++) {
	    	 
	    	 if(prototypes.get(i).id == id) {
	    		 p = prototypes.get(i);
	    	 }
	     }
           Card card;

           if(p.type.equals(Card.MONSTER)) {
               card = new UnitCard(p.id, p.name, p.desc, p.cost, p.atk, p.def, p.cdn, p.type, p.draw, p.cash, getPreview(p.prev), getUnit(p.type, p.unit), p.rarity) {

                   @Override
                   public void onSummon() {
                       if(getName().equals("Captain")) {
                           setCurrCountdown(0);
                       }
                   }

                   @Override
                   public void onAttack() {
						for(EffectCard c : getEffectCards()) {
							c.onAttack(this);
						}
                   }

                   @Override
                   public void onTowerAttack() {
                       for(EffectCard c : getEffectCards()) {
                           c.onTowerAttack(this);
                       }
                   }

                   @Override
                   public void onHit() {
                       for(EffectCard c : getEffectCards()) {
                           c.onHit(this);
                       }
                   }

                   @Override
                   public void onMove() {
                       for(EffectCard c : getEffectCards()) {
                           c.onMove(this);
                       }
                   }

                   @Override
                   public void onDeath() {
                       for(EffectCard c : getEffectCards()) {
                           c.onAttack(this);
                       }
                   }


				   @Override
			   	   public void onStartTurn() {
                       for(EffectCard c : getEffectCards()) {
                           c.onStartTurn(this);
                       }

					   setCurrCountdown(getCurrCountdown()- 1);
					   resetMove();
				   }
               };

           } else {
               card = new EffectCard(p.id, p.name, p.desc, p.cost, p.atk, p.def, p.cdn, p.type, p.draw, p.cash, getPreview(p.prev), p.rarity);
           }

           //...other code to add behaviours


	      return card;
	   }
	   
	private TextureRegion getPreview(String prev) {
		// SPECIFY WHAT PREVIEW IMAGE TO USE
		int sheetRow = 0;
		int sheetCol = 0;

		sheetRow = Character.getNumericValue(prev.charAt(2));
		sheetCol = Character.getNumericValue(prev.charAt(4));

		return new TextureRegion(Assets.manager.get(Assets.card_sheet,
				Texture.class), sheetCol * 260, sheetRow * 220, 260, 220);
	}

	private Sprite getUnit(String type, String unitString) {
		Sprite unit;
		int unitsheetRow = 0;
		int unitsheetCol = 0;

		unitsheetRow = Character.getNumericValue(unitString.charAt(2));
		unitsheetCol = Character.getNumericValue(unitString.charAt(4));

		if (type.equals(Card.MONSTER)) {
			unit = new Sprite(new TextureRegion(Assets.manager.get(
					Assets.unit_sheet, Texture.class), unitsheetCol * 125,
					unitsheetRow * 200, 125, 200));
		} else {
			unit = new Sprite();
		}

		return unit;
	}
}
