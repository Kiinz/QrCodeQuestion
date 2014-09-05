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
			int id = quest.getInt("id");
			Quest quest1 = new Quest(id, name);
			quests.add(quest1);
			
		}
		System.out.println("" + json);
		return quests;
	}
public static ArrayList<Node> getNodes(int questPk) throws JSONException{
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		String json = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/quest/show/" + questPk + ".json").toString();
		
//		System.out.println("" + json);
		
		JSONObject obj = new JSONObject(json);
		JSONArray array = obj.getJSONArray("nodes");

		
		for (int i = 0; i < array.length(); i++){
			JSONObject node = array.getJSONObject(i);
			int id = node.getInt("id");
			
			
			Node node1 = new Node(id);
			node1.setDescription(node.getString("description"));
			node1.setName(node.getString("name"));
//			node1.setRegistrationTarget1(node.getString("registrationTarget1"));
//			node1.setRegistrationTarget2(node.getString("registrationTarget2"));
			node1.setLocation(node.getString("location"));
//			node1.setActive(node.getBoolean("active"));
			node1.setSequence(node.getInt("sequence"));
			node1.setDtRegistration(node.getInt("dtRegistration"));
			
			String jsonn = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/node/show/" + id + ".json").toString();
			System.out.println("" + jsonn);
			
			JSONObject obj2 = new JSONObject(jsonn);
			
			
            node1.setQuestionIDs(obj2.getJSONArray("questions"));
			
			nodes.add(node1);
			
			
		}
		return nodes;
	}
}