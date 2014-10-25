package at.klu.qrcodequest;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

public class EnableGPSorWLAN extends Service{
	
	Context context;
	LocationManager locationManager;
	WifiManager wifiManager;
	
	boolean GPSenabled = false;
	boolean WIFIenabled = false;
	String selected;

public EnableGPSorWLAN(Context context) {
		super();
		this.context = context;
	}

public boolean isGPSenabled(){
	locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
	GPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	
	return GPSenabled;
}

public boolean isWIFIEnabled(){
	wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	WIFIenabled = wifiManager.isWifiEnabled();
	return WIFIenabled;
}

public void enableGPS(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	
    	builder.setTitle("Einstellungen ändern");
    	builder.setMessage("GPS ist nicht eingeschaltet. Wollen sie die Einstellungen ändern?");
    	
    	builder.setPositiveButton("Einstellungen", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
				
			}
		});
    	builder.setNegativeButton("Beenden", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				
			}
		});
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }

public void enableNetwork(){
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	
	builder.setTitle("Einstellungen �ndern");
	builder.setMessage("Mit WLAN kann eine besser Genauigkeit erreicht werden. Wollen Sie WLAN nun einschalten?");
	
	builder.setPositiveButton("Einstellungen", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			Intent intent = new Intent (android.provider.Settings.ACTION_WIRELESS_SETTINGS);
			context.startActivity(intent);
			
		}
	});
	
	builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			
		}
	});
	
	AlertDialog dialog = builder.create();
	
	dialog.show();
}

public void enableAll(){
AlertDialog.Builder builder = new AlertDialog.Builder(context);
	
	builder.setTitle("Einstellungen Ändern");
	
	
	 final CharSequence [] array = {"GPS","GPS + WLAN","WLAN"};
	 selected = "GPS + WLAN";
	    
	 builder.setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			selected = array[which].toString();
			
		}
	})
	           
	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			if (selected.equals("GPS")){
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
			if(selected.equals("GPS + WLAN")){
				wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				wifiManager.setWifiEnabled(true);
				Toast.makeText(context, "WIFI wurde automatisch eingeschalten", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
			if(selected.equals("WLAN")){
				wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				wifiManager.setWifiEnabled(true);
				Toast.makeText(context, "WIFI wurde automatisch eingeschalten", Toast.LENGTH_LONG).show();
				
			}
			
		}
	})
	
	.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			
		}
	});
	
	AlertDialog dialog = builder.create();
	
	dialog.show();
}

@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
}
}
