package com.bonkan.brao.server.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONManager {

	private static JSONArray itemsJSON;
	
	public static void init()
	{
		JSONObject obj = JSONParser.parse("data/items/items.json");
		itemsJSON = obj.getJSONArray("items");
	}
	
	public static String getItemName(int id)
	{
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (String) aux.get("name");
		}
		
		return null;
	}
	
	public static String getItemAtlasName(int id)
	{
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (String) aux.get("atlas_name");
		}
		
		return null;
	}
}
