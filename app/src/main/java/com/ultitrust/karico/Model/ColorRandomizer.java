package com.ultitrust.karico.Model;

import android.content.Context;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ColorRandomizer {

    final Integer RANDOMIZERNUMBER = 5;

    public List<Integer> colorRandomizer(){
        List<Integer> randomNumbers = new ArrayList<>();
        Random random = new Random();
        for (int start = 0; start < RANDOMIZERNUMBER; start++){
            int r_gen = random.nextInt(8);
            randomNumbers.add(r_gen);
            for (int s_start = 0; s_start < start; s_start++){
                if (randomNumbers.get(s_start).equals(r_gen)){
                    randomNumbers.add(random.nextInt(8));
                }
            }
        }
        return randomNumbers;
    }


    public String getMusicName(Context context, Uri uri){
        return DocumentFile.fromSingleUri(context,uri).getName();
    }
}
