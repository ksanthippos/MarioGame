package com.mrsir.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mrsir.PlatformerApp;
import com.mrsir.screens.PlayScreen;

public class Mario extends Sprite {

    // world and Box2d
    public World world;
    public Body b2body;

    // animation and sprites
    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;
    private TextureRegion marioStand;
    private Animation marioRun;
    private Animation marioJump;
    private boolean runningRight;
    private float stateTimer;


    public Mario(PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        // generate frames and animations
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }
        marioRun = new Animation(0.1f, frames);
        frames.clear(); // use same array for jumps also

        for (int i = 4; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }
        marioJump = new Animation(0.1f, frames);
        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        defineMario();

        setBounds(0, 0, 16 / PlatformerApp.PPM, 16 / PlatformerApp.PPM);
        setRegion(marioStand);
    }

    public void defineMario() {

        // create Mario body, define its type (dynamic), add shape and fixture --> attach all to Box2d object
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PlatformerApp.PPM, 32 / PlatformerApp.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PlatformerApp.PPM);

        // defines Mario as a collideable object
        fdef.filter.categoryBits = PlatformerApp.MARIO_BIT;
        // defines what Mario can collide with (| means "or")
        fdef.filter.maskBits = PlatformerApp.GROUND_BIT | PlatformerApp.BRICK_BIT |
                PlatformerApp.COIN_BIT | PlatformerApp.ENEMY_BIT | PlatformerApp.OBJECT_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        // Mario's head needs to contain "sensor surface" (a short line, like a hat), which activates coins and bricks when in collision
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / PlatformerApp.PPM, 5 / PlatformerApp.PPM), new Vector2(-2 / PlatformerApp.PPM, 5 / PlatformerApp.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");

    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:   // same animation when falling / standing
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        }
        else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        }
        else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }
        else {
            return State.STANDING;
        }
    }


}
