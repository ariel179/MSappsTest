package mobi.msapps.test.msappstest.Controller;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import mobi.msapps.test.msappstest.Model.Movie;
import mobi.msapps.test.msappstest.R;

public class MovieListRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private List<Movie> movies;
    private Activity context;

    public MovieListRecyclerAdapter(Activity context, List<Movie> movieList){

        this.movies = movieList;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_row,
                parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        holder.titleTv.setText(movies.get(position).getTitle());
        holder.genreTv.setText(Arrays.toString(movies.get(position).getGenresAr()));
        holder.releaseYearTv.setText(movies.get(position).getYear()+"");
        holder.ratingBar.setRating(movies.get(position).getRating()/2);
        AsyncTask downloadImageTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Bitmap bmp = null;
                try {
                    InputStream in = new java.net.URL(movies.get(position).getImage()).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Object o) {
                holder.poster.setImageBitmap((Bitmap)o);
                holder.poster.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
            }
        };

        downloadImageTask.execute();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair[] transitionPairs = new Pair[1];
                transitionPairs[0] = new Pair<View,String>(holder.poster,"posterTransition");
                //transitionPairs[1] = new Pair<View,String>(holder.titleTv,"titleTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context,transitionPairs);
                Intent intent = new Intent(context,MovieDetailActivity.class);
                intent.putExtra("title",movies.get(position).getTitle());
                context.startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}

class MovieViewHolder extends RecyclerView.ViewHolder {

    TextView titleTv, releaseYearTv, genreTv;
    ImageView poster;
    RatingBar ratingBar;
    ProgressBar progressBar;
    public MovieViewHolder(View itemView) {
        super(itemView);
        titleTv = itemView.findViewById(R.id.movieRowTitleTv);
        releaseYearTv = itemView.findViewById(R.id.movieRowReleaseYearTv);
        genreTv = itemView.findViewById(R.id.movieRowGenresTv);
        poster = itemView.findViewById(R.id.movieRowPosterImageView);
        ratingBar = itemView.findViewById(R.id.movieRowRatingBar);
        this.progressBar = itemView.findViewById(R.id.imageRowProgressBar);
    }
}
