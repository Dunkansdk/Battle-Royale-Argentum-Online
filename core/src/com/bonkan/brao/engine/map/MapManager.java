package com.bonkan.brao.engine.map;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.exception.BRAOException;

public class MapManager {
	
	private ArrayList<TiledMap> map;
    private OrthogonalTiledMapRenderer tiled;
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
		
		System.out.println("Maps: " + map.size());
		
		load(world, 1); // ESTAMOS CARGANDO POR DEFECTO!
	}
	
	/**
	 * Setea un {@link com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer} en base a los mapas precargados del directorio "maps/"
	 * @param actual	&emsp;<b>int</b> subindice del mapa
	 * @throws BRAOException
	 */
	public void load(World world, int actual) {
		currentMap = actual;
		tiled = new OrthogonalTiledMapRenderer(getCurrentMap());
		createCollision(world);
	}
	
	
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

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bodyDef);
            body.createFixture(shape, 1);

            shape.dispose();
        }
    }
	
	/**
	 * Asumimos que si se seteo un currentMap es porque realmente existe.
	 * @return	TiledMap
	 */
	public TiledMap getCurrentMap() {
		return map.get(currentMap);
	}
	
	public OrthogonalTiledMapRenderer getTiled() {
		return tiled;
	}
	

	public void dispose() {
		map.clear();
		tiled.dispose();
	}

}
