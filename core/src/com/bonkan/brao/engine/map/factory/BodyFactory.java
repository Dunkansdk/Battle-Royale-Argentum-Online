package com.bonkan.brao.engine.map.factory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * <p>Fábrica de {@link com.badlogic.gdx.physics.box2d.Body bodies} de box2d.</p>
 */
public class BodyFactory {
	
	// Filters
	public static final short BIT_WALL = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SENSOR = 4;
    public static final short BIT_NOLIGHT = 8;
    public static final short BIT_BREAKABLE = 16;

    /**
     * <p>Crea el {@link com.badlogic.gdx.physics.box2d.Body} de un player.</p>
     * @param data		&emsp;{@link java.lang.Object Object} instancia que crea el body
     * @return {@link com.badlogic.gdx.physics.box2d.Body}
     */
    public static Body createPlayerBox(final World world, float x, float y, int width, int height, Object data) 
    {
    	Body pBody;
        BodyDef bodyDef = new BodyDef(); 

        // Siempre va a ser dinamico un player
		bodyDef.type = BodyDef.BodyType.DynamicBody;
        
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        pBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.filter.categoryBits = BIT_PLAYER; // Es un...
		fixtureDef.filter.maskBits = 0; // Collisiona contra...
		fixtureDef.filter.groupIndex = 0;
        
        // Asociamos la instancia que lo crea como UserData (Para acceder a los datos desde el WorldContactListener)
        pBody.createFixture(fixtureDef).setUserData(data);
        
        /*
        FixtureDef SensorLeft = getSensorFixture(2, height / 2, new Vector2(-width / 2 - 1, 0));
        FixtureDef SensorRight = getSensorFixture(2, height / 2, new Vector2(width / 2 + 1, 0));
        FixtureDef SensorUp = getSensorFixture(width / 2, 2, new Vector2(0, height / 2 + 1));
        FixtureDef SensorDown = getSensorFixture(width / 2, 2, new Vector2(0, -height / 2 - 1));
        
        if(data instanceof Player) {
        	pBody.createFixture(SensorLeft).setUserData(((Player)data).getSensor(0));
            pBody.createFixture(SensorRight).setUserData(((Player)data).getSensor(1));
            pBody.createFixture(SensorUp).setUserData(((Player)data).getSensor(2));
            pBody.createFixture(SensorDown).setUserData(((Player)data).getSensor(3));
        }*/
        
        shape.dispose();
        return pBody;
    }
    
    /*
    private static FixtureDef getSensorFixture(float width, float height, Vector2 position) {
    	
    	PolygonShape shapeSensor = new PolygonShape();
        shapeSensor.setAsBox(width, height, position, 0);
        
        FixtureDef sensorDef = new FixtureDef();
        sensorDef.shape = shapeSensor;
        sensorDef.filter.categoryBits = BIT_SENSOR;
        sensorDef.filter.maskBits = BIT_PLAYER | BIT_WALL | BIT_SENSOR;
        sensorDef.filter.groupIndex = 0;
        sensorDef.isSensor = true;
        
        return sensorDef;
    }*/
    
    /**
     * <p>Crea un objeto en el mapa (Los obtiene de el editor Tiled).</p>
     * @param {@link com.bonkan.brao.engine.map.factory.ShapeFactory} Shape que crea el MapManager dependiendo de que se haya creado en el Tiled
     * @param data		&emsp;{@link java.lang.Object Object} instancia que crea el bodyInstancia que crea el body
     * @return {@link com.badlogic.gdx.physics.box2d.Body}
     */
    public static Body createMapBox(final World world, Shape shape, boolean isStatic, boolean fixedRotation, Object data) {
    	Body pBody;
		BodyDef bodyDef = new BodyDef();
		bodyDef.fixedRotation = fixedRotation;
		
		if(isStatic) {
			bodyDef.type = BodyDef.BodyType.StaticBody;
		} else {
			bodyDef.type = BodyDef.BodyType.DynamicBody;
		}
		
		pBody = world.createBody(bodyDef);  
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.filter.categoryBits = BIT_WALL; // Es un...
		fixtureDef.filter.maskBits = 0; // Collisiona contra...
		fixtureDef.filter.groupIndex = 0;
		fixtureDef.density = 0f;
		
		// Asociamos la instancia que lo crea como UserData (Para acceder a los datos desde el WorldContactListener)
		pBody.createFixture(fixtureDef).setUserData(data);
		
		return pBody;
	}

}