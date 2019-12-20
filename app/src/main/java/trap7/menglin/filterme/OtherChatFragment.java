package trap7.menglin.filterme;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChatFragment extends Fragment {
    private String name;
    private String msg;
    private boolean recieved;
    private String time;
    public ChatFragment(String name, String msg, String time, boolean recieved) {
        super();
        this.name = name;
        this.msg = msg;
        this.recieved = recieved;
        this.time = time;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        if (recieved)
            return inflater.inflate(R.layout.item_message_recieved, parent, true);
        return inflater.inflate(R.layout.item_message_sent, parent, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView nameView = (TextView) view.findViewById(R.id.message_name);
        nameView.setText(name);
        TextView msgView = (TextView) view.findViewById(R.id.message_body);
        nameView.setText(msg);
    }
//
//    public void buttonAction(View view) {
//        Intent i = new Intent(getContext(), clazz);
//        this.getContext().startActivity(i);
//    }
}
