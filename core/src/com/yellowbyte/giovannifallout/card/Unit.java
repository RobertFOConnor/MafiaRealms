package com.yellowbyte.giovannifallout.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbyte.giovannifallout.media.Assets;
import com.yellowbyte.giovannifallout.media.Fonts;
import com.yellowbyte.giovannifallout.tween.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by BobbyBoy on 10-Nov-15.
 */
public class Unit {

    //UNIT VARIABLES.
    private Sprite unit;
    private UnitStatBox statBox;
    private TweenManager tweenManager;
    private long startTime, delta;

    private UnitCard card;

    public Unit(UnitCard card, Sprite unit) {
        this.card = card;
        this.unit = unit;

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        tweenManager = new TweenManager();

        TweenCallback myCallBack = new TweenCallback(){
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                startTime = TimeUtils.millis();
            }
        };


        Tween.to(unit, SpriteAccessor.SCALE_XY, 50f)
                .target(1f, 1.05f)
                .repeatYoyo(Tween.INFINITY, 0)
                .ease(TweenEquations.easeNone)
                .setCallback(myCallBack)
                .setCallbackTriggers(TweenCallback.END)
                .start(tweenManager);

        startTime = TimeUtils.millis();
    }


    //Standard render method for drawing card unit.
    public void render(SpriteBatch sb, boolean owner) {
        render(sb, owner, card.getPos());
    }


    //Renders unit at given position.
    public void render(SpriteBatch sb, boolean owner, Vector2 pos) {
        delta = (TimeUtils.millis()-startTime+1000)/1000;
        tweenManager.update(delta);

        float offsetY = (unit.getHeight()*unit.getScaleY())-unit.getHeight()-(unit.getScaleY()*8);

        unit.setPosition(pos.x, pos.y + offsetY);

        if (!owner) {
            if(!unit.isFlipX()) {
                unit.flip(true, false);
            }
        }
        unit.draw(sb);
    }

    public void hitUnit() {
        unit.setColor(Color.RED);

        Tween.to(unit, SpriteAccessor.TINT, 50f)
                .target(1,1,1)
                .ease(TweenEquations.easeNone)
                .start(tweenManager);

        startTime = TimeUtils.millis();
    }


    public UnitStatBox getStatBox() {
        return statBox;
    }

    public class UnitStatBox {

        private float L_MAR = 0;
        private float statY;
        private TweenManager tweenManager;
        private long startTime, delta;
        private Sprite alphaBG;

        private Vector2 pos;


        public UnitStatBox() {
            alphaBG = new Sprite(Assets.manager.get(Assets.stat_back, Texture.class));
            alphaBG.setScale(0.001f);
            pos = card.getPos();
            alphaBG.setPosition(L_MAR - 30, pos.y);

            Tween.registerAccessor(Sprite.class, new SpriteAccessor());
            tweenManager = new TweenManager();

            Tween.to(alphaBG, SpriteAccessor.SCALE_XY, 20f)
                    .target(1f, 1f)
                    .ease(TweenEquations.easeOutBounce)
                    .start(tweenManager);
            startTime = TimeUtils.millis();
        }

        // Renders stat box.
        public void render(SpriteBatch sb, boolean owner) {
            L_MAR = pos.x + (alphaBG.getWidth()/1.667f);
            if (!owner) {
                L_MAR = pos.x - (alphaBG.getWidth()/0.845f);
            }

            delta = (TimeUtils.millis()-startTime+1000)/1000;
            tweenManager.update(delta);

            alphaBG.setPosition(L_MAR - 30, pos.y);
            alphaBG.draw(sb);

            Fonts.statBoxFont.setScale(alphaBG.getScaleX());
            Fonts.statBoxFont.draw(sb, card.getName(), L_MAR, pos.y + 230);

            statY = pos.y+210;
            drawStat("Attack: ", card.getCurrAttack(), card.getBaseAttack(), sb);
            drawStat("Cooldown: ", card.getCurrCountdown(), card.getBaseCountdown(), sb);
            drawStat("Health: ", card.getCurrHealth(), card.getBaseHealth(), sb);


            Fonts.MFont.setScale(1f);
        }

        private void drawStat(String statName, int currVal, int baseVal, SpriteBatch sb) {
            statY -= 50;
            if (currVal > baseVal) {
                Fonts.statBoxFont.setColor(Color.YELLOW);
            }
            Fonts.statBoxFont.draw(sb, statName + currVal + "/" + baseVal, L_MAR, statY);
            Fonts.statBoxFont.setColor(Color.WHITE);
        }
    }

    public void showStatBox() {
        statBox = new UnitStatBox();
    }
}
