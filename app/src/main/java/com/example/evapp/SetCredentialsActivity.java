package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


public class SetCredentialsActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button saveButton;
    private AppDatabase db;
    private UserDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_credentials);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "UserDatabase").allowMainThreadQueries().build();
        dao = db.userDao();

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("inshallah" + getIntent().getExtras());
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                int type = getIntent().getIntExtra("type", 0);

                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Save the username and password (not shown here)
                if (type == 0) {
                    dao.insertUser(username, password, name, email);
                } else {
                    dao.insertMember(username, password, name, email);
                }

                // Redirect to LoginActivity
                Intent intent = new Intent(SetCredentialsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Log.e("inshallah", "ERROR");


            }
        });
    }
}


