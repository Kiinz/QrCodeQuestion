package at.klu.qrcodequest;

import android.util.SparseArray;

import java.util.ArrayList;

public class QuestionWithAnswers {

    private String question;
    private ArrayList<String> answerList;
    private SparseArray<String> answerSparseArray;

    public QuestionWithAnswers() {
    }

    public QuestionWithAnswers(String question, ArrayList<String> answerList) {
        this.question = question;
        this.answerList = answerList;
        createSparseArray(answerList);
    }

    public SparseArray<String> getAnswerSparseArray() {
        return answerSparseArray;
    }

    public void createSparseArray(ArrayList<String> answerList){
        answerSparseArray = new SparseArray<String>(); //More efficient than Hashmap
        int i = 0;
        for (String answer : answerList) {
            answerSparseArray.append(i, answer);
            i++;
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswerList(ArrayList<String> answerList) {
        this.answerList = answerList;
        createSparseArray(answerList);
    }
}
