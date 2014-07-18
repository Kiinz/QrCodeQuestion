package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class RegistrationActivity extends Activity {

    private Button registerButton;
    private TextView vornameText, nachnameText, spitznameText;
    private CheckBox checkBox;
    private String vorname, nachname, spitzname, userID;
    private Boolean useName;
    private Map<String, String> parameterHashMap;
    private User user;
    public static Activity registrationActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationActivity = this;

        checkBox = (CheckBox) findViewById(R.id.checkBox);
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
                    String postParameter = UserMethodes.UsertoJSon(user);

                    try {
                        HTTPHelper.makePostRequest(new URL(" http://193.171.127.102:8080/Quest/user/save"), postParameter);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
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
