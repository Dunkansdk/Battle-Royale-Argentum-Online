package com.bonkan.brao.state.app;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.UUID;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.bonkan.brao.engine.entity.EntityManager;
import com.bonkan.brao.engine.entity.Human.PlayerState;
import com.bonkan.brao.engine.entity.humans.Enemy;
import com.bonkan.brao.engine.entity.humans.Player;
import com.bonkan.brao.engine.input.InputController;
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
    private InputController inputController;
    
    public PlayState(GameStateManager gameState) {
        super(gameState);
        WorldManager.init();
        EntityManager.init();
        map = new MapManager(WorldManager.world);
        b2dr = new Box2DDebugRenderer();
        inputController = new InputController(app.getClient(), map.createBlocks());

        LoggedUser aux = app.getLoggedUser();
   
        EntityManager.addPlayer(aux.getLoggedID(), new Player(aux.getX(), aux.getY(), aux.getLoggedDefaultBody(), 1, aux.getHP(), aux.getMana(), aux.getLoggedID(), aux.getLoggedUserName(), WorldManager.world));
    }

    @Override
    public void update(float delta)
    {
    	handlePlayerIncomingData();
    	
    	map.getTiled().setView(camera);
    	inputController.update(delta, EntityManager.getPlayer());
    	
    	//TODO: ESTO HAY QUE VOLARLO!
    	lerpToTarget(camera, EntityManager.getPlayer().getPos());
    	
    	// Limitamos para que no consuma tanto recursos
    	WorldManager.doPhysicsStep(delta);    	
    	EntityManager.update(delta);

    	map.getRayHandler().setCombinedMatrix(camera);
    	map.getRayHandler().update();
    }
    
    @Override
    public void render()
    {
    	map.getTiled().render();
    	
    	app.getBatch().begin();    	
    	EntityManager.render(batch);    	
    	app.getBatch().end();
    	
    	if(app.DEBUG) b2dr.render(WorldManager.world, camera.combined.cpy());

    	map.getRayHandler().render();
    }
    
    public void handlePlayerIncomingData()
    {
    	LoggedUser lu = app.getLoggedUser();
    	
    	if(lu.hasIncomingData())
    	{
    		// handleamos la incoming data
    		Iterator<Packet> it = lu.getIncomingData().iterator();
    		
    		while(it.hasNext())
    		{
    			Packet p = (Packet) it.next();
    		
    			// variables comunes
    			UUID id;
    			int bodyIndex, headIndex, x, y;
    			String nick;
    			PlayerState state;
    			
    			switch(p.getID())
    			{
	    			case PacketIDs.PACKET_USER_CHANGED_STATE:
    					id = UUID.fromString((String) p.getData());
    					bodyIndex = Integer.parseInt(p.getArgs().get(0)); 
    					headIndex = Integer.parseInt(p.getArgs().get(1)); 
    					x = Integer.parseInt(p.getArgs().get(2)); 
    					y = Integer.parseInt(p.getArgs().get(3)); 
    					nick = p.getArgs().get(4);
    					state = PlayerState.valueOf(p.getArgs().get(5));
    					
    					if(!getEnemyInArea(id))
    					{
    						addEnemyToArea(bodyIndex, headIndex, x, y, id, nick);
    						setEnemyState(id, state);
    					} else {
    						setEnemyState(id, state);
    					}

	    				break;
	    				
	    			case PacketIDs.PACKET_USER_ENTERED_AREA:
    					id = UUID.fromString((String) p.getData());
    					bodyIndex = Integer.parseInt(p.getArgs().get(0)); 
    					headIndex = Integer.parseInt(p.getArgs().get(1)); 
    					x = Integer.parseInt(p.getArgs().get(2)); 
    					y = Integer.parseInt(p.getArgs().get(3)); 
    					nick = p.getArgs().get(4);
    	
    					addEnemyToArea(bodyIndex, headIndex, x, y, id, nick);
	    				
	    				break;
	    				
	    			case PacketIDs.PACKET_USER_MOVED:
    					id = UUID.fromString((String) p.getData());
    					bodyIndex = Integer.parseInt(p.getArgs().get(0)); 
    					headIndex = Integer.parseInt(p.getArgs().get(1)); 
    					x = Integer.parseInt(p.getArgs().get(2)); 
    					y = Integer.parseInt(p.getArgs().get(3)); 
    					nick = p.getArgs().get(4);
    	
    					if(!getEnemyInArea(id))
    						addEnemyToArea(bodyIndex, headIndex, x, y, id, nick);
    					else
    						setEnemyPos(id, x, y);
	    				
	    				break;
	    				
	    			case PacketIDs.PACKET_CONFIRM_PLAYER_MOVEMENT:
	    				EntityManager.getPlayer().setPos(Integer.parseInt(p.getArgs().get(0)), Integer.parseInt(p.getArgs().get(1)));
	    				break;
    			}
    			
    			it.remove();
    		}
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
    
    public void addEnemyToArea(int bodyIndex, int headIndex, int x, int y, UUID id, String nick)
    {
    	if(!collidesWithPlayer(x, y))
    		EntityManager.addEnemy(id, new Enemy(x, y, bodyIndex, headIndex, id, nick, WorldManager.world));
    }
    
    public void setEnemyState(UUID enemyID, PlayerState newState)
    {
    	if(EntityManager.getEnemy(enemyID) != null)
    		EntityManager.getEnemy(enemyID).setState(newState);
    }
    
    public void setEnemyPos(UUID enemyID, int x, int y)
    {
    	if(EntityManager.getEnemy(enemyID) != null && !collidesWithPlayer(x, y))
    		EntityManager.getEnemy(enemyID).setPos(x, y);
    }
    
    public boolean getEnemyInArea(UUID enemyID)
    {
    	return (EntityManager.getEnemy(enemyID) != null);
    }
    
    public boolean collidesWithPlayer(int x, int y)
    {
    	Rectangle rect = new Rectangle(x - Constants.BODY_WIDTH / 2, y - Constants.BODY_HEIGHT / 2, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
    	Rectangle playerRect = new Rectangle((int) EntityManager.getPlayer().getPos().x - Constants.BODY_WIDTH / 2, (int) EntityManager.getPlayer().getPos().y - Constants.BODY_HEIGHT / 2, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
    
    	return(rect.intersects(playerRect));
    }

    @Override
    public void dispose() {
        map.dispose();
        b2dr.dispose();
    }
}