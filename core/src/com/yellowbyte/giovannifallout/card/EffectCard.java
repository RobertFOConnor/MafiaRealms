package com.yellowbyte.giovannifallout.card;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by BobbyBoy on 10-Nov-15.
 */
public class EffectCard extends Card {

    public EffectCard(int ID, String name, String description, int cost, int attack, int health, int countdown,
                      String type, int drawBonus, int cashBonus, TextureRegion preview, String rarity) {
        super(ID, name, description, cost, attack, health, countdown, type, drawBonus, cashBonus, preview, rarity);
    }

    @Override
    public Unit getUnit() {
        return null;
    }


    public void onStartTurn(UnitCard card) {}
    public void onMove(UnitCard card) {}
    public void onAttack(UnitCard card) {}
    public void onTowerAttack(UnitCard card) {}
    public void onHit(UnitCard card) {}
    public void onDeath(UnitCard card) {}
}
