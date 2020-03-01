package com.mrsir.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mrsir.PlatformerApp;
import com.mrsir.screens.PlayScreen;

import java.awt.*;

public abstract class InteractiveTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected com.badlogic.gdx.math.Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PlatformerApp.PPM, (bounds.getY() + bounds.getHeight() / 2) / PlatformerApp.PPM);

        body = world.createBody(bdef);
        shape.setAsBox(bounds.getWidth() / 2 / PlatformerApp.PPM, bounds.getHeight() / 2 / PlatformerApp.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    // this method returns the tiled map cell in which _head_ collision happens (and a brick needs to be taken out)
    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);   // int Tiled editor, graphics layer index = 1
        return layer.getCell((int) (body.getPosition().x * PlatformerApp.PPM / 16), (int) (body.getPosition().y * PlatformerApp.PPM / 16));
    }


}
