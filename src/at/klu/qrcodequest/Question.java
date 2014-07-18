package at.klu.qrcodequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Messna on 17.07.2014.
 */
public class Question {
    private int id, nodePk, active, sequence, dtEvaluation;
    private String name, description, option1, option2, option3, option4, option5, option6, option7, option8, option9, option10;
    private URL url;

    public Question(int id, int nodePk, int active, int sequence, int dtEvaluation, String name, String description, String option1, String option2, String option3, String option4, String option5, String option6, String option7, String option8, String option9, String option10) {
        this.id = id;
        this.nodePk = nodePk;
        this.active = active;
        this.sequence = sequence;
        this.dtEvaluation = dtEvaluation;
        this.name = name;
        this.description = description;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
        this.option6 = option6;
        this.option7 = option7;
        this.option8 = option8;
        this.option9 = option9;
        this.option10 = option10;

//        getQuestions();
    }

    public Question() {
        getQuestionData(id);
    }

    public Question(int active, int seq, int dtEval, String name, String descr, String o1, String o2, String o3, String o4, String o5, String o6, String o7, String o8, String o9, String o10) {

    }

    public void getQuestionData(int id) {
        JSONObject obj;
        String questionsString = HTTPHelper.makeGetRequest("http://192.168.136.81?id=" + id).toString();
        try {
            obj = new JSONObject(questionsString);
            active = obj.getInt("active");
            nodePk = obj.getInt("nodePk");
            sequence = obj.getInt("sequence");
            dtEvaluation = obj.getInt("dtEvaluation");
            name = obj.getString("name");
            description = obj.getString("description");
            option1 = obj.getString("option1");
            option2 = obj.getString("option2");
            option3 = obj.getString("option3");
            option4 = obj.getString("option4");
            option5 = obj.getString("option5");
            option6 = obj.getString("option6");
            option7 = obj.getString("option7");
            option8 = obj.getString("option8");
            option9 = obj.getString("option9");
            option10 = obj.getString("option10");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}

