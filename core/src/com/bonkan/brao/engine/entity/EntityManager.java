package com.bonkan.brao.engine.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.bonkan.brao.Game;
import com.bonkan.brao.engine.entity.entities.Chest;
import com.bonkan.brao.engine.entity.entities.Item;
import com.bonkan.brao.engine.entity.entities.Spell;
import com.bonkan.brao.engine.entity.entities.WorldObject;
import com.bonkan.brao.engine.entity.entities.human.Enemy;
import com.bonkan.brao.engine.entity.entities.human.Player;
import com.bonkan.brao.engine.entity.entities.particle.ParticlePool;
import com.bonkan.brao.engine.entity.entities.particle.ParticleType;
import com.bonkan.brao.engine.utils.CommonUtils;
import com.bonkan.brao.engine.utils.Constants;

/**
 * <p>Maneja las entidades... Quién lo hubiera dicho !!</p>
 */
public class EntityManager {
	
	private static Player player;
	private static HashMap<UUID, Enemy> enemies;
	private static ArrayList<Entity> worldSorted;
	private static ArrayList<Entity> worldUnsorted;
	private static ArrayList<Chest> chests;
	private static HashMap<UUID, Item> items;
	private static ArrayList<Spell> spells;
	private static ParticlePool particles;
	private static Game app;
	
	public static void init(Game app) {
		enemies = new HashMap<UUID, Enemy>();
		worldSorted = new ArrayList<Entity>();
		worldUnsorted = new ArrayList<Entity>();
		chests = new ArrayList<Chest>();
		items = new HashMap<UUID, Item>();
		particles = new ParticlePool();
		spells = new ArrayList<Spell>();
	}
	
	public static void setPlayer(Player p) 
	{
		player = p;
	}
	
	public static void addEnemy(UUID id, Enemy enemy) 
	{
		enemies.put(id, enemy);
	}
	
	/**
	 * <p>Compara si la una entidad esta abajo de la otra (y >) y la dibuja por encima</p>
	 */
	public static void render(SpriteBatch batch) {
		List<Entity> entityValues = new ArrayList<Entity>(enemies.values());
		entityValues.addAll(worldSorted);
		entityValues.addAll(chests);
		entityValues.add(player);
		
		Collections.sort(entityValues, new Comparator<Entity>() {
			@Override
			public int compare(Entity a, Entity b) {
				return ((b.getPos().y + Constants.BODY_HEIGHT / 2) - (a.getPos().y + Constants.BODY_HEIGHT / 2)) > 0 ? 1 : -1;
			}
	    });

		// los items se dibujan abajo de todo
		for (Map.Entry<UUID, Item> entry : items.entrySet())
			entry.getValue().render(batch);
		
		batch.begin();
			for (Entity entity : entityValues)
				entity.render(batch);
			
			for(Entity entity : worldUnsorted) // esto se usa? - Hay que ver
				entity.render(batch);
			
			particles.render(batch, Gdx.graphics.getDeltaTime());
		batch.end();
	}
	
	public static void update(OrthographicCamera camera, float delta) 
	{
		Iterator<Map.Entry<UUID, Enemy>> it = enemies.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<UUID, Enemy> pair = (Map.Entry<UUID, Enemy>) it.next();
			if(!CommonUtils.areInViewport(app, pair.getValue().getPos(), player.getPos()))
				it.remove();
		}
		
		player.update(delta);
		
		for(Spell spell : spells) {
			spell.update(delta);
		}

		/*Iterator<Spell> itSpell = spells.listIterator();
        while (itSpell.hasNext()) {
        	Spell spell = itSpell.next();
        	spell.update(delta);
        	if(spell.isComplete()) {
        		System.out.println("Spell complete!");
        		//itSpell.remove();
        	}
        }*/
			
		Vector3 mouseCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		// también actualizo los items xq tienen el check del mouseHover
		for(Map.Entry<UUID, Item> entry : items.entrySet())
			entry.getValue().update(delta, (int) mouseCoords.x, (int) mouseCoords.y);
	}
	
	public static void removeEnemy(UUID id) {
		if(enemies.get(id) != null)
			enemies.remove(id);
	}

	public static Enemy getEnemy(UUID id) {
		return enemies.get(id);
	}
	
	public static Player getPlayer() {
		return player;
	}
	
	public static Chest getChestByID(int id)
	{
		for (Chest c : chests) {
	        if (c.getID() == id)
	            return c;
	    }
		
		return null;
	}
	
	public static void addWorldObject(WorldObject entity) {
		worldSorted.add(entity);
	}
	
	public static void addParticle(ParticleType particle, int x, int y, boolean sorted) {
		particles.create(particle, x, y);
	}
	
	public static void createSpell(ParticleType effect) {
		spells.add(new Spell(player.getPos().x, player.getPos().y, 0, 0, particles, effect));
	}
	
	public static void addChest(Chest chest)
	{
		chests.add(chest);
	}
	
	public static void addItem(Item i)
	{
		items.put(i.getID(), i);
	}
	
	public static Item getItem(UUID id)
	{
		return items.get(id);
	}
	
	public static void deleteItem(UUID id)
	{
		if(items.get(id) != null) {
			items.get(id).disposeEffect();
			items.remove(id);
		}
	}
	
	public static ArrayList<Chest> getChests()
	{
		return chests;
	}
	
	public static HashMap<UUID, Item> getAllItems()
	{
		return items;
	}

}
