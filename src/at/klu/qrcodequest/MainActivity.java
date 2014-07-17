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
				// TODO Auto-generated method stub

				try {
					
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 0);
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "ERROR:" + e, 1).show();

				}

			}
		});

	}
	//In the same activity you’ll need the following to retrieve the results:
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
				textField1.setText(intent.getStringExtra("SCAN_RESULT"));
			} else if (resultCode == RESULT_CANCELED) {
				
			}
		}
	}

}