package com.bonkan.brao.engine.input;

import com.badlogic.gdx.Input;

public class KeyBindings {

	public static final int KEY_MOVE_DOWN = Input.Keys.DOWN;
	public static final int KEY_MOVE_LEFT = Input.Keys.LEFT;
	public static final int KEY_MOVE_RIGHT = Input.Keys.RIGHT;
	public static final int KEY_MOVE_UP = Input.Keys.UP;
	
	public static final int KEY_ACTION = Input.Keys.E;
	
	public static final int KEY_RED_POTION = Input.Keys.Q;
	public static final int KEY_BLUE_POTION = Input.Keys.R;
	
	public static String getKeyText(int code)
	{
		switch(code)
		{
			case Input.Keys.DOWN:
				return "AD";

			case Input.Keys.LEFT:
				return "AL";
				
			case Input.Keys.RIGHT:
				return "AR";
				
			case Input.Keys.UP:
				return "AU";
		
			case Input.Keys.A:
				return "A";
				
			case Input.Keys.B:
				return "B";
				
			case Input.Keys.C:
				return "C";
				
			case Input.Keys.D:
				return "D";
				
			case Input.Keys.E:
				return "E";
				
			case Input.Keys.F:
				return "F";
				
			case Input.Keys.G:
				return "G";
				
			case Input.Keys.H:
				return "H";
				
			case Input.Keys.I:
				return "I";
				
			case Input.Keys.J:
				return "J";
				
			case Input.Keys.K:
				return "K";
				
			case Input.Keys.L:
				return "L";
				
			case Input.Keys.M:
				return "M";
				
			case Input.Keys.N:
				return "N";
				
			case Input.Keys.O:
				return "O";
				
			case Input.Keys.P:
				return "P";
				
			case Input.Keys.Q:
				return "Q";
				
			case Input.Keys.R:
				return "R";
		
			case Input.Keys.S:
				return "S";
				
			case Input.Keys.T:
				return "T";
				
			case Input.Keys.U:
				return "U";
				
			case Input.Keys.V:
				return "V";
				
			case Input.Keys.W:
				return "W";
				
			case Input.Keys.X:
				return "X";
				
			case Input.Keys.Y:
				return "Y";
				
			case Input.Keys.Z:
				return "Z";
				
			case Input.Keys.NUM_0:
				return "0";
				
			case Input.Keys.NUM_1:
				return "1";
				
			case Input.Keys.NUM_2:
				return "2";
				
			case Input.Keys.NUM_3:
				return "3";
				
			case Input.Keys.NUM_4:
				return "4";
				
			case Input.Keys.NUM_5:
				return "5";
				
			case Input.Keys.NUM_6:
				return "6";
				
			case Input.Keys.NUM_7:
				return "7";
				
			case Input.Keys.NUM_8:
				return "8";
				
			case Input.Keys.NUM_9:
				return "9";
				
			case Input.Keys.NUMPAD_0:
				return "NP0";
				
			case Input.Keys.NUMPAD_1:
				return "NP1";
				
			case Input.Keys.NUMPAD_2:
				return "NP2";
				
			case Input.Keys.NUMPAD_3:
				return "NP3";
				
			case Input.Keys.NUMPAD_4:
				return "NP4";
				
			case Input.Keys.NUMPAD_5:
				return "NP5";
				
			case Input.Keys.NUMPAD_6:
				return "NP6";
				
			case Input.Keys.NUMPAD_7:
				return "NP7";
				
			case Input.Keys.NUMPAD_8:
				return "NP8";
				
			case Input.Keys.NUMPAD_9:
				return "NP9";

			case Input.Keys.F1:
				return "F1";
				
			case Input.Keys.F2:
				return "F2";
				
			case Input.Keys.F3:
				return "F3";
				
			case Input.Keys.F4:
				return "F4";
				
			case Input.Keys.F5:
				return "F5";
				
			case Input.Keys.F6:
				return "F6";
				
			case Input.Keys.F7:
				return "F7";
				
			case Input.Keys.F8:
				return "F8";
				
			case Input.Keys.F9:
				return "F9";
				
			case Input.Keys.F10:
				return "F10";
			
			case Input.Keys.F11:
				return "F11";
				
			case Input.Keys.F12:
				return "F12";
		}
		
		return "";
	}
}
