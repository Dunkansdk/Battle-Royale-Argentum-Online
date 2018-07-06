package com.bonkan.brao.state.app;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.bonkan.brao.engine.entity.Entity;
import com.bonkan.brao.engine.entity.EntityManager;
import com.bonkan.brao.engine.entity.Human.PlayerState;
import com.bonkan.brao.engine.entity.humans.Enemy;
import com.bonkan.brao.engine.entity.humans.Player;
import com.bonkan.brao.engine.map.MapManager;
import com.bonkan.brao.engine.map.WorldManager;
import com.bonkan.brao.engine.utils.Constants;
import com.bonkan.brao.networking.LoggedUser;
import com.bonkan.brao.networking.Packet;
import com.bonkan.brao.networking.PacketIDs;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;

/**
 * <p>Estado "Jugando".</p>
 */
public class PlayState extends AbstractGameState {
	
    private Box2DDebugRenderer b2dr;
    private MapManager map;
    private ArrayList<Shape> mapBlocks;
    private EntityManager entities;
    
    private static final int DIR_DOWN = 0;
    private static final int DIR_UP = 1;
    private static final int DIR_LEFT = 2;
    private static final int DIR_RIGHT = 3;
    
    public PlayState(GameStateManager gameState) {
        super(gameState);
        WorldManager.init();
        map = new MapManager(WorldManager.world);
        b2dr = new Box2DDebugRenderer();
        mapBlocks = map.createBlocks();
        entities = new EntityManager();

        LoggedUser aux = app.getLoggedUser();
        
        entities.addPlayer(aux.getLoggedID(), new Player(aux.getX(), aux.getY(), aux.getLoggedDefaultBody(), 1, aux.getHP(), aux.getMana(), aux.getLoggedID(), aux.getLoggedUserName(), WorldManager.world));
    }

    @Override
    public void update(float delta)
    {
    	//TODO: ESTO HAY QUE VOLARLO!
    	lerpToTarget(camera, entities.getPlayer().getPos());
    	inputUpdate(delta, entities.getPlayer());
    	//TODO: ESTO HAY QUE VOLARLO!
    	
    	// Limitamos para que no consuma tanto recursos
    	WorldManager.doPhysicsStep(delta);
    	
    	entities.update(delta);
    	
    	map.getTiled().setView(camera);
    	map.getRayHandler().setCombinedMatrix(camera);
    	map.getRayHandler().update();
    }
    
