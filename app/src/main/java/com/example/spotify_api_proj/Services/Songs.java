package com.example.spotify_api_proj.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotify_api_proj.data.AsyncHandler;
import com.example.spotify_api_proj.model.Artist;
import com.example.spotify_api_proj.model.Song;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Songs {
    private ArrayList<Artist> artists = new ArrayList<Artist>();
    private SharedPreferences sp;
    private RequestQueue q;

    public Songs(Context context){
        sp = context.getSharedPreferences("SPOTIFY", 0);
        q = Volley.newRequestQueue(context);
    }


    public  ArrayList<Artist> getArtists(){
        return artists;
    }

    public ArrayList<Artist> getTopArtists(final AsyncHandler callback){
        String url = "https://api.spotify.com/v1/me/top/artists";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            Gson gson = new Gson();
            JSONArray items = response.optJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                try{
                    //Log.d("ITEMS", String.valueOf(items));
                    JSONObject obj = items.getJSONObject(i);
                    int followers = obj.getJSONObject("followers").getInt("total");
                        //Log.d("FOLLOWERRS", String.valueOf(followers));
                        int popularity = obj.getInt("popularity");
                        //Log.d("POPULARITY", String.valueOf(popularity));
                        String id = obj.getString("id");
                        //Log.d("ID", String.valueOf(id));
                        String name = obj.getString("name");
                        //Log.d("NAME", String.valueOf(name));


                    Artist artist = new Artist(name, id, followers, popularity);

                    artists.add(artist);


                } catch (JSONException e){

                }
            }
            callback.finished();

        }, error -> getTopArtists(() -> {

        })) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap();
                String token = sp.getString("token", "");
                Log.d("TOKEEN FROM SONGS", token);
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }

        };
        q.add(jsonObjectRequest);

        return artists;

    }
}
