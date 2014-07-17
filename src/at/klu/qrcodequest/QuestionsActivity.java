package at.klu.qrcodequest;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionsActivity extends Activity {

    private ArrayList<QuestionWithAnswers> questionWithAnswersList = new ArrayList<QuestionWithAnswers>();
    private ArrayList<String> answerList = new ArrayList<String>();
    private ArrayList<String> answerList2 = new ArrayList<String>();
    private ArrayList<Boolean> rightAnswerChosen = new ArrayList<Boolean>();
    private SparseArray<String> answerSparseArray = new SparseArray<String>();
    private int questionNumber = 0;
    private List<Integer> randomKeys;
    private Integer[] numbers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        getQuestionsWithAnswers();
        generateNextQuestionWithAnswers();


    }

    @Override
    public void onBackPressed() {
        if (questionNumber > 0) {

            questionNumber--;
            shuffleAnswers();
            System.out.println(" a d");
            generateNextQuestionWithAnswers();
        } else {
            //TODO Go to last View
        }
    }

    public void generateNextQuestionWithAnswers() {

        TextView questionView = (TextView) findViewById(R.id.textViewQuestion);
        ViewGroup verticalLayout = (ViewGroup) findViewById(R.id.linearLayoutQuestions);
        verticalLayout.removeAllViews(); //Removes Buttons from last Question
        Button bt;
        View.OnClickListener buttonListener;

        questionView.setText(questionWithAnswersList.get(questionNumber).getQuestion());

        for (int i : randomKeys) {
            String answer = answerSparseArray.get(i);

            bt = new Button(this);
            bt.setText(answer);
            bt.setBackgroundResource(R.drawable.questionbutton);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,15,3);
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

                    if (questionNumber < questionWithAnswersList.size()-1) {
                        questionNumber++;
                        shuffleAnswers();
                    } else {
                        //TODO Go to next View
                    }
                    generateNextQuestionWithAnswers();
                }
            };
            bt.setOnClickListener(buttonListener);
        }

    }

    public void getQuestionsWithAnswers() {

        QuestionWithAnswers questionWithAnswers;

        answerList.add("Ja");
        answerList.add("Bla");
        answerList.add("Yo");
        answerList.add("asdf");
        answerList.add("fgdh");
        answerList.add("Yhzthto");
        answerList.add("hjmjhm");
        answerList.add("bvcvbv");
        answerList.add("dsdff");

        answerList2.add("asdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdad");
        answerList2.add("fdgh");
        answerList2.add("vbbdf");
        answerList2.add("sdffds");
        answerList2.add("dfgs");
        answerList2.add("sdgfg");
        answerList2.add("gdfg");
        answerList2.add("gfdgfd");
        answerList2.add("xxxxxx");


        questionWithAnswers = new QuestionWithAnswers();
        questionWithAnswers.setQuestion("Wie gehts?");

        questionWithAnswers.setAnswerList(answerList);
        //TODO Get Answers dynamically

        QuestionWithAnswers qwa2 = new QuestionWithAnswers("Und sonst?", answerList2);

        questionWithAnswersList.add(questionWithAnswers);
        questionWithAnswersList.add(qwa2);

        shuffleAnswers();
    }

    public void shuffleAnswers() {
        answerSparseArray = questionWithAnswersList.get(questionNumber).getAnswerSparseArray(); //TODO Buggy?
        numbers = new Integer[answerSparseArray.size()];
        for (int i = 0; i < answerSparseArray.size(); i++) {
            numbers[i] = i;
        }

        randomKeys = Arrays.asList(numbers);
        Collections.shuffle(randomKeys); //Zufällige Keys, um die Antworten zu mischen
    }
}
