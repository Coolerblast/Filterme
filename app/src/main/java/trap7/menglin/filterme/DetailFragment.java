package trap7.menglin.filterme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.     private String buttonname;
    private String name;
    private Class clazz;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public DetailFragment(String name, final Class<? extends Activity> clazz) {
        super();
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        pref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        return inflater.inflate(R.layout.fragment_foo, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        final Button button = (Button) view.findViewById(R.id.button);
        textView.setText(name);
        if(name.equals("Sign Out"))
            button.setText(name.toLowerCase());
        else
            button.setText("Open " + name.toLowerCase());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAction(view);
            }
        });
    }

    public void buttonAction(View view) {
        if(name.equals("Sign Out")){
            editor = pref.edit();
            editor.putBoolean("loggedin", false);
            editor.putString("email", "");
            editor.putString("password", "");
            editor.apply();
            Toast.makeText(this.getContext(), "Signed Out!", Toast.LENGTH_SHORT).show();

        }
        Intent i = new Intent(getContext(), clazz);
        this.getContext().startActivity(i);
    }
}