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
		if(id < 0 || id > itemsJSON.length()) return null;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (String) aux.get("name");
		}
		
		return null;
	}
	
	public static String getItemAtlasName(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return null;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (String) aux.get("atlas_name");
		}
		
		return null;
	}
	
	public static String getItemAnimAtlasName(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return null;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (String) aux.get("anim_atlas_name");
		}
		
		return null;
	}
	
	public static int getItemType(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return -1;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (Integer) aux.get("item_type");
		}
		
		return -1;
	}
	
	public static int getItemManaCost(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return -1;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (Integer) aux.get("mana_cost");
		}
		
		return -1;
	}
	
	public static String getItemDesc(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return null;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (String) aux.get("desc");
		}
		
		return null;
	}
	
	public static int getItemMinDamage(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return -1;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (Integer) aux.get("min_dmg");
		}
		
		return -1;
	}
	
	public static int getItemMaxDamage(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return -1;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (Integer) aux.get("max_dmg");
		}
		
		return -1;
	}
	
	public static int getItemSpellDamage(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return -1;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (Integer) aux.get("spell_damage");
		}
		
		return -1;
	}
	
	public static int getItemSpellResistance(int id)
	{
		if(id < 0 || id > itemsJSON.length()) return -1;
		
		if(itemsJSON.getJSONObject(id) != null)
		{
			JSONObject aux = itemsJSON.getJSONObject(id);
			
			return (Integer) aux.get("spell_resistance");
		}
		
		return -1;
	}
}
