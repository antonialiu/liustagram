package com.codepath.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private Button loginBtn;
    private Button bSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailInput = findViewById(R.id.etEmail);
        usernameInput = findViewById(R.id.etUsername2);
        passwordInput = findViewById(R.id.etPassword2);
        bSignup = findViewById(R.id.bSignup2);


        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String email = emailInput.getText().toString();

                signup(username, password, email);
            }
        });
    }

    private void signup(String username, String password, String email) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Set custom properties
        // user.put("phone", "650-253-0000");
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("SignupActivity", "Signup successful");
                    final Intent intent = new Intent(SignupActivity.this, TimelineActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("SignupActivity", "Signup failure");
                    Toast.makeText(getApplicationContext(),"Sign up failed; please enter a username, password, and a valid email address", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


    }
}
