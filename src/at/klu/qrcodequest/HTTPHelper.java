package at.klu.qrcodequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.*;
import java.net.*;
import java.util.Map;


public class HTTPHelper {

    private static HttpURLConnection urlConnection;
    private static StringBuffer stringBuffer = new StringBuffer();


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

    //Convert InputStream to StringBuffer
    private static void readStream(InputStream in) {
        BufferedReader reader = null;
        stringBuffer.setLength(0);
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static StringBuffer makePostRequest(String urlString, String postParameters) throws HTTPExceptions {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {

            urlConnection = (HttpURLConnection) (url != null ? url.openConnection() : null); //Check if URL!=null and open Connection
            if (urlConnection != null) {
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);

                PrintWriter out = new PrintWriter(urlConnection.getOutputStream()); //Post parameters
                out.print(postParameters);
                out.close();
//
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {  //!=200
                    throw new HTTPExceptions("falseStatusCode");
                }

                readStream(urlConnection.getInputStream()); //Antwort auslesen
            }

        } catch (SocketTimeoutException e) {
            throw new HTTPExceptions("timeout");
        } catch (IOException e) {
            //Network Error
            throw new HTTPExceptions("networkError");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return stringBuffer;

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
        if (errorString.equals("networkError")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Fehler: Keine Verbindung");
            builder.setMessage("Bitte stellen Sie sicher, dass eine Verbindung zum Internet besteht!");
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
        } else if (errorString.equals("falseStatusCode")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Fehler: Datei nicht gefunden");
            builder.setMessage("Der Server hat ein Problem festgestellt. Bitte versuchen Sie es später erneut!");
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
}

class HTTPExceptions extends Exception {
    public HTTPExceptions(String reason) {
        super(reason);
    }
}