package at.klu.qrcodequest;

import java.util.ArrayList;

import android.app.Activity;

public class AppDown {
	
	private static  ArrayList<Activity> activities = new ArrayList<Activity>();
	
	public static void register(Activity activity){
		
		activities.add(activity);
		
	}
	
	public static void allDown(){
		for (int i = 0; i < activities.size(); i++){
			activities.get(i).finish();
		}
	}

}

