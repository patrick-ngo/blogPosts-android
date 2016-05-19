package ngo.patrick.netccentricassignment;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * BlogPostHttpRequest: Handles getting and posting data to the provided API
 */
public class BlogPostHttpRequest
{
    private static String LOG_TAG = "BlogPostHttpRequest";
    private static String BASE_URL = "https://demo6401519.mockable.io/posts/";


    /**
     * Get Data in JSON format for either all blog posts or a single blog post
     */
    public static String GetData(String postId)
    {
        //These two need to be declared outside the try/catch
        //so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try
        {
            //Construct the URL for the query
            String baseUrl = BASE_URL;

            if (postId != "")
            {
                baseUrl = baseUrl + postId + "/";
            }

            URL url = new URL(baseUrl);

            //Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
            {
                //Nothing to do
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
            {
                //Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                //But it does make debugging a *lot* easier if you print out the completed
                //buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
            {
                //Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();

        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error ", e);
            //If the code didn't successfully get the data, there's no point in attempting to parse it
            return null;
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (final IOException e)
                {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return forecastJsonStr;
    }


    /**
     * Post Data in JSON format for a single blog post (only caption, no image
     * TODO: This function is currently not functional. To be investigated
     */
    public static void PostData(String caption)
    {
        HttpURLConnection connection = null;

        try
        {
            //Create the request and open the connection
            URL url = new URL(BASE_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            //Create output string to be written
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("caption", "Morning cup of coffee!");
            String query = builder.build().getEncodedQuery();

            OutputStream out = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));

            writer.write(query);
            writer.flush();
            writer.close();
            out.close();

            int responseCode = connection.getResponseCode();

            Log.v(LOG_TAG, "\nSending 'POST' request to URL : " + url);
            Log.v(LOG_TAG, "Response Code : " + responseCode);
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG, "Error ", e);
            //If the code didn't successfully get the data, there's no point in attempting to parse it
        }
        finally
        {
            if (connection != null)
            {
                // Make sure the connection is not null.
                connection.disconnect();
            }
        }
    }

}

