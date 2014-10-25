package at.klu.qrcodequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class QuestActivity extends Activity implements OnItemClickListener {

    ListView list;
    ProgressBar bar;
    QuestCustomAdapter adapter;
    ArrayList<Quest> quests = new ArrayList<Quest>();
    private String errorString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);


        AppDown.register(this);

        bar = (ProgressBar) findViewById(R.id.marker_progress);
        list = (ListView) findViewById(R.id.listView1);
        list.setOnItemClickListener(this);

        new QuestTask().execute();


    }

    private class QuestTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                quests = QuestMethods.getQuests(); //Einlesen der Quest
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                if (e.getMessage().equals("falseStatusCode")) {
                    errorString = "falseStatusCode";
                } else {
                    errorString="networkError";
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            HTTPHelper.HTTPExceptionHandler(errorString, QuestActivity.this);

            ArrayList<String> values = new ArrayList<String>();

            for (Quest quest : quests) {
                values.add(quest.getName()); //speichert die Namen der Quest in die ArrayList
            }

            adapter = new QuestCustomAdapter(getApplicationContext(), R.layout.row, values);
            list.setAdapter(adapter);

            bar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quest, menu);
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String itemValue = (String) list.getItemAtPosition(position);


//			if(quests.get(position).getDtRegistration() == 2){


        Intent qrreader = new Intent(getApplicationContext(), MainActivity.class);
        qrreader.putExtra("questPk", quests.get(position).getId());
        startActivity(qrreader);

//			}

//			Toast.makeText(getApplicationContext(), "" + position + " " + itemValue, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Programm beenden");
        builder.setMessage("Wollen sie das Programm wirklich beenden?");

        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppDown.allDown();

            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}


