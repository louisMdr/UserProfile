package com.example.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class GeneralSettings extends AppCompatActivity {
    private SeekBar seekSB;
    private SeekBar seekPB;
    private RadioGroup radioGroupUnits;
    private RadioButton radioButtonMiles;
    private RadioButton radioButtonKM;
    private SharedPreferences preferences;
    private static final String SB_PROGRESS = "sb_progress";
    private static final String PB_PROGRESS = "pb_progress";
    private static final String LANG_PROGRESS = "spinner_progress";
    private static final String UNIT_PROGRESS = "unit_progress";
    EditText eTTest;
    private TextToSpeech tts;
    private static final String[] languages = new String[]{"Language","en", "fr"};
    Spinner languageSpinner;
    LanguageManager lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);

        // Set title
        getSupportActionBar().setTitle("General Settings");

        seekSB = findViewById(R.id.sBSpeechRate);
        seekPB = findViewById(R.id.sBPitchRate);
        eTTest = findViewById(R.id.eTTest);
        Button textToSpeechButton = findViewById(R.id.testButton);

        preferences = getSharedPreferences("general_settings",MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        // UNIT OPTIONS
        int unitSelected = preferences.getInt(UNIT_PROGRESS,3);
        radioGroupUnits = findViewById(R.id.radioGroupUnits);
        radioButtonMiles = findViewById(R.id.radioButtonMiles);
        radioButtonKM = findViewById(R.id.radioButtonKM);

        if(unitSelected == 1){
            radioButtonMiles.setChecked(true);
        } else if(unitSelected == 0){
            radioButtonKM.setChecked(true);
        }

        radioGroupUnits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioButtonMiles){
                    editor.putInt(UNIT_PROGRESS,1);
                }else if(i == R.id.radioButtonKM){
                    editor.putInt(UNIT_PROGRESS,0);
                }
                editor.commit();
            }
        });

        // LANGUAGE OPTIONS
        lang = new LanguageManager(this);

        // Source code taken from https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        languageSpinner = findViewById(R.id.languageSpinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(GeneralSettings.this,
                android.R.layout.simple_spinner_item,languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
        languageSpinner.setSelection(0);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();

                if (selectedLang.equals("en")){
                    lang.updateResource("en");
                    editor.putString("lang_code", selectedLang);  // Saving string
                    recreate();
                } else if (selectedLang.equals("fr")){
                    lang.updateResource("fr");
                    editor.putString("lang_code", selectedLang);  // Saving string
                    recreate();
                } else {
                    Toast.makeText(GeneralSettings.this, "Please select a language",Toast.LENGTH_SHORT).show();
                }
                editor.putInt(LANG_PROGRESS, languageSpinner.getSelectedItemPosition());
                editor.commit(); // Save to file
                languageSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // VOICE FEEDBACK OPTIONS
        // Source code taken from https://gist.github.com/codinginflow/3091cb7f7d165359da1b173794094752
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "Language not Supported!",Toast.LENGTH_SHORT).show();

                    } else {
                        textToSpeechButton.setEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Could not load TTS Engine!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding OnClickListener
        textToSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        // Speed bar seekbar
        seekSB.setProgress(preferences.getInt(SB_PROGRESS,0));
        seekSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt(SB_PROGRESS, seekSB.getProgress());
                editor.commit();
            }
        });
        // Pitch bar seekbar
        seekPB.setProgress(preferences.getInt(PB_PROGRESS,0));
        seekPB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt(PB_PROGRESS, seekPB.getProgress());
                editor.commit();
            }
        });

    }
    private void speak() {
        String text = eTTest.getText().toString();
        float pitch = (float) seekPB.getProgress() / 10;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (float) seekSB.getProgress() / 10;
        if (speed < 0.1) speed = 0.1f;

        tts.setPitch(pitch);
        tts.setSpeechRate(speed);

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}