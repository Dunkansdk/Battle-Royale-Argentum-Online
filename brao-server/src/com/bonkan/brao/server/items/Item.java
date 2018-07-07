package com.bonkan.brao.server.items;

import java.util.UUID;

import com.bonkan.brao.server.utils.Position;

public class Item {

	private Position pos;
	private UUID id;
	
	public Item(UUID id, int x, int y)
	{
		this.id = id;
		this.pos = new Position(x, y);
	}
	
	public UUID getID()
	{
		return id;
	}
	
	public Position getPos()
	{
		return pos;
	}

}
