package com.example.yodgor777.themoviedb;

/**
 * Created by yodgor777 on 2017-02-08.
 */

public class FavoriteMovie {
    String movieID;
    String movieName;


    public FavoriteMovie() {

    }

    public FavoriteMovie(String movieID, String movieName) {
        this.movieID = movieID;
        this.movieName = movieName;
    }

    public String getMovieID() {
        return this.movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public static void findById(int add_favorite_btn) {
    }
}