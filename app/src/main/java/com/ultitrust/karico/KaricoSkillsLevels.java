package com.ultitrust.karico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class KaricoSkillsLevels extends AppCompatActivity {

    private ImageButton skillsBackBtn;
    private Button primaryMotorBtn, interMediaryBtn;
    private  String musicUriEndcodedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_skills_levels);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenwidth = (int) (displayMetrics.widthPixels * 0.9);
        int screenheight = (int) (displayMetrics.heightPixels * 0.3);

        getWindow().setLayout(screenwidth,screenheight);

        skillsBackBtn = findViewById(R.id.skillsbackbtn);
        primaryMotorBtn = findViewById(R.id.primaryMotorSkills);
        interMediaryBtn = findViewById(R.id.intermediaryMotorSkills);

        Intent musicUriIntent = getIntent();
        musicUriEndcodedString = musicUriIntent.getStringExtra("folderPath");


        skillsBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        primaryMotorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicUriEndcodedString != null){
                    if (!musicUriEndcodedString.isEmpty()){
                        Intent primaryMotorIntent = new Intent(KaricoSkillsLevels.this, KaricoMotorPlayer.class);
                        primaryMotorIntent.putExtra("primaryMotorFolderPath", musicUriEndcodedString);
                        startActivity(primaryMotorIntent);
                    } else {
                        Toast.makeText(KaricoSkillsLevels.this, "Somwething went wrong, please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        interMediaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicUriEndcodedString != null){
                    if (!musicUriEndcodedString.isEmpty()){
                        Intent intermediaryMotorIntent = new Intent(KaricoSkillsLevels.this, KaricoMotorSkillTwo.class);
                        intermediaryMotorIntent.putExtra("primaryMotorFolderPath", musicUriEndcodedString);
                        startActivity(intermediaryMotorIntent);
                    } else {
                        Toast.makeText(KaricoSkillsLevels.this, "Somwething went wrong, please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
    }
}
