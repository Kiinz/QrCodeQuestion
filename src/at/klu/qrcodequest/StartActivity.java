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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StartActivity extends Activity implements OnClickListener {

    private static String userID, errorString = "";
    private Intent intent;
    private Button start;
    private static User user;


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
                String userJSONString = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/user/show/" + userID + ".json");

                // User existiert -> Wird in Klasse gespeichert
                JSONObject userJSON = new JSONObject(userJSONString);
                int id = userJSON.getInt("userPk");
                String firstname = userJSON.getString("firstname");
                String lastname = userJSON.getString("lastname");
                String nickname = userJSON.getString("nickname");
                String userId = userJSON.getString("userId");
                int active = userJSON.getInt("active");
                user = new User(id,active,firstname,lastname,nickname,userId);

                // Wenn User existiert keine Registrierung
                intent = new Intent(getApplicationContext(), QuestActivity.class);
                start.setClickable(true);
            } catch (IOException e) {
                if (e.getMessage().equals("falseStatusCode")) {
                    // Wenn Seite nicht gefunden muss der User noch angelegt werden
                    intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    start.setClickable(true);
                } else {
                    errorString="networkError";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            HTTPHelper.HTTPExceptionHandler(errorString, StartActivity.this);
        }
    }

    public static User getUser() {
        return user;
    }
    public static void setUser(User user) {
        StartActivity.user = user;
    }
}
