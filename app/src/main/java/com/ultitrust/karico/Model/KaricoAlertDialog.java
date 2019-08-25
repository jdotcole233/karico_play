package com.ultitrust.karico.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

import com.ultitrust.karico.KaricoMotorActivity;

import java.util.ArrayList;

public class KaricoAlertDialog {
    private Context context;
    private String message;
    private ArrayList<Uri> musicContent;
    private KaricoMotorActivity karicoMotorActivity;

    public KaricoAlertDialog(Context context, String message, ArrayList<Uri> musicContent) {
        this.context = context;
        this.message = message;
        this.musicContent = musicContent;
        karicoMotorActivity = new KaricoMotorActivity();
    }

    public void createDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Karico Alert")
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        karicoMotorActivity.saveFolderList(musicContent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }
}
