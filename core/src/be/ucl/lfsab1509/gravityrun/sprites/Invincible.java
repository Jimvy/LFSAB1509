package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Invincible extends Bonus {

    private float collideTime;

    public Invincible(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public int getValue() {
        return INVINCIBLE;
    }

    @Override
    public boolean isFinished(Marble marble) {
        return collideTime >= 3;
    }

    @Override
    void onCollide(Marble marble) {
        collideTime = 0;
        marble.increaseActiveInvincibles();
        marble.setInvincible(true);
        playScreen.nbInvincible++;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        if (!marble.isDead() && collideTime >= 3 && marble.decreaseActiveInvicibles() == 0) {
            marble.setInvincible(false);
            marble.setInWall(true);
        }
    }

}
