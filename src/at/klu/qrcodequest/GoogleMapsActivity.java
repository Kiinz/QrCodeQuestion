package at.klu.qrcodequest;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class GoogleMapsActivity extends Activity {

	private GoogleMap map;
	private int questPk;
	private int dtRegistration = 4;
	private int userPk;
	private ArrayList<Node> nodes;
	private Context context;
	private String errorString = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_maps);
		
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
        
        Location location = map.getMyLocation();
        
        if(location != null){
        	double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            
            setCameraPosition(latitude, longitude);
        }
        

        Bundle bundle = getIntent().getExtras();
        questPk = bundle.getInt("questPk");
        userPk = bundle.getInt("userPk");
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
	
	private void placeMarker(double latitude, double longitude, String title) {

        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude));
        marker.title(title);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        map.addMarker(marker);
    }
	
	private void setCameraPosition(double latitude, double longitude){
		CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(12).build();
 
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	
}
