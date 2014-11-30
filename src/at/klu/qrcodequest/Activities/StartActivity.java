package at.klu.qrcodequest.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import at.klu.qrcodequest.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StartActivity extends Activity implements OnClickListener {

    private static String errorString = "";
    private Intent intent;
    private Button start;
    private Typeface typeface;
    private TextView welcomeUser;



    @Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		AppDown.register(this);

        start = (Button) findViewById(R.id.button1);

        new StartTask().execute();

        start.setOnClickListener(this);
        start.setClickable(false);
        TextView willkommen = (TextView) findViewById(R.id.textViewWillkommen);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/TYPOGRAPH PRO Light.ttf");
        willkommen.setTypeface(typeface);

        welcomeUser = (TextView) findViewById(R.id.textViewUser);
        welcomeUser.setTypeface(typeface);

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

    private class StartTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String userID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            userID = sha1(userID);

            try {
                String userJSONString = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/user/get?userId=" + userID);

                if (userJSONString.equals("[]")) {
                    throw new Exception("UserNotExisting");
                }

                // User existiert -> Wird in Klasse gespeichert
                JSONArray userJSONArray = new JSONArray(userJSONString);
                JSONObject userJSON = new JSONObject(userJSONArray.getString(0));
                
                int id = userJSON.getInt("id");
                String firstname = userJSON.getString("firstname");
                String lastname = userJSON.getString("lastname");
                String nickname = userJSON.getString("nickname");
                User user = new User(id, firstname, lastname, nickname, userID);

                Data data = (Data) getApplicationContext(); // Globale Datenklasse
                data.setUser(user); // User wird Global gespeichert

                if (firstname.equals("unknown")) {
                    welcomeUser.setText("zurück " + nickname + "!");
                } else {
                    welcomeUser.setText("zurück " + firstname + "!");
                }

                // Wenn User existiert keine Registrierung
                intent = new Intent(getApplicationContext(), QuestActivity.class);
                start.setClickable(true);
            } catch (IOException e) {
                errorString="networkError";
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                if (e.getMessage().equals("UserNotExisting")) {
                    intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    intent.putExtra("userID", userID);
                    start.setClickable(true);
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
