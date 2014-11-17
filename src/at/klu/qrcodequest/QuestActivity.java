package at.klu.qrcodequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ProgressBar;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class QuestActivity extends Activity /*implements OnItemClickListener*/ {

    ExpandableListView list;
    ProgressBar bar;
    ExpandableListAdapter adapter;
    private ArrayList<Quest> quests = new ArrayList<Quest>();
    private int userPk;
    private String errorString = "";
    SparseBooleanArray userQuestMap = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        Bundle bundle = getIntent().getExtras();
        userPk = bundle.getInt("userPk");
        
        AppDown.register(this);

        bar = (ProgressBar) findViewById(R.id.marker_progress);
        
//        list.setOnItemClickListener(this);

        list = (ExpandableListView) findViewById(R.id.listView1);
        new QuestTask().execute();
        
        list.setOnGroupExpandListener(new OnGroupExpandListener(){

			@Override
			public void onGroupExpand(int groupPosition) {
				for(int i = 0; i < quests.size(); i++){
					if(list.isGroupExpanded(i)){
						if(i != groupPosition){
							list.collapseGroup(i);
						}
						
					}
				}
			}
        	
        });


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

                for (Quest quest : quests) {
                    userQuestMap.put(quest.getId(), QuestMethods.getUserQuest(userPk, quest.getId()));
                    System.out.println("" + userPk + "   " + QuestMethods.getUserQuest(userPk, quest.getId()));
                }
                
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                if (e.getMessage().equals("falseStatusCode")) {
                    //Im Backgroundtask können keine UI-Methoden aufgerufen werden
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
            
            adapter = new ExpandableListAdapter(getApplicationContext(), values, quests, userQuestMap, userPk);
            list.setAdapter(adapter);

            bar.setVisibility(View.INVISIBLE);
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        String itemValue = (String) list.getItemAtPosition(position);
//
//
//			if(quests.get(position).getDtRegistration() == 2){
//
//
//        Intent qrreader = new Intent(getApplicationContext(), MainActivity.class);
//        qrreader.putExtra("questPk", quests.get(position).getId());
//        startActivity(qrreader);
//
//			}

//			Toast.makeText(getApplicationContext(), "" + position + " " + itemValue, Toast.LENGTH_LONG).show();
//    }

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


