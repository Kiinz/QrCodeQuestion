package at.klu.qrcodequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.*;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Map;


public class HTTPHelper {

    public static String makeGetRequest(String urlString) throws IOException {

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .build();
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("falseStatusCode");
        }
        return response.body().string();

    }

    public static StringBuffer makePostRequest(@SuppressWarnings("SameParameterValue") String urlString, String postParameters) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(mediaType, postParameters);
        Request request = new Request.Builder()
                .url(urlString)
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();
        return new StringBuffer(response.body().string());
    }

    public static String makeJSONPost(String urlString, String postParameters) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(mediaType, postParameters);
        Request request = new Request.Builder()
                .url(urlString)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static void makeJSONPost (String urlString, JSONObject postParameters, Context context) {
//        String url = "http://193.171.127.102:8080/Quest/score/save.json";
        System.out.println(postParameters);
        JsonObjectRequest postRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, urlString, postParameters,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
//        queue.add(postRequest);
        VolleySingleton.getInstance(context).addToRequestQueue(postRequest);
    }

    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';
    public static String createQueryStringForParameters(Map<String, String> parameters) {
        StringBuilder parametersAsQueryString = new StringBuilder();
        if (parameters != null) {
            boolean firstParameter = true;

            for (String parameterName : parameters.keySet()) {
                if (!firstParameter) {
                    parametersAsQueryString.append(PARAMETER_DELIMITER);
                }

                try {
                    parametersAsQueryString.append(parameterName)
                            .append(PARAMETER_EQUALS_CHAR)
                            .append(URLEncoder.encode(parameters.get(parameterName), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                firstParameter = false;
            }
        }
        return parametersAsQueryString.toString();
    }

    public static void HTTPExceptionHandler(String errorString, final Activity activity) {
        System.out.println(errorString);
        if (errorString.equals("networkError")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Fehler: Keine Verbindung");
            builder.setMessage("Bitte stellen Sie sicher, dass eine Verbindung zum Internet besteht!");
            createDialog(builder, activity);
        } else if (errorString.equals("falseStatusCode")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Fehler: Datei nicht gefunden");
            builder.setMessage("Der Server hat ein Problem festgestellt. Bitte versuchen Sie es später erneut!");
            createDialog(builder, activity);
        }
    }

    static void createDialog(AlertDialog.Builder builder, final Activity activity) {
        builder.setPositiveButton("Erneut versuchen", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.recreate();
            }
        });
        builder.setNegativeButton("Beenden", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppDown.allDown();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

