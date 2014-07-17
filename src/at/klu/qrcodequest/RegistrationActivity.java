package at.klu.qrcodequest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class RegistrationActivity extends Activity {

    private Button registerButton;
    private TextView vornameText, nachnameText, spitznameText;
    private CheckBox checkBox;
    private String vorname, nachname, spitzname;
    private Boolean useName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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

                if (!spitzname.matches("^[A-Za-z0-9_-]{3,15}$")) {
                    if (spitzname.matches("^{0,3}$")) {
                        Toast.makeText(getApplicationContext(), "Bitte geben Sie einen Spitznamen ein!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Keine Sonderzeichen im Namen", Toast.LENGTH_LONG).show();
                    }
                } else if ((!vorname.matches("^[A-Za-z]{3,15}$") || !nachname.matches("^[A-Za-z]{3,15}$")) && useName) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie einen gültigen Namen ein!", Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}
