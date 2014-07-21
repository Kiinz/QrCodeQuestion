package at.klu.qrcodequest;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QuestsMethodes {

	public static ArrayList<Quest> getQuestsfromJSONString() throws JSONException{
		
		
    	ArrayList <Quest> quests = new ArrayList<Quest>();
    	
    	JSONObject obj = new JSONObject(HTTPHelper.makeGetRequest("http://192.168.136.84/quests.html").toString());
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
}
