package com.bonkan.brao.server.items;

import java.util.UUID;

import com.bonkan.brao.server.utils.Position;

/**
 * <p>Clase que maneja todos los items tirados en el piso de la partida.</p>
 */
public class Item {

	private Position pos;
	private UUID id;
	private int index; // indice del item en el JSON
	private int rarity;
	
	public Item(UUID id, int x, int y, int index, int rarity)
	{
		this.id = id;
		this.pos = new Position(x, y);
		this.index = index;
		this.rarity = rarity;
	}
	
	public int getRarity()
	{
		return rarity;
	}
	
	public int getIndex()
	{
		return index;
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
