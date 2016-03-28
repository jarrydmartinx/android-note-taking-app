package com.example.jarryd.assignment_1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by jarryd on 22/03/16.
 * <p/>
 * A custom AsyncTask that allows for processing of Bitmaps off the main UI thread
 * This is heavily based on the Android Training Guide at http://developer.android.com/training/displaying-bitmaps/process-bitmap.html#BitmapWorkerTaskUpdated
 * Previously had a bespoke implementation that was far simpler but couldn't avoid ImageViews disappearing without handling recycling in exactly the suggested way.
 */
public class BackgroundBitmapTask extends AsyncTask<Integer, Void, Bitmap> {
    /**
     * A weak reference to the ImageView to which Bitmap is to be added.
     * Must be weak in case it disappears while processing still occurring
     */
    private final WeakReference<MyImageView> noteImageViewRef;
    private int data = 0;
    private String image_id;
    private ImageDAO imageDAO;
    private Context context;
    private MyImageView imageView;
    /**
     * The desired dimensions to decode the image with respect to (the dimensions of the target
     * ImageView
     */
    private int reqHeight;
    private int reqWidth;

    /**
     * Constructor for this Async Task
     *
     * @param context
     * @param noteImageView The ImageView to which the Bitmap will be set if successful
     * @param image_id      The id stored in the relevant Note
     * @param defaultDim    The default width (set relative to the device display), to decode
     *                      the image with respect to
     */
    public BackgroundBitmapTask(Context context, MyImageView noteImageView, String image_id, int defaultDim) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.image_id = image_id;
        this.context = context;
        noteImageViewRef = new WeakReference<>(noteImageView);
        imageDAO = new ImageDAOImpl(context);
        reqWidth = defaultDim;
        reqHeight = defaultDim;
        //Sets the desired dimensions of the Bitmap to be returned by the AsyncTask.
        // If the ImageView has not been drawn yet a default value (relative to display size) is used
    }

    /**
     * A helper method to get the BackgroundBitmapTask corresponding to our noteImageView
     *
     * @param noteImageView the ImageView to set the Bitmap for
     */
    private static BackgroundBitmapTask getBackgroundBitmapTask(ImageView noteImageView) {
        if (noteImageView != null) {
            final Drawable drawable = noteImageView.getDrawable();
            if (drawable instanceof BackgroundDrawable) {
                final BackgroundDrawable bgDrawable = (BackgroundDrawable) drawable;
                return bgDrawable.getBackgroundBitmapTask();
            }
        }
        return null;
    }

    /**
     * Makes sure that no existing task is ongoing for the same ImageView
     *
     * @param data
     * @param noteImageView
     * @return true if no other task ongoing or successfully cancelled
     */
    public static boolean cancelExistingBackgroundTask(int data, ImageView noteImageView) {
        final BackgroundBitmapTask bmpTask = getBackgroundBitmapTask(noteImageView);

        if (bmpTask != null) {
            final int bitmapData = bmpTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
                bmpTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    /**
     * Processes the Bitmap off the main UI thread
     */
    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        return imageDAO.getNoteImageFromFile(image_id, reqHeight, reqWidth);
    }

    /**
     * Once processing is completed the reference is checked, and the image is set.
     */
    @Override
    protected void onPostExecute(Bitmap photo) {
        if (isCancelled()) {
            photo = null;
        }
        if (noteImageViewRef != null && photo != null) {
            imageView = noteImageViewRef.get();
            final BackgroundBitmapTask bmpTask = getBackgroundBitmapTask(imageView);
            if (this == bmpTask && imageView != null) {
                imageView.setImageBitmap(photo);
            }
        }
    }

}










































