package at.klu.qrcodequest;

import java.util.ArrayList;

import org.json.JSONException;

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
	String result;
	int questPk;
	int nodePk = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppDown.register(this);

		btscan = (Button) findViewById(R.id.weiter);
		textField1 = (EditText) findViewById(R.id.editText1);
		Bundle bundle = getIntent().getExtras();
		questPk = bundle.getInt("questPk");
		
		Toast.makeText(getApplicationContext(), "" + questPk, Toast.LENGTH_LONG).show();
		

		//in some trigger function e.g. button press within your code you should add:
		btscan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				
				
				
				try {
					
					//Es wird eine neues Intent aufgerufen (QR-Code Reader)
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");//es zusätzliche Optionen gesetzt werden
					startActivityForResult(intent, 0); //Starten der Activity, die ein Ergebnis (Result) zurückliefert
					
					
					
				
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "ERROR:" + e, Toast.LENGTH_SHORT).show();

				}

			}
		});

	}
	//In the same activity you�ll need the following to retrieve the results:
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {		//RequestCode dient zu identifizierung der Activity, die das Ergebnis liefert

			if (resultCode == RESULT_OK) {
				
				result = intent.getStringExtra("SCAN_RESULT");
				System.out.println("" + result);
				textField1.setText(result); //das Scan-Result wird ausgegeben (Daten von gelesen QR-Code)
//				if(NodeMethodes.checkUserQuestNode == true){
//					Toast.makeText(this.getApplicationContext(), "Sie haben diesen Quest bereits abgeschlossen", Toast.LENGTH_LONG).show();
//				}else{
//				Intent questions = new Intent (getApplicationContext(), QuestionsActivity.class);
//				startActivity(questions);
				ArrayList<Node> nodes = new ArrayList<Node>();
				
				try {
					nodes = QuestsMethodes.getNodes(questPk);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("" + nodes.get(0));
				System.out.println("" + nodes.get(1));
				System.out.println("" + nodes.get(2));
				System.out.println("" + nodes.get(3));
				
				
				for (int i = 0; i < nodes.size(); i++){
						
					System.out.println("asdfasdfassfasddfasdfasddfasfafsdf");
					
					if(nodes.get(i).getRegistrationTarget1().equals(result)){
							nodePk = nodes.get(i).getId();
							System.out.println("" + nodePk);
							
							
					Intent questions = new Intent(getApplicationContext(), QuestionsActivity.class);
						
							questions.putExtra("nodePk", nodePk);
							questions.putExtra("questPk", questPk);
							
							startActivity(questions);		
						}
					}
//			}
					
			} else if (resultCode == RESULT_CANCELED) {
			}
		}
	}

}