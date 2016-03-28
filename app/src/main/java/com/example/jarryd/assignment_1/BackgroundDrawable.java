package com.example.jarryd.assignment_1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by jarryd on 22/03/16.
 * <p/>
 * This is heavily based on the Android Training Guide at http://developer.android.com/training/displaying-bitmaps/process-bitmap.html#BitmapWorkerTaskUpdated
 * Previously had a bespoke implementation that was far simpler but couldn't avoid ImageViews disappearing without handling recycling in exactly the suggested way.
 */
public class BackgroundDrawable extends BitmapDrawable {
    private final WeakReference<BackgroundBitmapTask> backgroundBitmapTaskWeakRef;

    public BackgroundDrawable(Resources resources, Bitmap photo, BackgroundBitmapTask backgroundBitmapTask) {
        super(resources, photo);
        backgroundBitmapTaskWeakRef = new WeakReference<>(backgroundBitmapTask);
    }

    public BackgroundBitmapTask getBackgroundBitmapTask() {
        return backgroundBitmapTaskWeakRef.get();
    }
}

