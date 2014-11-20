package at.klu.qrcodequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ProgressBar;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class QuestActivity extends Activity /*implements OnItemClickListener*/ {

    ExpandableListView list;
    ProgressBar bar;
    ExpandableListAdapter adapter;
    private ArrayList<Quest> quests = new ArrayList<>();
    private int userPk;
    private int finished = 0;
    SparseBooleanArray userQuestMap = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        Bundle bundle = getIntent().getExtras();
        userPk = bundle.getInt("userPk");
        
        AppDown.register(this);

        bar = (ProgressBar) findViewById(R.id.marker_progress);
        bar.setVisibility(View.VISIBLE);

//        list.setOnItemClickListener(this);

        list = (ExpandableListView) findViewById(R.id.listView1);

        getQuests();
//        new QuestTask().execute();


        
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

    private void getQuests() {
        String url = "http://193.171.127.102:8080/Quest/quest.json";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject quest = response.getJSONObject(i);
                                String name = quest.getString("name");
                                int id = quest.getInt("id");
                                int dtRegistration = quest.getInt("dtRegistration");
                                Quest quest1 = new Quest(id, name, dtRegistration);
                                quests.add(quest1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getUserQuests();
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println(error);
                    }
                });
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private void getUserQuests() {

        for (final Quest quest : quests) {
            String url = ("http://193.171.127.102:8080/Quest/userQuest/get?userPk=" + userPk + "&questPk=" + quest.getId());

            JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    boolean existing = !response.toString().equals("[]"); // Wenn die Antwort [] ist -> false
                    userQuestMap.put(quest.getId(), existing);
                    finished++;
                    if (finished == quests.size()) { // Wenn alle Requests abgearbeitet sind
                        drawList();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    System.out.println(error);
                }
            });
            VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsObjRequest);
        }
    }

    private void drawList() {
        ArrayList<String> values = new ArrayList<>();

        for (Quest quest : quests) {
            values.add(quest.getName()); //speichert die Namen der Quest in die ArrayList
        }

        adapter = new ExpandableListAdapter(getApplicationContext(), values, quests, userQuestMap, userPk);
        list.setAdapter(adapter);

        bar.setVisibility(View.INVISIBLE);
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


