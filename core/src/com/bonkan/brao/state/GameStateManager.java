package com.bonkan.brao.state;

import com.bonkan.brao.Game;
import com.bonkan.brao.state.app.LobbyState;
import com.bonkan.brao.state.app.LoginState;
import com.bonkan.brao.state.app.OptionState;
import com.bonkan.brao.state.app.PlayState;

import java.util.Stack;

public class GameStateManager {

    private final Game app;

    private Stack<AbstractGameState> states;

    public enum State {
        LOGIN,
        PLAY,
        OPTION,
        LOBBY
    }

    public GameStateManager(final Game app) {
        this.app = app;
        this.states = new Stack<AbstractGameState>();
        //this.setState(State.PLAY);
        this.setState(State.LOGIN);
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

    private AbstractGameState getState(State state)
    {
        switch (state) {
            case LOGIN:
                return new LoginState(this);
            case PLAY:
                return new PlayState(this);
            case LOBBY:
                return new LobbyState(this);
            case OPTION:
                return new OptionState(this);
        }
        return null;
    }
}
