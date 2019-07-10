package com.codepath.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private Button loginBtn;
    private Button bSignup;
//    private String usernameToPass;
//    private String passwordToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {

            final Intent intent = new Intent(LoginActivity.this, TimelineActivity.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_login);

            usernameInput = findViewById(R.id.etUsername);
            passwordInput = findViewById(R.id.etPassword);
            loginBtn = findViewById(R.id.bLogin);
            emailInput = findViewById(R.id.etEmail);
            bSignup = findViewById(R.id.bSignup);

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = usernameInput.getText().toString();
                    final String password = passwordInput.getText().toString();

                    login(username, password);
                }
            });

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
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState){
//
//        outState.putString("username", usernameToPass);
//        outState.putString("password", passwordToPass);
//
//        super.onSaveInstanceState(outState);
//
//    }


    private void login(final String username, final String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {

//                    usernameToPass = username;
//                    passwordToPass = password;

                    Log.d("LoginActivity", "Login successful");
                    final Intent intent = new Intent(LoginActivity.this, TimelineActivity.class);
                    startActivity(intent);
                }
                else {
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
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
                    final Intent intent = new Intent(LoginActivity.this, TimelineActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("SignupActivity", "Signup failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
