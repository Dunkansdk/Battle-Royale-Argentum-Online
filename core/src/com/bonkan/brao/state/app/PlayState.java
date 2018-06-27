package com.bonkan.brao.state.app;

import java.util.Iterator;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.controller.KeyboardController;
import com.bonkan.brao.engine.entity.component.B2dBodyComponent;
import com.bonkan.brao.engine.entity.component.CollisionComponent;
import com.bonkan.brao.engine.entity.component.PlayerComponent;
import com.bonkan.brao.engine.entity.component.StateComponent;
import com.bonkan.brao.engine.entity.component.TextureComponent;
import com.bonkan.brao.engine.entity.component.TiledMapComponent;
import com.bonkan.brao.engine.entity.component.TiledMapLayerComponent;
import com.bonkan.brao.engine.entity.component.TransformComponent;
import com.bonkan.brao.engine.entity.component.TypeComponent;
import com.bonkan.brao.engine.entity.component.UUIDComponent;
import com.bonkan.brao.engine.entity.system.AnimationSystem;
import com.bonkan.brao.engine.entity.system.CollisionSystem;
import com.bonkan.brao.engine.entity.system.PhysicsDebugSystem;
import com.bonkan.brao.engine.entity.system.PhysicsSystem;
import com.bonkan.brao.engine.entity.system.PlayerControlSystem;
import com.bonkan.brao.engine.entity.system.RenderingSystem;
import com.bonkan.brao.engine.util.BodyFactory;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;

public class PlayState extends AbstractGameState {

    private TiledMap map;
    
	private KeyboardController controller;
	private PooledEngine engine;
	private World world;
	private BodyFactory bodyFactory;
//	private TextureAtlas atlas;

    public PlayState(GameStateManager gameState) {
        super(gameState);
        
        map = new TmxMapLoader().load("map1.tmx");
        
        controller = new KeyboardController();
		world = new World(new Vector2(0, 0), true);
		//world.setContactListener(new B2dContactListener());
		bodyFactory = BodyFactory.getInstance(world);
			
		RenderingSystem renderingSystem = new RenderingSystem(app.getBatch(), new OrthogonalTiledMapRenderer(map), app.getCamera());
		
		engine = new PooledEngine();

        engine.addSystem(renderingSystem);
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerControlSystem(controller));
        
        createPlayer();
        loadMap(map);
    }


    @Override
    public void update(float delta)
    {
    	Gdx.input.setInputProcessor(controller);
        engine.update(delta);
    }

    @Override
    public void render()
    {
    	
    }

    @Override
    public void dispose() {
        map.dispose();
    }
    
    private void createPlayer() {
		Entity entity = engine.createEntity();
		B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
		TransformComponent position = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		PlayerComponent player = engine.createComponent(PlayerComponent.class);
		CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
		TypeComponent type = engine.createComponent(TypeComponent.class);
		StateComponent stateCom = engine.createComponent(StateComponent.class);
		UUIDComponent uuidComp = engine.createComponent(UUIDComponent.class);
		
		// TODO: Utilizar AnimationComponent y hacer un AssetManager() para un TextureAtlas (atlas.pack)
		Texture sprite = new Texture(Gdx.files.internal("body.png"));
		
		uuidComp.id = app.getLoggedID();
		b2dbody.body = bodyFactory.makeCirclePolyBody(10, 10, 1, BodyFactory.STONE, BodyType.DynamicBody, true);
		position.position.set(10, 10, 0); // set object position (x,y,z) z used to define draw order 0 first drawn
		texture.region = new TextureRegion(sprite, 26, 42);
		type.type = TypeComponent.PLAYER;
		stateCom.set(StateComponent.STATE_NORMAL);
		b2dbody.body.setUserData(entity);
		
		entity.add(b2dbody);
		entity.add(position);
		entity.add(texture);
		entity.add(player);
		entity.add(colComp);
		entity.add(type);
		entity.add(stateCom);
		entity.add(uuidComp);
		
		engine.addEntity(entity);
		
		System.out.println("Player creado con ID: " + uuidComp.id);
	}
    
    
    public void createMap(TiledMap tiledMap) {
    	
    	Entity entity = engine.createEntity();
        TiledMapComponent tiled = new TiledMapComponent();
        TypeComponent type = engine.createComponent(TypeComponent.class);
        
        tiled.map = tiledMap;
        type.type = TypeComponent.SCENERY;
        
        entity.add(tiled);
        entity.add(type);

        engine.addEntity(entity);
    }
    
    private void loadMap(TiledMap tiledMap){
		Iterator<MapLayer> i = tiledMap.getLayers().iterator();
		TiledMapTileLayer layer;
		Vector2 position = new Vector2(0, 0);

		while(i.hasNext()) {
			MapLayer m = i.next();
			
			if(m instanceof TiledMapTileLayer)
				layer  = (TiledMapTileLayer) m;
			else
				continue;
			
			System.out.println("Loading layer " + layer.getName());
			Entity entity = new Entity();

			// Obtiene la capa donde se encuentran los obstáculos
			if(layer.getName().startsWith("objects")) {
				for(int x = 0; x < layer.getWidth(); x++) {
					for(int y = 0; y < layer.getHeight(); y++) {
						TiledMapTileLayer.Cell cell = layer.getCell(x, y);
						if(cell != null) {
							if(position.y != y * 32) {
								TiledMapLayerComponent component = new TiledMapLayerComponent(layer);
								TransformComponent transform = new TransformComponent(new Vector3(x * 32, y * 32, y * 32));
								entity.add(component);
								entity.add(transform);
								position = new Vector2(x * 32, y * 32);
							}
							break;
						}
					} 
				}
			} else { // Background
				TiledMapLayerComponent component = new TiledMapLayerComponent(layer);
				TransformComponent transform = new TransformComponent(new Vector3(0, 0, -1));
				entity.add(component);
				entity.add(transform);
			}

			engine.addEntity(entity);
		}
	}
    
}