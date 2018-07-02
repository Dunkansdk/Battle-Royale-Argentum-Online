package com.bonkan.brao.engine.map;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.map.factory.BodyFactory;
import com.bonkan.brao.engine.map.factory.ShapeFactory;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 *<p>Carga todos los mapas del directiorio "maps/" y crea un {@link com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer OrthogonalTiledMapRenderer}
 * para el mapa específico que se va a mostrar.</p>
 */
public class MapManager {
	
	private ArrayList<TiledMap> map;
    private OrthogonalTiledMapRenderer tiled;
    private RayHandler rays;
    private int currentMap;
	
    /**
     * <p>Carga los mapas desde el directorio /maps/</p>
     * <p><b>Atencion: Por defecto carga el primer mapa que se cargo!</b></p>
     */
	public MapManager(World world) {
		
		map = new ArrayList<TiledMap>();
   
		FileHandle dirHandle = Gdx.files.internal("maps/");
		
		// Recorre la carpeta maps y devuelve los archivos
		for (FileHandle entry: dirHandle.list()) {
			
			if(entry.extension().compareTo("tmx") == 0) {
				map.add(new TmxMapLoader().load("" + entry.toString()));
				System.out.println("Agregue: " + entry.toString());
			}

		}
		
		rays = new RayHandler(world);
		RayHandler.setGammaCorrection(true);     // enable or disable gamma correction
		RayHandler.useDiffuseLight(true);
		this.rays.setAmbientLight(0.4f, 0.4f, 0.4f, 0.1f);
		this.rays.setBlur(true);           // enabled or disable blur
		this.rays.setBlurNum(1);           // set number of gaussian blur passes
		this.rays.setShadows(true);        // enable or disable shadow
		this.rays.setCulling(true);        // enable or disable culling
		
		System.out.println("Maps: " + map.size());

		load(world,  1); // ESTAMOS CARGANDO POR DEFECTO!
	}
	
	/**
	 * Setea un {@link com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer OrthogonalTiledMapRenderer} 
	 * en base a los mapas precargados del directorio "maps/"
	 * @param actual	&emsp;<b>int</b> subindice del mapa (en el ArrayList de mapas)
	 */
	public void load(World world, int actual) {
		currentMap = actual;
		tiled = new OrthogonalTiledMapRenderer(getCurrentMap());
		createCollision(world);
		createLights();
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

            BodyFactory.createMapBox(world, shape, true, false, this);

            shape.dispose();
        }
    }
	
	/**
	 * <p>Crea las luces !!! (las carga de la capa "lights" del .tmx).</p>
	 */
	private void createLights() {
		MapObjects objects = getCurrentMap().getLayers().get("lights").getObjects();
		
		for(MapObject object : objects) {
			if(object.getProperties().get("angle") == null) {
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
	 * <p>Devuelve el mapa que está actualmente cargado.</p>
	 * @return	TiledMap
	 */
	public TiledMap getCurrentMap() 
	{
		return map.get(currentMap);
	}
	
	public OrthogonalTiledMapRenderer getTiled() {
		return tiled;
	}
	
	public RayHandler getRayHandler() {
		return rays;
	}

	public void dispose() {
		map.clear();
		tiled.dispose();
	}

}
