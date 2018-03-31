package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Wall extends Obstacle {

    public Wall(float y, int standardWidth, int marbleWidth) {
        super(y, "drawable-" + standardWidth + "/wall.png");
        setX(random.nextBoolean()
                ? -random.nextInt(2 * marbleWidth)
                : -random.nextInt(2 * marbleWidth) + GravityRun.WIDTH / 2);
    }

    @Override
    public void collides(Marble marble, SoundManager soundManager) {
        float marbleCenterX = marble.getCenterPosition().x;
        float marbleCenterY = marble.getCenterPosition().y;

        float bottomBound = position.y;
        float leftBound = position.x;
        float rightBound = position.x + obstacleTexture.getWidth();

        if (!Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) || !marble.isInWall()) {
            marble.setInWall(false);

            if (!marble.isInvincible() && Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
                marble.setBlockedOnLeft(marbleCenterX > rightBound);
                marble.setBlockedOnRight(marbleCenterX < leftBound);
                marble.setBlockedOnTop(marbleCenterY < bottomBound);
                PlayState.isCollideWall = true;
                if (!marble.isLifeLost()) {
                    marble.setMarbleLife(marble.getMarbleLife() - 1);
                    marble.setLifeLost(true);
                }
            } else {
                marble.setBlockedOnLeft(false);
                marble.setBlockedOnRight(false);
                marble.setBlockedOnTop(false);
                PlayState.isCollideWall = false;
                marble.setLifeLost(false);
            }
        }
    }

}
