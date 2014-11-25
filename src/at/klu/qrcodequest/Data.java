package at.klu.qrcodequest;

import android.app.Application;

/**
 * Created by Messna on 25.11.2014.
 */
public class Data extends Application {
    private User user;
    private int questPk;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
