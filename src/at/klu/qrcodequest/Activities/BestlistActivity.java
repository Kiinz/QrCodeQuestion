package at.klu.qrcodequest.activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import at.klu.qrcodequest.AppDown;
import at.klu.qrcodequest.QuestMethods;
import at.klu.qrcodequest.R;
import at.klu.qrcodequest.Score;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


public class BestlistActivity extends Activity {

    ArrayList<Score> scores = new ArrayList<Score>();
    ArrayList<Score> bldata = new ArrayList<Score>();
    HashMap<String, Score> hMap = new HashMap<String, Score>();
    int questPk = 0;
    Context context;
    TextView text1, text2, text3, text4, title;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestlist);
        AppDown.register(this);

        context = this;

        bar = (ProgressBar) findViewById(R.id.progressBar1);

        Bundle bundle = getIntent().getExtras();
        questPk = bundle.getInt("questPk");


        //Titel in die Tabelle einfügen
//        TableLayout layout = (TableLayout) findViewById(R.id.table);
//        title = new TextView(this);
//		title.setText("Bestenliste");
//		title.setTextSize(16);
//		title.setPadding(0, 0, 0, 15);
//		title.setTypeface(Typeface.DEFAULT_BOLD);
//		title.setTextColor(Color.parseColor("#FF0000"));
//		title.setGravity(Gravity.CENTER_HORIZONTAL);
//		layout.addView(title);

        new bestlistTask().execute();

    }

    public void setRows(ArrayList<Score> scores) {

        TableLayout layout = (TableLayout) findViewById(R.id.table);
        int length = scores.size(); //Länge der Array List abfragen


        for (int x = 0; x < length; x++) {

            TableRow row = new TableRow(this); //Erstellen einer neuen Reihe
            TableRow.LayoutParams layoutp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            layoutp.setMargins(2, 2, 2, 2);
            row.setLayoutParams(layoutp);


            text1 = new TextView(this);
            text2 = new TextView(this);
            text3 = new TextView(this);
            text4 = new TextView(this);

//    		Drawable hintergrund = getResources().getDrawable(R.drawable.bestenliste_style); //

            //textViews erhalten einen Hintergrund mit schwarzem Rahmen für die Tabellenansicht
            text1.setBackgroundResource(R.drawable.bestenliste_row1);
            text2.setBackgroundResource(R.drawable.bestenliste_style2);
            text3.setBackgroundResource(R.drawable.bestenliste_style2);
            text4.setBackgroundResource(R.drawable.bestenliste_style2);


//    		text1.setPadding(15, 0, 0, 0);
//    		text2.setPadding(15, 0, 0, 0);
//    		text3.setPadding(15, 0, 0, 0);
//    		text4.setPadding(15, 0, 0, 0);

//    		text1.setBackgroundColor(Color.parseColor("#FF0000"));
//    		text2.setBackgroundColor(Color.parseColor("#FF0000"));
//    		text3.setBackgroundColor(Color.parseColor("#FF0000"));
//    		text4.setBackgroundColor(Color.parseColor("#FF0000"));

            String firstname = scores.get(x).getFirstname();
            String lastname = scores.get(x).getLastname();
            int score = scores.get(x).getScore();
            String nickname = scores.get(x).getNickname();

            text3.setText(firstname);
            text2.setText(lastname);
            text4.setText("" + score);
            text1.setText(nickname);


            row.addView(text1);//einer Reihe wird ein textView hinzugfügt
            row.addView(text2);
            row.addView(text3);
            row.addView(text4);

//    		int i = x+1;

            layout.addView(row, x); //Reihe wird zum TableLayout hinzugefügt


        }

    }

    public class bestlistTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                scores = QuestMethods.getScore(questPk);

                for (Score score1 : scores) {

                    if (hMap.containsKey(score1.getNickname())) {
                        String nickname = score1.getNickname();
                        String lastname = hMap.get(score1.getNickname()).getLastname();
                        String firstname = hMap.get(score1.getNickname()).getFirstname();
                        int score = hMap.get(score1.getNickname()).getScore() + score1.getScore();
                        hMap.put(score1.getNickname(), new Score(firstname, lastname, nickname, score));
                    } else {
                        hMap.put(score1.getNickname(), score1);
                    }
                }

                for (Object o : hMap.entrySet()) {
                    Entry tEntry = (Entry) o;
                    Score score = (Score) tEntry.getValue();
                    bldata.add(score);

                }

                Collections.sort(bldata, new ScoreComparator());


                System.out.println("" + hMap.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            bar.setVisibility(View.INVISIBLE);
            setRows(bldata);
        }
    }

}

class ScoreComparator implements Comparator<Score> {

    @Override
    public int compare(Score lhs, Score rhs) {
        return rhs.getScore() - lhs.getScore();
    }

}
