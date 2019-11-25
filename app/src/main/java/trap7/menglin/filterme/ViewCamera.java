package trap7.menglin.filterme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

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
        bites = i.getByteArrayExtra("bytes");
        imgView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
    }
    public void deleteImg(View view){
        File fdelete = new File(imgPath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + imgPath);
            } else {
                System.out.println("file not Deleted :" + imgPath);
            }
        }
    }
    public void saveImg(View view) throws  IOException{
        File fsave = new File(imgPath);
        if (fsave.exists()) {
            OutputStream output = null;
            File root = Environment.getExternalStorageDirectory();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imgName = "/FILTERME_"+timeStamp+"_";
            String uniqueName = imgName+".jpg";
            String galleryFilePath = root.getAbsolutePath()+uniqueName;
            System.out.println(imgPath);
            System.out.println(galleryFilePath);
            try {
                output = new FileOutputStream(galleryFilePath);
                output.write(bites);
                fsave.delete();
            } finally {
                if (null != output) {
                    output.close();
                }
            }
        }
    }
}
///can u help me fix a problem