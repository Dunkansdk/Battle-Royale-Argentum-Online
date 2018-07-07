package com.bonkan.brao.engine.input;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.bonkan.brao.engine.entity.EntityManager;
import com.bonkan.brao.engine.entity.entities.Chest;
import com.bonkan.brao.engine.entity.entities.Human.PlayerState;
import com.bonkan.brao.engine.entity.entities.human.Player;
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
    	Vector2 oldPos = new Vector2(player.getPos().x, player.getPos().y);
    	
    	// solamente cambiamos el state cuando no hay bloqueo para no marear al server
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
        	if(player.getState() != PlayerState.MOVE_DOWN)
				changedState = true;
    		
    		player.setLastValidState(player.getState());
			player.setState(PlayerState.MOVE_DOWN);
        	
        	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        	{
        		if(player.getState() != PlayerState.MOVE_RIGHT_DOWN)
					changedState = true;
        		
        		player.setLastValidState(player.getState());
    			player.setState(PlayerState.MOVE_RIGHT_DOWN);
        		
        		if(!blockedDirs[DIR_RIGHT] && !blockedDirs[DIR_DOWN])
        			player.setLocation((int) player.getPos().x + 2, (int) player.getPos().y - 2);
        		else if (!blockedDirs[DIR_DOWN])
        			player.setLocation((int) player.getPos().x, (int) player.getPos().y - 2);
        		else if (!blockedDirs[DIR_RIGHT]) 
        			player.setLocation((int) player.getPos().x + 2, (int) player.getPos().y);
        		
        	} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        		if(player.getState() != PlayerState.MOVE_LEFT_DOWN)
    				changedState = true;
    			
    			player.setLastValidState(player.getState());
    			player.setState(PlayerState.MOVE_LEFT_DOWN);
        		
        		if(!blockedDirs[DIR_LEFT] && !blockedDirs[DIR_DOWN])
        			player.setLocation((int) player.getPos().x - 2, (int) player.getPos().y - 2);
        		else if (!blockedDirs[DIR_DOWN])
        			player.setLocation((int) player.getPos().x, (int) player.getPos().y - 2);
        		else if (!blockedDirs[DIR_LEFT]) 
        			player.setLocation((int) player.getPos().x - 2, (int) player.getPos().y);
        	} else if(!blockedDirs[DIR_DOWN]) {

    			player.setLocation((int) player.getPos().x, (int) player.getPos().y - 2);

        	}
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	
        	if(player.getState() != PlayerState.MOVE_UP)
				changedState = true;
    		
    		player.setLastValidState(player.getState());
			player.setState(PlayerState.MOVE_UP);
        	
        	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        	{
        		if(player.getState() != PlayerState.MOVE_RIGHT_UP)
    				changedState = true;
    			
    			player.setLastValidState(player.getState());
    			player.setState(PlayerState.MOVE_RIGHT_UP);
        		
        		if(!blockedDirs[DIR_RIGHT] && !blockedDirs[DIR_UP])
        			player.setLocation((int) player.getPos().x + 2, (int) player.getPos().y + 2);
        		else if (!blockedDirs[DIR_UP])
        			player.setLocation((int) player.getPos().x, (int) player.getPos().y + 2);
        		else if (!blockedDirs[DIR_RIGHT])
        			player.setLocation((int) player.getPos().x + 2, (int) player.getPos().y);
 
        	} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        		if(player.getState() != PlayerState.MOVE_LEFT_UP)
    				changedState = true;
    			
    			player.setLastValidState(player.getState());
    			player.setState(PlayerState.MOVE_LEFT_UP);
        		
        		if(!blockedDirs[DIR_LEFT] && !blockedDirs[DIR_UP])
        			player.setLocation((int) player.getPos().x - 2, (int) player.getPos().y + 2);
        		else if (!blockedDirs[DIR_UP])
        			player.setLocation((int) player.getPos().x, (int) player.getPos().y + 2);
        		else if (!blockedDirs[DIR_LEFT])
        			player.setLocation((int) player.getPos().x - 2, (int) player.getPos().y);

        	} else if(!blockedDirs[DIR_UP]) {

    			player.setLocation((int) player.getPos().x, (int) player.getPos().y + 2);
        			
        	}	
    	} else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
    		if(player.getState() != PlayerState.MOVE_LEFT)
				changedState = true;
    		
    		player.setLastValidState(player.getState());
    		player.setState(PlayerState.MOVE_LEFT);
    		
        	if(!blockedDirs[DIR_LEFT])
        		player.setLocation((int) player.getPos().x - 2, (int) player.getPos().y);

    	} else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
    		if(player.getState() != PlayerState.MOVE_RIGHT)
				changedState = true;
    		
    		player.setState(PlayerState.MOVE_RIGHT);
    		player.setLastValidState(player.getState());
    		
    		if(!blockedDirs[DIR_RIGHT])
    			player.setLocation((int) player.getPos().x + 2, (int) player.getPos().y);
        		
    	}

        if(	!Gdx.input.isKeyPressed(Input.Keys.DOWN) 	&& 
        	!Gdx.input.isKeyPressed(Input.Keys.UP) 		&& 
        	!Gdx.input.isKeyPressed(Input.Keys.LEFT) 	&& 
        	!Gdx.input.isKeyPressed(Input.Keys.RIGHT)) 
        {
        	if(player.getState() != PlayerState.NONE)
        	{
        		changedState = true;
        		player.setLastValidState(player.getState());
        	}

        	player.setState(PlayerState.NONE);
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
        {
        	int chestID = checkChestPosition(player);
        	if(chestID > -1 && !EntityManager.getChestByID(chestID).getOpened())
        		client.sendTCP(new Packet(PacketIDs.PACKET_TRY_OPEN_CHEST, String.valueOf(chestID), null));
        }
        
        // como esto es posible que se mande en cada frame, lo mandamos via UDP
        if(oldPos.x != player.getPos().x || oldPos.y != player.getPos().y)
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
    
    private int checkChestPosition(Player player)
    {
    	int realX = (int) player.getPos().x - Constants.BODY_WIDTH / 2;
    	int realY = (int) player.getPos().y - Constants.BODY_HEIGHT / 2;
    	
    	if(player.getLastValidState() == PlayerState.MOVE_DOWN)
    		realY -= 5;
    	else if(player.getLastValidState() == PlayerState.MOVE_UP)
    		realY += 5;
    	else if(player.getLastValidState() == PlayerState.MOVE_LEFT)
    		realX -= 5;
    	else if(player.getLastValidState() == PlayerState.MOVE_RIGHT)
    		realX += 5;
    	else if(player.getLastValidState() == PlayerState.MOVE_LEFT_DOWN)
    	{
    		realY -= 5;
    		realX -= 5;
    	}
    	else if(player.getLastValidState() == PlayerState.MOVE_LEFT_UP)
    	{
    		realY += 5;
    		realX -= 5;
    	}
    	else if(player.getLastValidState() == PlayerState.MOVE_RIGHT_DOWN)
    	{
    		realY -= 5;
    		realX += 5;
    	}
    	else if(player.getLastValidState() == PlayerState.MOVE_RIGHT_UP)
    	{
    		realY += 5;
    		realX += 5;
    	}
    	
    	Rectangle playerRect = new Rectangle(realX, realY, Constants.BODY_WIDTH, Constants.BODY_HEIGHT);

    	for(Chest c : EntityManager.getChests())
    	{
    		Rectangle chestRect = new Rectangle((int) c.getPos().x, (int) c.getPos().y, 32, 32);
    		if(chestRect.intersects(playerRect))
    			return c.getID();
    	}
    	
    	return -1;
    }
}
