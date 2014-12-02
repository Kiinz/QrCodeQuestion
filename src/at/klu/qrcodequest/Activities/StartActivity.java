package at.klu.qrcodequest.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import at.klu.qrcodequest.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
    private String userID;


    @Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		AppDown.register(this);

        start = (Button) findViewById(R.id.button1);

        getUser();

        start.setOnClickListener(this);
        start.setClickable(false);
        TextView willkommen = (TextView) findViewById(R.id.textViewWillkommen);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/TYPOGRAPH PRO Light.ttf");
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

    private void getUser() {
        userID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        userID = sha1(userID);

        String url = "http://193.171.127.102:8080/Quest/user/get?userId=" + userID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.toString().equals("[]")) {
                        intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                        intent.putExtra("userID", userID);
                        start.setClickable(true);
                    } else {
                        JSONObject userJSON = response.getJSONObject(0);

                        int id = userJSON.getInt("id");
                        String firstname = userJSON.getString("firstname");
                        String lastname = userJSON.getString("lastname");
                        String nickname = userJSON.getString("nickname");
                        User user = new User(id, firstname, lastname, nickname, userID);

                        Data data = (Data) getApplicationContext(); // Globale Datenklasse
                        data.setUser(user); // User wird Global gespeichert

                        // Wenn User existiert keine Registrierung
                        intent = new Intent(getApplicationContext(), QuestActivity.class);
                        start.setClickable(true);

                        TextView welcomeUser = (TextView) findViewById(R.id.textViewUser);
                        welcomeUser.setTypeface(typeface);
                        if (firstname.equals("unknown")) {
                            welcomeUser.setText("zurück " + nickname + "!");
                        } else {
                            welcomeUser.setText("zurück " + firstname + "!");
                        }
                    }
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.marker_progress);
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                System.out.println(error);
            }
        });
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }
}
