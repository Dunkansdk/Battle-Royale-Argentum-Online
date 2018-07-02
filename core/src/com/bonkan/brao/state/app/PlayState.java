package com.bonkan.brao.state.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.bonkan.brao.engine.entity.Player;
import com.bonkan.brao.engine.entity.Player.playerState;
import com.bonkan.brao.engine.map.MapManager;
import com.bonkan.brao.engine.map.WorldManager;
import com.bonkan.brao.networking.LoggedUser;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;

/**
 * <p>Estado "Jugando".</p>
 */
public class PlayState extends AbstractGameState {
	
    private Player player;
    private WorldManager world;
    private Box2DDebugRenderer b2dr;
    private MapManager map;
    
    public PlayState(GameStateManager gameState) {
        super(gameState);
        world = new WorldManager();
        map = new MapManager(world.getWorld());
        b2dr = new Box2DDebugRenderer();

        LoggedUser aux = app.getLoggedUser();
        player = new Player(aux.getLoggedDefaultBody(), aux.getLoggedID(), aux.getLoggedUserName(), world.getWorld());
    }

    @Override
    public void update(float delta)
    {
    	//TODO: ESTO HAY QUE VOLARLO!
    	lerpToTarget(camera, player.getBody().getPosition());
    	inputUpdate(delta);
    	//TODO: ESTO HAY QUE VOLARLO!
    	
    	world.step();
    	map.getTiled().setView(camera);
    	map.getRayHandler().setCombinedMatrix(camera);
    }
    
    /**
     * 
     * TODO: HAY QUE SEPARAR ESTO!
     * 
     * @param delta
     */
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
        
        // Si esta haciendo contacto con otro body es true
        // if(player.isContact())
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) app.DEBUG = !app.DEBUG;
        
        player.getBody().setLinearVelocity(horizontalForce * 256, verticalForce * 256); // ( 8 * 32 = 256 )
    }
    
    /**
     * Setea la camara en la posicion donde se encuentra el personaje (target)
     * @param camera
     * @param target
     */
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
    	map.getTiled().render();
    	
    	app.getBatch().begin();
    	player.render(app.getBatch());
    	app.getBatch().end();
    	
    	if(app.DEBUG) b2dr.render(world.getWorld(), camera.combined.cpy());
    	
    	map.getRayHandler().updateAndRender();
    }

    @Override
    public void dispose() {
        map.dispose();
        b2dr.dispose();
    }
}