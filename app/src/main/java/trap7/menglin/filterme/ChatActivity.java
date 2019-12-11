package trap7.menglin.filterme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private Button send;
    private EditText message;
    private TextView chat;

    private String username, roomname;
    private DatabaseReference root;
    private String tempKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        send = (Button) findViewById(R.id.sendButton);
        message = (EditText) findViewById(R.id.sendMessage);
        chat = (TextView) findViewById(R.id.chat);

        // username = getIntent().getExtras().get("user_name").toString();
        // roomname = getIntent().getExtras().get("room_name").toString();

        username = "forrest";
        roomname = "vegansociety";
        setTitle(" Room - "+roomname);

        root = FirebaseDatabase.getInstance().getReference().child(roomname);

        root.addChildEventListener(new ChildEventListener() {
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

            chat.append(chatUsername +" : "+chatMsg +" \n");
        }
    }

    public void sendMessage(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                tempKey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference messageRoot = root.child(tempKey);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",username);
                map2.put("msg",message.getText().toString());
                messageRoot.updateChildren(map2);
                message.setText("");
            }
        }