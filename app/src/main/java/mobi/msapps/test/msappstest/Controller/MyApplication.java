package mobi.msapps.test.msappstest.Controller;

import android.app.Application;

import java.util.ArrayList;

import mobi.msapps.test.msappstest.Model.Movie;

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private ArrayList<Movie> movies;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        movies = new ArrayList<>();

    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public Movie getMovieByTitle(String title){
        for (int i =0; i < movies.size(); i++){
            if (title.equals(movies.get(i).getTitle())){
                return movies.get(i);
            }
        }
        return null;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

}