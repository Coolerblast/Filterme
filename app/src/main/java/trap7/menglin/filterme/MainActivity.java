package trap7.menglin.filterme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    private static final int MY_CAMERA_REQUEST_CODE = 200;
    private static final int MY_WRITE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_REQUEST_CODE);


        // Begin the transaction
        FragmentTransaction ftCamera = getSupportFragmentManager().beginTransaction();
        ftCamera.add(R.id.placeholderCamera, new DetailFragment("Camera", CameraActivity.class));
        ftCamera.commit();

        FragmentTransaction ftSettings = getSupportFragmentManager().beginTransaction();
        ftSettings.add(R.id.placeholderSettings, new DetailFragment("Settings", SettingsActivity.class));
        ftSettings.commit();

        FragmentTransaction ftCredits = getSupportFragmentManager().beginTransaction();
        ftCredits.add(R.id.placeholderCredits, new DetailFragment("Credits", CreditsActivity.class));
        ftCredits.commit();

        FragmentTransaction ftChat = getSupportFragmentManager().beginTransaction();
        ftChat.add(R.id.placeholderChat, new DetailFragment("Chat", ChatActivity.class));
        ftChat.commit();
        FragmentTransaction ftSignOut = getSupportFragmentManager().beginTransaction();
        ftChat.add(R.id.placeholderSignOut, new DetailFragment("Sign Out", LoginActivity.class));
        ftSignOut.commit();
    }
}
