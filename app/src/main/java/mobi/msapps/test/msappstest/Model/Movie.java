package mobi.msapps.test.msappstest.Model;

import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;


public class Movie {

    @SerializedName("title")
    @NonNull
    private String title;

    @SerializedName("releaseYear")
    private int year;

    @SerializedName("rating")
    private float rating;

    @SerializedName("image")
    private String image;

    @SerializedName("genre")
    private String[] genresAr;

    public Movie(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getGenresAr() {
        return genresAr;
    }

    public void setGenresAr(String genresAr[]) {
        this.genresAr = genresAr;
    }
}