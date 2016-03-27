package com.example.jarryd.assignment_1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

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
    public Bitmap getNoteImageFromFile(String image_id, int req_height, int req_width) {
        String imageFilePath = image_id;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFilePath, options);
        System.out.println("imageFilepath passed to BitMapFactory.decodeFile is: "+ imageFilePath +"______________________");

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, req_height, req_width);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFilePath, options);
    }

//    @Override
//    public void saveNoteImageToFile(Context context, Note note) {
//        // THis is about the CAMERA
//    }

    @Override
    public void deleteNoteImageFromFile(Note aNote) {
        File fileToDelete = new File(aNote.getImage_id());
        fileToDelete.delete();
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

    public Intent createImageCaptureIntent(Note note){
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageCaptureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File imageFile = createFileForNoteImage(note);
            // Continue only if the File was successfully created
            if (imageFile != null) {
                imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            }
            return imageCaptureIntent;
        }
        return null;
    }

    private File createFileForNoteImage(Note aNote){
        // Create an image file name
        String imageFileName = ("JPEG_" + aNote.getNote_id() + "_");
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(imageFileName, ".jpg", dir);
            aNote.setImage_id(imageFile.getAbsolutePath());
            System.out.println("absolute path is : " + imageFile.getAbsolutePath());
            System.out.println("image id is:    " + aNote.getImage_id());
            return imageFile;
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}