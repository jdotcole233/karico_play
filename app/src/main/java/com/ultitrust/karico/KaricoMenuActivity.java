package com.ultitrust.karico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class KaricoMenuActivity extends AppCompatActivity {

    private Button motor_activity_btn, speech_activity_btn, other_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_menu);
        motor_activity_btn = findViewById(R.id.motoractivitybutton);
        speech_activity_btn = findViewById(R.id.speechbutton);
        other_btn = findViewById(R.id.otherbutton);

        speech_activity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KaricoMenuActivity.this, "Try again later!", Toast.LENGTH_LONG).show();
            }
        });

        other_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KaricoMenuActivity.this, "Try again later", Toast.LENGTH_LONG).show();
            }
        });

        motor_activity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KaricoMenuActivity.this, KaricoMotorActivity.class));
            }
        });
    }
}
