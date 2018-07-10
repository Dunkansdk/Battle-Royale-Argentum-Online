package com.bonkan.brao.state.app;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.bonkan.brao.engine.entity.EntityManager;
import com.bonkan.brao.engine.entity.entities.Human.PlayerState;
import com.bonkan.brao.engine.entity.entities.Item;
import com.bonkan.brao.engine.entity.entities.human.Enemy;
import com.bonkan.brao.engine.entity.entities.human.Player;
import com.bonkan.brao.engine.entity.entities.particle.ParticleType;
import com.bonkan.brao.engine.input.InputController;
import com.bonkan.brao.engine.map.MapManager;
import com.bonkan.brao.engine.map.WorldManager;
import com.bonkan.brao.engine.ui.ItemSlot;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.engine.utils.CommonUtils;
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
    private ItemSlot[] inventory;
    
    @SuppressWarnings("static-access")
	public PlayState(GameStateManager gameState) {
        super(gameState);
        WorldManager.init();
        EntityManager.init(app);
        map = new MapManager(WorldManager.world);
        b2dr = new Box2DDebugRenderer();
        inputController = new InputController(app.getClient(), map.createBlocks());

        LoggedUser aux = app.getLoggedUser();
   
        EntityManager.setPlayer(new Player(aux.getX(), aux.getY(), aux.getLoggedDefaultBody(), 1, aux.getHP(), aux.getMana(), aux.getLoggedID(), aux.getLoggedUserName(), WorldManager.world));
        
        EntityManager.addParticle(ParticleType.TEST2, 10, 10, true); 
        //hinchapelotas EntityManager.addParticle(ParticleType.TEST2, 300, 200, false); 
        EntityManager.addParticle(ParticleType.TEST1, 150, 150, false); 
        
        inventory = new ItemSlot[3]; // casco, escudo, arma
        
        // el offset de separacion siempre es 15 pixeles
        inventory[ItemSlot.INVENTORY_SHIELD_SLOT] = new ItemSlot(app.V_WIDTH / 2 - Constants.ITEM_SIZE, 50); // en el diome
        inventory[ItemSlot.INVENTORY_WEAPON_SLOT] = new ItemSlot(app.V_WIDTH / 2 - Constants.ITEM_SIZE - 64 - 15, 50); // primero
        inventory[ItemSlot.INVENTORY_HELMET_SLOT] = new ItemSlot(app.V_WIDTH / 2 - Constants.ITEM_SIZE + 64 + 15, 50); // ultimo
    }

    @Override
    public void update(float delta)
    {
    	handlePlayerIncomingData();
    	
    	map.getTiled().setView(camera);
    	inputController.update(delta, EntityManager.getPlayer());
    	
    	if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) // no lo pongo en el inputController porque es un quilombo (no tengo acceso al inventory ni al app)
    	{
    		@SuppressWarnings("static-access")
    		Point p = new Point(Gdx.input.getX(), app.V_HEIGHT - Gdx.input.getY()); // gdx.input usa otro sist de coordenadas -.- (0,0) arriba izq
        	for(int i = 0; i < inventory.length; i++)
        	{
        		ItemSlot is = inventory[i];
        		if(is.getRect().contains(p) && !is.isEmpty())
        		{
        			ArrayList<String> args = new ArrayList<String>();
        			args.add(String.valueOf(i));
        			app.getClient().sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_UNEQUIP_ITEM, EntityManager.getPlayer().getID().toString(), args));
        		}
        	}
    	}
    	
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
    	
    	/**
    	 * Todo lo del world
    	 */
    	EntityManager.render(batch); 
    	
    	if(app.DEBUG) b2dr.render(WorldManager.world, camera.combined.cpy());
    	map.getRayHandler().render();
    	
    	/**
    	 * Todo lo de la interfaz
    	 */
    	app.getHudBatch().begin();
    		AssetsManager.getDefaultFont().draw(app.getHudBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 50, 50);
    		for(ItemSlot is : inventory)
    			is.render(app.getHudBatch());
    	app.getHudBatch().end();
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
    					state = PlayerState.valueOf(p.getArgs().get(0));

    					if(!isEnemyInArea(id))
    					{
    						ArrayList<String> args = new ArrayList<String>();
    						args.add(id.toString());
    						app.getClient().sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_FULL_BODY, EntityManager.getPlayer().getID().toString(), args));
    					} else
    						setEnemyState(id, state);
    					
	    				break;
	    				
	    			case PacketIDs.PACKET_USER_MOVED:
	    				id = UUID.fromString((String) p.getData());
    					x = Integer.parseInt(p.getArgs().get(0)); 
    					y = Integer.parseInt(p.getArgs().get(1)); 

    					if(!isEnemyInArea(id))
    					{
    						ArrayList<String> args = new ArrayList<String>();
    						args.add(id.toString());
    						app.getClient().sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_FULL_BODY, EntityManager.getPlayer().getID().toString(), args));
    					} else
    						setEnemyPos(id, x, y);
	    				
    					checkEnemyInArea(id); // chequeamos si no se fue del area
	    				break;
	    				
	    			case PacketIDs.PACKET_CHEST_OPENED:
	    				int chestID = Integer.parseInt((String) p.getData());
	    				EntityManager.getChestByID(chestID).open();
	    				break;
	    				
	    			case PacketIDs.PACKET_ITEM_THROWN:
	    				id = UUID.fromString(p.getArgs().get(0));
	    				int rarity = Integer.parseInt(p.getArgs().get(1));
	    				x = Integer.parseInt(p.getArgs().get(2));
	    				y = Integer.parseInt(p.getArgs().get(3));
	    				nick = p.getArgs().get(4);
	    				String animTexture = p.getArgs().get(6);
	    				int type = Integer.parseInt(p.getArgs().get(7));

	    				EntityManager.addItem(new Item(x, y, rarity, nick, AssetsManager.getItem(p.getArgs().get(5)), animTexture, type, id));
	    				break;
	    				
	    			case PacketIDs.PACKET_PLAYER_CONFIRM_GET_ITEM:
	    				
	    				Item i = EntityManager.getItem(UUID.fromString((String) p.getData()));
	    				
	    				switch(i.getType())
	    				{
	    					case Constants.ITEM_TYPE_WEAPON:
		    					EntityManager.getPlayer().setWeapon(i.getAnimTexture());
		    					
		    					inventory[ItemSlot.INVENTORY_WEAPON_SLOT].setItem(i);
		    					break;
	    				
	    					case Constants.ITEM_TYPE_SHIELD:
		    					EntityManager.getPlayer().setShield(i.getAnimTexture());
		    					
		    					inventory[ItemSlot.INVENTORY_SHIELD_SLOT].setItem(i);
		    					break;
	    				}
	    				
	    				break;
	    				
	    			case PacketIDs.PACKET_REMOVE_ITEM_FROM_FLOOR:
	    				EntityManager.deleteItem(UUID.fromString((String) p.getData()));
	    				break;
	    				
	    			case PacketIDs.PACKET_USER_IN_AREA_EQUIPPED_ITEM:
	    				id = UUID.fromString((String) p.getData());
	    				String shieldID = p.getArgs().get(0);
	    				String weaponID = p.getArgs().get(1);
	    				
	    				if(!isEnemyInArea(id))
	    				{
	    					ArrayList<String> args = new ArrayList<String>();
    						args.add(id.toString());
    						app.getClient().sendTCP(new Packet(PacketIDs.PACKET_PLAYER_REQUEST_FULL_BODY, EntityManager.getPlayer().getID().toString(), args));
	    				} else {
	    					EntityManager.getEnemy(id).setShield(shieldID);
	    					EntityManager.getEnemy(id).setWeapon(weaponID);
	    				}
	    					
	    				break;
	    				
	    			case PacketIDs.PACKET_PLAYER_SEND_FULL_BODY:
	    				id = UUID.fromString((String) p.getData());
    					bodyIndex = Integer.parseInt(p.getArgs().get(0)); 
    					headIndex = Integer.parseInt(p.getArgs().get(1)); 
    					x = Integer.parseInt(p.getArgs().get(2)); 
    					y = Integer.parseInt(p.getArgs().get(3)); 
    					nick = p.getArgs().get(4);
    					state = PlayerState.valueOf(p.getArgs().get(5));
    					String shieldAnim = p.getArgs().get(6);
    					String weaponAnim = p.getArgs().get(7);

	    				if(!isEnemyInArea(id))
	    				{
	    					addEnemyToArea(bodyIndex, headIndex, x, y, id, nick, weaponAnim, shieldAnim);
	    				
	    					ArrayList<String> args = new ArrayList<String>();
    						args.add(id.toString());
	    					app.getClient().sendTCP(new Packet(PacketIDs.PACKET_USER_ENTERED_PLAYER_AREA, EntityManager.getPlayer().getID().toString(), args));
	    				}

	    				break;
	    				
	    			case PacketIDs.PACKET_PLAYER_CONFIRM_UNEQUIP_ITEM:
	    				int slot = Integer.parseInt((String) p.getData());
	    				inventory[slot].unequip();
	    				
	    				switch(slot)
	    				{
		    				case ItemSlot.INVENTORY_HELMET_SLOT:
		    					
		    					break;
		    					
		    				case ItemSlot.INVENTORY_SHIELD_SLOT:
		    					EntityManager.getPlayer().setShield(null);
		    					break;
		    					
		    				case ItemSlot.INVENTORY_WEAPON_SLOT:
		    					EntityManager.getPlayer().setWeapon(null);
		    					break;
	    				}
	    				break;
    			}
    			
    			it.remove();
    		}
    	}
    }

    /**
     * <p>Setea la camara en la posicion donde se encuentra el personaje (target)</p>
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
    
    public void addEnemyToArea(int bodyIndex, int headIndex, int x, int y, UUID id, String nick, String weaponAnim, String shieldAnim)
    {
    	EntityManager.addEnemy(id, new Enemy(x, y, bodyIndex, headIndex, id, nick, WorldManager.world, weaponAnim, shieldAnim));
    }
    
    public void setEnemyState(UUID enemyID, PlayerState newState)
    {
    	if(EntityManager.getEnemy(enemyID) != null)
    		EntityManager.getEnemy(enemyID).setState(newState);
    }
    
    public void setEnemyPos(UUID enemyID, int x, int y)
    {
    	if(EntityManager.getEnemy(enemyID) != null)
    		EntityManager.getEnemy(enemyID).setLocation(x, y);
    }
    
    public boolean isEnemyInArea(UUID enemyID)
    {
    	return (EntityManager.getEnemy(enemyID) != null);
    }
    
    /**
     * <p>Checkea si un enemigo esta en tu area de vision</p>
     * @param enemyID
     */
    public void checkEnemyInArea(UUID enemyID)
    {
    	if(EntityManager.getEnemy(enemyID) != null)
    	{
    		if(!CommonUtils.areInViewport(app, EntityManager.getPlayer().getPos(), EntityManager.getEnemy(enemyID).getPos()))
    		{
    			// lo borramos
    			EntityManager.removeEnemy(enemyID);
    		}
    	}
    }

    @Override
    public void dispose() {
        map.dispose();
        b2dr.dispose();
    }
}