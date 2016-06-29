package com.mycompany.aftest.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kosalgeek.android.caching.FileCacher;
import com.mycompany.aftest.R;
import com.mycompany.aftest.adapter.CustomListAdapter;
import com.mycompany.aftest.model.Promotions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = "https://www.abercrombie.com/anf/nativeapp/Feeds/promotions.json";
    private ProgressDialog pDialog;
    private List<Promotions> promotionsList = new ArrayList<Promotions>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, promotionsList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        final FileCacher<Promotions> cacher = new FileCacher<Promotions>(getApplicationContext(),"test");
        if(!isNetworkAlive()){
            if(cacher.hasCache()) {
                try {
                    List<Promotions> promotionsList1 = cacher.getAllCaches();
                    for (Promotions promotions : promotionsList1) {
                        promotionsList.add(promotions);
                    }

                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }

        JsonObjectRequest afrequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());
                hidePDialog();
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("promotions");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject af =jsonArray.getJSONObject(i);
                        Promotions promotions = new Promotions();
                        Log.d("check", af.getString("title"));
                        promotions.setName(af.getString("title"));
                        promotions.setThumbnailUrl(af.getString("image"));
                        promotions.setDescription(af.getString("description"));
                        if(af.has("footer")) {
                            promotions.setFooter(af.getString("footer"));
                        }
                        Object button = af.get("button");
                        if(button instanceof JSONArray) {
                            JSONArray buttonArray = (JSONArray) button;
                            for (int j = 0; j < buttonArray.length(); j++) {
                                JSONObject buttonObject = buttonArray.getJSONObject(j);
                                promotions.setButtonTarget(buttonObject.getString("target"));
                                promotions.setButtonTitle(buttonObject.getString("title"));

                            }
                        }else if(button instanceof JSONObject) {
                            JSONObject buttonObject2 = (JSONObject) button;
                            promotions.setButtonTarget(buttonObject2.getString("target"));
                            promotions.setButtonTitle(buttonObject2.getString("title"));
                        }


                        if(!cacher.hasCache()){
                            try {
                                cacher.appendOrWriteCache(promotions);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                if(cacher.getAllCaches().size() < jsonArray.length()){
                                    try {
                                        cacher.appendOrWriteCache(promotions);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }




                        try {
                            Log.d("cacher",""+cacher.getAllCaches().size());
                            Log.d("Array",""+ jsonArray.length());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        promotionsList.add(promotions);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                Log.d("shefeng", volleyError.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(afrequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    protected Boolean isNetworkAlive(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
