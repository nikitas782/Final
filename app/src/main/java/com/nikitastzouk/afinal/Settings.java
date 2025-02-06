package com.nikitastzouk.afinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends AppCompatActivity {

    TextView textView_firstname, textView_lastname;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Switch switcher;
    boolean nightMODE;
    SeekBar fontseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView_firstname = findViewById(R.id.textViewFirsname);
        textView_lastname = findViewById(R.id.textView4);
        switcher = findViewById(R.id.switch1);
        fontseekBar = findViewById(R.id.seekBar);

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);

        String firstname = sharedPreferences.getString("first_name", null);
        String lastname = sharedPreferences.getString("last_name", null);

        if (firstname != null || lastname != null) {
            textView_firstname.setText("First Name - " + firstname);
            textView_lastname.setText("Last Name - " + lastname);
        }


        nightMODE = sharedPreferences.getBoolean("night", false);

        if (nightMODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switcher.setChecked(true);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switcher.setChecked(false);
        }

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMODE) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
                nightMODE = !nightMODE; // Toggle the night mode value
            }
        });

        int saveFontsize = sharedPreferences.getInt("font_size",20);
        textView_firstname.setTextSize(saveFontsize);
        textView_lastname.setTextSize(saveFontsize);
        switcher.setTextSize(saveFontsize);
        fontseekBar.setProgress(saveFontsize,true);

        fontseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textView_firstname.setTextSize(progress);
                textView_lastname.setTextSize(progress);
                switcher.setTextSize(progress);


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("font_size", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }
}