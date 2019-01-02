package mobi.msapps.test.msappstest.Controller;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mobi.msapps.test.msappstest.Model.Movie;
import mobi.msapps.test.msappstest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mainLayout, movieListLayout;
    private RecyclerView moviesRecyclerView;
    private FloatingActionButton qrCodeFab;
    private Handler handler = new Handler();
    private List<Movie> movies;
    private static final String path = "https://api.androidhive.info/json/";
    private MyApplication myApp = MyApplication.getInstance();
    private Activity activity;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            movieListLayout.setVisibility(View.VISIBLE);
            qrCodeFab.setVisibility(View.VISIBLE);
        }

    };
    private MovieListRecyclerAdapter recyclerAdapter;
    private final String MY_PREFS_NAME = "moviesSharedPrefs";
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        movieListLayout = findViewById(R.id.movieListLayout);
        mainLayout = findViewById(R.id.mainLayout);
        moviesRecyclerView = findViewById(R.id.movieListRecyclerView);
        qrCodeFab = findViewById(R.id.qrCodeFab);

        //stops images from reloading on scroll (allows them to cache)
        moviesRecyclerView.setItemViewCacheSize(800);

        mainLayout.getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);



        handler.postDelayed(runnable, 2500);

        movies = Arrays.asList();

        readMoviesFromSharedPreferences();

        if (movies.size() > 0) {
            setupRecyclerView();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(path)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);
            Call<List<Movie>> call = api.getMovies();

            call.enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {

                    movies = response.body();
                    myApp.setMovies(new ArrayList<>(movies));
                    editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

                    for (int i = 0; i < movies.size(); i++) {
                        editor.putString("title"+i, movies.get(i).getTitle());
                        editor.putInt("year"+i, movies.get(i).getYear());
                        editor.putFloat("rating"+i,movies.get(i).getRating());
                        String genresString = Arrays.toString(movies.get(i).getGenresAr());
                        String genrestring2 = genresString.substring(1,genresString.length()-1);
                        editor.putString("genres"+i,genrestring2);
                        editor.putString("image"+i,movies.get(i).getImage());
                        editor.apply();
                    }

                setupRecyclerView();

                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        qrCodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},1);
                }else {
                    Intent qrIntent = new Intent(MainActivity.this, QrScannerActivity.class);
                    startActivity(qrIntent);
                }
            }
        });


    }

    private void setupRecyclerView(){

        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {

                Integer movie1Year = movie1.getYear();
                Integer movie2Year = movie2.getYear();

                return movie2Year.compareTo(movie1Year);
            }
        });

        recyclerAdapter = new MovieListRecyclerAdapter(this, movies);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        moviesRecyclerView.setAdapter(recyclerAdapter);
    }

    private List<Movie> readMoviesFromSharedPreferences(){
        movies = new ArrayList<>();
        int counter = 0;
        String title = prefs.getString("title"+counter, null);
        while(title!=null){
            Movie movie = new Movie();
            title = prefs.getString("title"+counter, null);
            movie.setTitle(title);
            movie.setImage(prefs.getString("image"+counter, null));
            movie.setRating(prefs.getFloat("rating"+counter, -1));
            movie.setYear(prefs.getInt("year"+counter, -1));
            String genres = prefs.getString("genres",null);
            if (genres!=null) {
                movie.setGenresAr(genres.split(","));
                movies.add(movie);
            }


            counter++;

        }
        if (counter == 0){
            return null;
        }
        return movies;
    }
}

interface Api {
    @GET("movies.json")
    Call<List<Movie>> getMovies();

}