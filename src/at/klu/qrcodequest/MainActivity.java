package at.klu.qrcodequest;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private GoogleMap map;
    Button btscan;
    String result;
    int questPk = 0;
    int nodePk = 0;
    EnableGPSorWLAN enable;
    ArrayList<Node> nodes;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppDown.register(this);
        
        context = this;
        
        //GoogleMaps
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        
        //Abfragen ob die Map erstellt werden konnte
        if(map == null){
			Toast.makeText(getApplicationContext(), "Die Karte konnte nicht erstellt werden", Toast.LENGTH_LONG).show();
		}else{
			map.setMyLocationEnabled(true);
			
			abfrage();

		}

        btscan = (Button) findViewById(R.id.weiter);
        Bundle bundle = getIntent().getExtras();
        questPk = bundle.getInt("questPk");
        System.out.println("" + questPk);

        //Thread für die Abfrage der Nodes
        Thread thread = new Thread (){
				public void run() {
					
					nodes = new ArrayList<Node>();

	                try {
	                    nodes = QuestMethods.getNodes(questPk);
	                    System.out.println("" + nodes);
	                    
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                } catch (IOException e) {
                        // TODO Exception
                        e.printStackTrace();
                    }

                    for (int i = 0; i < nodes.size(); i++){
	                	if(nodes.get(i).getLocation() != null){
	                		String s = nodes.get(i).getLocation();
	                		System.out.println("" + s);
	                		if (s.charAt(0) == '@'){
	                			String locationString = s.substring(1);
	                			
	                			String [] location;
	                			
	                			location = locationString.split(", ");
	                		
	                			
	                			System.out.println(location [0] + "  " + location [1]);
	                			
	                			final int position = i;
	                			final double latitude = Double.parseDouble(location[0]);
	                			final double longitude = Double.parseDouble(location[1]);

	                			Handler handler = new Handler(context.getMainLooper());
	                			handler.post(new Runnable(){

									@Override
									public void run() {
										placeMarker(latitude,longitude, nodes.get(position).getName());
										
									}
	                				
	                			});
	                		}
	                	}
	                }
					
	               
				}
        };
        
        thread.start();
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

    //In the same activity you'll need the following to retrieve the results:
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {        //RequestCode dient zu identifizierung der Activity, die das Ergebnis liefert

            if (resultCode == RESULT_OK) {

                result = intent.getStringExtra("SCAN_RESULT");
                System.out.println("" + result);

                ArrayList<Node> nodes = new ArrayList<Node>();

                try {
                    nodes = QuestMethods.getNodes(questPk);
                } catch (JSONException e) {
                    e.printStackTrace();
//                } catch (HTTPExceptions httpExceptions) {
//                    if (httpExceptions.getMessage().equals("timeout")) {
//                        Handler handler = new Handler(getApplicationContext().getMainLooper());
//                        handler.post( new Runnable(){
//                            public void run(){
//                                Toast.makeText(getApplicationContext(), "Fehler: Anfrage dauerte zu lange, bitte überprüfen Sie Ihre Internetverbindung oder versuchen Sie es später erneut.", Toast.LENGTH_LONG).show();                        }
//                        });
//                    } else if (httpExceptions.getMessage().equals("falseStatusCode")) {
//                        Handler handler = new Handler(getApplicationContext().getMainLooper());
//                        handler.post( new Runnable(){
//                            public void run(){
//                                Toast.makeText(getApplicationContext(), "Fehler: Nodes konnten nicht abgerufen werden. Serverfehler.", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO Exception
                }

                for (Node node : nodes) {
                    if (node.getRegistrationTarget1().equals(result)) {
                        nodePk = node.getId();
                        System.out.println("" + nodePk);


                        Intent questions = new Intent(getApplicationContext(), QuestionsActivity.class);

                        questions.putExtra("nodePk", nodePk);
                        questions.putExtra("questPk", questPk);
                        questions.putExtra("questionIDs", node.getQuestionIDs());

                        startActivity(questions);
                    }
                }
            }
        }
    }
    
   
		public void abfrage(){
			EnableGPSorWLAN enable = new EnableGPSorWLAN(this);
			
			if(!enable.isGPSenabled() && enable.WIFIenabled){
				enable.enableGPS();	
			}
			if(!enable.isWIFIEnabled() && enable.isGPSenabled()){
				enable.enableNetwork();
			}
			if(!enable.isWIFIEnabled() && !enable.isGPSenabled()){
				enable.enableAll();
			}
		}

		public void placeMarker(double latitude, double longitude, String title){
			
			MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude));
			marker.title(title);
			marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			map.addMarker(marker);
		}
		
		public void setCameraPosition(double latitude, double longitude){
			CameraPosition position = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(15).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
		}
		
		public void makeCircle(double latitude, double longitude){
			CircleOptions options = new CircleOptions();
			options.center(new LatLng(latitude, longitude));
			options.radius(10);
			options.strokeColor(Color.parseColor("#00000000"));
			options.fillColor(Color.parseColor("#99FF0000"));
			this.map.addCircle(options);
		}
		
		@Override
		public void onBackPressed(){
			Intent intent = new Intent (this, QuestActivity.class);
			startActivity(intent);
		}	
}