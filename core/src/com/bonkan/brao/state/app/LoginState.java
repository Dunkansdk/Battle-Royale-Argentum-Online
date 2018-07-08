package com.bonkan.brao.state.app;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.networking.Packet;
import com.bonkan.brao.networking.PacketIDs;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;

/**
 * <p>Estado "Login".</p>
 */
public class LoginState extends AbstractGameState {

	private Stage stage;
	private Skin skin;
	private TextField username;
	private TextField password;
	private TextButton connect;
	private Label label;
	
	private double labelTimer; // para que se borre despues de un toque
	private final int LABEL_TIME = 3000; // en 3 segundos desaparece
	
    public LoginState(GameStateManager gameState) {
        super(gameState);

        labelTimer = 0;
        
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //skin = new Skin();
        skin = AssetsManager.getDefaultSkin();
        
        username = new TextField("", skin);
        username.setBlinkTime(0.7f);
        username.setMessageText("username");
        username.setBounds(stage.getWidth() / 2 - 75, stage.getHeight() - 200, 150, 25);
        
        username.addListener(new InputListener() {
        	@Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(keycode == Keys.ENTER)
                    connect();
				return false;
        	}
        });
        
        password = new TextField("", skin);
        password.setBlinkTime(0.7f);
        password.setMessageText("password");
        password.setBounds(stage.getWidth() / 2 - 75, stage.getHeight() - 230, 150, 25);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        
        password.addListener(new InputListener() {
        	@Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(keycode == Keys.ENTER)
                    connect();
				return false;
        	}
        });
        
        connect = new TextButton("Conectar", skin);
        connect.setBounds(stage.getWidth() / 2 - 40, stage.getHeight() - 280, 80, 25);
        
        connect.addListener(new ClickListener() {
        	public void clicked(InputEvent e, float x, float y)
        	{
        		connect();
        	}
        });
        
        label = new Label("", skin);
        label.setBounds(stage.getWidth() / 2 - 75, stage.getHeight() - 310, 150, 25);
        label.setAlignment(Align.center);
        
        SelectBox<String> test = new SelectBox<String>(skin);
        test.setItems("Clerigo", "Bardo", "Mago", "Druida", "Paladin", "Asesino");
        test.setBounds(stage.getWidth() / 2 - 40, stage.getHeight() - 350, 80, 25);
        
        stage.addActor(test);
        stage.addActor(username);
        stage.addActor(password);
        stage.addActor(connect);
        stage.addActor(label);
    }

    private void connect()
    {
    	ArrayList<String> args = new ArrayList<String>();
		args.add(username.getText());
		args.add(password.getText());
		
		app.getClient().sendTCP(new Packet(PacketIDs.PACKET_LOGIN, "", args));
    }
    
    public void setErrorLabelText(String text)
    {
    	label.setColor(Color.RED);
    	label.setText(text);
    	labelTimer = System.currentTimeMillis();
    }
    
    @Override
    public void update(float delta) {
    	stage.act(delta);
    	
    	// si esta logueado desde el app cambiamos el estado
    	if(app.isLogged())
    		gameState.setState(GameStateManager.State.PLAY);
    	
    	if(labelTimer != 0 && labelTimer + LABEL_TIME < System.currentTimeMillis())
    	{
    		labelTimer = 0;
    		label.setText("");
    	}
    }

    @Override
    public void render() {
    	stage.draw();
    }

    @Override
    public void dispose() {
    	/*stage.dispose();
    	skin.dispose();
    	defaultFont.dispose();*/
    }
}
