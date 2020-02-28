package com.mrsir.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mrsir.PlatformerApp;

import java.awt.*;

public abstract class InteractiveTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected com.badlogic.gdx.math.Rectangle bounds;
    protected Body body;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PlatformerApp.PPM, (bounds.getY() + bounds.getHeight() / 2) / PlatformerApp.PPM);

        body = world.createBody(bdef);
        shape.setAsBox(bounds.getWidth() / 2 / PlatformerApp.PPM, bounds.getHeight() / 2 / PlatformerApp.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }

}