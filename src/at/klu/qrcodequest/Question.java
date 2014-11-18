package at.klu.qrcodequest;

import android.util.SparseArray;

public class Question {
    private int nodePk;
    private int id;
    private Boolean active;
    private String name, description, option1, option2, option3, option4, option5, option6, option7, option8, option9, option10;
    private SparseArray<String> answerSparseArray;


    public String getQuestionName() {
        return name;
    }

    public Question(int id, int nodePk, Boolean active, String name, String descr, String o1, String o2, String o3, String o4, String o5, String o6, String o7, String o8, String o9, String o10) {
        this.id = id;
        this.nodePk = nodePk;
        this.active = active;
        this.name = name;
        this.description = descr;
        this.option1 = o1;
        this.option2 = o2;
        this.option3 = o3;
        this.option4 = o4;

        this.option5 = o5;
        this.option6 = o6;
        this.option7 = o7;
        this.option8 = o8;
        this.option9 = o9;
        this.option10 = o10;

        String[] answersString = {o1,o2,o3,o4,o5,o6,o7,o8,o9,o10};
        createSparseArray(answersString);

    }

    public SparseArray<String> getAnswerSparseArray() {
        return answerSparseArray;
    }

    public void createSparseArray(String[] answersString){
        answerSparseArray = new SparseArray<String>(); //More efficient than Hashmap
        int i = 0;
        for (String answer : answersString) {
            if (!answer.equals("null")) { //Don't add empty answers
                answerSparseArray.append(i, answer); //Remember index
                i++;
            }

        }
    }

    public int getId() {
        return id;
    }

}

