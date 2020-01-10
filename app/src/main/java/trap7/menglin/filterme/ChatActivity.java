package trap7.menglin.filterme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter otherAdapter;
    private LinearLayoutManager layoutManager;
    private Button send;
    private EditText message;
    private String username, roomname;
    private DatabaseReference root;
    private String tempKey;
    List<String> input = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        send = (Button) findViewById(R.id.sendButton);
        message = (EditText) findViewById(R.id.sendMessage);
        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseAuth auth = FirebaseAuth.getInstance(); //Initialize cloud firebase authentication
        username = auth.getCurrentUser().getDisplayName();
        otherAdapter = new ChatAdapter(input, username);
        recyclerView.setAdapter(otherAdapter);
        // roomname = getIntent().getExtras().get("room_name").toString();


//        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                recyclerView.smoothScrollToPosition(otherAdapter.getItemCount());
//            }
//        });

        roomname = "Chatroom";
        setTitle(" Room - "+roomname);

        root = FirebaseDatabase.getInstance().getReference().child(roomname);
        root.limitToLast(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                appendToChat(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                appendToChat(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private String chatMsg,chatUsername;

    private void appendToChat(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){
            chatMsg = (String) ((DataSnapshot)i.next()).getValue();
            chatUsername = (String) ((DataSnapshot)i.next()).getValue();
            input.add(chatUsername+":"+chatMsg);
            otherAdapter.notifyDataSetChanged();
            //layoutManager.smoothScrollToPosition( recyclerView, null, otherAdapter.getItemCount());

            //recyclerView.setAdapter(otherAdapter);
        }
    }

    public void sendMessage(View view) {
        if(!message.getText().toString().equals("")) {
            Map<String, Object> map = new HashMap<String, Object>();
            tempKey = root.push().getKey();
            root.updateChildren(map);

            DatabaseReference messageRoot = root.child(tempKey);
            Map<String, Object> map2 = new HashMap<String, Object>();
            if(!message.getText().toString().equals("")) {
                map2.put("name", username);
                map2.put("msg", message.getText().toString().trim());
                messageRoot.updateChildren(map2);
                message.setText("");
                otherAdapter.notifyDataSetChanged();
            }
            //recyclerView.setAdapter(otherAdapter);

        }
    }

}