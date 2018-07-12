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
	public static final int PACKET_TRY_OPEN_CHEST = 7;
	public static final int PACKET_CHEST_OPENED = 8;
	public static final int PACKET_ITEM_THROWN = 9;
	public static final int PACKET_PLAYER_REQUEST_GET_ITEM = 10;
	public static final int PACKET_PLAYER_CONFIRM_GET_ITEM = 11;
	public static final int PACKET_REMOVE_ITEM_FROM_FLOOR = 12;
	public static final int PACKET_USER_IN_AREA_EQUIPPED_ITEM = 13;
	public static final int PACKET_PLAYER_REQUEST_FULL_BODY = 14;
	public static final int PACKET_PLAYER_SEND_FULL_BODY = 15;
	public static final int PACKET_USER_ENTERED_PLAYER_AREA = 16;
	public static final int PACKET_PLAYER_REQUEST_UNEQUIP_ITEM = 17;
	public static final int PACKET_PLAYER_CONFIRM_UNEQUIP_ITEM = 18;
	public static final int PACKET_PLAYER_REMOVE_POTION = 19;
	public static final int PACKET_PLAYER_REQUEST_UNEQUIP_SPELL = 20;
	public static final int PACKET_PLAYER_CONFIRM_UNEQUIP_SPELL = 21;
	
}
