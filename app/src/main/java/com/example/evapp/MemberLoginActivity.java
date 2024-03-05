package com.example.evapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

public class MemberLoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button memberloginButton;
    private Button membersignUpButton;
    private AppDatabase db;
    private UserDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "UserDatabase").allowMainThreadQueries().build();
        dao = db.userDao();

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        memberloginButton = findViewById(R.id.login_button);
        membersignUpButton = findViewById(R.id.signup_button);

        memberloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    performLogin();
                }
            }
        });

        membersignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberLoginActivity.this, SignUpActivity.class);
                intent.putExtra("type", 1); // Pass the login type
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

    private void performLogin(){
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (dao.memberExists(username, password)) {
            // Simulate successful login
            Toast.makeText(this, "Member login successful " + username, Toast.LENGTH_SHORT).show();

            // Start MapsActivity
            Intent intent = new Intent(MemberLoginActivity.this, MemberViewActivity.class);
            startActivity(intent);
            finish();

        } else Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();

    }

}
