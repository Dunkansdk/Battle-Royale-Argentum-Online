package com.bonkan.brao.engine.map;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.EntityManager;
import com.bonkan.brao.engine.entity.entities.Chest;
import com.bonkan.brao.engine.entity.entities.WorldObject;
import com.bonkan.brao.engine.map.factory.BodyFactory;
import com.bonkan.brao.engine.map.factory.ShapeFactory;
import com.bonkan.brao.engine.utils.AssetsManager;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 *<p>Carga todos los mapas del directiorio "maps/" y crea un {@link com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer OrthogonalTiledMapRenderer}
 * para el mapa específico que se va a mostrar.</p>
 */
public class MapManager {
	
    private BatchTiledMapRenderer tiled;
    private RayHandler rays;
    private TiledMap map;
	
    /**
     * <p>Carga los mapas desde el directorio /maps/</p>
     * <p><b>Atencion: Por defecto carga el primer mapa que se cargo!</b></p>
     */
	public MapManager(World world) {
		
		map = new TmxMapLoader().load("map1.tmx");
		
		rays = new RayHandler(world);
		RayHandler.setGammaCorrection(true);     // enable or disable gamma correction
		RayHandler.useDiffuseLight(true);
		this.rays.setAmbientLight(0.4f, 0.4f, 0.4f, 0.1f);
		this.rays.setBlur(true);           // enabled or disable blur
		this.rays.setBlurNum(2);           // set number of gaussian blur passes
		this.rays.setShadows(true);        // enable or disable shadow
		this.rays.setCulling(false);       // enable or disable culling

		load(world,  0); // ESTAMOS CARGANDO POR DEFECTO!
	}
	
	/**
	 * Setea un {@link com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer OrthogonalTiledMapRenderer} 
	 * en base a los mapas precargados del directorio "maps/"
	 * @param actual	&emsp;<b>int</b> subindice del mapa (en el ArrayList de mapas)
	 */
	public void load(World world, int actual) {
		//currentMap = actual;
		tiled = new OrthogonalTiledMapRenderer(getCurrentMap());
		createCollision(world);
		createLights();
		createObjects();
		createChests();
	}
	
	/**
	 * <p>Crea las colisiones (los <i>bodies</i> de box2d) para todos los objetos del mapa.</p>
	 * @param world	&emsp;{@link com.badlogic.gdx.physics.box2d.World World} el mundo !!
	 */
	private void createCollision(World world) {
		MapObjects objects = getCurrentMap().getLayers().get("collision").getObjects();

        for (MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;

            if (object instanceof RectangleMapObject) {
                shape = ShapeFactory.getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = ShapeFactory.getPolygon((PolygonMapObject) object);
                
            } else if (object instanceof PolylineMapObject) {
                shape = ShapeFactory.getPolyline((PolylineMapObject) object);
            } else {
                continue;
            }

            BodyFactory.createMapBox(world, shape);

            shape.dispose();
        }
    }
	
	private void createObjects() {
		MapObjects objects = getCurrentMap().getLayers().get("sprites").getObjects();

        for (MapObject object : objects) {
        	if(object.getProperties().containsKey("sprite")) {
        		TextureRegion texture = AssetsManager.getWorldSprite((String)object.getProperties().get("sprite"));
        		EntityManager.addWorldObject(new WorldObject(texture,
        													 (Float)object.getProperties().get("x"),
        													 (Float)object.getProperties().get("y") - texture.getRegionHeight()));
        		
        	}
        }
	}
	
	/**
	 * <p>Crea las luces !!! (las carga de la capa "lights" del .tmx).</p>
	 */
	private void createLights() {
		MapObjects objects = getCurrentMap().getLayers().get("lights").getObjects();

		for(MapObject object : objects) {
			if(!object.getProperties().containsKey("angle")) {
				new PointLight(rays, 120, (Color)object.getProperties().get("color"), (Integer)object.getProperties().get("size"), 
										  (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y"));
			} else {
				new ConeLight(rays, 120, (Color)object.getProperties().get("color"), (Integer)object.getProperties().get("size"), 
										 (Float)object.getProperties().get("x"), 	 (Float)object.getProperties().get("y"), 
										 270, (Integer)object.getProperties().get("amplitude")).setXray(true);
				
				
			}
		}
	}
	
	/**
	 * 
	 */
	
	
	
	/**
	 * <p>Función que pasa todos los objetos del mapa (que en el createCollision() se cargan
	 * como bodies de box2d) a Shapes de java.awt, que son las que usamos para chequear colisiones
	 * (con el método intersects() de la interfaz {@link java.awt.Shape Shape})</p>
	 */
	public ArrayList<java.awt.Shape> createBlocks()
	{
		ArrayList<java.awt.Shape> blocks = new ArrayList<java.awt.Shape>();
		
		MapObjects objects = getCurrentMap().getLayers().get("collision").getObjects();

        for (MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }
            
            if (object instanceof RectangleMapObject) {
            	RectangleMapObject rmo = (RectangleMapObject) object;
                Rectangle rect = new Rectangle((int) rmo.getRectangle().x, (int) rmo.getRectangle().y, (int) rmo.getRectangle().width, (int) rmo.getRectangle().height);
                blocks.add(rect);
            } else if(object instanceof PolylineMapObject) {
            	PolylineMapObject plo = (PolylineMapObject) object;
                
                float[] vertices = plo.getPolyline().getTransformedVertices();
                
                int polisX[] = new int[vertices.length / 2];
                int polisY[] = new int[vertices.length / 2];

                for (int i = 0; i < vertices.length / 2; ++i) {
                    polisX[i] = (int) vertices[i * 2];
                    polisY[i] = (int) vertices[i * 2 + 1];
                }
                
                java.awt.Polygon poly = new java.awt.Polygon(polisX, polisY, vertices.length / 2);
                blocks.add(poly);
            }
        }
		
		return blocks;
	}
	
	private void createChests()
	{
		MapObjects objects = getCurrentMap().getLayers().get("chests").getObjects();

        for (MapObject object : objects) {
        	if(object.getProperties().containsKey("chestID")) {
        		TextureRegion texture = AssetsManager.getWorldSprite("closed_chest");
        		EntityManager.addChest(new Chest(texture,
											 (Float) object.getProperties().get("x"),
											 (Float) object.getProperties().get("y") - texture.getRegionHeight(),
											 (Integer) object.getProperties().get("chestID")));
        	}
        }
	}
	
	/**
	 * <p>Devuelve el mapa que está actualmente cargado.</p>
	 * @return	TiledMap
	 */
	public TiledMap getCurrentMap() 
	{
		return map;
	}
	
	public BatchTiledMapRenderer getTiled() 
	{
		return tiled;
	}
	
	public RayHandler getRayHandler() 
	{
		return rays;
	}

	public void dispose() {
		map.dispose();
		tiled.dispose();
		rays.dispose();
	}

}
