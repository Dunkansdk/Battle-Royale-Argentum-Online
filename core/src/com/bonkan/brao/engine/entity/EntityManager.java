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
	
	private HashMap<UUID, Entity> entities;
	
	public EntityManager() {
		entities = new HashMap<UUID, Entity>();
	}
	
	public void addPlayer(UUID id, Player player) {
		entities.put(id, player);
	}
	
	public void addEnemy(UUID id, Enemy enemy) {
		entities.put(id, enemy);
	}
	
	/**
	 * <p>Compara si la una entidad esta abajo de la otra (y >) y la dibuja por encima</p>
	 */
	public void render(SpriteBatch batch) {
		List<Entity> entityValues = new ArrayList<Entity>(entities.values());
		
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
	
	public void update(float delta) {
		for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
			entry.getValue().update(delta);
		}
	}
	
	public void removeEnemy(UUID id) {
		
		Iterator<Map.Entry<UUID, Entity>> it = entities.entrySet().iterator();
	    while (it.hasNext()) {
	    	if(it.next().getKey().compareTo(id) == 0) {
	    		it.remove();
	    		break;
	    	}
	    } 
	}

	public Enemy getEnemy(UUID id) {
		for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
			if(entry.getValue() instanceof Enemy)
				if(entry.getKey().compareTo(id) == 0)
					return (Enemy) entry.getValue();
		}
		return null;
	}
	
	public Player getPlayer() {
		for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
			if(entry.getValue() instanceof Player)
				return (Player) entry.getValue();
		}
		return null;
	}
	
	public HashMap<UUID, Entity> getAllEntities()
	{
		return entities;
	}

}
