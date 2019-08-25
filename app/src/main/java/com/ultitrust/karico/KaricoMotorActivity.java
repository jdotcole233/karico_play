package com.ultitrust.karico;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.ultitrust.karico.Adapter.KaricoMusicAdapater;
import com.ultitrust.karico.Model.KaricoAlertDialog;
import com.ultitrust.karico.Model.MusicModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ultitrust.karico.Adapter.KaricoContracts.MUSIC_SAVED_NAME;

public class KaricoMotorActivity extends AppCompatActivity {

    private ImageButton music_dismiss, music_file_chooser;
    private final int REQUEST_CODE = 999;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private List<MusicModel> musicModels;
    private ArrayList<Uri> savedMusicPaths;
    private ArrayList<Uri> originalMusicPaths;
    private int size_of_readFolderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karico_motor);
        music_dismiss = findViewById(R.id.musicdismissbtn);
        music_file_chooser = findViewById(R.id.musicfilechooser);
        recyclerView = findViewById(R.id.music_list_display);
        savedMusicPaths = new ArrayList<>();
        originalMusicPaths = new ArrayList<>();

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



        String readFolders = readSavedFolderList();

        if (readFolders != null){
            if(!readFolders.isEmpty()){
                JSONArray jsonArray = folderListContent(readFolders);
                if (jsonArray != null) {
                    size_of_readFolderList = jsonArray.length();
                    for (int i = 0; i < size_of_readFolderList; i++){
                        try {
                            savedMusicPaths.add(Uri.parse(jsonArray.getString(i)));
                            originalMusicPaths.add(Uri.parse(jsonArray.getString(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                size_of_readFolderList = 0;
            }
        }


        music_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer numberOfChanges = 0;

                if (savedMusicPaths != null){
                    Integer size_of_savedpaths = savedMusicPaths.size();
                    if (size_of_savedpaths == size_of_readFolderList){
                        ArrayList<Uri> similarFolders = new ArrayList<>();
                        for (int i = 0; i < savedMusicPaths.size(); i++){
                             if (!originalMusicPaths.contains(savedMusicPaths.get(i))){
                                 similarFolders.add(savedMusicPaths.get(i));
                             }
                        }
                        if (similarFolders != null){
                            if(similarFolders.size() > 0){
                                numberOfChanges = similarFolders.size();
                            }
                            String message = "Karico found that you replaced " + numberOfChanges + " new folders to existing playlist.\n" +
                                    "Do you want to save changes?";
                            createDialog(message);

                        }

                    } else if (size_of_savedpaths > size_of_readFolderList) {
                        numberOfChanges = savedMusicPaths.size() - originalMusicPaths.size();
                        String message = "Karico found that you added " + numberOfChanges + " new folders to existing playlist.\n" +
                                "Do you want to save changes?";
                        createDialog(message);
                    } else if (size_of_savedpaths < size_of_readFolderList) {
                        numberOfChanges = originalMusicPaths.size() - savedMusicPaths.size();
                        String message = "Karico found that you deleted " + numberOfChanges +" folder(s) from existing playlist.\n" +
                                "Do you want to save changes?";
                        createDialog(message);
                    }
                }
            }
        });

        music_file_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                open_chooser.addCategory(Intent.CATEGORY_DEFAULT);
                open_chooser.putExtra("android.content.extra.SHOW_FILESIZE", true);
                startActivityForResult(Intent.createChooser(open_chooser, "Choose Directory"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:

                if (savedMusicPaths != null){
                    loadMusic(savedMusicPaths);
                    DocumentFile isDirecotyFilePath = DocumentFile.fromTreeUri(this, data.getData());
                    if (isDirecotyFilePath.isDirectory()){
                        if (folderExists(savedMusicPaths, isDirecotyFilePath.getName())){
                            Toast.makeText(this, "Found a folder with the same name in existing list", Toast.LENGTH_LONG).show();
                            return;
                        }
                        savedMusicPaths.add(data.getData());
                    } else {
                        Toast.makeText(this,"Sorry, you cannot add single files, add a directory", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                break;

        }
    }


    public void createDialog(String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Karico Alert")
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveFolderList(savedMusicPaths);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }


    public boolean folderExists(ArrayList<Uri> musiclist, String foldername){
        return (musiclist.contains(foldername)) ? true : false;
    }


    public boolean saveFolderList(ArrayList<Uri> save_musiclist){
        SharedPreferences sharedPreferences =  getSharedPreferences(MUSIC_SAVED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String music_save_json = gson.toJson(save_musiclist);
        Log.i("Karico", music_save_json);
        editor.putString("KARICO_SAVED", music_save_json);

        return editor.commit();
    }



    public String readSavedFolderList() {
        SharedPreferences sharedPreferences = getSharedPreferences(MUSIC_SAVED_NAME, Context.MODE_PRIVATE);
        String readFolderList = sharedPreferences.getString("KARICO_SAVED", "");
        return readFolderList;
    }

    public JSONArray folderListContent(String readFolderList){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(readFolderList);
            Log.i("Karico", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public void loadMusic(ArrayList<Uri> musicpaths){
        int size = 0;
        long folderSizeInMB = (long) 0.0;

        if (musicpaths != null){
            if (musicpaths.size() == 0){
                Toast.makeText(this, "No Music to be loaded yet", Toast.LENGTH_LONG).show();
                return;
            } else {
                for (Uri uri : musicpaths) {
                    DocumentFile documentsFromURIs = DocumentFile.fromTreeUri(this,uri);

                    if (documentsFromURIs.isDirectory()){
                        for (DocumentFile file : documentsFromURIs.listFiles()){
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
                            folderSizeInMB = documentsFromURIs.length() /1024;
                            musicModels.add(new MusicModel(documentsFromURIs.getName(), folderSizeInMB + " MB -", size + " Songs"));
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    } else if (documentsFromURIs.isFile()){
                        Toast.makeText(this,"Sorry, you cannot add single files, add a directory", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            }
        }

    }


}
