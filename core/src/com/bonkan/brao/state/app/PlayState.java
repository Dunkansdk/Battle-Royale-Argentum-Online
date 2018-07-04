package com.bonkan.brao.state.app;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Human.playerState;
import com.bonkan.brao.engine.entity.humans.Enemy;
import com.bonkan.brao.engine.entity.humans.Player;
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
    private HashMap<UUID, Enemy> enemiesInViewport;
    private WorldManager world;
    private Box2DDebugRenderer b2dr;
    private MapManager map;
    
    public PlayState(GameStateManager gameState) {
        super(gameState);
        world = new WorldManager();
        map = new MapManager(world.getWorld());
        b2dr = new Box2DDebugRenderer();

        LoggedUser aux = app.getLoggedUser();
        
        player = new Player(aux.getX(), aux.getY(), aux.getLoggedDefaultBody(), 1, aux.getHP(), aux.getMana(), aux.getLoggedID(), aux.getLoggedUserName(), world.getWorld());
        enemiesInViewport = new HashMap<UUID, Enemy>();
    }

    @Override
    public void update(float delta)
    {
    	//TODO: ESTO HAY QUE VOLARLO!
    	lerpToTarget(camera, player.getPos());
    	inputUpdate(delta);
    	//TODO: ESTO HAY QUE VOLARLO!
    	world.step();
    	player.update(delta);
    	
    	for (Map.Entry<UUID, Enemy> entry : enemiesInViewport.entrySet())
    		entry.getValue().update(delta);
    	
    	map.getTiled().setView(camera);
    	map.getRayHandler().setCombinedMatrix(camera);
    	
    	//lastPositionUpdate += delta;
    }
    
    /**
     * 
     * TODO: HAY QUE SEPARAR ESTO!
     * 
     * @param delta
     */
    private void inputUpdate(float delta) {
        
        /*if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
        	if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        		player.setState(playerState.MOVE_LEFT_DOWN);
        	else if (Gdx.input.isKeyPressed(Input.Keys.UP))
        		player.setState(playerState.MOVE_LEFT_UP);
        	else
        		player.setState(playerState.MOVE_LEFT);
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
        	if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        		player.setState(playerState.MOVE_RIGHT_DOWN);
        	else if (Gdx.input.isKeyPressed(Input.Keys.UP))
        		player.setState(playerState.MOVE_RIGHT_UP);
        	else
        		player.setState(playerState.MOVE_RIGHT);
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
        	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        		player.setState(playerState.MOVE_RIGHT_DOWN);
        	else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        		player.setState(playerState.MOVE_LEFT_DOWN);
        	else
        		player.setState(playerState.MOVE_DOWN);
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
        	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        		player.setState(playerState.MOVE_RIGHT_UP);
        	else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        		player.setState(playerState.MOVE_LEFT_UP);
        	else
        		player.setState(playerState.MOVE_UP);
        }*/
    	
    	if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
    		player.setPos(player.getPos().x, player.getPos().y - 1);
    	if(Gdx.input.isKeyPressed(Input.Keys.UP))
    		player.setPos(player.getPos().x, player.getPos().y + 1);
    	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
    		player.setPos(player.getPos().x + 1, player.getPos().y);
    	if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
    		player.setPos(player.getPos().x - 1, player.getPos().y);
    	
        if(	!Gdx.input.isKeyPressed(Input.Keys.DOWN) 	&& 
        	!Gdx.input.isKeyPressed(Input.Keys.UP) 		&& 
        	!Gdx.input.isKeyPressed(Input.Keys.LEFT) 	&& 
        	!Gdx.input.isKeyPressed(Input.Keys.RIGHT)) 
        {
        	player.setState(playerState.NONE);
        }
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
    
    public void addEnemyToArea(int bodyIndex, int headIndex, float x, float y, UUID id, String nick)
    {
    	enemiesInViewport.put(id, new Enemy(x, y, bodyIndex, headIndex, id, nick, world.getWorld()));
    }
    
    public void setEnemyState(UUID enemyID, playerState newState)
    {
    	if(enemiesInViewport.get(enemyID) != null)
    		enemiesInViewport.get(enemyID).setState(newState);
    }
    
    public boolean getEnemyInArea(UUID enemyID)
    {
    	return (enemiesInViewport.get(enemyID) != null);
    }
    
    public World getWorld()
    {
    	return world.getWorld();
    }

    @Override
    public void render()
    {
    	map.getTiled().render();
    	
    	app.getBatch().begin();
    	player.render(app.getBatch());
    	
    	for (Map.Entry<UUID, Enemy> entry : enemiesInViewport.entrySet())
    		entry.getValue().render(app.getBatch());
    	
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