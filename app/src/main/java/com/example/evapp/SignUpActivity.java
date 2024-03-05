package com.example.evapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
                performSignUp();
                }

        });
    }

    private void performSignUp() {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        int type = getIntent().getIntExtra("type", 0);

        // Simulate successful signup
        Toast.makeText(this, "Signed up as " + name, Toast.LENGTH_SHORT).show();

        // Start SetCredentialsActivity
        Intent intent = new Intent(SignUpActivity.this, SetCredentialsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("type", type); // Pass the login type
        Log.e("astagfirullah", intent.getExtras().toString());
        startActivity(intent);
    }

}
