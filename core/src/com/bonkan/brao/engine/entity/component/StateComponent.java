package com.bonkan.brao.engine.entity.component;

import com.badlogic.ashley.core.Component;

/**
 * Sabemos si el usuario se esta moviendo, esta quieto, esta muerto, esta golpeando.
 *
 */
public class StateComponent implements Component {
	public static final int STATE_NORMAL = 0;
	public static final int STATE_MOVING = 1;
	public static final int STATE_HIT = 2;
	public static final int STATE_DIE = 3;
	
	private int state = 0;
    public float time = 0.0f;
    public boolean isLooping = false;

    public void set(int newState){
        state = newState;
        time = 0.0f;
    }

    public int get(){
        return state;
    }
}
