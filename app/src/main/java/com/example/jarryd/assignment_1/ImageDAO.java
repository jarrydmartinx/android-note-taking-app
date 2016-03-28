package com.example.jarryd.assignment_1;

import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Created by jarryd on 25/03/16.
 */
public interface ImageDAO {

    Bitmap getNoteImageFromFile(String image_id, int req_height, int req_width);

    //   void saveNoteImageToFile(Context context, Note aNote);
    void deleteNoteImageFromFile(Note aNote);

    Intent createImageCaptureIntent(Note note);

}


