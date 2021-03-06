package com.bonkan.brao.engine.input;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bonkan.brao.engine.entity.EntityManager;
import com.bonkan.brao.engine.entity.entities.Chest;
import com.bonkan.brao.engine.entity.entities.Item;
import com.bonkan.brao.engine.entity.entities.Human.PlayerState;
import com.bonkan.brao.engine.entity.entities.human.Player;
import com.bonkan.brao.engine.ui.SpellSlot;
import com.bonkan.brao.engine.utils.Constants;
import com.bonkan.brao.networking.Packet;
import com.bonkan.brao.networking.PacketIDs;
import com.esotericsoftware.kryonet.Client;

public class InputController {
	
	private static ArrayList<Shape> mapBlocks;
    
    private static final int DIR_DOWN = 0;
    private static final int DIR_UP = 1;
    private static final int DIR_LEFT = 2;
    private static final int DIR_RIGHT = 3;
	
	private static Client client;
	private static OrthographicCamera camera;
	private static SpellSlot[] spellsInventory;
	
	// intervalos
	private static final int RED_POT_INTERVAL = 700;
	private static final int BLUE_POT_INTERVAL = 400;
	private static long lastRedPot;
	private static long lastBluePot;
	
	public static void init(Client c, OrthographicCamera cam, SpellSlot[] spells, ArrayList<Shape> blocks) {
		client = c;
		camera = cam;
		spellsInventory = spells;
		mapBlocks = blocks;
		lastRedPot = System.currentTimeMillis();
		lastBluePot = System.currentTimeMillis();
	}
	
