package com.ultitrust.karico;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class KaricoMotorActivity extends AppCompatActivity {

    private ImageButton music_dismiss, music_file_chooser;
    private final int REQUEST_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_motor);
        music_dismiss = findViewById(R.id.musicdismissbtn);
        music_file_chooser = findViewById(R.id.musicfilechooser);

        music_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        music_file_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                open_chooser.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(Intent.createChooser(open_chooser, "Choose Directory"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                Log.i("Karico", data.getData().toString());
                break;

        }
    }
}
