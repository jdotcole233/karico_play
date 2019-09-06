package com.ultitrust.karico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class KaricoInstruction extends AppCompatActivity {

    private ImageButton closeInstruction;
    private Button continueButton;
    private String helpButtonClickedState;
    private String intentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_instruction);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenwidth = (int)(displayMetrics.widthPixels * 0.9);
        int screenheight = (int)(displayMetrics.heightPixels * 0.6);
        getWindow().setLayout(screenwidth, screenheight);

        closeInstruction = findViewById(R.id.instructionbackbtn);
        continueButton = findViewById(R.id.continuebutton);

        SharedPreferences sharedPreferences = getSharedPreferences("CONTINUE_BUTTON_CLICKED", MODE_PRIVATE);
        helpButtonClickedState = sharedPreferences.getString("continueButtonState", null);

        Intent intent = getIntent();
        intentState = intent.getStringExtra("KaricoMotor");


        if (helpButtonClickedState != null && intentState != null){
            if (!helpButtonClickedState.isEmpty() && !intentState.isEmpty()){
                if (helpButtonClickedState.equals("continueClicked")) {
                    continueButton.setVisibility(View.INVISIBLE);
                }
            }
        }


        Intent selectedMusicIntent = getIntent();
        final String selectedMusicEncodedUri = selectedMusicIntent.getStringExtra("selectedMusicEncodedUri");


        closeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent skillLevelIntent = new Intent(KaricoInstruction.this, KaricoMotorActivity.class);
                        skillLevelIntent.putExtra("continueButtonState", "continueClicked");
                        startActivity(skillLevelIntent);
                        finish();
            }
        });




    }
}
