package com.example.jarryd.assignment_1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by jarryd on 22/03/16.
 *
 * This is heavily based on http://developer.android.com/training/displaying-bitmaps/process-bitmap.html#BitmapWorkerTaskUpdated
 * Previously had an implementation that was far simpler but couldn't avoid ImageViews disappearing without handling recycling in this way
 * */

public class BackgroundBitmapTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> noteImageViewRef;
        private int data = 0;
        private String pathname;
        private int resId;
        private Context context;

        public BackgroundBitmapTask(ImageView noteImageView, String pathname) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            noteImageViewRef = new WeakReference<>(noteImageView);
            this.pathname = pathname;

        }

        // Constructor for testing, takes a resource id rather than a pathname
        public BackgroundBitmapTask(ImageView noteImageView, int resId, Context context) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
            noteImageViewRef = new WeakReference<>(noteImageView);
            pathname = "no_path";
            this.resId = resId;
            this.context = context;

        }

        // An AsyncTask method. Process the photo in a background thread rather than the UI thread
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
//            return decodeSampledBitmapFromFile(pathname, 100, 100);
            return decodeSampledBitmapFromResource(resId, context, 100, 100);
        }

        // An AsyncTask method. When the background photo processing is completed, the reference is checked the photo is set.
        @Override
        protected void onPostExecute(Bitmap photo) {
            if(isCancelled()) {
                photo = null;
            }
            if (noteImageViewRef != null && photo != null) {
                final ImageView noteImageView = noteImageViewRef.get();
                final BackgroundBitmapTask bmpTask = getBackgroundBitmapTask(noteImageView);
                if (this == bmpTask && noteImageView != null) {
                    noteImageView.setImageBitmap(photo);
                }
            }
        }

        /* A helper method to get the BackgroundBitmapTask corresponding to our noteImageView */
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

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
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

    public static Bitmap decodeSampledBitmapFromFile(String pathname,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathname, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathname, options);
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










































