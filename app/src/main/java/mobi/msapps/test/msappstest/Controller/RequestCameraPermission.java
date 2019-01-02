package mobi.msapps.test.msappstest.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class RequestCameraPermission {

    private Activity activity;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };

    public RequestCameraPermission(Activity activity) {
        this.activity = activity;
    }

    public  void verifyCameraPermissions() {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_CAMERA,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}