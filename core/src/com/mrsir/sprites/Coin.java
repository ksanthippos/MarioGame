package com.mrsir.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mrsir.PlatformerApp;
import com.mrsir.scenes.Hud;

import java.awt.*;

public class Coin extends InteractiveTileObject {

    public static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28; // on mario_tileset indexes start at 0 and blank coin = 27

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformerApp.COIN_BIT);
        tileSet = map.getTileSets().getTileSet("mario_tileset");
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin collision!", "");

        if (getCell().getTile().getId() == BLANK_COIN) {
            PlatformerApp.manager.get("audio/sounds/bump.wav", Sound.class).play();
        }
        else {
            PlatformerApp.manager.get("audio/sounds/coin.wav", Sound.class).play();
            Hud.addScore(100);
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));

    }
}
