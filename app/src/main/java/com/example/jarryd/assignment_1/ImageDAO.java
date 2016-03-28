package com.example.jarryd.assignment_1;

import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Created by jarryd on 25/03/16.
 * <p/>
 * Image Data Access Object Interface
 * Defines methods for accessing notes in persistent storage
 */

public interface ImageDAO {

    Bitmap getNoteImageFromFile(String image_id, int req_height, int req_width);

    void deleteNoteImageFromFile(Note aNote);

    Intent createImageCaptureIntent(Note note);

}


