package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class OptionScreen extends AbstractMenuScreen {

    private String username; // Le dernier username valide, tel que retourné par le user.

    OptionScreen(GravityRun gravityRun) {
        super(gravityRun);

        username = user.getUsername();

        Label title = new Label(game.i18n.get("option"), titleSkin, "title");

        TextButton usernameButton = new TextButton(game.i18n.format("edit_username"), tableSkin, "round");
        usernameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popUsernameDialog();
            }
        });


        TextButton lvlButton = new TextButton(game.i18n.format("choose_lvl"), tableSkin, "round");
        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popLevelSelectionDialog();
            }
        });


        Table table = new Table();
        table.add(title).colspan(2).expandX();
        table.row();
        table.add(usernameButton).colspan(2).expandX().fillX().padTop(height - containerHeight).maxWidth(containerWidth);
        table.row();
        table.add(lvlButton).colspan(2).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);

        initStage(table);
    }

    @Override
    public void render(float dt) {
        if (clickedBack() && openDialogs == 0) {
            user.write(); // TODO le retirer du coup
            screenManager.pop();
            return;
        }

        super.render(dt);
    }

    private void popUsernameDialog() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        TextField usernameField = new TextField(username, tableSkin);
        usernameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                usernameField.selectAll();
            }
        });
        Table content = new Table();
        content.add(usernameField).width(0.7f * width).pad(10);
        EditDialog editUsernameDialog = new EditDialog(game.i18n.format("enter_username"), content, new DialogResultMethod() {
            @Override
            public boolean result(Object object) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                if (object.equals(true))
                    return validateUserName(usernameField);
                else {
                    return true;
                }
            }
        });
        editUsernameDialog.show(stage);
    }

    private void popLevelSelectionDialog() {
        List<String> levelSelectionList = new List<>(aaronScoreSkin);
        levelSelectionList.setItems(game.i18n.format("beginner"), game.i18n.format("inter"), game.i18n.format("expert"));
        levelSelectionList.setSelectedIndex(user.getIndexSelected());
        levelSelectionList.setAlignment(Align.center);
        Table content = new Table();
        content.add(levelSelectionList);
        EditDialog editLevelSelectionDialog = new EditDialog(game.i18n.format("select_level"), content, new DialogResultMethod() {
            @Override
            public boolean result(Object object) {
                if (object.equals(true))
                    validateLevelSelection(levelSelectionList);
                return true;
            }
        });
        levelSelectionList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                validateLevelSelection(levelSelectionList);
                editLevelSelectionDialog.hide();
            }
        });
        editLevelSelectionDialog.show(stage);
    }

    private boolean validateUserName(TextField usernameField) {
        String newUsername = usernameField.getText();
        if (user.setUsername(newUsername)) {
            user.write();
            username = newUsername; // don't forget me too
            return true;
        } else {
            spawnErrorDialog(game.i18n.format("error_username_default"), user.getUsernameError(newUsername));
            usernameField.setText(username);
            return false;
        }
    }

    private void validateLevelSelection(List levelList) {
        if (levelList.getSelected().equals(game.i18n.format("beginner")))
            user.setIndexSelected(0);
        else if (levelList.getSelected().equals(game.i18n.format("inter")))
            user.setIndexSelected(1);
        else if (levelList.getSelected().equals(game.i18n.format("expert")))
            user.setIndexSelected(2);
    }

}
