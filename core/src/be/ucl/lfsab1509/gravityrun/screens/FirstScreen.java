package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class FirstScreen extends AbstractMenuScreen {

    private String username = game.i18n.format("username");

    public FirstScreen(GravityRun gravityRun) {
        super(gravityRun);

        Label title = new Label(game.i18n.format("welcome"), game.titleSkin, "title");

        final TextButton startButton = new TextButton(game.i18n.format("start"), game.tableSkin, "round");
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                validateUsername();
            }
        });
        final TextField usernameField = new TextField(username, game.tableSkin);
        usernameField.setText(username);
        usernameField.setMessageText(game.i18n.format("username"));
        usernameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                usernameField.selectAll();
            }
        });
        usernameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                username = usernameField.getText();
            }
        });
        usernameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c== '\r') {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    validateUsername();
                }
            }
        });

        Table table = new Table();
        table.add(title).top();
        table.row();
        table.add(usernameField).expandX().fillX().padTop(height - containerHeight);
        table.row();
        table.add(startButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }

    private void initUser() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            arrayList.add(0);

        game.user.setUsername(username);
        game.user.setFirstTimeTrue();
        game.user.setBeginnerScoreList(new ArrayList<>());
        game.user.setIntermediateScoreList(new ArrayList<>());
        game.user.setExpertScoreList(new ArrayList<>());
        game.user.setIndexSelected(1);
        game.user.setHighScoreList(arrayList);

        game.user.write();
    }

    private void validateUsername() {
        if (!game.user.checkUsername(username)) {
            spawnErrorDialog(game.i18n.format("error_username_default"), game.user.getUsernameError(username));
        } else {
            initUser();
            screenManager.set(new HomeScreen(game));
        }
    }
}
