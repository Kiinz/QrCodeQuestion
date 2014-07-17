package at.klu.qrcodequest;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class BestlistActivity extends Activity {

ArrayList<Daten>daten = new ArrayList<Daten>();
	
	
	Daten data1 = new Daten ("Alexander","Kainz","Kiinz44",12);
	Daten data2 = new Daten ("Messner","Dominik","Messi",100);
	Daten data3 = new Daten ("Hans","Koch","Kochl",32);
	Daten data4 = new Daten ("Franz","Swatolav","Franzi1",45);
	Daten data5 = new Daten ("Ignaz","Maier","Igi",66);
	Daten data6 = new Daten ("Ignaz","Maier","Igi",66);
	Daten data7 = new Daten ("Alexander","Kainz","Kiinz44",12);
	Daten data8 = new Daten ("Messner","Dominik","Messi",100);
	Daten data9 = new Daten ("Hans","Koch","Kochl",32);
	Daten data10 = new Daten ("Franz","Swatolav","Franzi1",45);
	Daten data11 = new Daten ("Ignaz","Maier","Igi",66);
	Daten data12 = new Daten ("Alexander","Kainz","Kiinz44",12);
	Daten data13 = new Daten ("Messner","Dominik","Messi",100);
	Daten data14 = new Daten ("Hans","Koch","Kochl",32);
	Daten data15 = new Daten ("Franz","Swatolav","Franzi1",45);
	Daten data16 = new Daten ("Ignaz","Maier","Igi",66);
	Daten data17 = new Daten ("Alexander","Kainz","Kiinz44",12);
	Daten data18 = new Daten ("Messner","Dominik","Messi",100);
	Daten data19 = new Daten ("Hans","Koch","Kochl",32);
	Daten data20 = new Daten ("Franz","Swatolav","Franzi1",45);
	Daten data21 = new Daten ("Ignaz","Maier","Igi",66);
	Daten data22 = new Daten ("Alexander","Kainz","Kiinz44",12);
	Daten data23 = new Daten ("Messner","Dominik","Messi",100);
	Daten data24 = new Daten ("Hans","Koch","Kochl",32);
	Daten data25 = new Daten ("Franz","Swatolav","Franzi1",45);
	Daten data26 = new Daten ("Ignaz","Maier","Igi",66);
	Daten data27 = new Daten ("Alexander","Kainz","Kiinz44",12);
	Daten data28 = new Daten ("Messner","Dominik","Messi",100);
	Daten data29 = new Daten ("Hans","Koch","Kochl",32);
	Daten data30 = new Daten ("Franz","Swatolav","Franzi1",45);
	Daten data31 = new Daten ("Ignaz","Maier","Igi",66);
	Daten data32 = new Daten ("Alexander","Kainz","Kiinz44",12);
	Daten data33 = new Daten ("Messner","Dominik","Messi",100);
	Daten data34 = new Daten ("Hans","Koch","Kochl",32);
	Daten data35 = new Daten ("Franz","Swatolav","Franzi1",45);

	
	TextView text1;
	TextView text2;
	TextView text3;
	TextView text4;
	TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bestlist);
		
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
		
		
        daten.add(data1);
        daten.add(data2);
        daten.add(data3);
        daten.add(data4);
        daten.add(data5);
        daten.add(data6);
        daten.add(data7);
        daten.add(data8);
        daten.add(data9);
        daten.add(data10);
        daten.add(data11);
        daten.add(data12);
        daten.add(data13);
        daten.add(data14);
        daten.add(data15);
        daten.add(data16);
        daten.add(data17);
        daten.add(data18);
        daten.add(data19);
        daten.add(data20);
        daten.add(data21);
        daten.add(data22);
        daten.add(data23);
        daten.add(data24);
        daten.add(data25);
        daten.add(data26);
        daten.add(data27);
        daten.add(data28);
        daten.add(data29);
        daten.add(data30);
        daten.add(data31);
        daten.add(data32);
        daten.add(data33);
        daten.add(data34);
        daten.add(data35);
        
        
        setRows(daten);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bestlist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
	
public void setRows(ArrayList<Daten>daten){
    	
    	TableLayout layout = (TableLayout) findViewById(R.id.table);
    	int length = daten.size();
    	
    	
    	for (int x = 0; x < length; x++){
    		
    		TableRow row = new TableRow(this);
    		TableRow.LayoutParams layoutp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
    		layoutp.setMargins(2, 2, 2, 2);
    		row.setLayoutParams(layoutp);
    		
    		
    		text1 = new TextView(this);
    		text2 = new TextView(this);
    		text3 = new TextView(this);
    		text4 = new TextView(this);
    		
    		Drawable hintergrund = getResources().getDrawable(R.drawable.bestenliste_style);
    		
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
    		
    		String vorname = daten.get(x).getVorname();
    		String nachname = daten.get(x).getNachname();
    		int punkte = daten.get(x).getPunkte();
    		String benutzer = daten.get(x).getBenutzername();
    		
    		text3.setText(vorname);
    		text2.setText(nachname);
    		text4.setText("" + punkte);
    		text1.setText("" + benutzer);
    		
    		
    		row.addView(text1);
    		row.addView(text2);
    		row.addView(text3);
    		row.addView(text4);
    		
//    		int i = x+1;
    		
    		layout.addView(row,x);
    		
    		
    	}
    	
    }
	
	
}
