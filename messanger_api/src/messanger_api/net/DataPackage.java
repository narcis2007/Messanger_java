package messanger_api.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ConvertStringToMap;

public class DataPackage {

	//private Map<String, String> data;		//fac list<map> sa mearga pentru obiecte multiple
	private List<Map<String, String>> data;
	private Map<String,String> header=new HashMap<>();
	
	public DataPackage(){
		data=new ArrayList<>();
		header.put("app", "messenger");
	}
	
	public void addData(Map<String, String> map) {
		this.data.add(map);
	}
	
	public void setHeader(Map<String, String> map) {
		this.header=map;
	}
	
	/*@Override
	public String toString() {
		String stringData=header.toString();
		for(Map<String,String> mapData:data)
			stringData=stringData.concat('\n' +mapData.toString());
		return stringData;
	}
	 */
	public List<Map<String, String>> getData() {
		return data;
	}
	
	public void writeTo(OutputStream os) throws IOException {	//fac ca sa scrie obiecte multiple
		String stringData=header.toString();
		for(Map<String,String> mapData:data)
			stringData=stringData.concat('\n' +mapData.toString());
		
		os.write(stringData.getBytes());
		//os.write(data.toString().getBytes());
    }

    public void readFrom(InputStream is) throws IOException {							//fac ca sa citeasca obiecte multiple
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        	String string=br.readLine();
        	System.out.println(string);
        	header=ConvertStringToMap.convert(string);
        	string=br.readLine();
            while(string!=null){
            	//System.out.println(string);
            	data.add(ConvertStringToMap.convert(string));
            	string=br.readLine();
        		
            }
        }
    }

	public Map<String, String> getHeader() {
		return header;
	}
	

}
