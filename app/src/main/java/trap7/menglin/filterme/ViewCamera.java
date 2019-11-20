package trap7.menglin.filterme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ViewCamera extends AppCompatActivity {
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_camera);
        Intent i = getIntent();
        String imgPath = i.getStringExtra("imagePath");
        imgView = findViewById(R.id.imageView);
        imgView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
    }
}
