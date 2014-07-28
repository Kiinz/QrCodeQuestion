package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StartActivity extends Activity implements OnClickListener {

    private static String userID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		AppDown.register(this);

        Button start = (Button) findViewById(R.id.button1);
        TextView willkommen = (TextView) findViewById(R.id.textViewWillkommen);
		start.setOnClickListener(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/TYPOGRAPH PRO Light.ttf");
        willkommen.setTypeface(typeface);

		
		userID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        userID = sha1(userID);
	}


	@Override
	public void onClick(View v) {
		
//		if(userID.equals())
		Intent registration = new Intent(getApplicationContext(), RegistrationActivity.class);
		startActivity(registration);
		
	}
	
	private String sha1(String s) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md != null; //Programm bricht ab wenn md null
        md.update(s.getBytes());
        byte[] bytes = md.digest();
        StringBuilder buffer = new StringBuilder();
        for (byte aByte : bytes) {
            String tmp = Integer.toString((aByte & 0xff) + 0x100, 16).substring(1);
            buffer.append(tmp);
        }
        return buffer.toString();
    }

    public static String getUserID() {
        return userID;
    }
}
