package com.example.jarryd.assignment_1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by jarryd on 25/03/16.
 */
public class ImageDAOImpl implements ImageDAO {
    public Context context;

    public ImageDAOImpl(Context context) {
        this.context = context;
    }

    @Override
    //Implements getNoteImageFromFile by retrieving a sampled bitmap of the required width and height
    public Bitmap getNoteImageFromFile(Context context, String image_id,
                                       int req_height, int req_width) {

        String image_path = getImagePathFromId(context, image_id);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image_path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, req_height, req_width);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(image_path, options);
    }

    @Override
    public void saveNoteImageToFile(Context context, Note note) {
            // THis is about the CAMERA
    }

    @Override
    public void deleteNoteImageFromFile(Context context, Note note) {
        context.deleteFile(note.getImage_id());
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;            /////////////////////MUST  SIMPLIFY THIS IN SOME WAY
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



    private String getImagePathFromId(Context context, String image_id) {
        String image_path = context.getFilesDir() + image_id;
        return image_path;
    }


    public static Bitmap decodeSampledBitmapFromResource(int resId, Context context,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }






}
