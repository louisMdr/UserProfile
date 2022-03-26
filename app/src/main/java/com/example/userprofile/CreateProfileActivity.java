package com.example.userprofile;


import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

public class CreateProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private EditText editFullName, editDOB, editWeight;
    private RadioGroup radioGroupGender;
    private RadioButton radioGroupGenderSelected;
    private RadioButton radioButtonMale;
    User user;
    private double weight;
    protected static final String PROFILE_FILE_NAME = "profile.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        getSupportActionBar().setTitle("Create Profile");
        Toast.makeText(CreateProfileActivity.this,"Please create a profile now",Toast.LENGTH_LONG).show();

        editFullName = findViewById(R.id.editFullName);
        editDOB = findViewById(R.id.editDOB);
        editWeight = findViewById(R.id.editWeight);

        // Radiobuttons for gender
        radioGroupGender = findViewById(R.id.radioGroupGender);
        // To clear button when started or resumed
        radioGroupGender.clearCheck();
        radioButtonMale  = findViewById(R.id.radioButtonMale);

        // Datepicker for date of birth
        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Action that happens when save if pressed
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderID = radioGroupGender.getCheckedRadioButtonId();
                radioGroupGenderSelected = findViewById(selectedGenderID);

                // Get input data
                String textFN = editFullName.getText().toString();
                String textDOB = editDOB.getText().toString();
                String textWeight = editWeight.getText().toString();
                String textGender;

                // Check if date fields are empty
                if(textFN == null || textFN.equals("") || textFN.isEmpty()) {
                    Toast.makeText(CreateProfileActivity.this,"Please enter your full name", Toast.LENGTH_LONG).show();
                    editFullName.setError("Full name is required");
                    editFullName.requestFocus();
                } else if(textDOB == null || textDOB.equals("") || textDOB.isEmpty()) {
                    Toast.makeText(CreateProfileActivity.this,"Please enter your birthdate", Toast.LENGTH_LONG).show();
                    editDOB.setError("Birthdate is required");
                    editDOB.requestFocus();
                } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CreateProfileActivity.this,"Please select your gender", Toast.LENGTH_LONG).show();
                    radioButtonMale.setError("Gender is required");
                    radioButtonMale.requestFocus();
                } else if (textWeight == null || textWeight.equals("") || textWeight.isEmpty() || textWeight.equals("0")) {
                    Toast.makeText(CreateProfileActivity.this,"Please enter your weight", Toast.LENGTH_LONG).show();
                    editWeight.setError("Weight is required");
                    editWeight.requestFocus();
                } else {
                    textGender = radioGroupGenderSelected.getText().toString();
                    weight = Double.parseDouble(textWeight);
                    // Average weight for 13 year olds is ~45 kg and Max weight for average bike ~136 kg
                    if (weight >= 45 && weight <= 136) {
                        user = new User(textFN,textDOB,textGender,weight);
                        saveProfile(user);
                        // Redirect to view profile page
                        Intent displayProfileIntent = new Intent(CreateProfileActivity.this , DisplayProfileActivity.class);
                        startActivity(displayProfileIntent);
                    } else {
                        Toast.makeText(CreateProfileActivity.this,"Invalid weight", Toast.LENGTH_LONG).show();
                        editWeight.setError("Weight is required");
                        editWeight.requestFocus();
                    }
                }
            }
        });
    }

    // Source code taken from https://github.com/mitchtabian/DatePickerDialog-Example
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        // Only set date if user is 13 or up
        if (getAge(year, month, dayOfMonth) < 13){
            editDOB.setError("Invalid Age! You are too young to create a profile");
            editDOB.requestFocus();
            Toast.makeText(this, "Invalid Age! You are too young to create a profile", Toast.LENGTH_LONG).show();
        } else {
            editDOB.setText(date);
        }
    }

    public void saveProfile(User u) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new  FileOutputStream(getFilesDir() + "/" + PROFILE_FILE_NAME,false);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(u);
            oos.close();
            fos.close();
            Toast.makeText(this, "File saved to " + getFilesDir() + "/" + PROFILE_FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Source code taken from https://stackoverflow.com/questions/38967422/calculate-age-from-birthdate
    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }
}