package com.example.spotify_api_proj.data;
import android.content.SharedPreferences;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.example.spotify_api_proj.model.User;
import java.util.HashMap;
import java.util.Map;

public class Repo {

    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private SharedPreferences msharedPreferences;
    private RequestQueue q;
    private User user;

    public Repo(){}


    public Repo(RequestQueue queue, SharedPreferences sharedPreferences) {
        q = queue;
        msharedPreferences = sharedPreferences;
    }

    public User getUser() {
        return user;
    }

    //make request
    public void get(final AsyncHandler callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, response -> {
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), User.class);
            callBack.finished();
        }, error -> get(() -> {

        })) {
            //need this override method to provide header to request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        q.add(jsonObjectRequest);
    }


}