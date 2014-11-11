package at.klu.qrcodequest;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class BestlistActivity extends Activity {

ArrayList<Score>scores = new ArrayList<Score>();
int questPk = 0;

Context context;

TextView text1;
TextView text2;
TextView text3;
TextView text4;
TextView title;

ProgressBar bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bestlist);
		AppDown.register(this);
		
		context = this;
		
		bar = (ProgressBar) findViewById(R.id.progressBar1);
		
		Bundle bundle = getIntent().getExtras();
		questPk = bundle.getInt("questPk");
		
		
		//Titel in die Tabelle einf�gen
//        TableLayout layout = (TableLayout) findViewById(R.id.table);
//        title = new TextView(this);
//		title.setText("Bestenliste");
//		title.setTextSize(16);
//		title.setPadding(0, 0, 0, 15);
//		title.setTypeface(Typeface.DEFAULT_BOLD);
//		title.setTextColor(Color.parseColor("#FF0000"));
//		title.setGravity(Gravity.CENTER_HORIZONTAL);
//		layout.addView(title);
		
		new bestlistTask().execute();
		

		
	}
	
public void setRows(ArrayList<Score>scores){
    	
    	TableLayout layout = (TableLayout) findViewById(R.id.table);
    	int length = scores.size(); //Länge der Array List abfragen
    	
    	
    	for (int x = 0; x < length; x++){
    		
    		TableRow row = new TableRow(this); //Erstellen einer neuen Reihe
    		TableRow.LayoutParams layoutp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
    		layoutp.setMargins(2, 2, 2, 2);
    		row.setLayoutParams(layoutp);
    		
    		
    		text1 = new TextView(this);
    		text2 = new TextView(this);
    		text3 = new TextView(this);
    		text4 = new TextView(this);
    		
//    		Drawable hintergrund = getResources().getDrawable(R.drawable.bestenliste_style); //
    		
    		//textViews erhalten einen Hintergrund mit schwarzem Rahmen f�r die Tabellenansicht
    		text1.setBackgroundResource(R.drawable.bestenliste_row1);
    		text2.setBackgroundResource(R.drawable.bestenliste_style2);
    		text3.setBackgroundResource(R.drawable.bestenliste_style2);
    		text4.setBackgroundResource(R.drawable.bestenliste_style2);
    		
    		
//    		text1.setPadding(15, 0, 0, 0);
//    		text2.setPadding(15, 0, 0, 0);
//    		text3.setPadding(15, 0, 0, 0);
//    		text4.setPadding(15, 0, 0, 0);
    		
//    		text1.setBackgroundColor(Color.parseColor("#FF0000"));
//    		text2.setBackgroundColor(Color.parseColor("#FF0000"));
//    		text3.setBackgroundColor(Color.parseColor("#FF0000"));
//    		text4.setBackgroundColor(Color.parseColor("#FF0000"));
    		
    		String firstname = scores.get(x).getFirstname();
    		String lastname = scores.get(x).getLastname();
    		int score = scores.get(x).getScore();
    		String nickname = scores.get(x).getNickname();
    		
    		text3.setText(firstname);
    		text2.setText(lastname);
    		text4.setText("" + score);
    		text1.setText(nickname);
    		
    		
    		row.addView(text1);//einer Reihe wird ein textView hinzugfügt
    		row.addView(text2);
    		row.addView(text3);
    		row.addView(text4);
    		
//    		int i = x+1;
    		
    		layout.addView(row,x); //Reihe wird zum TableLayout hinzugefügt
    		
    		
    	}
    	
    }

public class bestlistTask extends AsyncTask<Void, Void, Void>{

	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		bar.setVisibility(View.VISIBLE);
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		try {
			scores = QuestMethods.getScore(questPk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Handler handler = new Handler(context.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                setRows(scores); 
//            }
//        });
        
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		bar.setVisibility(View.INVISIBLE);
		setRows(scores); 
	}
}
	
}
