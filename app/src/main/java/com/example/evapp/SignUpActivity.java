package com.example.evapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText emailInput;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        signUpButton = findViewById(R.id.signup_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the input
                if (validateInput()) {
                    // Start SetCredentialsActivity
                    Intent intent = new Intent(SignUpActivity.this, SetCredentialsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateInput() {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void performSignUp() {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();

        // Simulate successful signup
        Toast.makeText(this, "Signed up as " + name, Toast.LENGTH_SHORT).show();

        // Start SetCredentialsActivity
        Intent intent = new Intent(SignUpActivity.this, SetCredentialsActivity.class);
        startActivity(intent);
    }

}
