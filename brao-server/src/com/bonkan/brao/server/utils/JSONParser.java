package com.bonkan.brao.server.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class JSONParser {

	public static JSONObject parse(String path)
	{
		String content;
	    
		try {
			content = readFile(path, StandardCharsets.UTF_8);
			
			// Convert JSON string to JSONObject
		    return new JSONObject(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}
	
	private static String readFile(String path, Charset encoding) throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
	
}
