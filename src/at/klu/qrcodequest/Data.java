package at.klu.qrcodequest;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Messna on 25.11.2014.
 */
public class Data extends Application {
    private User user;
    private Quest quest;
    private Node node;
    private ArrayList<Node> nodeList = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quest getQuest() {
        return quest;
    }
    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }
}
