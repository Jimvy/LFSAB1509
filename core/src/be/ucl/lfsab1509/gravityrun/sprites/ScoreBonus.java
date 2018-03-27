package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import be.ucl.lfsab1509.gravityrun.states.PlayState;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ScoreBonus extends Bonus {

    private int offset;

    public ScoreBonus(float y, int sw, int offset) {
        super();

        this.offset = offset;
        bonusTexture = new Texture("drawable-" + sw + "/scorebonus.png");
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), (Rectangle) bounds);
    }

    @Override
    public boolean isFinished() {
        PlayState.scoreBonus += 100;
        return true;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void update(float dt) {

    }
}
