package com.bonkan.brao.networking;

/**
 * <p>Un humilde homenaje al viejo y querido <b>ClientPacketIDs</b> :')</p>
 */
public class PacketIDs {

	// paquetes generales
	public static final int PACKET_LOGIN = 0;
	public static final int PACKET_LOGIN_SUCCESS = 1;
	public static final int PACKET_LOGIN_FAILED = 2;
	
	// paquetes de PARTIDA
	public static final int PACKET_PLAYER_CHANGED_STATE = 3;
	public static final int PACKET_USER_CHANGED_STATE = 4;
	public static final int PACKET_PLAYER_MOVED = 5;
	public static final int PACKET_USER_MOVED = 6;
	public static final int PACKET_USER_ENTERED_AREA = 7;
	public static final int PACKET_TRY_OPEN_CHEST = 8;
	public static final int PACKET_CHEST_OPENED = 9;
}
