package at.klu.qrcodequest;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.Map;


public class HTTPHelper {

    private static StringBuffer stringBuffer = new StringBuffer();


    public static StringBuffer makeGetRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            StrictMode.ThreadPolicy policy = new StrictMode.
                    ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            readStream(con.getInputStream());
            return stringBuffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

//            postParameters = "test=" + postParameters;
            HttpURLConnection urlConnection = (HttpURLConnection) (url != null ? url.openConnection() : null); //Check if URL!=null and open Connection

            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);

            PrintWriter out = new PrintWriter(urlConnection.getOutputStream()); //Post parameters
            out.print(postParameters);
            out.close();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {  //!=200
                throw new HTTPExceptions("falseStatusCode");
            }

            readStream(urlConnection.getInputStream()); //Antwort auslesen

        } catch (SocketTimeoutException e) {
            throw new HTTPExceptions("timeout");
        } catch (IOException e) {
            e.printStackTrace();
            throw new HTTPExceptions("falseStatusCode");
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
}

class HTTPExceptions extends Exception {
    public HTTPExceptions(String reason) {
        super(reason);
    }
}