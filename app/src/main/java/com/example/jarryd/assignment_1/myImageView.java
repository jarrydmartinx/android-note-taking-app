package com.example.jarryd.assignment_1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import static com.example.jarryd.assignment_1.BackgroundBitmapTask.cancelExistingBackgroundTask;

/**
 * Created by jarryd on 23/03/16.
 */
public class myImageView extends ImageView {

    /**
     * @param context
     */
    public myImageView(Context context){
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public myImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public myImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setBitmapViaBackgroundTaskFromResource(int image_id) {
        if (cancelExistingBackgroundTask(this.getId(), this)) {
            final BackgroundBitmapTask bgBmpTask = new BackgroundBitmapTask(this, image_id, getContext());
            final BackgroundDrawable bgDrawable = new BackgroundDrawable(getContext().getResources(), null, bgBmpTask);
                /* Set the drawable object as the content of this imageView */
            this.setImageDrawable(bgDrawable);
                /* Execute Bitmap Processing in the background thread */
            bgBmpTask.execute(this.getId());
        }
    }

            public void setBitmapViaBackgroundTask(String image_pathname) {
            if (cancelExistingBackgroundTask(this.getId(), this)) {
                final BackgroundBitmapTask bgBmpTask = new BackgroundBitmapTask(this, image_pathname);
                final BackgroundDrawable bgDrawable = new BackgroundDrawable(getContext().getResources(), null, bgBmpTask);
                /* Set the drawable object as the content of this imageView */
                this.setImageDrawable(bgDrawable);
                /* Execute Bitmap Processing in the background thread */
                bgBmpTask.execute(this.getId());
            }
        }
}
