package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


public class SetCredentialsActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_credentials);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Save the username and password (not shown here)

                // Redirect to LoginActivity
                Intent intent = new Intent(SetCredentialsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}


