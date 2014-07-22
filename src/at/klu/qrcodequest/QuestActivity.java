package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import org.json.JSONException;

import java.util.ArrayList;


public class QuestActivity extends Activity implements OnItemClickListener {

	ListView list;
	QuestCustomAdapter adapter;
	ArrayList<Quest> quests = new ArrayList<Quest>();
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_quest);
	        AppDown.register(this);
	        
	        list = (ListView)findViewById(R.id.listView1);
	        list.setOnItemClickListener(this);
	        
	        try {
				quests = QuestsMethodes.getQuestsfromJSONString(); //Einlesen der Quest
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        ArrayList<String> values =  new ArrayList<String>();

            for (Quest quest : quests) {
                values.add(quest.getName()); //speichert die Namen der Quest in die ArrayList
            }
	        
//	        values.add("HTL-M�ssingerstra�e");
//	        values.add("FH-K�rnten");
//	        values.add("Hauptschule Neumarkt");
//	        values.add("HTL-M�ssingerstra�e");
//	        values.add("FH-K�rnten");
//	        values.add("Hauptschule Neumarkt");
//	        values.add("HTL-M�ssingerstra�e");
//	        values.add("FH-K�rnten");
//	        values.add("Hauptschule Neumarkt");
//	        values.add("HTL-M�ssingerstra�e");
//	        values.add("FH-K�rnten");
//	        values.add("Hauptschule Neumarkt");
	        
	        //ArrayAdapter<String>data = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1, values);
	        
	        adapter = new QuestCustomAdapter(getApplicationContext(),R.layout.row, values);
	        list.setAdapter(adapter);
	        
	        Intent intent = adapter.getIntent();
	       
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
	
			
			if(quests.get(position).getDtRegistration() == 2){
				
				Intent qrreader = new Intent (getApplicationContext(), MainActivity.class);
				startActivity(qrreader);
				
			}
			
//			Toast.makeText(getApplicationContext(), "" + position + " " + itemValue, Toast.LENGTH_LONG).show();
		}
		
	}


