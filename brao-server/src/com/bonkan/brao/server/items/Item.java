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
	private int amount; // solo es distinto de 1 si son potas
	
	public Item(UUID id, int x, int y, int index, int rarity, int amount)
	{
		this.id = id;
		this.pos = new Position(x, y);
		this.index = index;
		this.rarity = rarity;
		this.amount = amount;
	}
	
	public int getAmount()
	{
		return amount;
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
