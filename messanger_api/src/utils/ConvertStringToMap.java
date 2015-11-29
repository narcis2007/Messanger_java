package utils;

import java.util.HashMap;
import java.util.Map;

public class ConvertStringToMap {

	public static Map<String,String> convert(String value){
		value = value.substring(1, value.length()-1);           //remove curly brackets
		String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
		Map<String,String> map = new HashMap<>();               

		for(String pair : keyValuePairs)                        //iterate over the pairs
		{
		    String[] entry = pair.split("=");                   //split the pairs to get key and value 
		    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
		}
		return map;
	}
	
	//de facut: o functie care imi converteste un string intr-o lista de dictionare
}
