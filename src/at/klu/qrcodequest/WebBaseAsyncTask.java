package at.klu.qrcodequest;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Messna on 18.11.2014.
 */
public class WebBaseAsyncTask extends AsyncTask <Void, Void, Void> {

    private Activity activity;
    private ProgressBar bar;
    private TextView loadQuestionsTextView;

    public WebBaseAsyncTask (Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        bar = (ProgressBar) activity.findViewById(R.id.marker_progress);
        loadQuestionsTextView = (TextView) activity.findViewById(R.id.loadQuestionsText);
        bar.setVisibility(View.VISIBLE);
        loadQuestionsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        bar.setVisibility(View.GONE);
        loadQuestionsTextView.setVisibility(View.GONE);
    }

}
