package at.klu.qrcodequest;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Messna on 17.07.2014.
 */
public class Question {
    private int id, nodePk, active, sequence, dtEvaluation;
    private String name, description, option1, option2, option3, option4, option5, option6, option7, option8, option9, option10;

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

        getdata();
    }

    public Question() {
        System.out.println("asd");
        getdata();
    }


    private void getdata() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.
                    ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL("http://192.168.136.81");
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            readStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readStream(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

