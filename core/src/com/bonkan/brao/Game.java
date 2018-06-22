package com.bonkan.brao;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bonkan.brao.networking.Packet;
import com.bonkan.brao.networking.PacketIDs;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;
import com.bonkan.brao.state.GameStateManager.State;
import com.bonkan.brao.state.app.LoginState;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Main Class
 */
public class Game extends ApplicationAdapter {

	public boolean DEBUG = false;

	// Game information
	public static final String TITLE = "Battle Royale AO";
	public static final float SCALE = 1.0f;
	public static final int V_WIDTH = 1280;
	public static final int V_HEIGHT = 720;

	private OrthographicCamera camera;
	private GameStateManager gameState;
	private SpriteBatch batch;
	private Client client;
	private AssetManager assets;
	
	private boolean isLogged; // si se logueo el user

	@Override
	public void create () {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		isLogged = false;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width / SCALE, height / SCALE);
		batch = new SpriteBatch();
		gameState = new GameStateManager(this);
		
		// instanciamos el cliente y tratamos de conectar
		// esto también se hace en otro thread
		client = new Client(); // el constrctor de client puede recibir un buffersize, si hay errores probablemente sean por paquetes muy grandes, hay que tocar aca
		
		// inicializa el nuevo thread
		new Thread(client).start(); // hay que hacerlo asi xq de la otra forma se finaliza -.-
		
	    // registramos las clases a usar
	    Kryo kryo = client.getKryo();
	    kryo.register(Packet.class);
	    kryo.register(ArrayList.class);
	    
	    try {
	    	// 5000 es la cant. maxima de milisegundos que se bloquea el thread tratando de conectar
			client.connect(5000, "127.0.0.1", 7666, 54777);

		    client.addListener(new Listener() {
		        public void received (Connection connection, Object object) {
		        	if (object instanceof Packet) {
		        		handleData((Packet) object, connection);
		        	}
		        }
		    });

		    ArrayList<String> arguments = new ArrayList<String>();
		    arguments.add("asdasd");
		    
		    Packet request = new Packet(1, "bon3", arguments); // las clases que se mandan como "object" del packet tambien tienen que estar registradas
		    client.sendTCP(request);
		    
		} catch (IOException e) {
			//e.printStackTrace();
	
			AbstractGameState ags = gameState.getCurrentState();
			
			if(ags instanceof LoginState)
				((LoginState) ags).setErrorLabelText("Server offline.");
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameState.update(Gdx.graphics.getDeltaTime());
		gameState.render();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}

	@Override
	public void resize(int width, int height) {
		gameState.resize((int)(width / SCALE), (int)(height / SCALE));
	}

	@Override
	public void dispose () {
		gameState.dispose();
		batch.dispose();
	}
	
	// para la llegada de paquetes (despues lo movemos a otro lado??)
	public void handleData(Packet p, Connection conn)
	{
		switch(p.getID())
		{
			case PacketIDs.PACKET_LOGIN_SUCCESS:
				isLogged = true;
				break;
				
			case PacketIDs.PACKET_LOGIN_FAILED:
				AbstractGameState ags = gameState.getCurrentState();
				
				if(ags instanceof LoginState)
					((LoginState) ags).setErrorLabelText("Nickname or password invalid.");
				break;
		}
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
	
	public Client getClient() {
		return client;
	}
	
	public boolean isLogged() {
		return isLogged;
	}
}
