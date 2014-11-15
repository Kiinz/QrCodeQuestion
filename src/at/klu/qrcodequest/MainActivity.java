package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private GoogleMap map;
    private String result;
    private int questPk = 0;
    private int nodePk = 0;
    private ArrayList<Node> nodes;
    private int dtRegistration;
    private Context context;
    private int userPk;
    private String errorString = "";

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
        if (map == null) {
            Toast.makeText(getApplicationContext(), "Die Karte konnte nicht erstellt werden", Toast.LENGTH_LONG).show();
        } else {
            map.setMyLocationEnabled(true);
            abfrage();
        }

        Button btscan = (Button) findViewById(R.id.weiter);
        Bundle bundle = getIntent().getExtras();
        questPk = bundle.getInt("questPk");
        userPk = bundle.getInt("userPk");
        dtRegistration = bundle.getInt("dtRegistration");
        
//        System.out.println("" + questPk);

        //Thread für die Abfrage der Nodes
        new MainNodeTask().execute();

        btscan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Es wird eine neues Intent aufgerufen (QR-Code Reader)
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
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

    void placeMarker(double latitude, double longitude, String title) {

        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude));
        marker.title(title);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        map.addMarker(marker);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, QuestActivity.class);
        startActivity(intent);
    }

    private class MainNodeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            nodes = new ArrayList<Node>();

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

            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).getLocation() != null) {
                    String s = nodes.get(i).getLocation();
                    System.out.println("" + s);
                    if (s.charAt(0) == '@') {
                        String locationString = s.substring(1);
                        String[] location;
                        location = locationString.split(", ");
                        System.out.println(location[0] + "  " + location[1]);
                        final int position = i;
                        final double latitude = Double.parseDouble(location[0]);
                        final double longitude = Double.parseDouble(location[1]);

                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                placeMarker(latitude, longitude, nodes.get(position).getName());
                            }
                        });
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            HTTPHelper.HTTPExceptionHandler(errorString, MainActivity.this);
        }
    }
}