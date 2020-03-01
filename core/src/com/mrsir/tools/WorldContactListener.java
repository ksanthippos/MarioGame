package com.mrsir.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mrsir.sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() != null && fixB.getUserData() != null) { // bypass NullPointer
            if (fixA.getUserData().equals("head") || fixB.getUserData().equals("head")) {
                Fixture head = fixA.getUserData().equals("head") ? fixA : fixB;
                Fixture object = head == fixA ? fixB : fixA;

                if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                    ((InteractiveTileObject) object.getUserData()).onHeadHit();
                }
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        //Gdx.app.log("End contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
