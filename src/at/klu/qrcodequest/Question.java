package at.klu.qrcodequest;

import java.net.MalformedURLException;
import java.net.URL;

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
        try {
            url = new URL("http://192.168.136.81");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HTTPHelper.makeGetRequest(url);
        HTTPHelper.makePostRequest(url, "asd");
    }

    public Question() {
        System.out.println("asd");
        try {
            url = new URL("http://192.168.136.81");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HTTPHelper.makeGetRequest(url);


    }



}

