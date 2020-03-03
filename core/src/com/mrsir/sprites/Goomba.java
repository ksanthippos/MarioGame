package com.mrsir.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mrsir.PlatformerApp;
import com.mrsir.screens.PlayScreen;


public class Goomba extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyded;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i*16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / PlatformerApp.PPM, 16 / PlatformerApp.PPM);
        setToDestroy = false;
        destroyded = false;

    }

    public void update(float dt) {
        stateTime += dt;

        if (setToDestroy && !destroyded) {
            world.destroyBody(b2body);
            destroyded = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
        }
        else if (!destroyded) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }

    }

    @Override
    protected void defineEnemy() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PlatformerApp.PPM);

        fdef.filter.categoryBits = PlatformerApp.ENEMY_BIT;
        fdef.filter.maskBits = PlatformerApp.GROUND_BIT | PlatformerApp.BRICK_BIT |
                PlatformerApp.COIN_BIT | PlatformerApp.OBJECT_BIT | PlatformerApp.ENEMY_BIT | PlatformerApp.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        // create head for goomba
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1/ PlatformerApp.PPM);
        vertice[1] = new Vector2(5, 8).scl(1/ PlatformerApp.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1/ PlatformerApp.PPM);
        vertice[3] = new Vector2(3, 3).scl(1/ PlatformerApp.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f; // bounciness of Mario when hitting Goombas head
        fdef.filter.categoryBits = PlatformerApp.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);


    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
    }
}
