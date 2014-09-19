package at.klu.qrcodequest;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class HTTPHelper {

    private static HttpURLConnection urlConnection;
    private static StringBuffer stringBuffer = new StringBuffer();


    public static StringBuffer makeGetRequest(String urlString) {

        URL url = null;
        
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if (url != null) {
            	System.out.println(url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setDoOutput(false);
                
                int responseCode = urlConnection.getResponseCode(); //can call this instead of con.connect()
                if (responseCode >= 400 && responseCode <= 499) {
                    try {
						throw new Exception("Bad authentication status: " + responseCode);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //provide a more meaningful exception message
                }
                readStream(urlConnection.getInputStream());
                return stringBuffer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            
        }
 
        return result;
    }
 
    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
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
            e.printStackTrace();
//            throw new HTTPExceptions("falseStatusCode");
            //TODO Activate Again!
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
}

class HTTPExceptions extends Exception {
    public HTTPExceptions(String reason) {
        super(reason);
    }
}