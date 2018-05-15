package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class SpeedUp extends Bonus {

    private static final float ACTIVE_ADVERSE_SPEEDUP = 0.5f;
    private static final float ACTIVE_SPEEDUP = 2f;
    private static final float ACTIVE_INVINCIBLE_TIME = 4f;
    private static final float ACTIVE_SPEEDUP_TIME = 3f;
    private static final float INACTIVE_SPEEDUP = 1f;

    private float collideTime;

    public SpeedUp(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public int getValue() {
        return SPEED_UP;
    }

    @Override
    public boolean isFinished(Marble marble) {
        return collideTime >= ACTIVE_INVINCIBLE_TIME;
    }

    @Override
    void onCollide(Marble marble) {
        collideTime = 0;
        marble.setInvincible(true);
        marble.increaseActiveSpeedUps();
        marble.setSpeedUp(ACTIVE_SPEEDUP);
        playScreen.nbSpeedUp++;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        marble.getCenterPosition().z = Marble.JUMP_HEIGHT;

        if (!marble.isDead() && collideTime >= ACTIVE_SPEEDUP_TIME && marble.getActiveSpeedUps() == 1) {
            marble.setSpeedUp(INACTIVE_SPEEDUP);
        }

        if (!marble.isDead() && collideTime >= ACTIVE_INVINCIBLE_TIME && marble.decreaseActiveSpeedUps() == 0) {
            marble.setInvincible(false);
            marble.setInWall(true);
        }
    }

    public void activateSpeedUp(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSpeedUps();
        marble.setSpeedUp(ACTIVE_ADVERSE_SPEEDUP);
    }

}
