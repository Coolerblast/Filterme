package trap7.menglin.filterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    Button btnLogin;
    Button btnSignUp;
    TextView login;
    EditText edtEmail;
    EditText edtPassword;
    boolean loggedin;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("loggedin", false))
        {
            email = pref.getString("email", email);
            password = pref.getString("password", password);
            login();
        }
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        loggedin = false;
        btnSignUp = findViewById(R.id.btnSignUp);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnLogin:
                email = edtEmail.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                login();


                break;
            case R.id.btnSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }
    private void login() {
        /*
        Check for empty fields to avoid exception
         */
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_LONG).show();
            return;
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user = auth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            loggedin = true;
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Login unsuccessful. Check details", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = pref.edit();
        if(loggedin) {
            editor.putBoolean("loggedin", true);
            editor.putString("email", email);
            editor.putString("password", password);
        }
        editor.apply();
    }
}
