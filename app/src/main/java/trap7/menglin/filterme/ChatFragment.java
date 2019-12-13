package trap7.menglin.filterme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ChatFragment extends Fragment {
    private String name;
    private String msg;
    public ChatFragment(String name, String msg) {
        super();
        this.name = name;
        this.msg = msg;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_chat_fragment, parent, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView nameView = (TextView) view.findViewById(R.id.nameView);
        nameView.setText(name);
        TextView msgView = (TextView) view.findViewById(R.id.msgView);
        nameView.setText(msg);
    }
//
//    public void buttonAction(View view) {
//        Intent i = new Intent(getContext(), clazz);
//        this.getContext().startActivity(i);
//    }
}
