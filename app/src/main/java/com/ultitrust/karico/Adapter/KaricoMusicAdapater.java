package com.ultitrust.karico.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ultitrust.karico.KaricoMotorPlayer;
import com.ultitrust.karico.KaricoSkillsLevels;
import com.ultitrust.karico.Model.MusicModel;
import com.ultitrust.karico.R;

import java.util.List;

public class KaricoMusicAdapater extends RecyclerView.Adapter<KaricoMusicAdapater.MyViewHolder> {


    private List<MusicModel> musicModels;
    private Context context;
    private RecyclerView recyclerView;

    public KaricoMusicAdapater(List<MusicModel> musicModels, Context context, RecyclerView recyclerView) {
        this.musicModels = musicModels;
        this.context = context;
        this.recyclerView = recyclerView;
        Log.i("Karico", musicModels.size() + "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
         View myview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_music_row_view, viewGroup, false);

        return new MyViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
            myViewHolder.folder_name.setText(musicModels.get(i).getFolder_name());
            myViewHolder.folder_size.setText(musicModels.get(i).getFolder_size());
            myViewHolder.music_number.setText(musicModels.get(i).getNumber_of_music());

            myViewHolder.remove_folder_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    musicModels.remove(i);
                    recyclerView.getAdapter().notifyItemRemoved(i);
                    recyclerView.getAdapter().notifyDataSetChanged();
//                    Toast.makeText(context, "Folder clicked " + i + " " + musicModels.get(i).getFolder_name(), Toast.LENGTH_LONG).show();
                }
            });

            myViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playerIntent = new Intent(context.getApplicationContext(), KaricoSkillsLevels.class);
                    String folderPath = Uri.encode(musicModels.get(i).getFolder_path().toString());
                    playerIntent.putExtra("folderPath",folderPath);
                    context.startActivity(playerIntent);


                }
            });

    }



    @Override
    public int getItemCount() {
        return musicModels.size();
    }




    public class  MyViewHolder extends RecyclerView.ViewHolder {

        private TextView folder_name, folder_size, music_number;
        private ImageButton remove_folder_btn;
        private RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            folder_name = itemView.findViewById(R.id.folderName);
            folder_size = itemView.findViewById(R.id.foldersize);
            music_number = itemView.findViewById(R.id.music_number);
            remove_folder_btn = itemView.findViewById(R.id.removemusic);
            relativeLayout = itemView.findViewById(R.id.selectFolderCard);
        }
    }
}
