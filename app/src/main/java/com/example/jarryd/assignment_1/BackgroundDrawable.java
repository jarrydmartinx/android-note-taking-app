package com.example.jarryd.assignment_1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by jarryd on 22/03/16.
 *
 * http://developer.android.com/training/displaying-bitmaps/process-bitmap.html#BitmapWorkerTaskUpdated
 */
public class BackgroundDrawable extends BitmapDrawable {
    private final WeakReference<BackgroundBitmapTask> backgroundBitmapTaskWeakRef;

    public BackgroundDrawable(Resources resources, Bitmap photo, BackgroundBitmapTask backgroundBitmapTask) {
        super(resources, photo);
        backgroundBitmapTaskWeakRef = new WeakReference<BackgroundBitmapTask>(backgroundBitmapTask);
    }

    public BackgroundBitmapTask getBackgroundBitmapTask() {
        return backgroundBitmapTaskWeakRef.get();
    }
}

