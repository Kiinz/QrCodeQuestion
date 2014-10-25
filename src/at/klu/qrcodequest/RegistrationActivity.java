package at.klu.qrcodequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends Activity {

    private TextView vornameText, nachnameText, spitznameText;
    private ProgressBar bar;
    Button registerButton;
    private CheckBox checkBox;
    private String vorname, nachname, spitzname, userID, postParameter;
    private Boolean useName;
    private User user;
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
                    user = new User(1, vorname, nachname, spitzname, userID);
                    Map<String, String> userParameters = new HashMap<String, String>();
                    userParameters.put("userId", userID);
                    userParameters.put("firstname", vorname);
                    userParameters.put("lastname", nachname);
                    userParameters.put("nickname", spitzname);
                    userParameters.put("dtOwner", "2");
                    userParameters.put("active", "1");
                    postParameter = HTTPHelper.createQueryStringForParameters(userParameters);
//                    postParameter = UserMethodes.UsertoJSon(user);

                    new ProgressTask().execute();
                }
            }
        });
    }

    private class ProgressTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                HTTPHelper.makePostRequest("http://193.171.127.102:8080/Quest/user/save", postParameter);
            } catch (HTTPExceptions e) {
                if (e.getMessage().equals("timeout")) {
                    Handler handler = new Handler(getApplicationContext().getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(), "Fehler: Anfrage dauerte zu lange, bitte überprüfen Sie Ihre Internetverbindung oder versuchen Sie es später erneut.", Toast.LENGTH_LONG).show();                        }
                    });
                } else if (e.getMessage().equals("falseStatusCode")) {
                    Handler handler = new Handler(getApplicationContext().getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(), "Fehler: User konnte nicht erstellt werden.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                registerButton.setClickable(true);
                return null;
            }
            Intent intent = new Intent (getApplicationContext(),QuestActivity.class);
            startActivity(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            bar.setVisibility(View.GONE);
        }
    }

    @Override
	public void onBackPressed(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	builder.setTitle("Programm beenden");
    	builder.setMessage("Wollen sie das Programm wirklich beenden?");
    	
    	builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppDown.allDown();
				
			}
		});
    	builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
			}
		});
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }

}

