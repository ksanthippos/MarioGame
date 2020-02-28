package com.mrsir.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mrsir.PlatformerApp;
import com.mrsir.scenes.Hud;
import com.mrsir.sprites.Mario;
import com.mrsir.tools.B2WorldCreator;


public class PlayScreen implements Screen {

    // game, view, camera
    private PlatformerApp game;
    private OrthographicCamera gamecamera;
    private Viewport gamePort;
    private Hud hud;

    // Box2d vars
    private World world;
    private Box2DDebugRenderer b2dr;

    // Tiled map vars
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // player variable
    private Mario player;

    // game init!
    public PlayScreen(PlatformerApp game) {

        this.game = game;
        gamecamera = new OrthographicCamera();
        gamePort = new FitViewport(PlatformerApp.V_WIDTH / PlatformerApp.PPM, PlatformerApp.V_HEIGHT / PlatformerApp.PPM, gamecamera);   // FitViewPort scales game always in right way, no strecthing etc
        hud = new Hud(game.batch);

        world = new World(new Vector2(0, -10), true); // game world with gravity
        b2dr = new Box2DDebugRenderer();

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1-1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PlatformerApp.PPM);
        gamecamera.position.set(gamePort.getWorldWidth() / 2 ,gamePort.getWorldHeight() / 2, 0);

        // creates game world
        new B2WorldCreator(world, map);

        player = new Mario(world);
    }

    // updates the game screen
    public void update(float dt) {
        handleInput(dt);

        world.step(1/60f, 6, 2);

        // camera follows player (only) along the x axis
        gamecamera.position.x = player.b2body.getPosition().x;

        gamecamera.update();
        renderer.setView(gamecamera);
    }

    // checks the input (keys, mouse etc)
    public void handleInput(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            // Ainear impulse: a force with short interaction time, which is applied upwards (x = 0) and towards center point of players body.
            // That way player is pushed without any torque/spinning and player goes straight up. True stands for "waking" players body up.
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }
        // Notice difference: KeyPressed means constant press, not just a click as before (KeyJustPressed)
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    @Override
    public void show() {

    }

    // draws the graphics on screen
    @Override
    public void render(float delta) {

        update(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the game map
        renderer.render();

        // render Box2dDebugLines (= lines drawed in the Tiled map editor: borders for collision etc)
        b2dr.render(world, gamecamera.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    // setting the correct play screen size
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
