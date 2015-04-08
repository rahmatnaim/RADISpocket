/*
 * All Content are Copyright (c) University Technology Malaysia 2015.
 * Unauthorized use and/or duplication of all materials without express and written from UTM is strictly prohibited.
 * Excerpts and links may be used, provided that full and clear credit is given to
 * University Technology Malaysia with appropriate and specific direction to the original content.
 */

package my.utm.rmc.radispocket.connector;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.content.Intent;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.utm.rmc.radispocket.R;

/**
 * Created by rahmatnaim on 3/15/15.
 */
public class AllDataActivity extends ListActivity {

    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> dataList;

    private static String url_all_data = "http://161.139.30.13/radismob/get_all_download_data.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DATA = "data";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";

    JSONArray downloads = null;

    class LoadAllDatas extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllDataActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            //building parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_data, "GET", params);
            //check log data for JSON response
            Log.d("All Data : ", json.toString());

            try {
                //products found
                //getting Array of Products
                downloads = json.getJSONArray(TAG_DATA);

                for (int i = 0; i < downloads.length(); i++) {
                    JSONObject c = downloads.getJSONObject(i);

                    String id = c.getString(TAG_PID);
                    String name = c.getString(TAG_NAME);

                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put(TAG_PID, id);
                    hashMap.put(TAG_NAME, name);

                    dataList.add(hashMap);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            AllDataActivity.this, dataList,
                            R.layout.download_list_item, new String[]{TAG_PID, TAG_NAME},
                            new int[]{R.id.pid, R.id.lblListItem});
                    setListAdapter(adapter);
                }


            });
        }
    }

}


