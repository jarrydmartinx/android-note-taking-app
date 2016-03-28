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
 * This is heavily based on the Android Training Guide at http://developer.android.com/training/displaying-bitmaps/process-bitmap.html#BitmapWorkerTaskUpdated
 * Previously had a bespoke implementation that was far simpler but couldn't avoid ImageViews disappearing without handling recycling in exactly the suggested way.
 * <p/>
 * Implementation of Image Data Access Object Interface
 * Defines methods for accessing notes in external storage and helper methods for image processing
 */
public class ImageDAOImpl implements ImageDAO {
    public Context context;

    public ImageDAOImpl(Context context) {
        this.context = context;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    @Override
    //Implements getNoteImageFromFile by retrieving a sampled bitmap of the required width and height
    public Bitmap getNoteImageFromFile(String image_id, int req_height, int req_width) {
        String imageFilePath = image_id;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFilePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, req_height, req_width);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFilePath, options);
    }

    @Override
    public void deleteNoteImageFromFile(Note aNote) {
        File fileToDelete = new File(aNote.getImage_id());
        fileToDelete.delete();
    }

    public Intent createImageCaptureIntent(Note note) {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageCaptureIntent.resolveActivity(context.getPackageManager()) != null) {
            File imageFile = createFileForNoteImage(note);
            if (imageFile != null) {
                imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            }
            return imageCaptureIntent;
        }
        return null;
    }

    private File createFileForNoteImage(Note aNote) {
        // Create an image file name
        String imageFileName = ("JPEG_" + aNote.getNote_id() + "_");
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(imageFileName, ".jpg", dir);
            aNote.setImage_id(imageFile.getAbsolutePath());
            System.out.println("absolute path is : " + imageFile.getAbsolutePath());
            System.out.println("image id is:    " + aNote.getImage_id());
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}