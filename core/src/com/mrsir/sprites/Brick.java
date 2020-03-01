package com.mrsir.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mrsir.PlatformerApp;
import com.mrsir.scenes.Hud;
import com.mrsir.screens.PlayScreen;

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformerApp.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        //Gdx.app.log("Brick contact!", "");
        setCategoryFilter(PlatformerApp.DESTROYED_BIT);
        getCell().setTile(null);    // destroys tile cell
        Hud.addScore(200);
        PlatformerApp.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }


}
