package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends Activity {

    private TextView vornameText, nachnameText, spitznameText;
    private ProgressBar bar;
    Button registerButton;
    private CheckBox checkBox;
    private String vorname, nachname, spitzname, userID, postParameter;
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
                    } else if (!spitzname.matches("^[A-Za-zöäüÜÄÖ]{0,}$")) {
                        Toast.makeText(getApplicationContext(), "Im Spitznamen sind nur folgende Sonderzeichen erlaubt: ,._-:()", Toast.LENGTH_LONG).show();
                    }
                } else if ((!vorname.matches("^[A-Za-zöäüÜÄÖ]{3,15}$") || !nachname.matches("^[A-Za-zöäüÜÄÖ]{3,15}$")) && useName) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie einen gültigen Namen ein!", Toast.LENGTH_LONG).show();

                } else { //Alle Eingaben valid
                    registerButton.setClickable(false);

                    userID = StartActivity.getUserID();
                    Map<String, String> userParameters = new HashMap<>();
                    userParameters.put("userId", userID);
                    userParameters.put("firstname", vorname);
                    userParameters.put("lastname", nachname);
                    userParameters.put("nickname", spitzname);
                    userParameters.put("dtOwner", "2");
                    userParameters.put("active", "1");
                    postParameter = HTTPHelper.createQueryStringForParameters(userParameters);

                    new RegistrationTask().execute();
                }
            }
        });
    }

    private class RegistrationTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            StringBuffer stringBuffer;
            try {
                stringBuffer = new StringBuffer(HTTPHelper.makePostRequest("http://193.171.127.102:8080/Quest/user/save", postParameter));

                // Temporäre Suche nach ID
                String[] lines = stringBuffer.toString().split("\\n");
                for(String s: lines){
                    if (s.contains("<form action=\"/Quest/user/delete/")) { // String wird gesucht
                        String[] splitted = s.split("[\"/]"); // Split bei / oder "
                        for (String s1 : splitted) {
                            if (s1.matches("\\d+")) { // Mindestens eine Ziffer
                                id = Integer.parseInt(s1);
                            }
                        }
                    }
                }
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
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // User wird in StartActivity gespeichert
            // TODO Sauberer?
            StartActivity.setUser(new User(id, vorname, nachname, spitzname, userID));

            bar.setVisibility(View.GONE);
            Intent intent = new Intent (getApplicationContext(),QuestActivity.class);
            startActivity(intent);
        }
    }

}

