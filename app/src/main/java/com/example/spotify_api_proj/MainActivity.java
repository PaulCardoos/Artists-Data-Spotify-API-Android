package com.example.spotify_api_proj;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import com.android.volley.RequestQueue;
import com.example.spotify_api_proj.Services.Songs;
import com.example.spotify_api_proj.databinding.ActivityMainBinding;
import com.example.spotify_api_proj.model.Artist;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Artist> artists = new ArrayList<>();
    private ArrayList<String> artistNameList = new ArrayList<>();;
    private Songs songs;
    private RequestQueue q;

    //array adapter for list view
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //data binding for easy access
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        SharedPreferences sp = this.getSharedPreferences("SPOTIFY", 0);
        String name = sp.getString("name", null);
        binding.nameTxtViewName.setText(name);
        songs = new Songs(getApplicationContext());
        getArtistsForListView();

        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                artistNameList
        );
    }

    private void getArtistsForListView(){
        songs.getTopArtists(() -> {
            artists = songs.getArtists();
            updateListView(artists);
        });
    }

    private void updateListView(ArrayList<Artist> a) {
        artists = a;
        for(Artist artist : a ){
            artistNameList.add(artist.getName());
        }

        binding.ArtistListview.setAdapter(arrayAdapter);
        binding.ArtistListview.setOnItemClickListener((parent, view, position, id) -> {
            String name = artistNameList.get(position);

        });
    }
}
