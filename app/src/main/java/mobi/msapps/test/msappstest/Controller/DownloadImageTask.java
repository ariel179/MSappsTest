package mobi.msapps.test.msappstest.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... url) {

        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(url[0]).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bmp;
    }
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}