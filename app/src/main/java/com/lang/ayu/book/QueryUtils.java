package com.lang.ayu.book;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils()
    { }

    public static ArrayList<Book> extractBook(String SAMPLE_JSON)
    {
        ArrayList<Book> books = new ArrayList<>();
        try{
            JSONObject baseJsonResponse = new JSONObject(SAMPLE_JSON);

            JSONArray bookArray = baseJsonResponse.getJSONArray("items");
            for(int i =0;i<bookArray.length();i++)
            {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject info = currentBook.getJSONObject("volumeInfo");
                String Bname = info.getString("title");
                String Bauthor;
                if(info.has("authors"))
                {
                   Bauthor = info.optString("authors");
                }
                else
                    Bauthor = "unkonwn";

                Bauthor= Bauthor.replace("\"", "").replace("[", "").replace("]", "");
                JSONObject links = info.getJSONObject("imageLinks");
                String imgUrl = links.getString("thumbnail");
                String infoLink = info.getString("infoLink");
                Book book = new Book(Bname,Bauthor,imgUrl,infoLink);
                books.add(book);
            }

        }catch(JSONException e)
        {
            Log.e("QuerryUtils","Problem parsing book JSON results");
        }
        return books;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    public static List<Book> fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Book> Books = extractBook(jsonResponse);

        // Return the list of {@link Earthquake}s
        return Books;
    }
}
