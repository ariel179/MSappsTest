package mobi.msapps.test.msappstest.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Arrays;

import mobi.msapps.test.msappstest.Model.Movie;
import mobi.msapps.test.msappstest.R;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView posterImageView;
    private TextView titleTv, genresTv, yearTv;
    private RatingBar rating;
    private Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_movie_layout);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.movie_details);


        movie = MyApplication.getInstance().getMovieByTitle(getIntent().getStringExtra("title"));

        posterImageView = findViewById(R.id.detailedMoviePosterImageView);
        titleTv = findViewById(R.id.detailedMovieTitleTv);
        genresTv = findViewById(R.id.detailedMovieGenresTv);
        rating = findViewById(R.id.detailedMovieRatingBar);
        yearTv = findViewById(R.id.detailedMovieYearTv);

        titleTv.setText(movie.getTitle());
        yearTv.setText(movie.getYear() + "");
        genresTv.setText(Arrays.toString(movie.getGenresAr()));
        rating.setRating(movie.getRating()/2);

        AsyncTask downloadImageTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Bitmap bmp = null;
                try {
                    InputStream in = new java.net.URL(movie.getImage()).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Object o) {
                posterImageView.setImageBitmap((Bitmap) o);

            }
        };

        downloadImageTask.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
