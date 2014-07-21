package at.klu.qrcodequest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NodeMethodes {
	
public static ArrayList<Node> getNodesfromJSONString() throws JSONException{
		
		
    	ArrayList <Node> nodes = new ArrayList<Node>();
    	
    	JSONObject obj = new JSONObject(HTTPHelper.makeGetRequest("http://192.168.136.84/nodes.html").toString());
    	JSONArray array = obj.getJSONArray("Nodes");
    	
    	for (int i = 0; i < array.length(); i++){
    		
    		JSONObject node = array.getJSONObject(i);
    		
    		String description = node.getString("description");
    		String name = node.getString("name");
    		String location = node.getString("location");
    		int questPk = node.getInt("questPk");
    		int active = node.getInt("active");
    		int sequence = node.getInt("sequence");
    		int dtRegistration = node.getInt("dtRegistration");
    		int id = node.getInt("id");
    		
    		
    		
    		
    		Node quest1 = new Node(id, questPk, active, sequence, dtRegistration, name, description, location);
    		nodes.add(quest1);
	}
    	return nodes;
}
}


