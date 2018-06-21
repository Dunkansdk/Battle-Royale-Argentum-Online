package com.bonkan.brao.state.app;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;

import networking.Packet;
import networking.PacketIDs;

public class LoginState extends AbstractGameState {

	private Stage stage;
	private Skin skin;
	private BitmapFont defaultFont;
	private TextField username;
	private TextField password;
	private TextButton connect;
	
    public LoginState(GameStateManager gameState) {
        super(gameState);
        
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("segoeui.ttf"));
 		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
 		parameter.size = 14;
 		defaultFont = freeTypeFontGenerator.generateFont(parameter);

 		skin = new Skin();
 		skin.add("default-font", defaultFont, BitmapFont.class);
 		FileHandle fileHandle = Gdx.files.internal("uiskin.json");
 		FileHandle atlasFile = fileHandle.sibling("uiskin.atlas");

 		if (atlasFile.exists()) {
 		    skin.addRegions(new TextureAtlas(atlasFile));
 		}

 		skin.load(fileHandle);
        
        username = new TextField("", skin);
        username.setBounds(50, 100, 100, 30);
        password = new TextField("", skin);
        password.setBounds(50, 60, 100, 30);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        
        connect = new TextButton("Conectar", skin);
        connect.setBounds(50, 40, 80, 20);
        
        connect.addListener(new ClickListener() {
        	public void clicked(InputEvent e, float x, float y)
			{
        		ArrayList<String> args = new ArrayList<String>();
        		args.add(username.getText());
        		args.add(password.getText());
        		
        		// HARDCODED! le mandamos el paquete
				app.getClient().sendTCP(new Packet(PacketIDs.PACKET_LOGIN, "", args));
			}
        });
        
        stage.addActor(username);
        stage.addActor(password);
        stage.addActor(connect);
    }

    @Override
    public void update(float delta) {
    	stage.act(delta);
    }

    @Override
    public void render() {
    	stage.draw();
    }

    @Override
    public void dispose() {
    	stage.dispose();
    }
}
