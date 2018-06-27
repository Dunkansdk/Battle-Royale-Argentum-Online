package com.bonkan.brao.engine.entity.system;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.bonkan.brao.engine.entity.component.TextureComponent;
import com.bonkan.brao.engine.entity.component.TiledMapLayerComponent;
import com.bonkan.brao.engine.entity.component.TransformComponent;

public class RenderingSystem extends IteratingSystem {

    static final float PPM = 32.0f;
    static final float FRUSTUM_WIDTH = Gdx.graphics.getWidth();//37.5f;
    static final float FRUSTUM_HEIGHT = Gdx.graphics.getHeight() ;//.0f;

    public static final float PIXELS_TO_METRES = 1.0f;

    private static Vector2 meterDimensions = new Vector2();
    private static Vector2 pixelDimensions = new Vector2();
    
    public static Vector2 getScreenSizeInMeters() {
        meterDimensions.set(Gdx.graphics.getWidth() * PIXELS_TO_METRES,
                            Gdx.graphics.getHeight() * PIXELS_TO_METRES);
        return meterDimensions;
    }

    public static Vector2 getScreenSizeInPixesl(){
        pixelDimensions.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return pixelDimensions;
    }

    public static float PixelsToMeters(float pixelValue) {
        return pixelValue * PIXELS_TO_METRES;
    }

    private SpriteBatch batch;
    private BatchTiledMapRenderer tiledMapRenderer;
    private Array<Entity> renderQueue;
    private Array<Entity> backgroundQueue;
	private Array<Entity> foregroundQueue;
    private Comparator<Entity> comparator;
    private OrthographicCamera cam;

    private ComponentMapper<TextureComponent> texture;
    private ComponentMapper<TransformComponent> transform;
    private ComponentMapper<TiledMapLayerComponent> mapLayerMapper;

	public RenderingSystem(SpriteBatch batch, BatchTiledMapRenderer tiledMapRenderer, OrthographicCamera camera) {
		super(Family.one(TransformComponent.class, TextureComponent.class, TiledMapLayerComponent.class).get());

        texture = ComponentMapper.getFor(TextureComponent.class);
        transform = ComponentMapper.getFor(TransformComponent.class);
        mapLayerMapper = ComponentMapper.getFor(TiledMapLayerComponent.class);
        
        comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity entityA, Entity entityB) {
				return (int)Math.signum(transform.get(entityB).position.z -
										transform.get(entityA).position.z);
			}
		};

		renderQueue = new Array<Entity>();
        backgroundQueue = new Array<Entity>();
		foregroundQueue = new Array<Entity>();

        this.batch = batch;
        this.tiledMapRenderer = tiledMapRenderer;
        cam = camera;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderQueue.sort(comparator);
		backgroundQueue.sort(comparator);
		foregroundQueue.sort(comparator);
		        
        cam.update();
        
        tiledMapRenderer.setView(cam);
		tiledMapRenderer.getBatch().setProjectionMatrix(cam.combined);
        for(Entity entity : backgroundQueue){
			renderMapLayer(entity);
		}
        
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();

        for (Entity entity : renderQueue) {
        	if(entity.getComponent(TiledMapLayerComponent.class) != null) {
        		renderMapLayer(entity);
        	} else {
	            TextureComponent tex = texture.get(entity);
	            TransformComponent t = transform.get(entity);
	
	            if (tex.region == null || t.isHidden) {
	                continue;
	            }
	
	            float width = tex.region.getRegionWidth();
	            float height = tex.region.getRegionHeight();
	
	            float originX = width / 2f;
	            float originY = height / 2f;
	            batch.begin();
	            batch.draw(tex.region,
	                    t.position.x - originX, t.position.y - originY,
	                    originX, originY,
	                    width, height,
	                    PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y),
	                    t.rotation);
	            batch.end();
        	}
        }

        
        for(Entity entity : foregroundQueue){
			renderMapLayer(entity);
		}
        
        renderQueue.clear();        
		foregroundQueue.clear();
		backgroundQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        if(mapLayerMapper.has(entity)) {
			if(transform.get(entity).position.z >= 0)
				renderQueue.add(entity);
			else
				backgroundQueue.add(entity);
		}else{
			renderQueue.add(entity);
		}
    }
    
    private void renderMapLayer(Entity entity){
		tiledMapRenderer.getBatch().begin();
		TiledMapLayerComponent layer = mapLayerMapper.get(entity);
		if(layer.getLayer() == null){
			System.err.println("Null map Layer");
			return;
		}
		tiledMapRenderer.renderTileLayer(layer.getLayer());
		tiledMapRenderer.getBatch().end();
	}
    
    public OrthographicCamera getCamera() {
        return cam;
    }
    
}