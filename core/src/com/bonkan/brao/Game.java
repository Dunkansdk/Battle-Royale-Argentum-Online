package com.bonkan.brao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.networking.LoggedUser;
import com.bonkan.brao.networking.Packet;
import com.bonkan.brao.networking.PacketIDs;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;
import com.bonkan.brao.state.app.LoginState;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * <p>Clase principal.</p>
 */
public class Game extends ApplicationAdapter {

	public boolean DEBUG = false;

	// Game information
	public static final String TITLE = "Battle Royale AO";
	public static final float SCALE = 1.0f;
	public static final int V_WIDTH = 1280;
	public static final int V_HEIGHT = 720;

	private OrthographicCamera camera;
	private OrthographicCamera hud;
	private GameStateManager gameState;
	private SpriteBatch batch;
	private SpriteBatch hudBatch;
	private Client client;

	private LoggedUser loggedUser; // cuando loguea un user
	private boolean serverOffline;
	
	@Override
	public void create () {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		// instanciamos el cliente de KRYO
		client = new Client(); // el consturctor de client puede recibir un buffersize, si hay errores probablemente sean por paquetes muy grandes, hay que tocar aca
		
		loggedUser = null;
		serverOffline = false;
		
		AssetsManager.init();
		AssetsManager.loadAssets();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width / SCALE, height / SCALE);
		hud = new OrthographicCamera();
		hud.setToOrtho(false, width / SCALE, height / SCALE);
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		gameState = new GameStateManager(this);
		
		// inicializa el nuevo thread
		new Thread(client).start(); // hay que hacerlo asi xq de la otra forma se finaliza -.-
		
	    // registramos las clases a usar
	    Kryo kryo = client.getKryo();
	    kryo.register(Packet.class);
	    kryo.register(ArrayList.class);
	    kryo.register(String.class);
	    kryo.register(UUID.class);

	    try {
	    	// 5000 es la cant. maxima de milisegundos que se bloquea el thread tratando de conectar
	    	client.connect(5000, "127.0.0.1", 7666, 7667); //190.191.191.51

	    	client.addListener(new Listener() {
		        public void received (Connection connection, Object object) {
		        	if (object instanceof Packet) {
		        		handleData((Packet) object, connection);
		        	}
		        }
		    });
		    
		} catch (IOException e) {
			serverOffline = true;
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		hudBatch.setProjectionMatrix(hud.combined);
		gameState.update(Gdx.graphics.getDeltaTime());
		gameState.render();
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) DEBUG = !DEBUG;
	}

	@Override
	public void resize(int width, int height) {
		gameState.resize((int)(width / SCALE), (int)(height / SCALE));
	}

	@Override
	public void dispose() {
		gameState.dispose();
		batch.dispose();
		hudBatch.dispose();
		client.close();
		AssetsManager.dispose();
	}
	
	// para la llegada de paquetes (despues lo movemos a otro lado??)
	public void handleData(Packet p, Connection conn)
	{
		final AbstractGameState ags;
		
		switch(p.getID())
		{
			case PacketIDs.PACKET_LOGIN_SUCCESS:
				loggedUser = new LoggedUser(UUID.fromString(p.getArgs().get(0)), p.getArgs().get(1), Integer.valueOf(p.getArgs().get(2)), Integer.valueOf(p.getArgs().get(3)), Integer.valueOf(p.getArgs().get(4)), Integer.valueOf(p.getArgs().get(5)), Integer.valueOf(p.getArgs().get(6)));
				break;
				
			case PacketIDs.PACKET_LOGIN_FAILED:
				ags = gameState.getCurrentState();
				
				if(ags instanceof LoginState)
					((LoginState) ags).setErrorLabelText("Nickname or password invalid.");
				break;
				
			default:
				loggedUser.addIncomingData(p);
				break;
		}
	}

	public OrthographicCamera getCamera() 
	{
		return camera;
	}
	
	public OrthographicCamera getHud() {
		return hud;
	}
	
	public void setCamera(OrthographicCamera camera) 
	{
		this.camera = camera;
	}

	public SpriteBatch getBatch() 
	{
		return batch;
	}
	
	public SpriteBatch getHudBatch() {
		return hudBatch;
	}
	
	public Client getClient() 
	{
		return client;
	}
	
	public boolean isLogged() 
	{
		return loggedUser != null;
	}
	
	public boolean serverOffline()
	{
		return serverOffline;
	}
	
	public LoggedUser getLoggedUser()
	{
		return loggedUser;
	}
}
