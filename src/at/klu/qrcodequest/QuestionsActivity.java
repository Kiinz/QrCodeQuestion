package at.klu.qrcodequest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionsActivity extends Activity {

    private static ArrayList <Question> questions;
    private ArrayList<Boolean> rightAnswerChosen = new ArrayList<Boolean>();
    private SparseArray<String> answerSparseArray = new SparseArray<String>();
    private int questionNumber = 0;
    private int nodePk, questPk;
    private int[] questionIDs;
    private List<Integer> randomKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        AppDown.register(this);

        Bundle bundle = getIntent().getExtras();
        nodePk = bundle.getInt("nodePk");
        questPk = bundle.getInt("questPk");
        questionIDs = bundle.getIntArray("questionIDs");
        new getQuestionTask().execute();



    }

    @Override
    public void onBackPressed() {
        if (questionNumber > 0) {

            questionNumber--;
            shuffleAnswers();
            generateNextQuestionWithAnswers();
        } else {
            Intent nodeIntent = new Intent (getApplicationContext(), MainActivity.class);
            nodeIntent.putExtra("finished", false);
            nodeIntent.putExtra("questPk", questPk);
            startActivity(nodeIntent);
        }
    }

    public void generateNextQuestionWithAnswers() {

        TextView questionView = (TextView) findViewById(R.id.textViewQuestion);
        ViewGroup verticalLayout = (ViewGroup) findViewById(R.id.linearLayoutQuestions);
        verticalLayout.removeAllViews(); //Removes Buttons from last Question
        Button bt;
        View.OnClickListener buttonListener;

        questionView.setText(questions.get(questionNumber).getQuestionName());

        for (int i : randomKeys) {
            String answer = answerSparseArray.get(i);

            bt = new Button(this);
            bt.setText(answer);
            bt.setTextSize(20);
            bt.setBackgroundResource(R.drawable.questionbutton);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 15, 3);
            bt.setLayoutParams(layoutParams);
            verticalLayout.addView(bt);

            buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    if (answerSparseArray.indexOfValue(b.getText().toString()) == 0) {
                        rightAnswerChosen.add(questionNumber, true);
                    } else {
                        rightAnswerChosen.add(questionNumber, false);
                    }

                    if (questionNumber < questions.size()-1) {
                        questionNumber++;
                        shuffleAnswers();
                    } else {
                        Intent nodeIntent = new Intent (getApplicationContext(), MainActivity.class);
                        nodeIntent.putExtra("finished", true);
                        nodeIntent.putExtra("questPk", questPk);
                        startActivity(nodeIntent);
                    }
                    generateNextQuestionWithAnswers();
                }
            };
            bt.setOnClickListener(buttonListener);
        }

    }


    public void shuffleAnswers() {
        answerSparseArray = questions.get(questionNumber).getAnswerSparseArray();
//        Integer[] numbers = new Integer[answerSparseArray.size()];
        randomKeys = new ArrayList<Integer>(answerSparseArray.size()+5);
        for (int i = 0; i < answerSparseArray.size(); i++) {
//            numbers[i] = i;
            randomKeys.add(i);
        }

//        randomKeys = Arrays.asList(numbers);
        Collections.shuffle(randomKeys); //Zufällige Keys, um die Antworten zu mischen
    }

    private class getQuestionTask extends AsyncTask<Void, Void, Void> {
        private ProgressBar bar;
        private TextView loadQuestionsTextView;

        @Override
        protected void onPreExecute(){
            bar = (ProgressBar) findViewById(R.id.marker_progress);
            loadQuestionsTextView = (TextView) findViewById(R.id.loadQuestionsText);
            bar.setVisibility(View.VISIBLE);
            loadQuestionsTextView.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            int nodePk;
            boolean active;
            String o1,o2,o3,o4,o5,o6,o7,o8,o9,o10, name, descr;

            for (int questionID : questionIDs) {
                String questionsString = HTTPHelper.makeGetRequest("http://193.171.127.102:8080/Quest/question/show/" + questionID + ".json").toString() + "]}";

                questions = new ArrayList<Question>();

                JSONObject questionJSON;
                try {
                    questionJSON = new JSONObject(questionsString);

                    nodePk = 2;
                    active = questionJSON.getBoolean("active");
                    name = questionJSON.getString("name");
                    descr = questionJSON.getString("description");
                    o1 = questionJSON.getString("option1");
                    o2 = questionJSON.getString("option2");
                    o3 = questionJSON.getString("option3");
                    o4 = questionJSON.getString("option4");
                    o5 = questionJSON.getString("option5");
                    o6 = questionJSON.getString("option6");
                    o7 = questionJSON.getString("option7");
                    o8 = questionJSON.getString("option8");
                    o9 = questionJSON.getString("option9");
                    o10 = questionJSON.getString("option10");

                    Question question = new Question(nodePk, active, name, descr, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10);
                    questions.add(question);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            bar.setVisibility(View.GONE);
            loadQuestionsTextView.setVisibility(View.GONE);

            shuffleAnswers();
            generateNextQuestionWithAnswers();
        }
    }
}
