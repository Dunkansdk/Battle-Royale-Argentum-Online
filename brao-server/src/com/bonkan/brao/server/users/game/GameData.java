package com.bonkan.brao.server.users.game;

import com.bonkan.brao.server.utils.Position;

public class GameData {

	private Position pos;
	private int hp, mana;
	// etc etc
	
	public GameData(Position pos, int hp, int mana)
	{
		this.pos = pos;
		this.hp = hp;
		this.mana = mana;
	}
	
	public Position getPos()
	{
		return pos;
	}
	
	public int getHP()
	{
		return hp;
	}
	
	public int getMana()
	{
		return mana;
	}
}