    /**
     * <p>Maneja los inputs del player.
     * TODO: inputs customizables y cargados desde un JSON</p>
     * @param delta		&emsp;<b>float</b> el deltaTime (Gdx)
     */
    private void inputUpdate(float delta, Player player) {
        
    	boolean[] blockedDirs = blockedDirections(player);
    	boolean changedState = false;
    	Vector2 currPlayerPos = new Vector2(player.getPos().x, player.getPos().y);
    	
    	// solamente cambiamos el state cuando no hay bloqueo para no marear al server
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
        	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        	{
        		if(!blockedDirs[DIR_RIGHT] && !blockedDirs[DIR_DOWN])
        		{
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x + 2, (int) player.getPos().y - 2))
        			{
        				if(player.getState() != PlayerState.MOVE_RIGHT_DOWN)
        					changedState = true;
            			
            			player.setState(PlayerState.MOVE_RIGHT_DOWN);
            			player.setPos((int) player.getPos().x + 2, (int) player.getPos().y - 2);
        			
        			} else if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y - 2)) {
        				
        				if(player.getState() != PlayerState.MOVE_DOWN)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_DOWN);
	        			player.setPos((int) player.getPos().x, (int) player.getPos().y - 2);
        			
        			} else if(!checkCollisionWithEnemies(player, (int) player.getPos().x + 2, (int) player.getPos().y)) {
        				
        				if(player.getState() != PlayerState.MOVE_RIGHT)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_RIGHT);
	        			player.setPos((int) player.getPos().x + 2, (int) player.getPos().y);
        			
        			}

        		} else if (!blockedDirs[DIR_DOWN]) { 
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y - 2))
        			{
	        			if(player.getState() != PlayerState.MOVE_DOWN)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_DOWN);
	        			player.setPos((int) player.getPos().x, (int) player.getPos().y - 2);
        			}
        			
        		} else if (!blockedDirs[DIR_RIGHT]) {

        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x + 2, (int) player.getPos().y))
        			{
	        			if(player.getState() != PlayerState.MOVE_RIGHT)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_RIGHT);
	        			player.setPos((int) player.getPos().x + 2, (int) player.getPos().y);
        			}
        			
        		} else {
        			
        			if(player.getState() != PlayerState.NONE)
        				changedState = true;
        			
        			player.setState(PlayerState.NONE);
        			
        		}
        	} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        		if(!blockedDirs[DIR_LEFT] && !blockedDirs[DIR_DOWN])
        		{
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x - 2, (int) player.getPos().y - 2))
        			{
	        			if(player.getState() != PlayerState.MOVE_LEFT_DOWN)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_LEFT_DOWN);
	        			player.setPos((int) player.getPos().x - 2, (int) player.getPos().y - 2);
        			
        			} else if(!checkCollisionWithEnemies(player, (int) player.getPos().x - 2, (int) player.getPos().y)) {
        				
        				if(player.getState() != PlayerState.MOVE_LEFT)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_LEFT);
	        			player.setPos((int) player.getPos().x - 2, (int) player.getPos().y);
        				
        			} else if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y - 2)) {
        				
        				if(player.getState() != PlayerState.MOVE_DOWN)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_DOWN);
	        			player.setPos((int) player.getPos().x, (int) player.getPos().y - 2);
        				
        			}
        			
        		} else if (!blockedDirs[DIR_DOWN]) { 
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y - 2))
        			{
	        			if(player.getState() != PlayerState.MOVE_DOWN)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_DOWN);
	        			player.setPos((int) player.getPos().x, (int) player.getPos().y - 2);
        			}
        			
        		} else if (!blockedDirs[DIR_LEFT]) {
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x - 2, (int) player.getPos().y))
        			{
	        			if(player.getState() != PlayerState.MOVE_LEFT)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_LEFT);
	        			player.setPos((int) player.getPos().x - 2, (int) player.getPos().y);
        			}
        			
        		} else {
        			
        			if(player.getState() != PlayerState.NONE)
        				changedState = true;
        			
        			player.setState(PlayerState.NONE);
        			
        		}
        	} else if(!blockedDirs[DIR_DOWN]) {

        		if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y - 2))
    			{
	    			if(player.getState() != PlayerState.MOVE_DOWN)
	    				changedState = true;
	    			
	    			player.setState(PlayerState.MOVE_DOWN);
	    			player.setPos((int) player.getPos().x, (int) player.getPos().y - 2);
    			}

        	} else {
        	
        		if(player.getState() != PlayerState.NONE)
    				changedState = true;
    			
    			player.setState(PlayerState.NONE);
        		
        	}
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP))
    	{
        	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        	{
        		if(!blockedDirs[DIR_RIGHT] && !blockedDirs[DIR_UP])
        		{
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x + 2, (int) player.getPos().y + 2))
        			{
        				
	        			if(player.getState() != PlayerState.MOVE_RIGHT_UP)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_RIGHT_UP);
	        			player.setPos((int) player.getPos().x + 2, (int) player.getPos().y + 2);
	        			
        			} else if(!checkCollisionWithEnemies(player, (int) player.getPos().x + 2, (int) player.getPos().y)) {
        				
        				if(player.getState() != PlayerState.MOVE_RIGHT)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_RIGHT);
	        			player.setPos((int) player.getPos().x + 2, (int) player.getPos().y);
        				
        			} else if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y + 2)) {
        				
        				if(player.getState() != PlayerState.MOVE_UP)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_UP);
	        			player.setPos((int) player.getPos().x, (int) player.getPos().y + 2);
        				
        			}
        			
        		} else if (!blockedDirs[DIR_UP]) { 
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y + 2))
        			{
	        			if(player.getState() != PlayerState.MOVE_UP)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_UP);
	        			player.setPos((int) player.getPos().x, (int) player.getPos().y + 2);
        			}
        			
        		} else if (!blockedDirs[DIR_RIGHT]) {
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x + 2, (int) player.getPos().y))
        			{
	        			if(player.getState() != PlayerState.MOVE_RIGHT)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_RIGHT);
	        			player.setPos((int) player.getPos().x + 2, (int) player.getPos().y);
        			}
        			
        		} else {
        			
        			if(player.getState() != PlayerState.NONE)
        				changedState = true;
        			
        			player.setState(PlayerState.NONE);
        			
        		}
        	} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        		if(!blockedDirs[DIR_LEFT] && !blockedDirs[DIR_UP])
        		{
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x - 2, (int) player.getPos().y + 2))
        			{
        				
	        			if(player.getState() != PlayerState.MOVE_LEFT_UP)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_LEFT_UP);
	        			player.setPos((int) player.getPos().x - 2, (int) player.getPos().y + 2);
        			
        			} else if(!checkCollisionWithEnemies(player, (int) player.getPos().x - 2, (int) player.getPos().y)) {
        				
	        			if(player.getState() != PlayerState.MOVE_LEFT)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_LEFT);
	        			player.setPos((int) player.getPos().x - 2, (int) player.getPos().y);
        			
        			} else if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y + 2)) {
        				
	        			if(player.getState() != PlayerState.MOVE_UP)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_UP);
	        			player.setPos((int) player.getPos().x, (int) player.getPos().y + 2);
        			
        			}
        			
        		} else if (!blockedDirs[DIR_UP]) { 
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y + 2))
        			{
	        			if(player.getState() != PlayerState.MOVE_UP)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_UP);
	        			player.setPos((int) player.getPos().x, (int) player.getPos().y + 2);
        			}
        			
        		} else if (!blockedDirs[DIR_LEFT]) {
        			
        			if(!checkCollisionWithEnemies(player, (int) player.getPos().x - 2, (int) player.getPos().y))
        			{
	        			if(player.getState() != PlayerState.MOVE_LEFT)
	        				changedState = true;
	        			
	        			player.setState(PlayerState.MOVE_LEFT);
	        			player.setPos((int) player.getPos().x - 2, (int) player.getPos().y);
        			}
        			
        		} else {
        			
        			if(player.getState() != PlayerState.NONE)
        				changedState = true;
        			
        			player.setState(PlayerState.NONE);
        			
        		}
        	} else if(!blockedDirs[DIR_UP]) {
        		
        		if(!checkCollisionWithEnemies(player, (int) player.getPos().x, (int) player.getPos().y + 2))
    			{
	    			if(player.getState() != PlayerState.MOVE_UP)
	    				changedState = true;
	    			
	    			player.setState(PlayerState.MOVE_UP);
	    			player.setPos((int) player.getPos().x, (int) player.getPos().y + 2);
    			}
        			
        	} else {
        		
        		if(player.getState() != PlayerState.NONE)
    				changedState = true;
    			
    			player.setState(PlayerState.NONE);
        		
        	}	
    	}
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
    	{
        	if(!blockedDirs[DIR_LEFT])
        	{
        		
        		if(!checkCollisionWithEnemies(player, (int) player.getPos().x - 2, (int) player.getPos().y))
    			{
	        		if(player.getState() != PlayerState.MOVE_LEFT)
	    				changedState = true;
	        		
	        		player.setState(PlayerState.MOVE_LEFT);
	        		player.setPos((int) player.getPos().x - 2, (int) player.getPos().y);
    			}
        		
        	} else {
        		
        		if(player.getState() != PlayerState.NONE)
    				changedState = true;
    			
    			player.setState(PlayerState.NONE);
        		
        	}
    	}
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
    	{
        	if(!blockedDirs[DIR_RIGHT])
        	{
        		
        		if(!checkCollisionWithEnemies(player, (int) player.getPos().x + 2, (int) player.getPos().y))
    			{
	        		if(player.getState() != PlayerState.MOVE_RIGHT)
	    				changedState = true;
	        		
	        		player.setPos((int) player.getPos().x + 2, (int) player.getPos().y);
	        		player.setState(PlayerState.MOVE_RIGHT);
    			}
        		
        	} else {
        		
        		if(player.getState() != PlayerState.NONE)
    				changedState = true;
    			
    			player.setState(PlayerState.NONE);
        		
        	}
    	}

        if(	!Gdx.input.isKeyPressed(Input.Keys.DOWN) 	&& 
        	!Gdx.input.isKeyPressed(Input.Keys.UP) 		&& 
        	!Gdx.input.isKeyPressed(Input.Keys.LEFT) 	&& 
        	!Gdx.input.isKeyPressed(Input.Keys.RIGHT)) 
        {
        	if(player.getState() != PlayerState.NONE)
				changedState = true;

        	player.setState(PlayerState.NONE);
        }
        
        // como esto es posible que se mande en cada frame, lo mandamos via UDP
        if(currPlayerPos.x != player.getPos().x || currPlayerPos.y != player.getPos().y)
        {
        	ArrayList<String> args = new ArrayList<String>();
        	args.add(String.valueOf((int) player.getPos().x));
        	args.add(String.valueOf((int) player.getPos().y));
        	app.getClient().sendUDP(new Packet(PacketIDs.PACKET_PLAYER_MOVED, player.getID().toString(), args));
        }
        
        if(changedState) {
        	ArrayList<String> args = new ArrayList<String>();
        	args.add(String.valueOf(player.getState()));
        	app.getClient().sendTCP(new Packet(PacketIDs.PACKET_PLAYER_CHANGED_STATE, player.getID().toString(), args));
        }
    }
    
    /**
     * <p>Devuelve un array booleano con las direcciones bloqueadas.</p>
     */
    private boolean[] blockedDirections(Player player)
    {
    	boolean[] ret = new boolean[8];
    	
    	int realX = (int) player.getPos().x - Constants.BODY_WIDTH / 2;
    	int realY = (int) player.getPos().y - Constants.BODY_HEIGHT / 2;
    	
    	Rectangle playerRectLeft = new Rectangle(realX - 2, realY, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
    	Rectangle playerRectRight = new Rectangle(realX + 2, realY, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
    	Rectangle playerRectUp = new Rectangle(realX, realY + 2, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
    	Rectangle playerRectDown = new Rectangle(realX, realY - 2, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
    	
    	for(Shape s : mapBlocks)
    	{
    		if(s.intersects(playerRectLeft))
    			ret[DIR_LEFT] = true;
    		if(s.intersects(playerRectRight))
    			ret[DIR_RIGHT] = true;
    		if(s.intersects(playerRectDown))
    			ret[DIR_DOWN] = true;
    		if(s.intersects(playerRectUp))
    			ret[DIR_UP] = true;
    	}
    	
    	return ret;
    }
    
    /**
     * <p>Chequea si el player en la posición destino colisiona con algún otro player</p>
     * @param p		&emsp;{@link com.bonkan.brao.engine.entity.humans.Player Player} el player
     * @param x		&emsp;<b>int</b> la posición destino X
     * @param y		&emsp;<b>int</b> la posición destino Y
     * @return	<b>boolean</b>
     */
    private boolean checkCollisionWithEnemies(Player p, int x, int y)
    {
    	Rectangle playerRect = new Rectangle(x - Constants.BODY_WIDTH / 2, y - Constants.BODY_HEIGHT / 2, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
    	
    	// iteramos las entidades
    	HashMap<UUID, Entity> entidades = entities.getAllEntities();
    	
    	for (Map.Entry<UUID, Entity> entry : entidades.entrySet()) {
			if(entry.getValue() instanceof Enemy)
			{
				Enemy e = (Enemy) entry.getValue(); 
				Rectangle enemyRect = new Rectangle((int) e.getPos().x - Constants.BODY_WIDTH / 2 - 5, (int) e.getPos().y - Constants.BODY_HEIGHT / 2, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
				
				if(enemyRect.intersects(playerRect)) return true;
			}
		}
    	
    	return false;
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
    
    public void addEnemyToArea(int bodyIndex, int headIndex, int x, int y, UUID id, String nick)
    {
    	entities.addEnemy(id, new Enemy(x, y, bodyIndex, headIndex, id, nick, WorldManager.world));
    }
    
    public void setEnemyState(UUID enemyID, PlayerState newState)
    {
    	if(entities.getEnemy(enemyID) != null)
    		entities.getEnemy(enemyID).setState(newState);
    }
    
    public void setEnemyPos(UUID enemyID, int x, int y)
    {
    	if(entities.getEnemy(enemyID) != null)
    		entities.getEnemy(enemyID).setPos(x, y);
    }
    
    public boolean getEnemyInArea(UUID enemyID)
    {
    	return (entities.getEnemy(enemyID) != null);
    }

    @Override
    public void render()
    {
    	int[] bglayers = {0, 1};
    	int[] foreground = {2};
    	
    	map.getTiled().render(bglayers);

    	app.getBatch().begin();
    	
    	entities.render(batch);
    	
    	app.getBatch().end();

    	map.getTiled().render(foreground);
    	
    	if(app.DEBUG) b2dr.render(WorldManager.world, camera.combined.cpy());

    	map.getRayHandler().render();
    }

    @Override
    public void dispose() {
        map.dispose();
        b2dr.dispose();
    }
}