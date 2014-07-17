package at.klu.qrcodequest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class QuestActivity extends Activity implements OnItemClickListener {

	ListView list;
	QuestCustomAdapter adapter;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_quest);
	        
	        list = (ListView)findViewById(R.id.listView1);
	        list.setOnItemClickListener(this);
	        
	        ArrayList<String> values = new ArrayList<String>();
	        
	        values.add("HTL-Mössingerstraße");
	        values.add("FH-Kärnten");
	        values.add("Hauptschule Neumarkt");
	        values.add("HTL-Mössingerstraße");
	        values.add("FH-Kärnten");
	        values.add("Hauptschule Neumarkt");
	        values.add("HTL-Mössingerstraße");
	        values.add("FH-Kärnten");
	        values.add("Hauptschule Neumarkt");
	        values.add("HTL-Mössingerstraße");
	        values.add("FH-Kärnten");
	        values.add("Hauptschule Neumarkt");
	        
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
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        if (id == R.id.action_settings) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int iposition = position;
			String itemValue = (String) list.getItemAtPosition(position);
			
			Intent qrreader = new Intent (getApplicationContext(), MainActivity.class);
			startActivity(qrreader);
			
//			Toast.makeText(getApplicationContext(), "" + iposition + " " + itemValue, Toast.LENGTH_LONG).show();
		}
		
	}


