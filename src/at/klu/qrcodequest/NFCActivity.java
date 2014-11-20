package at.klu.qrcodequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


import java.util.ArrayList;

import org.json.JSONException;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupExpandListener;


public class NFCActivity extends Activity {
	
	public final String MIME_TEXT_PLAIN = "text/plain";
	
	private Context context;
	private PendingIntent mPendingIntent;
	private IntentFilter[] intentFilter;
	private NfcAdapter nfcAdapter;
	private String[][] mNFCTechLists;
	
	private int questPk = 0;
	private int nodePk = 0;
	private int dtRegistration = 3; //NFC_dtRegistration = 3
	private ArrayList<Node> nodes;
	private String errorString="";
	private int userPk;
	
	private ExpandableListViewNodes adapter;
	private ExpandableListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		
		
		AppDown.register(this); // Methode für das Beenden der Applikation
		
		Bundle bundle = getIntent().getExtras();
		questPk = bundle.getInt("questPk");
		userPk = bundle.getInt("userPk");
		
		list = (ExpandableListView) findViewById(R.id.listView1);
		new MainNodeTask().execute();
		
		list.setOnGroupExpandListener(new OnGroupExpandListener(){

			@Override
			public void onGroupExpand(int groupPosition) {
				for(int i = 0; i < nodes.size(); i++){
					if(list.isGroupExpanded(i)){
						if(i != groupPosition){
							list.collapseGroup(i);
						}
						
					}
				}
			}
        	
        });
		
		context = this;
		//Laden des NFC Adapters. Wird verwendet um die Verfügbarkeit zu überprüfen
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		//Abfrage, ob das Gerät NFC unterstützt
		if(nfcAdapter == null){
			Toast.makeText(getApplicationContext(), "This device doesn't support NFC", Toast.LENGTH_LONG).show();
		}else{
			//Abfrage, ob NFC eingeschaltet ist
			if(!nfcAdapter.isEnabled()){
				enableNFC(); //Starten eines Allert Dialogs, um NFC einzuschalten
			}
		}
		
		// create an intent with tag data and deliver to this activity
				mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
						getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		// set an intent filter for all MIME data
				IntentFilter ndefIntent = new IntentFilter(
						NfcAdapter.ACTION_NDEF_DISCOVERED);
				try {
					ndefIntent.addDataType("*/*");
					intentFilter = new IntentFilter[] { ndefIntent };
				} catch (Exception e) {
					Log.e("TagDispatch", e.toString());
				}
				mNFCTechLists = new String[][] { new String[] { Ndef.class.getName() } };
				
	}
	
	private void enableNFC(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Einstellungen");
		builder.setMessage("NFC is not enabled. Please go to the wireless settings to enable it");

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (android.os.Build.VERSION.SDK_INT >= 16) {
					Intent intent = new Intent(
							android.provider.Settings.ACTION_NFC_SETTINGS);
					context.startActivity(intent);
				} else {
					startActivity(new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				}
			}
		});
		builder.setNegativeButton("Abbrechen",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		AppDown.register(this);
		nfcAdapter.enableForegroundDispatch(this, mPendingIntent, intentFilter, mNFCTechLists);
	}

	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		handleIntent(intent);
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(nfcAdapter != null){
			nfcAdapter.disableForegroundDispatch(this);
		}
		
	}
	
	private void handleIntent(Intent intent) {
		System.out.println("Hier");
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			String type = intent.getType();
			System.out.println("" + type);
			if (MIME_TEXT_PLAIN.equals(type)) {

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				new NdefReaderTask().execute(tag);

			} else {
				Log.d("Tag", "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

			// In case we would still use the Tech Discovered Intent
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();

			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					new NdefReaderTask().execute(tag);
					break;
				}
			}
		}
	}
	
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		@Override
		protected String doInBackground(Tag... params) {

			Tag tag = params[0];

			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				// NDEF is not supported by this Tag.
				System.out.println("Fehler");
				return null;
			}

			NdefMessage ndefMessage = ndef.getCachedNdefMessage();

			NdefRecord[] records = ndefMessage.getRecords();

			NdefRecord ndefRecord = records[0];

			if (ndefRecord.getTnf() == NdefRecord.TNF_MIME_MEDIA
					|| ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN) {

				try {
//					System.out.println("Hier bini ich");
					return readText(ndefRecord);

				} catch (UnsupportedEncodingException e) {
					Log.e("Tag", "Unsupported Encoding", e);
				}
				// }
			} else {

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				
//				System.out.println("Read content: " + result);
				
				System.out.println("" + result);
				for (Node node : nodes) {
                    if (node.getRegistrationTarget1().equals(result)) {
                        nodePk = node.getId();
                        System.out.println("" + nodePk);

                        
                        Intent questions = new Intent(getApplicationContext(), QuestionsActivity.class);

                        questions.putExtra("nodePk", nodePk);
                        questions.putExtra("questPk", questPk);
                        questions.putExtra("dtRegistration", dtRegistration);
                        questions.putExtra("questionIDs", node.getQuestionIDs());

                        startActivity(questions);
                    }
			}
		}
	}
	}
	
	private class MainNodeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            nodes = new ArrayList<Node>();
            
            System.out.println("Hier" + questPk);
            try {
                nodes = QuestMethods.getNodes(questPk);
                

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
            HTTPHelper.HTTPExceptionHandler(errorString, NFCActivity.this);
            
            adapter = new ExpandableListViewNodes(getApplicationContext(), nodes);
            list.setAdapter(adapter);
        }
    }
	
	private String readText(NdefRecord record) throws UnsupportedEncodingException{
		
		//Einlesen des Payloads
		byte[]payload = record.getPayload();
		
		//Erfassen der Codierung
		String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
		
		//byte[], Encoding - z.B. UTF-8
		return new String(payload, textEncoding);
		
	}

	@Override
    public void onBackPressed() {
        Intent intent = new Intent(this, QuestActivity.class);
        intent.putExtra("userPk", userPk);
        startActivity(intent);
    }
	
	public void abfrage() {
        EnableGPSorWLAN enable = new EnableGPSorWLAN(this);

        if (!enable.isGPSenabled() && enable.WIFIenabled) {
            enable.enableGPS();
        }
        if (enable.isWIFIDisabled() && enable.isGPSenabled()) {
            enable.enableNetwork();
        }
        if (enable.isWIFIDisabled() && !enable.isGPSenabled()) {
            enable.enableAll();
        }
    }

}
