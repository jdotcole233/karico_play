package com.ultitrust.karico.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ultitrust.karico.Model.MusicModel;
import com.ultitrust.karico.R;

import java.util.List;

public class KaricoMusicAdapater extends RecyclerView.Adapter<KaricoMusicAdapater.MyViewHolder> {


    private List<MusicModel> musicModels;
    private Context context;

    public KaricoMusicAdapater(List<MusicModel> musicModels, Context context) {
        this.musicModels = musicModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
         View myview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_music_row_view, viewGroup, false);
        return new MyViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
            myViewHolder.folder_name.setText(musicModels.get(i).getFolder_name());
            myViewHolder.folder_size.setText(musicModels.get(i).getFolder_size().toString());
            myViewHolder.music_number.setText(musicModels.get(i).getNumber_of_music().toString());

            myViewHolder.remove_folder_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Folder clicked " + musicModels.get(i).getFolder_name(), Toast.LENGTH_LONG).show();
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
        public MyViewHolder(View itemView) {
            super(itemView);
            folder_name = itemView.findViewById(R.id.folderName);
            folder_size = itemView.findViewById(R.id.foldersize);
            music_number = itemView.findViewById(R.id.music_number);
            remove_folder_btn = itemView.findViewById(R.id.removemusic);
        }
    }
}
