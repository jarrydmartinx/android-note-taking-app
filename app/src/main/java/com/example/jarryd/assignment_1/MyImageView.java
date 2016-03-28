package com.example.jarryd.assignment_1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import static com.example.jarryd.assignment_1.BackgroundBitmapTask.cancelExistingBackgroundTask;

/**
 * Created by jarryd on 23/03/16.
 * A custom ImageView class with a member method to allow setting of a Bitmap
 * processed off the main UI thread
 */

public class MyImageView extends ImageView {
    /**
     * @param context
     */
    public MyImageView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Performs processing of a Bitmap off the main UI thread
     * Code structure draws heavily on the Android Developers Training Guide, found at:
     * http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
     *
     * @param context
     * @param image_id     id of the image to be processed
     * @param displayWidth the width of the device display to decode down the image with respect to
     * @param scaleFactor  the reduction factor (imageWidth = displayWidth/scaleFactor)
     */
    public void setBitmapViaBackgroundTask(Context context, String image_id, int displayWidth, int scaleFactor) {
        if (cancelExistingBackgroundTask(this.getId(), this)) {
            final BackgroundBitmapTask bgBmpTask = new BackgroundBitmapTask(context, this, image_id, (int) (displayWidth / scaleFactor));
            final BackgroundDrawable bgDrawable = new BackgroundDrawable(getContext().getResources(), null, bgBmpTask);
                /* Set the drawable object as the content of this imageView */
            this.setImageDrawable(bgDrawable);
                /* Execute Bitmap Processing in the background thread */
            bgBmpTask.execute(this.getId());
        }
    }


}
