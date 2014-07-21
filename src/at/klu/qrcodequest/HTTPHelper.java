package at.klu.qrcodequest;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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

<<<<<<< HEAD
    public static StringBuffer makePostRequest(String urlString, String postParameters, Context context) throws HTTPExceptions {
=======
    public static StringBuffer makePostRequest(String urlString, String postParameters) {
>>>>>>> origin/master
        URL url = null;
        stringBuffer.setLength(0);
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

//            postParameters = "test=" + postParameters;
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();
            readStream(urlConnection.getInputStream());

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
<<<<<<< HEAD
                throw new HTTPExceptions("falseStatusCode");
            }

        } catch (SocketTimeoutException e) {
            throw new HTTPExceptions("timeout");
=======
                //TODO Exception werfen
<<<<<<< HEAD
                Toast.makeText(RegistrationActivity.registrationActivity, "Fehler: User konnte nicht erstellt werden.", Toast.LENGTH_LONG).show(); 
                
            }
            return stringBuffer;
=======
                Toast.makeText(RegistrationActivity.registrationActivity, "Fehler: User konnte nicht erstellt werden.", Toast.LENGTH_LONG).show();
            }

>>>>>>> parent of 439e7e2... HTTPException
>>>>>>> origin/master
        } catch (IOException e) {
            e.printStackTrace();
            return stringBuffer;
        }
       
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
<<<<<<< HEAD

class HTTPExceptions extends Exception {
    public HTTPExceptions(String reason) {
        super(reason);
    }
}
=======
>>>>>>> origin/master
