package at.klu.qrcodequest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button btscan;
	EditText textField1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btscan = (Button) findViewById(R.id.weiter);
		textField1 = (EditText) findViewById(R.id.editText1);
		

		//in some trigger function e.g. button press within your code you should add:
		btscan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					
					//Es wird eine neues Intent aufgerufen (QR-Code Reader)
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");//für das Intent können zusätzliche Optionen gesetzt werden
					startActivityForResult(intent, 0); //Starten der Activity, die ein Ergebnis (Result) zurückliefert
				
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "ERROR:" + e, Toast.LENGTH_SHORT).show();

				}

			}
		});

	}
	//In the same activity youï¿½ll need the following to retrieve the results:
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {		//RequestCode dient zu identifizierung der Activity, die das Ergebnis liefert

			if (resultCode == RESULT_OK) {	
				textField1.setText(intent.getStringExtra("SCAN_RESULT")); //das Scan-Result wird ausgegeben (Daten von gelesen QR-Code)
			} else if (resultCode == RESULT_CANCELED) {
				
			}
		}
	}

}