	/**
     * <p>Maneja los inputs del player.
     * TODO: inputs customizables y cargados desde un JSON</p>
     * @param delta		&emsp;<b>float</b> el deltaTime (Gdx)
     */
    public static void update(float delta, Player player) 
    {
    	boolean[] blockedDirs = blockedDirections(player);
    	boolean changedState = false;
    	Vector2 oldPos = new Vector2(player.getPos().x, player.getPos().y);
    	ArrayList<String> args = new ArrayList<String>();
    	
    	// solamente cambiamos el state cuando no hay bloqueo para no marear al server
        if(Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_DOWN))
        {
        	if(player.getState() != PlayerState.MOVE_DOWN)
				changedState = true;
    		
    		player.setLastValidState(player.getState());
			player.setState(PlayerState.MOVE_DOWN);
        	
        	if(Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_RIGHT))
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
        		
        	} else if (Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_LEFT)) {
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
        } else if(Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_UP)) {
        	
        	if(player.getState() != PlayerState.MOVE_UP)
				changedState = true;
    		
    		player.setLastValidState(player.getState());
			player.setState(PlayerState.MOVE_UP);
        	
        	if(Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_RIGHT))
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
 
        	} else if (Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_LEFT)) {
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
    	} else if(Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_LEFT)) {
    		if(player.getState() != PlayerState.MOVE_LEFT)
				changedState = true;
    		
    		player.setLastValidState(player.getState());
    		player.setState(PlayerState.MOVE_LEFT);
    		
        	if(!blockedDirs[DIR_LEFT])
        		player.setLocation((int) player.getPos().x - 2, (int) player.getPos().y);

    	} else if(Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_RIGHT)) {
    		if(player.getState() != PlayerState.MOVE_RIGHT)
				changedState = true;
    		
    		player.setState(PlayerState.MOVE_RIGHT);
    		player.setLastValidState(player.getState());
    		
    		if(!blockedDirs[DIR_RIGHT])
    			player.setLocation((int) player.getPos().x + 2, (int) player.getPos().y);
        		
    	}

        if(	!Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_DOWN) 	&& 
        	!Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_UP) 		&& 
        	!Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_LEFT) 	&& 
        	!Gdx.input.isKeyPressed(KeyBindings.KEY_MOVE_RIGHT)) 
        {
        	if(player.getState() != PlayerState.NONE)
        	{
        		changedState = true;
        		player.setLastValidState(player.getState());
        	}

        	player.setState(PlayerState.NONE);
        }
        
        if(Gdx.input.isKeyJustPressed(KeyBindings.KEY_ACTION))
        {
        	// chequeamos cofres
        	int chestID = checkChestPosition(player);
        	if(chestID > -1 && !EntityManager.getChestByID(chestID).getOpened())
        	{
        		args.clear();
        		args.add(String.valueOf((int) EntityManager.getChestByID(chestID).getPos().x));
        		args.add(String.valueOf((int) EntityManager.getChestByID(chestID).getPos().y));
        		
        		client.sendTCP(new Packet(PacketIDs.PACKET_TRY_OPEN_CHEST, String.valueOf(chestID), args));
        	}
        	
        	// chequeamos items
        	UUID itemID = checkItemPosition(player);
        	if(itemID != null)
        	{
        		args.clear();
        		args.add(itemID.toString());
        		args.add(String.valueOf(EntityManager.getItem(itemID).getAmount()));
        		client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_GET_ITEM, player.getID().toString(), args));
        	}
        }
        
        Vector3 mouseCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));;
        
        if(Gdx.input.isKeyJustPressed(KeyBindings.KEY_SPELL_1) && !spellsInventory[SpellSlot.SLOT_SPELL_1].isEmpty() && !EntityManager.getPlayer().hasSpellCasted())
        {
        	args.clear();
        	args.add(String.valueOf(SpellSlot.SLOT_SPELL_1));
        	args.add(String.valueOf(mouseCoords.x));
        	args.add(String.valueOf(mouseCoords.y));
        	args.add(String.valueOf(EntityManager.getPlayer().getPos().x));
        	args.add(String.valueOf(EntityManager.getPlayer().getPos().y));
        	client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_CAST_SPELL, EntityManager.getPlayer().getID().toString(), args));
        }
        
        if(Gdx.input.isKeyJustPressed(KeyBindings.KEY_SPELL_2) && !spellsInventory[SpellSlot.SLOT_SPELL_2].isEmpty() && !EntityManager.getPlayer().hasSpellCasted())
        {
        	args.clear();
        	args.add(String.valueOf(SpellSlot.SLOT_SPELL_2));
        	args.add(String.valueOf(mouseCoords.x));
        	args.add(String.valueOf(mouseCoords.y));
        	args.add(String.valueOf(EntityManager.getPlayer().getPos().x));
        	args.add(String.valueOf(EntityManager.getPlayer().getPos().y));
        	client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_CAST_SPELL, EntityManager.getPlayer().getID().toString(), args));
        }
        
        if(Gdx.input.isKeyJustPressed(KeyBindings.KEY_SPELL_3) && !spellsInventory[SpellSlot.SLOT_SPELL_3].isEmpty() && !EntityManager.getPlayer().hasSpellCasted())
        {
        	args.clear();
        	args.add(String.valueOf(SpellSlot.SLOT_SPELL_3));
        	args.add(String.valueOf(mouseCoords.x));
        	args.add(String.valueOf(mouseCoords.y));
        	args.add(String.valueOf(EntityManager.getPlayer().getPos().x));
        	args.add(String.valueOf(EntityManager.getPlayer().getPos().y));
        	client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_CAST_SPELL, EntityManager.getPlayer().getID().toString(), args));
        }
        
        if(Gdx.input.isKeyJustPressed(KeyBindings.KEY_SPELL_4) && !spellsInventory[SpellSlot.SLOT_SPELL_4].isEmpty() && !EntityManager.getPlayer().hasSpellCasted())
        {
        	args.clear();
        	args.add(String.valueOf(SpellSlot.SLOT_SPELL_4));
        	args.add(String.valueOf(mouseCoords.x));
        	args.add(String.valueOf(mouseCoords.y));
        	args.add(String.valueOf(EntityManager.getPlayer().getPos().x));
        	args.add(String.valueOf(EntityManager.getPlayer().getPos().y));
        	client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_CAST_SPELL, EntityManager.getPlayer().getID().toString(), args));
        }
        
        if(Gdx.input.isKeyJustPressed(KeyBindings.KEY_RED_POTION) && EntityManager.getPlayer().getRedPotionsAmount() > 0 && redPotInterval() && EntityManager.getPlayer().getHealth() != EntityManager.getPlayer().getMaxHealth())
        {
        	args.clear();
        	args.add(String.valueOf(Constants.RED_POTION_INDEX));
        	client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_USE_POTION, EntityManager.getPlayer().getID().toString(), args));
        
        	resetLastRedPotion();
        }
        
        if(Gdx.input.isKeyJustPressed(KeyBindings.KEY_BLUE_POTION) && EntityManager.getPlayer().getBluePotionsAmount() > 0 && bluePotInterval() && EntityManager.getPlayer().getMana() != EntityManager.getPlayer().getMaxMana())
        {
        	args.clear();
        	args.add(String.valueOf(Constants.BLUE_POTION_INDEX));
        	client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_USE_POTION, EntityManager.getPlayer().getID().toString(), args));
        	
        	resetLastBluePotion();
        }
        
        // como esto es posible que se mande en cada frame, lo mandamos via UDP
        if(oldPos.x != player.getPos().x || oldPos.y != player.getPos().y)
        {
        	args.clear();
        	args.add(String.valueOf((int) player.getPos().x));
        	args.add(String.valueOf((int) player.getPos().y));
        	client.sendUDP(new Packet(PacketIDs.PACKET_PLAYER_MOVED, player.getID().toString(), args));
        }
        
        if(changedState) {
        	args.clear();
        	args.add(String.valueOf(player.getState()));
        	client.sendTCP(new Packet(PacketIDs.PACKET_PLAYER_CHANGED_STATE, player.getID().toString(), args));
        }
    }
    
    /**
     * <p>Intervalo de potas rojas.</p>
     * <p>Es p�blico porque tambi�n se chequea desde el PlayState cuando se usa una pota clickeando el item.</p>
     */
    public static boolean redPotInterval()
    {
    	return (lastRedPot + RED_POT_INTERVAL < System.currentTimeMillis());
    }
    
    /**
     * <p>Intervalo de potas azules.</p>
     * <p>Es p�blico porque tambi�n se chequea desde el PlayState cuando se usa una pota clickeando el item.</p>
     */
    public static boolean bluePotInterval()
    {
    	return (lastBluePot + BLUE_POT_INTERVAL < System.currentTimeMillis());
    }
    
    public static void resetLastRedPotion()
    {
    	lastRedPot = System.currentTimeMillis();
    }
    
    public static void resetLastBluePotion()
    {
    	lastBluePot = System.currentTimeMillis();
    }
    
    /**
     * <p>Devuelve un array booleano con las direcciones bloqueadas.</p>
     */
    private static boolean[] blockedDirections(Player player)
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
    
    private static UUID checkItemPosition(Player player)
    {
    	HashMap<UUID, Item> items = EntityManager.getAllItems();
    	Rectangle playerRect = new Rectangle((int) player.getPos().x - Constants.BODY_WIDTH / 2, (int) player.getPos().y, Constants.BODY_WIDTH, Constants.BODY_HEIGHT / 2); // uso solamente la mitad inferior del body para chequear por items
    	for (Map.Entry<UUID, Item> entry : items.entrySet()) 
    	{
			Rectangle itemRect = new Rectangle((int) entry.getValue().getPos().x, (int) entry.getValue().getPos().y, Constants.ITEM_SIZE, Constants.ITEM_SIZE);
			if(itemRect.intersects(playerRect)) return entry.getValue().getID();
    	}
    	
    	return null;
    }
    
    private static int checkChestPosition(Player player)
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
