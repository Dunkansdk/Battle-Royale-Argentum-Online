package com.bonkan.brao.state.app;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Player;
import com.bonkan.brao.engine.entity.Player.playerState;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;

public class PlayState extends AbstractGameState {

    private TiledMap map;
    private OrthogonalTiledMapRenderer tiled;
    private Player player;
    private World world;
    private Box2DDebugRenderer b2dr;

    public PlayState(GameStateManager gameState) {
        super(gameState);
        map = new TmxMapLoader().load("map1.tmx");
        tiled = new OrthogonalTiledMapRenderer(map);
        world = new World(new Vector2(0f, 0f), false);
        b2dr = new Box2DDebugRenderer();
        player = new Player(new TextureRegion(new Texture(Gdx.files.internal("body.png")), 32, 38), UUID.randomUUID(), createBox(10, 10, 32, 38, false));
    }

    @Override
    public void update(float delta)
    {
    	world.step(1 / 60f, 6, 2);
    	lerpToTarget(camera, player.getBody().getPosition());
        tiled.setView(camera);
        inputUpdate(delta);
    }
    
    private void inputUpdate(float delta) {
        int horizontalForce = 0;
        int verticalForce = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        	player.setState(playerState.MOVE_LEFT);
            horizontalForce -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	player.setState(playerState.MOVE_RIGHT);
            horizontalForce += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	player.setState(playerState.MOVE_UP);
            verticalForce += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	player.setState(playerState.MOVE_DOWN);
            verticalForce -= 1;
        }
        
        if(	!Gdx.input.isKeyPressed(Input.Keys.DOWN) 	&& 
        	!Gdx.input.isKeyPressed(Input.Keys.UP) 		&& 
        	!Gdx.input.isKeyPressed(Input.Keys.LEFT) 	&& 
        	!Gdx.input.isKeyPressed(Input.Keys.RIGHT)) 
        {
        	player.setState(playerState.NONE);
        }
        
        player.getBody().setLinearVelocity(horizontalForce * 256, verticalForce * 256); // ( 8 * 32 = 256 )
    }

    private Body createBox(int x, int y, int width, int height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x, y);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        pBody.createFixture(shape, 1.0f);
        shape.dispose(); // keep it clean

        return pBody;

    }
    
    private void lerpToTarget(Camera camera, Vector2 target) {
        // a + (b - a) * lerp factor
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * .1f;
        position.y = camera.position.y + (target.y - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }

    @Override
    public void render()
    {
    	tiled.render();
    	app.getBatch().begin();
    	player.render(app.getBatch());
    	app.getBatch().end();
    	b2dr.render(world, camera.combined.cpy());
    }

    @Override
    public void dispose() {
        map.dispose();
        tiled.dispose();
    }
}