package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StartActivity extends Activity implements OnClickListener {

    private static String userID, errorString = "";
    private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        new StartTask().execute();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		AppDown.register(this);

        Button start = (Button) findViewById(R.id.button1);
        TextView willkommen = (TextView) findViewById(R.id.textViewWillkommen);
		start.setOnClickListener(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/TYPOGRAPH PRO Light.ttf");
        willkommen.setTypeface(typeface);

	}


	@Override
	public void onClick(View v) {
		
		startActivity(intent);
		
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

    private class StartTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            userID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            userID = sha1(userID);

            try {
                HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/user/show/" + userID);

                //Wenn User existiert keine Registrierung
                intent = new Intent(getApplicationContext(), QuestActivity.class);
            } catch (IOException e) {
                if (e.getMessage().equals("falseStatusCode")) {
                    //Wenn Seite nicht gefunden muss der User noch angelegt werden
                    intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                } else {
                    errorString="networkError";
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            HTTPHelper.HTTPExceptionHandler(errorString, StartActivity.this);
        }
    }
}
