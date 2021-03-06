package at.klu.qrcodequest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import at.klu.qrcodequest.*;
import at.klu.qrcodequest.activities.QuestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegistrationActivity extends Activity {

    private TextView vornameText, nachnameText, spitznameText;
    private ProgressBar bar;
    Button registerButton;
    private CheckBox checkBox;
    private String vorname, nachname, spitzname, userID;
    private int userPk;
    private Boolean useName;
    private Data data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        AppDown.register(this);

        data = (Data) getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userID");

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        bar = (ProgressBar) findViewById(R.id.marker_progress);
        registerButton = (Button) findViewById(R.id.button);
        vornameText = (EditText) findViewById(R.id.editText);
        nachnameText = (EditText) findViewById(R.id.editText2);
        spitznameText = (EditText) findViewById(R.id.editText3);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vorname = vornameText.getText().toString();
                nachname = nachnameText.getText().toString();
                spitzname = spitznameText.getText().toString();
                useName = checkBox.isChecked();

                if (!useName) {
                    vorname = "unknown";
                    nachname = "unknown";
                }

                if (!spitzname.matches("^[A-Za-z0-9öäüÜÄÖ:)(,._-]{3,15}$")) {
                    if (spitzname.matches("^.{0,3}$")) {
                        Toast.makeText(getApplicationContext(), "Bitte geben Sie einen Spitznamen ein!", Toast.LENGTH_LONG).show();
                    } else if (!spitzname.matches("^[A-Za-zöäüÜÄÖ]*$")) {
                        Toast.makeText(getApplicationContext(), "Im Spitznamen sind nur folgende Sonderzeichen erlaubt: ,._-:()", Toast.LENGTH_LONG).show();
                    }
                } else if ((!vorname.matches("^[A-Za-zöäüÜÄÖ]{3,15}$") || !nachname.matches("^[A-Za-zöäüÜÄÖ]{3,15}$")) && useName) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie einen gültigen Namen ein!", Toast.LENGTH_LONG).show();

                } else { //Alle Eingaben valid
                    registerButton.setClickable(false);

                    // User wird in der Datenklasse gespeichert
                    data.setUser(new User(userPk, vorname, nachname, spitzname, userID));


                    new RegistrationTask().execute();
                }
            }
        });
    }

    private class RegistrationTask extends AsyncTask<Void, Void, Void> {
        Boolean existing = false;
        @Override
        protected void onPreExecute(){
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (!HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/user/exists?nickname=" + spitzname).equals("[]")) {
                    existing = true;
                    return null;
                }

                String JSONOutput = HTTPHelper.makeJSONPost("http://193.171.127.102:8080/Quest/user/save.json", data.getUser().getJSONString());
                JSONObject jsonObjectOutput = new JSONObject(JSONOutput);
                userPk = jsonObjectOutput.getInt("id"); // Zurückbekommene ID wird gespeichert
                data.getUser().setId(userPk);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            bar.setVisibility(View.GONE);
            if (!existing) { // Wenn der Nickname bereits verwendet wird in der Activity bleiben
                Intent intent = new Intent (getApplicationContext(),QuestActivity.class);
//                intent.putExtra("userPk", userPk);
//                EventBus.getDefault().postSticky(user);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Dieser Spitzname ist leider bereits vergeben.", Toast.LENGTH_LONG).show();
                registerButton.setClickable(true);
            }

        }
    }

}

