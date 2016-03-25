package com.example.jarryd.assignment_1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jarryd on 21/03/16.
 */
     public class NoteGridAdapter extends ArrayAdapter<Note> {

        private ArrayList<Note> noteArray;
        private final Context context;
        private int layout_id;


        private static class ViewHolder {
            protected TextView textView;
            protected MyImageView imageView;
        }

        public NoteGridAdapter(Context context, int layout_id, ArrayList<Note> noteArray) {
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
                    holder.imageView = (MyImageView) notePreview.findViewById(R.id.noteImageView);
                    notePreview.setTag(
                            context.getResources().getInteger(R.integer.view_tag_for_viewholder),
                            holder);
            } else {
                holder = (ViewHolder) notePreview.getTag(
                        context.getResources().getInteger(R.integer.view_tag_for_viewholder));
            }

            /* Sets the title and image (if any) of the notePreview*/
            holder.textView.setText(noteArray.get(index).note_title);
            if (noteArray.get(index).image_id !=  null) {
                holder.imageView.setBitmapViaBackgroundTask(context, noteArray.get(index).image_id);
            }
            // Sets a Tag in order to keep the data of the associated Note object with this view for later use
            notePreview.setTag(
                    context.getResources().getInteger(R.integer.view_tag_for_note),
                    noteArray.get(index));

            return notePreview;
        }






    }