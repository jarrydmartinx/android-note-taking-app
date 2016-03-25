package com.example.jarryd.assignment_1;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by jarryd on 25/03/16.
 */
public interface ImageDAO {

    Bitmap getNoteImageFromFile(Context context, String image_id, int req_height, int req_width);
    void saveNoteImageToFile(Context context, Note note);
    void deleteNoteImageFromFile(Context context, Note note);
}


