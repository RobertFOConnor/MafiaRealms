package com.yellowbyte.giovannifallout.card;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by BobbyBoy on 10-Nov-15.
 */
public class UnitCard extends Card {

    private int currAttack;
    private int currHealth;
    private int currCountdown;
    private int baseMove, currMove;

    private Unit unit;

    private Array<EffectCard> effects;
    private boolean hasEffects = false;
    private boolean isPoisoned = false;
    private boolean unstopable = false;

    public UnitCard(int ID, String name, String description, int cost, int attack, int health, int countdown,
                      String type, int drawBonus, int cashBonus, TextureRegion preview, Sprite unit, String rarity) {
        super(ID, name, description, cost, attack, health, countdown, type, drawBonus, cashBonus, preview, rarity);

        currAttack = attack;
        currHealth = health;
        currCountdown = countdown;
        baseMove = 1;
        currMove = 1;

        this.unit = new Unit(this, unit);

        effects = new Array<EffectCard>();
    }

    public void addEffect(EffectCard actionCard) {

        currAttack = (currAttack + actionCard.getBaseAttack());
        currCountdown = (currCountdown + actionCard.getBaseCountdown());
        int oldHealth = currHealth;
        currHealth = (currHealth + actionCard.getBaseHealth());

        if(currHealth < oldHealth) { //Show unit just got hurt.
            unit.hitUnit();
        }


        effects.add(actionCard);
        hasEffects = true;
    }

    public Unit getUnit() { return unit; }

    public int getCurrAttack() {
        return currAttack;
    }

    public void setCurrAttack(int currAttack) {
        this.currAttack = currAttack;
    }

    public int getCurrHealth() {
        return currHealth;
    }

    public void setCurrHealth(int currHealth) {
        this.currHealth = currHealth;
    }

    public int getCurrCountdown() {
        return currCountdown;
    }

    public void setCurrCountdown(int currCountdown) {
        this.currCountdown = currCountdown;

        if(this.currCountdown < 0) {
            this.currCountdown = 0;
        }
    }

    public int getCurrMove() {
        return currMove;
    }

    public void setCurrMove(int currMove) {
        this.currMove = currMove;
    }

    public void resetMove() {
        currMove = baseMove;
    }


    public void onSummon() {}
    public void onStartTurn() {}
    public void onMove() {}
    public void onAttack() {}
    public void onTowerAttack() {}
    public void onHit() {}
    public void onDeath() {}

    public Array<EffectCard> getEffectCards() { return effects; }
}
