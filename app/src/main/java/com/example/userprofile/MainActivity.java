package com.example.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LanguageManager lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("general_settings", MODE_PRIVATE);
        String selectedLanguage = prefs.getString("lang_code", "en");
        lang = new LanguageManager(this);
        lang.updateResource(selectedLanguage);
        setContentView(R.layout.activity_main);

        // Set title
        getSupportActionBar().setTitle("User Settings");

        // Open create profile page
        Button createButton = findViewById(R.id.createProfileButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
                startActivity(intent);
            }
        });

        Button viewButton = findViewById(R.id.viewProfileButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayProfileActivity.class);
                startActivity(intent);
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File dir = getFilesDir();
                File myFile = new File(dir, CreateProfileActivity.PROFILE_FILE_NAME);
                if (myFile.delete()) {
                    Toast.makeText(MainActivity.this,"File Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,"File does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button settingsBtn = findViewById(R.id.generalSettingsButton);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new intent to open settings activity.
                Intent i = new Intent(MainActivity.this, GeneralSettings.class);
                startActivity(i);
            }
        });


    }
}
