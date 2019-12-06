package trap7.menglin.filterme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewCamera extends AppCompatActivity {
    ImageView imgView;
    String imgPath;
    byte[] bites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_camera);
        Intent i = getIntent();
        imgPath = i.getStringExtra("imagePath");
        imgView = findViewById(R.id.imageView);

        imgView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
    }
    public void deleteImg(View view){
        File fdelete = new File(imgPath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Toast toast=Toast.makeText(getApplicationContext(),"Image Deleted!", Toast. LENGTH_SHORT);
                toast.show();
                Intent i = new Intent(this, CameraActivity.class);
                this.startActivity(i);
                this.finish();
            } else {
                Toast toast=Toast.makeText(getApplicationContext(),"Image Not Deleted!", Toast. LENGTH_SHORT);
                toast.show();

            }
        }
    }
    public void saveImg(View view) throws  IOException{
        Bitmap image = BitmapFactory.decodeFile(imgPath);  // Gets the Bitmap
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imagTitle = "FILTERME_"+timeStamp;
        MediaStore.Images.Media.insertImage(getContentResolver(), image, imagTitle , timeStamp);  // Saves the image.
        File fdelete = new File(imgPath);
        if (fdelete.exists()) {
            fdelete.delete();
        }
        Toast toast=Toast.makeText(getApplicationContext(),"Image Saved!", Toast. LENGTH_SHORT);
        toast.show();
        Intent i = new Intent(this, CameraActivity.class);
        this.startActivity(i);
        this.finish();
    }
}
///can u help me fix a problem