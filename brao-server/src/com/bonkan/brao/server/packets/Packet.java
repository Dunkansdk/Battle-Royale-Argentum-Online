package com.bonkan.brao.server.packets;
import java.util.ArrayList;

/**
 * <p>Clase principal para la comunicaci�n cliente-servidor. El paquete est� compuesto por
 * un {@link java.lang.Object objeto} que puede ser cualquier clase <b>REGISTRADA</b> por 
 * {@link com.esotericsoftware.kryo.Kryo Kryo} y una serie de argumentos adicionales que
 * se agrupan en un {@link java.util.ArrayList ArrayList} de <b>strings</b>.</p>
 */
public class Packet {

	private Object data; // informaci�n a enviar
	private int id; // identificador del paquete
	private ArrayList<String> args; // argumentos adicionales
	
	// constructor default para la registraci�n
	public Packet()
	{
	}
	
	public Packet(int id, Object data, ArrayList<String> args)
	{
		this.id = id;
		this.data = data;
		this.args = args;
	}
	
	public int getID()
	{
		return id;
	}
	
	public Object getData()
	{
		return data;
	}
	
	public ArrayList<String> getArgs()
	{
		return args;
	}
	
}
