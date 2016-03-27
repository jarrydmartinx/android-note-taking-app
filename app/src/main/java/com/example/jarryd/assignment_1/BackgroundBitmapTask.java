package com.example.jarryd.assignment_1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by jarryd on 22/03/16.
 *
 * This is heavily based on http://developer.android.com/training/displaying-bitmaps/process-bitmap.html#BitmapWorkerTaskUpdated
 * Previously had an implementation that was far simpler but couldn't avoid ImageViews disappearing without handling recycling in this way
 * */

public class BackgroundBitmapTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<MyImageView> noteImageViewRef;
        private int data = 0;
        private String image_id;
        private ImageDAO imageDAO;
        private Context context;
        private MyImageView imageView;

        public BackgroundBitmapTask(Context context, MyImageView noteImageView, String image_id) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            this.image_id = image_id;
            this.context = context;
            noteImageViewRef = new WeakReference<>(noteImageView);
            imageDAO = new ImageDAOImpl(context);
            imageView = noteImageView;
        }

        // An AsyncTask method. Process the photo in a background thread rather than the UI thread
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return imageDAO.getNoteImageFromFile(image_id, imageView.getMeasuredHeight(), imageView.getMeasuredWidth());
//            return decodeSampledBitmapFromResource(resId, context, 100, 100);
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

}










































