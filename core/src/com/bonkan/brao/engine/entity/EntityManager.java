package com.bonkan.brao.engine.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bonkan.brao.engine.entity.humans.Enemy;
import com.bonkan.brao.engine.entity.humans.Player;
import com.bonkan.brao.engine.utils.Constants;

/**
 * <p>Maneja las entidades... Quién lo hubiera dicho !!</p>
 */
public class EntityManager {
	
	private static HashMap<UUID, Entity> entities;
	private static ArrayList<Entity> worldEntities;
	
	public static void init() {
		entities = new HashMap<UUID, Entity>();
		worldEntities = new ArrayList<Entity>();
	}
	
	public static void addPlayer(UUID id, Player player) {
		entities.put(id, player);
	}
	
	public static void addEnemy(UUID id, Enemy enemy) {
		entities.put(id, enemy);
	}
	
	/**
	 * <p>Compara si la una entidad esta abajo de la otra (y >) y la dibuja por encima</p>
	 */
	public static void render(SpriteBatch batch) {
		List<Entity> entityValues = new ArrayList<Entity>(entities.values());
		entityValues.addAll(worldEntities);
		
		Collections.sort(entityValues, new Comparator<Entity>() {
			@Override
			public int compare(Entity a, Entity b) {
				return ((b.getPos().y + Constants.BODY_HEIGHT / 2) - (a.getPos().y + Constants.BODY_HEIGHT / 2)) > 0 ? 1 : -1;
			}
	    });
		
		for (Entity entity : entityValues) {
			entity.render(batch);
		}
	}
	
	public static void update(float delta) {
		for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
			entry.getValue().update(delta);
		}
	}
	
	public static void removeEnemy(UUID id) {
		Iterator<Map.Entry<UUID, Entity>> it = entities.entrySet().iterator();
	    while (it.hasNext()) {
	    	if(it.next().getKey().compareTo(id) == 0) {
	    		it.remove();
	    		break;
	    	}
	    } 
	}

	public static Enemy getEnemy(UUID id) {
		for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
			if(entry.getValue() instanceof Enemy)
				if(entry.getKey().compareTo(id) == 0)
					return (Enemy) entry.getValue();
		}
		return null;
	}
	
	public static Player getPlayer() {
		for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
			if(entry.getValue() instanceof Player)
				return (Player) entry.getValue();
		}
		return null;
	}
	
	public static void addWorldObject(WorldObject entity) {
		worldEntities.add(entity);
	}
	
	public static HashMap<UUID, Entity> getAllEntities()
	{
		return entities;
	}

}
