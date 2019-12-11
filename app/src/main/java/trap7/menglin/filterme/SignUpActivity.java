package trap7.menglin.filterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    Button btnSignUp;
    EditText edtEmail, edtName, edtPassword;
    FirebaseAuth auth;
    FirebaseFirestore database;
    FirebaseUser user;
    String name, email, password, userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        auth = FirebaseAuth.getInstance(); //Initialize cloud firebase authentication
        database = FirebaseFirestore.getInstance(); //Initialize cloud firestore
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                signupUser();
                break;
        }
    }

    private void signupUser() {
        name = edtName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this,"Enter name",Toast.LENGTH_LONG).show();
            return;}
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Enter email",Toast.LENGTH_LONG).show();
            return;}
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Enter password",Toast.LENGTH_LONG).show();
            return;}

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        /*
                        Get current user from firebase authentication, then set username as user display name
                         */
                            user = auth.getCurrentUser();
                            userID = user.getUid(); //retrieve the user id so we can later use as key in the database
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdate);
                            addUserToDatabase();
                            Toast.makeText(getApplicationContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Registration error. Check your details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void addUserToDatabase(){
        HashMap<String, String> userData = new HashMap<>();
        userData.put("a1_name", name);
        userData.put("a2_email", email);
        userData.put("a3_password", password);

        Map<String, Object> update = new HashMap<>();
        update.put(userID, userData);
        database.collection("users").document(userID).set(update); //update details to Firestore
    }
}
