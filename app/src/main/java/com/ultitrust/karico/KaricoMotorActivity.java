package com.ultitrust.karico;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ultitrust.karico.Adapter.KaricoMusicAdapater;
import com.ultitrust.karico.Model.MusicModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class KaricoMotorActivity extends AppCompatActivity {

    private ImageButton music_dismiss, music_file_chooser;
    private final int REQUEST_CODE = 999;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private List<MusicModel> musicModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_motor);
        music_dismiss = findViewById(R.id.musicdismissbtn);
        music_file_chooser = findViewById(R.id.musicfilechooser);
        recyclerView = findViewById(R.id.music_list_display);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        musicModels = new ArrayList<>();

        if (musicModels != null){
            recyclerViewAdapter = new KaricoMusicAdapater(musicModels, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }




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
                open_chooser.putExtra("android.content.extra.SHOW_FILESIZE", true);
//                open_chooser.setAction(Intent.ACTION_GET_CONTENT);
//                open_chooser.setType("file/*");
                startActivityForResult(Intent.createChooser(open_chooser, "Choose Directory"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                Integer size = 0;
                long folderSizeInMB = (long) 0.0;
                Uri uri = data.getData();
                DocumentFile documentFile = DocumentFile.fromTreeUri(this,uri);

                if (documentFile.isDirectory()){
                    for (DocumentFile file : documentFile.listFiles()){
                        if (file.isFile()){
                            if (file.getType().equals("audio/*")){
                            }
                        }
                        size++;
                        Log.i("Karico", file.getName() + " " + file.length());
                    }

                    if (size == 0){
                        Toast.makeText(this, "You are attempting to add empty music directory", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (musicModels != null){
                        folderSizeInMB = documentFile.length() /1024;
                        musicModels.add(new MusicModel(documentFile.getName(), folderSizeInMB + " MB -", size + " Songs"));
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                } else if (documentFile.isFile()){
                    Toast.makeText(this,"Sorry, you cannot add single files, add a directory", Toast.LENGTH_LONG).show();
                    return;
                }
                break;

        }
    }



}
