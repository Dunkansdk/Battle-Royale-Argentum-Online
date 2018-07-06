package com.bonkan.brao.engine.input;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.bonkan.brao.engine.entity.Entity;
import com.bonkan.brao.engine.entity.EntityManager;
import com.bonkan.brao.engine.entity.Human.PlayerState;
import com.bonkan.brao.engine.entity.humans.Enemy;
import com.bonkan.brao.engine.entity.humans.Player;
import com.bonkan.brao.engine.utils.Constants;
import com.bonkan.brao.networking.Packet;
import com.bonkan.brao.networking.PacketIDs;
import com.esotericsoftware.kryonet.Client;

public class InputController {
	
	private ArrayList<Shape> mapBlocks;
    
    private static final int DIR_DOWN = 0;
    private static final int DIR_UP = 1;
    private static final int DIR_LEFT = 2;
    private static final int DIR_RIGHT = 3;
	
	private Client client;
	
	public InputController(Client client, ArrayList<Shape> blocks) {
		this.client = client;
		mapBlocks = blocks;
	}
	
	/**
     * <p>Maneja los inputs del player.
     * TODO: inputs customizables y cargados desde un JSON</p>
     * @param delta		&emsp;<b>float</b> el deltaTime (Gdx)
     */
    public void update(float delta, Player player) {
        
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
        	client.sendUDP(new Packet(PacketIDs.PACKET_PLAYER_MOVED, player.getID().toString(), args));
        }
        
        if(changedState) {
        	ArrayList<String> args = new ArrayList<String>();
        	args.add(String.valueOf(player.getState()));
        	client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_CHANGED_STATE, player.getID().toString(), args));
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
     * <p>Chequea si el player en la posici�n destino colisiona con alg�n otro player</p>
     * @param p		&emsp;{@link com.bonkan.brao.engine.entity.humans.Player Player} el player
     * @param x		&emsp;<b>int</b> la posici�n destino X
     * @param y		&emsp;<b>int</b> la posici�n destino Y
     * @return	<b>boolean</b>
     */
    private boolean checkCollisionWithEnemies(Player p, int x, int y)
    {
    	Rectangle playerRect = new Rectangle(x - Constants.BODY_WIDTH / 2, y - Constants.BODY_HEIGHT / 2, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);
    	
    	// iteramos las entidades
    	HashMap<UUID, Entity> entidades = EntityManager.getAllEntities();
    	
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

}
