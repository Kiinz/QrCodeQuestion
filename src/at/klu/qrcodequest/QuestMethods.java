package at.klu.qrcodequest;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QuestMethods {


    public static ArrayList<Quest> getQuests() throws JSONException, IOException {
		
		ArrayList<Quest> quests = new ArrayList<Quest>();

        String json;
        json = "{Quests:" + HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/quest.json") + "}";
        System.out.println("" + json);
		
		
		JSONObject obj = new JSONObject(json);
		JSONArray array = obj.getJSONArray("Quests");
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject quest = array.getJSONObject(i);
			String name = quest.getString("name");
			int id = quest.getInt("id");
			int dtRegistration = quest.getInt("dtRegistration");
			System.out.println("" + dtRegistration);
			Quest quest1 = new Quest(id, name, dtRegistration);
			quests.add(quest1);
			
		}
		System.out.println("" + json);
		return quests;
	}

    public static ArrayList<Node> getNodes(int questPk) throws JSONException, IOException {

        ArrayList<Node> nodes = new ArrayList<Node>();

        String json;
        json = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/quest/show/" + questPk + ".json");

//		System.out.println("" + json);

        JSONObject obj = new JSONObject(json);
        JSONArray array = obj.getJSONArray("nodes");


        for (int i = 0; i < array.length(); i++) {
            JSONObject node = array.getJSONObject(i);
            int id = node.getInt("id");


            Node node1 = new Node(id);
            node1.setDescription(node.getString("description"));
            node1.setName(node.getString("name"));
            node1.setRegistrationTarget1(node.getString("registrationTarget1"));
            node1.setRegistrationTarget2(node.getString("registrationTarget2"));
            node1.setLocation(node.getString("location"));
//			node1.setActive(node.getBoolean("active"));
            node1.setSequence(node.getInt("sequence"));
            node1.setDtRegistration(node.getInt("dtRegistration"));

            String jsonn;
            jsonn = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/node/show/" + id + ".json");
            System.out.println("" + jsonn);

            JSONObject obj2 = new JSONObject(jsonn);


            node1.setQuestionIDs(obj2.getJSONArray("questions"));

            nodes.add(node1);
        }
        return nodes;
    }
    
    public static ArrayList<Score> getScore(int questPk) throws IOException, JSONException{
    	
    	ArrayList<Score> scores = new ArrayList<Score>();
    	
    	String json = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/userQuest/scores?questPk=" + questPk);
    	
    	JSONArray array = new JSONArray(json);
    	
    	for (int i = 0; i < array.length(); i++){
    		JSONObject scoreObj = array.getJSONObject(i);
    		JSONObject userObj = scoreObj.getJSONObject("user");
    		
    		int score = scoreObj.getInt("score");
    		String firstname = userObj.getString("firstname");
    		String lastname = userObj.getString("lastname");
    		String nickname = userObj.getString("nickname");
    		
    		
    		Score score1 = new Score(firstname, lastname, nickname, score);
    		
    		scores.add(score1);
    	}
    	
    	return scores;
    }
    
    public static void setUserQuest(int userId, int questId) throws JSONException, IOException{
    	
    	JSONObject user = new JSONObject();
    	
    	user.put("id", userId );
    	
    	JSONObject quest = new JSONObject();
    	
    	quest.put("id", questId);
    	
    	JSONObject userquest = new JSONObject();
    	
    	userquest.put("dtState", 1);
    	
    	userquest.put("user", user);
    	userquest.put("quest", quest);
    	
    	HTTPHelper.makeJSONPost("http://193.171.127.102:8080/Quest/userQuest/save.json", userquest.toString());
    	
    	System.out.println("" + userquest);
    }
}