package com.example.evapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    performLogin();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInput() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    private void performLogin() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Simulate successful login
        Toast.makeText(this, "Logged in as " + username, Toast.LENGTH_SHORT).show();

        // Start MapsActivity
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

}
