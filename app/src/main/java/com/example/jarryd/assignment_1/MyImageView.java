package com.example.jarryd.assignment_1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;

import static com.example.jarryd.assignment_1.BackgroundBitmapTask.cancelExistingBackgroundTask;

/**
 * Created by jarryd on 23/03/16.
 */
public class MyImageView extends ImageView {
//    public int minWidth;
    /**
     * @param context
     */
    public MyImageView(Context context){
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public MyImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

            public void setBitmapViaBackgroundTask(Context context, String image_id, int displayWidth, int scaleFactor) {
            if (cancelExistingBackgroundTask(this.getId(), this)) {
                final BackgroundBitmapTask bgBmpTask = new BackgroundBitmapTask(context, this, image_id, (int)(displayWidth/scaleFactor));
                System.out.println("########### display width is : "+displayWidth+"  scaleFactor is:  "+scaleFactor+ "quotient is:  " +displayWidth/scaleFactor);
                final BackgroundDrawable bgDrawable = new BackgroundDrawable(getContext().getResources(), null, bgBmpTask);
                /* Set the drawable object as the content of this imageView */
                this.setImageDrawable(bgDrawable);
                /* Execute Bitmap Processing in the background thread */
                bgBmpTask.execute(this.getId());
                }
            }

//    public void setMinimumWidth(Display display, int scaleFactor){
//        DisplayMetrics metrics = new DisplayMetrics();
//        display.getMetrics(metrics);
//        this.minWidth = (metrics.widthPixels)/scaleFactor;
//        setMinimumWidth(minWidth);
//        System.out.println("________________###########DISPLAY WIDTH IS:  " + metrics.widthPixels + "_____________________##########################");
//    }




}
