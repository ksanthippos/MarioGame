package com.mrsir.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mrsir.PlatformerApp;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private Integer score;
    private float timeCount;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label marioLabel;

    public Hud(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(PlatformerApp.V_WIDTH, PlatformerApp.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        // table toimii samaan tapaan kuin CSS:n grid, eli ankkuroidaan stageen esim näitä alla olevia labeleita
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // luodaan labelit
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // asetellaan labelit tableen
        table.add(marioLabel).expandX().padTop(10);     // label asetetaan ylimpään riviin, 10px paddingilla vierekkäin seuraavien kanssa
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();                                    // lisätään uusi rivi, jonka jälkeen loput samalla lailla paitsi ilman paddingia
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        // lisätään table stageen
        stage.addActor(table);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}