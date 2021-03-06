package com.bonkan.brao.state;

import com.bonkan.brao.Game;
import com.bonkan.brao.state.app.LoadingState;
import com.bonkan.brao.state.app.LobbyState;
import com.bonkan.brao.state.app.LoginState;
import com.bonkan.brao.state.app.PlayState;

import java.util.Stack;

/**
 * <p>Maneja los estados del juego (lobby, login, jugando, etc)</p>
 * <p>Usa una {@link java.util.Stack Pila} para el manejo de los estados.</p>
 */
public class GameStateManager {

    private final Game app;

    private Stack<AbstractGameState> states;

    public enum State {
        LOGIN,
        PLAY,
        LOBBY,
        LOADING
    }

    public GameStateManager(final Game app) {
        this.app = app;
        this.states = new Stack<AbstractGameState>();
        this.setState(State.LOADING);
    }

    public Game getApp() {
        return app;
    }

    public void update(float delta)
    {
        states.peek().update(delta);
    }

    public void resize(int width, int height)
    {
        states.peek().resize(width, height);
    }

    public void render()
    {
        states.peek().render();
    }

    public void dispose()
    {
        for(AbstractGameState gs : states) {
            gs.dispose();
        }
        states.clear();
    }

    public void setState(State state)
    {
        if(states.size() >= 1) {
            states.pop().dispose();
        }
        states.push(this.getState(state));
    }
    
    public AbstractGameState getCurrentState()
    {
    	return states.peek();
    }

    /**
     * <p>No confundir con {@link com.bonkan.brao.state.GameStateManager#getCurrentState getCurrentState()},
     * este m�todo devuelve una NUEVA INSTANCIA del estado solicitado.</p>
     * @param state		&emsp;<b>State (enum privado de esta clase)</b> el estado solicitado
     * @return	AbstractGameState
     */
    private AbstractGameState getState(State state)
    {
        switch (state) {
            case LOGIN:
                return new LoginState(this);
            case PLAY:
                return new PlayState(this);
            case LOBBY:
                return new LobbyState(this);
            case LOADING:
            	return new LoadingState(this);
        }
        return null;
    }
}
