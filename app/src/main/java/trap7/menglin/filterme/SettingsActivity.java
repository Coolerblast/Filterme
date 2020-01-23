package trap7.menglin.filterme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Button demo, glasses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }
    public void onClick(View view){
        editor = pref.edit();
        switch (view.getId()){
            case R.id.demoBtn:
                editor.putString("Filter", "Demo");
                Toast.makeText(this, "Demo Filter Chosen", Toast.LENGTH_LONG).show();
                break;
            case R.id.glassesBtn:
                editor.putString("Filter", "Glasses");
                Toast.makeText(this, "Glasses Filter Chosen", Toast.LENGTH_LONG).show();
                break;
        }
        editor.apply();
    }
}
