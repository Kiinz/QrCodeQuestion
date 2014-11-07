package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegistrationActivity extends Activity {

    private TextView vornameText, nachnameText, spitznameText;
    private ProgressBar bar;
    Button registerButton;
    private CheckBox checkBox;
    private String vorname, nachname, spitzname, userID;
    private int id;
    private Boolean useName;
    public static Activity registrationActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationActivity = this;
        AppDown.register(this);

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

                    userID = StartActivity.getUserID();
                    // User wird in StartActivity gespeichert
                    StartActivity.setUser(new User(id, vorname, nachname, spitzname, userID));


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
                    System.out.println("User existiert bereits");
                    existing = true;
                    return null;
                }

                String JSONOutput = HTTPHelper.makeJSONPost("http://193.171.127.102:8080/Quest/user/save.json", StartActivity.getUser().getJSONString());
                JSONObject jsonObjectOutput = new JSONObject(JSONOutput);
                StartActivity.getUser().setId(jsonObjectOutput.getInt("id")); // Zurückbekomme ID wird gespeichert

/*            } catch (final HTTPExceptions e) {
                Handler handler = new Handler(getApplicationContext().getMainLooper());
                handler.post( new Runnable(){
                    public void run(){
                        if (e.getMessage().equals("timeout")) {
                            Toast.makeText(getApplicationContext(), "Fehler: Anfrage dauerte zu lange, bitte überprüfen Sie Ihre Internetverbindung oder versuchen Sie es später erneut.", Toast.LENGTH_LONG).show();
                        } else if (e.getMessage().equals("falseStatusCode")) {
                            Toast.makeText(getApplicationContext(), "Fehler: User konnte nicht erstellt werden.", Toast.LENGTH_LONG).show();
                        } else if (e.getMessage().equals("networkError")) {
                            Toast.makeText(getApplicationContext(), "Fehler: Keine Internetverbindung.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                registerButton.setClickable(true);
                return null;*/
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            bar.setVisibility(View.GONE);
            if (!existing) { // Wenn der Nickname bereits verwendet wird in der Activity bleiben
                Intent intent = new Intent (getApplicationContext(),QuestActivity.class);
                startActivity(intent);
            }
            Toast.makeText(getApplicationContext(), "Dieser Spitzname ist leider bereits vergeben.", Toast.LENGTH_LONG).show();
            registerButton.setClickable(true);

        }
    }

}

