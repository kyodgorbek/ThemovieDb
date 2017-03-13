package com.example.yodgor777.themoviedb;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.yodgor777.themoviedb.R.id.add_favorite_btn;

public class MovieDetailsActivity extends AppCompatActivity {
    ProgressDialog dialog;
    NetworkHelper networkHelper;
    TextView movieTitle;
    TextView movieRelease;
    TextView movieVote;
    TextView movieSynopsis;
    MyListView VideoList, reviews;
    Button addToFavoritesBtn;
    ArrayList<VideosDataHolder> videolist = new ArrayList<>();


    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SugarContext.init(this);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        movieVote = (TextView) findViewById(R.id.vote);
        movieSynopsis = (TextView) findViewById(R.id.movie_synopsis);
        movieRelease = (TextView) findViewById(R.id.release);
        addToFavoritesBtn = (Button) findViewById(add_favorite_btn);


        networkHelper = NetworkHelper.getInstance(this);
        movieTitle = (TextView) findViewById(R.id.title);

        VideoList = (MyListView) findViewById(R.id.VideoList);

        reviews = (MyListView) findViewById(R.id.reviews);
        id = getIntent().getStringExtra("MovieId");

        String title = getIntent().getStringExtra("MovieTitle");
        String MovieImage = getIntent().getStringExtra("MovieImage");

        getSupportActionBar().setTitle(title);


        ImageView BannerImage = (ImageView) findViewById(R.id.BannerImage);
        Picasso.with(this).load(MovieImage).fit().centerCrop().into(BannerImage);
        getMovieDetails(id);

        VideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videolist.get(i).url)));
            }
        });



        addToFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                Log.i("sddm", "onClick: movie id:"+id);
                Log.i("sddm", "onClick: movie name:"+movieTitle.getText().toString());
                FavoriteMovie favoriteMovie = new FavoriteMovie(id, movieTitle.getText().toString());
                favoriteMovie.save();

                Toast.makeText(MovieDetailsActivity.this,"Movie added to favorites list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMovieDetails(final String id) {
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.show();
        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=45bf6592c14a965b33549f4cc7e6c664&language=en-US";
        networkHelper.doNetworkRequestFor(url, new NetworkHelper.NetworkCallBack() {
            @Override
            public void onSuccess(String response) {
                dialog.dismiss();
                Log.d("response", "onSuccess: " + response);
                try {

                    JSONObject ResponseObject = new JSONObject(response);

                    movieTitle.setText(ResponseObject.getString("original_title"));
                    movieRelease.setText(ResponseObject.getString("release_date"));
                    movieVote.setText(ResponseObject.getString("vote_average") + " out of 10");
                    movieSynopsis.setText(ResponseObject.getString("overview"));

                    Picasso.with(MovieDetailsActivity.this).load("https://image.tmdb.org/t/p/w185/" + ResponseObject.getString("poster_path")).fit().centerCrop().into((ImageView) findViewById(R.id.BannerImage));
                    getMovieVideos(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error) {
                dialog.dismiss();
            }
        });
    }

    private void getMovieVideos(final String id) {

        String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=45bf6592c14a965b33549f4cc7e6c664&language=en-US";
        networkHelper.doNetworkRequestFor(url, new NetworkHelper.NetworkCallBack() {
            @Override
            public void onSuccess(String response) {

                Log.d("response", "videos: " + response);
                try {

                    JSONObject ResponseObject = new JSONObject(response);
                    JSONArray videosArray = ResponseObject.getJSONArray("results");
                    for (int i = 0; i < videosArray.length(); i++) {
                        JSONObject video = videosArray.getJSONObject(i);

                        VideosDataHolder dataHolder = new VideosDataHolder();
                        dataHolder.name = video.getString("name");
                        dataHolder.url = "http://www.youtube.com/watch?v=" + video.getString("key");
                        videolist.add(dataHolder);
                        // }
                    }
                    Log.d("video", "onSuccess: " + videolist.size());
                    videoListAdapter videosAdapter = new videoListAdapter(MovieDetailsActivity.this, videolist);
                    VideoList.setAdapter(videosAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                getMovieReviews(id);
            }


            @Override
            public void onError(VolleyError error) {
                dialog.dismiss();
            }
        });
    }

    private void getMovieReviews(String id) {

        String url = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=45bf6592c14a965b33549f4cc7e6c664&language=en-US";
        networkHelper.doNetworkRequestFor(url, new NetworkHelper.NetworkCallBack() {
            @Override
            public void onSuccess(String response) {
                ArrayList<reviewDataHolder> reviewList = new ArrayList<reviewDataHolder>();
                Log.d("response", "onSuccess: " + response);
                try {

                    JSONObject ResponseObject = new JSONObject(response);
                    JSONArray reviewsArray = ResponseObject.getJSONArray("results");
                    for (int i = 0; i < reviewsArray.length(); i++) {
                        JSONObject review = reviewsArray.getJSONObject(i);
                        reviewDataHolder dataHolder = new reviewDataHolder();
                        dataHolder.userName = review.getString("author");
                        dataHolder.Comment = review.getString("content");
                        reviewList.add(dataHolder);
                    }
                    reviews.setAdapter(new ReviewAdapter(MovieDetailsActivity.this, reviewList));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}
