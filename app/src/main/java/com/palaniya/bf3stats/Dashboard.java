package com.palaniya.bf3stats;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.palaniya.bf3stats.utils.AppUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Dashboard extends Activity {

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        jsonTest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void jsonTest() {
        String platform = "pc";
        String type = "player";
        String playerName = "classifiedBenQ";

        new RetrieveFeedTask().execute(AppUtils.APP_URL);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {

                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(AppUtils.APP_URL);

                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("platform", "pc");
                jsonObject.accumulate("DataGroup", "player");
                jsonObject.accumulate("player", "classifiedBenQ");
                // jsonObject.accumulate("twitter", person.getTwitter());

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                // ** Alternative way to convert Person object to JSON string using
                // Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);

                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);

                // 6. set httpPost Entity
                httpPost.setEntity(se);

                // 7. Set some headers to inform server about the type of the
                // content
                // httpPost.setHeader("Accept", "application/json");
                //httpPost.setHeader("Content-type", "application/json");

                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                } else {
                    result = "Did not work!";
                }

                Log.i("JSON", "" + result);
            } catch (Exception e) {
                Log.e("InputStream", "" + e);
            }
            return result;
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            Log.d("OnPost", "" + feed);
        }
    }
}
