package com.mrsir.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mrsir.PlatformerApp;
import com.mrsir.sprites.Brick;
import com.mrsir.sprites.Coin;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map) {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // next 4 loops are identical except for indexes in get()-method!

        // ground (Tiled map editor index = 2)
        for (MapObject mo : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) mo).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PlatformerApp.PPM, (rect.getY() + rect.getHeight() / 2) / PlatformerApp.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / PlatformerApp.PPM, rect.getHeight() / 2 / PlatformerApp.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // pipes (Tiled map editor index = 3)
        for (MapObject mo : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) mo).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PlatformerApp.PPM, (rect.getY() + rect.getHeight() / 2) / PlatformerApp.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / PlatformerApp.PPM, rect.getHeight() / 2 / PlatformerApp.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // coins (Tiled map editor index = 4)
        for (MapObject mo : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) mo).getRectangle();
            new Coin(world, map, rect);
        }

        // bricks (Tiled map editor index = 5)
        for (MapObject mo : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) mo).getRectangle();
            new Brick(world, map, rect);
        }
    }
}
