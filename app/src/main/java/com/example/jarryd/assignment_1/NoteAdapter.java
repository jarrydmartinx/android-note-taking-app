package com.example.jarryd.assignment_1;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import static com.example.jarryd.assignment_1.BackgroundBitmapTask.cancelExistingBackgroundTask;

/**
 * Created by jarryd on 21/03/16.
 */
     public class NoteAdapter extends ArrayAdapter<Note> {

        private Note[] noteArray;
        private final Context context;
        private int layout_id;


        private static class ViewHolder {
            protected TextView textView;
            protected myImageView imageView;
        }

        public NoteAdapter(Context context, int layout_id, Note[] noteArray) {
            super(context, layout_id, noteArray);
            this.context = context;
            this.layout_id = layout_id;
            this.noteArray = noteArray;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            /* Declare a ViewHolder object that will hold all the View objects for the Note */
            ViewHolder holder;
            View notePreview = convertView;

            /* Check that a usable View object doesn't already exist */
            if (notePreview == null) {
            /* Inflate (render) the layout file */
                    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                    notePreview = inflater.inflate(layout_id, parent, false);
                    holder = new ViewHolder();
                    holder.textView = (TextView) notePreview.findViewById(R.id.noteTextView);
                    holder.imageView = (myImageView) notePreview.findViewById(R.id.noteImageView);
                    notePreview.setTag(holder);
            } else {
                holder = (ViewHolder) notePreview.getTag(); //Still not totally clear on the Tags
            }

         /* get data from the noteArray to set text for each Note Preview to  note_head */

            holder.textView.setText(noteArray[index].note_head);

            /* get data from the noteArray to set Image for each Note Preview */

//            if (noteArray[index].image_id !=  null) {
//                String image_pathname = context.getFilesDir() + "IMAGE_" + noteArray[index].image_id + "_";
//              setBitmapViaBackgroundTask(holder.imageView.getId(), image_pathname, holder.imageView);
              holder.imageView.setBitmapViaBackgroundTaskFromResource(R.drawable.john);

//            }
            return notePreview;
        }






    }