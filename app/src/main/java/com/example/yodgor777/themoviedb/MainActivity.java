package com.example.yodgor777.themoviedb;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.orm.SugarContext;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView MovieList;
    ArrayList<MovieDataHolder> MovieItems = new ArrayList<>();
    NetworkHelper networkHelper;
    MovieAdapter adapter;
    ProgressDialog progressDialog;
    Spinner popularitySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.activity_main);
        networkHelper = NetworkHelper.getInstance(this);
        MovieList = (RecyclerView) findViewById(R.id.MovieList);
        popularitySpinner = (Spinner) findViewById(R.id.popularitySpinner);

        adapter = new MovieAdapter(this, MovieItems);
        MovieList.setLayoutManager(new GridLayoutManager(this, 2));
        MovieList.setItemAnimator(new DefaultItemAnimator());
        MovieList.setAdapter(adapter);

        ArrayList<String> data = new ArrayList<String>();
        data.add("Popular");
        data.add("highest rated");

        data.add("favorites");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_dropdown_item, data);
        popularitySpinner.setAdapter(spinnerAdapter);
        popularitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String Url;
                if (i == 0) {
                    Url = "http://api.themoviedb.org/3/movie/popular?api_key=45bf6592c14a965b33549f4cc7e6c664";
                    loadMoviewList(Url);

                } else if (i == 1) {
                    Url = "http://api.themoviedb.org/3/movie/top_rated?api_key=45bf6592c14a965b33549f4cc7e6c664";
                    loadMoviewList(Url);
                } else if (i == 2) {



                    List<FavoriteMovie> favoriteMoviesList = FavoriteMovie.listAll(FavoriteMovie.class);

                    for (FavoriteMovie movie : favoriteMoviesList) {
                        Log.i("sddm", "movieId: " + movie.getMovieID());
                        Log.i("sddm", "movieName: " + movie.getMovieName());


                        loadMoviewFavoriteList(favoriteMoviesList);

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadMoviewFavoriteList(List<FavoriteMovie> favoriteMoviesList) {
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Hang on....");
        progressDialog.show();


        progressDialog.dismiss();
        MovieItems.clear();
        for (FavoriteMovie movie : favoriteMoviesList) {

            MovieDataHolder dataHolder = new MovieDataHolder();
            dataHolder.id = movie.getMovieID();
            dataHolder.Title = movie.getMovieName();

            MovieItems.add(dataHolder);
        }
        adapter.notifyDataSetChanged();

    }


    private void loadMoviewList(String Url) {
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Hang on....");
        progressDialog.show();

        networkHelper.doNetworkRequestFor(Url, new NetworkHelper.NetworkCallBack() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                MovieItems.clear();
                Log.d("movies", "onSuccess: " + response);
                try {
                    JSONObject responceObject = new JSONObject(response);
                    JSONArray results = responceObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movieObject = results.getJSONObject(i);
                        MovieDataHolder dataHolder = new MovieDataHolder();
                        dataHolder.id = movieObject.getString("id");
                        dataHolder.ImageUrl = "https://image.tmdb.org/t/p/w185/" + movieObject.getString("backdrop_path");
                        dataHolder.Title = movieObject.getString("title");
                        dataHolder.OverView = movieObject.getString("overview");
                        MovieItems.add(dataHolder);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });
    }
}
