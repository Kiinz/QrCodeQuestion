package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends Activity {

    private TextView vornameText, nachnameText, spitznameText;
    private CheckBox checkBox;
    private String vorname, nachname, spitzname, userID;
    private Boolean useName;
    private User user;
    public static Activity registrationActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationActivity = this;

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        final Button registerButton = (Button) findViewById(R.id.button);
        vornameText = (EditText) findViewById(R.id.editText);
        nachnameText = (EditText) findViewById(R.id.editText2);
        spitznameText = (EditText) findViewById(R.id.editText3);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setClickable(false);

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
                        Toast.makeText(getApplicationContext(), "Bitte verwenden sie keine Sonderzeichen im Namen", Toast.LENGTH_LONG).show();
                    }
                } else if ((!vorname.matches("^[A-Za-zöäüÜÄÖ]{3,15}$") || !nachname.matches("^[A-Za-zöäüÜÄÖ]{3,15}$")) && useName) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie einen gültigen Namen ein!", Toast.LENGTH_LONG).show();

                } else { //Alle Eingaben valid

                    userID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    userID = sha1(userID);
                    System.out.println(userID);
                    user = new User(1, vorname, nachname, spitzname, userID);
                    Map<String, String> userParameters = new HashMap<String, String>();
                    userParameters.put("userId", userID);
                    userParameters.put("firstname", vorname);
                    userParameters.put("lastname", nachname);
                    userParameters.put("nickname", spitzname);
                    userParameters.put("dtOwner", "2");
                    userParameters.put("active", "1");
                    String postParameter = HTTPHelper.createQueryStringForParameters(userParameters);
//                    String postParameter = UserMethodes.UsertoJSon(user);
<<<<<<< HEAD
                    try {
                        HTTPHelper.makePostRequest("http://193.171.127.102:8080/Quest/user/save", postParameter, getApplicationContext());
                    } catch (HTTPExceptions e) {
                        if (e.getMessage().equals("timeout")) {
                            Toast.makeText(getApplicationContext(), "Fehler: Anfrage dauerte zu lange, bitte überprüfen Sie Ihre Internetverbindung oder versuchen Sie es später erneut.", Toast.LENGTH_LONG).show();
                        } else if (e.getMessage().equals("falseStatusCode")) {
                            Toast.makeText(getApplicationContext(), "Fehler: User konnte nicht erstellt werden.", Toast.LENGTH_LONG).show();
                        }
                        registerButton.setClickable(true);
                        return;
                    }

                    Intent intent = new Intent (getApplicationContext(),MainActivity.class);
                    startActivity(intent);
=======
<<<<<<< HEAD
                    
                    
                    try {
						HTTPHelper.makePostRequest("http://193.171.127.102:8080/Quest/user/save", postParameter);
						
						Intent intent = new Intent (getApplicationContext(),QuestActivity.class);
	                    startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "Bei Übertragung ist ein Fehler aufgetreten", Toast.LENGTH_LONG);
					}
                    
=======
                    HTTPHelper.makePostRequest("http://193.171.127.102:8080/Quest/user/save", postParameter);
                    
                    Intent intent = new Intent (getApplicationContext(),QuestActivity.class);
                    startActivity(intent);
>>>>>>> parent of 439e7e2... HTTPException
>>>>>>> a9cc6fb66e4a1675584566ec40b148bc68361234

                }
            }
        });
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
}
