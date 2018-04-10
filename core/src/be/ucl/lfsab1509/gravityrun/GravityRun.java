package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.states.*;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import be.ucl.lfsab1509.gravityrun.tools.User;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Map;

public class GravityRun extends Game {

    public static float DENSITY;
    public static int HEIGHT;
    public static final String TITLE = "Gravity Run";
    public static int WIDTH;

    public ArrayList<Integer> scoreList;
    public Preferences pref;
    public ScreenManager screenManager;
	public SoundManager soundManager;
    public SpriteBatch batch;
    public User user;

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);

        DENSITY = Gdx.graphics.getDensity();
        HEIGHT = Gdx.graphics.getHeight();
        WIDTH = Gdx.graphics.getWidth();

        State.initializeSkins();

        batch = new SpriteBatch();
        screenManager = new ScreenManager(this);
        soundManager = new SoundManager();

        pref = Gdx.app.getPreferences("Player");
        pref.flush();

        Map<String, ?> map = pref.get();

        if (!pref.getBoolean(User.FIRSTTIME)) {
            user = new User();
            screenManager.push(new FirstState(this));
        } else {
            user = new User(map);
            screenManager.push(new MenuState(this));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        screenManager.disposeAll();
        soundManager.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    public void exit() {
        dispose();
        Gdx.app.exit();
    }

}
