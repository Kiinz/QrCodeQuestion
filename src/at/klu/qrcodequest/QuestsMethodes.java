package at.klu.qrcodequest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QuestsMethodes {


	public static ArrayList<Quest> getQuestsfromJSONString() throws JSONException{
		
		
    	ArrayList <Quest> quests = new ArrayList<Quest>();
    	
    	JSONObject obj = new JSONObject(HTTPHelper.makeGetRequest("http://pastebin.com/raw.php?i=DsQuFG6c").toString());
    	JSONArray array = obj.getJSONArray("Quests");
    	
    	for (int i = 0; i < array.length(); i++){
    		
    		JSONObject quest = array.getJSONObject(i);
    		
    		String description = quest.getString("description");
    		String name = quest.getString("name");
    		int id = quest.getInt("id");
    		int active = quest.getInt("active");
    		int sequence = quest.getInt("sequence");
    		int dtOwner = quest.getInt("dtOwner");
    		int dtRegistration = quest.getInt("dtRegistration");
    		
    		
    		Quest quest1 = new Quest(id, active, sequence,dtOwner,dtRegistration,name,description);
    		quests.add(quest1);
	}
    	return quests;
}
	
	public static ArrayList<Quest> getQuests() throws JSONException{
		
		ArrayList<Quest> quests = new ArrayList<Quest>();
		
		String json = "{Quests:" + HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/quest.json").toString() + "}";
		System.out.println("" + json);
		
		
		JSONObject obj = new JSONObject(json);
		JSONArray array = obj.getJSONArray("Quests");
		
		for (int i = 0; i < array.length(); i++){
			JSONObject quest = array.getJSONObject(i);
			String name = quest.getString("name");
			Quest quest1 = new Quest(name);
			quests.add(quest1);
			
		}
		System.out.println("" + json);
		return quests;
	}
public static ArrayList<Node> getNodes(int questPk) throws JSONException{
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		String json = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/quest/show/" + questPk + ".json").toString() +"]}";
//		System.out.println("" + json);
		
		JSONObject obj = new JSONObject(json);
		JSONArray array = obj.getJSONArray("nodes");
		
		for (int i = 0; i < array.length(); i++){
			JSONObject node = array.getJSONObject(i);
			int id = node.getInt("id");
			
			
			Node node1 = new Node(id);
			
			
			String jsonn = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/node/show/" + id + ".json").toString() +"]}";
//			System.out.println("" + jsonn);
			
			JSONObject obj2 = new JSONObject(jsonn);
			
			node1.setDescription(obj2.getString("description"));
			node1.setName(obj2.getString("name"));
			node1.setRegistrationTarget1(obj2.getString("registrationTarget1"));
			node1.setRegistrationTarget2(obj2.getString("registrationTarget2"));
			node1.setLocation(obj2.getString("location"));
			node1.setActive(obj2.getBoolean("active"));
			node1.setSequence(obj2.getInt("sequence"));
			node1.setDtRegistration(obj2.getInt("dtRegistration"));
			
			nodes.add(node1);
			
			
		}
		return nodes;
	}
}