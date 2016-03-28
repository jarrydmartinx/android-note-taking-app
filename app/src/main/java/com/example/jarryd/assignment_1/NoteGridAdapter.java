package com.example.jarryd.assignment_1;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jarryd on 21/03/16.
 */
     public class NoteGridAdapter extends ArrayAdapter<Note> {

    private ArrayList<Note> noteArray;
    public ArrayList<Note> checkedNotes;
    private final Context context;
    private Resources res;
    private int layout_id;
    private Display display;
    private DisplayMetrics metrics;


    private static class ViewHolder {
        protected TextView titleView;
        protected TextView textView;
        protected MyImageView imageView;
        private String imageId;
    }

    public NoteGridAdapter(Context context, int layout_id, ArrayList<Note> noteArray, Display display) {
        super(context, layout_id, noteArray);
        this.context = context;
        this.layout_id = layout_id;
        this.noteArray = noteArray;
        this.display = display;
        res = context.getResources();
        checkedNotes= null;

    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {

       /* Declare a ViewHolder object that will hold all the View objects for the Note */
        ViewHolder holder;
        View notePreview = convertView;

        Note note = getItem(index);
        boolean newViewFlag = false;
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        /* Creates a new View Object if there isn't an existing one to recycle */
        if (notePreview == null) {
            /* Inflate (render) the layout file */

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            notePreview = inflater.inflate(layout_id, parent, false);
            newViewFlag = true;


            holder = new ViewHolder();
            holder.titleView = (TextView) notePreview.findViewById(R.id.noteTitleView);
            holder.textView = (TextView) notePreview.findViewById(R.id.noteTextView);
            holder.imageView = (MyImageView) notePreview.findViewById(R.id.noteImageView);

            holder.imageId = note.getNote_id();
            notePreview.setTag(holder);

        }
        //Recycles an old View if available
        else {
            holder = (ViewHolder) notePreview.getTag();
        }

        if(note.getImage_id()==null) {
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setHeight(metrics.widthPixels / 4);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.textView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, holder.titleView.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
            holder.textView.setLayoutParams(layoutParams);

            //If there is no image for this Note, show both the Note title and text in the preview
            if (note.getNote_title().isEmpty()) {
                holder.titleView.setText("...");
            } else {
                holder.titleView.setText((note.getNote_title()));
            }

            holder.textView.setText(note.getNote_text());
            //If the Note has no Image, and an existing View is being reused by the Adapter,
            // set the ImageDrawable to null (if an new view is being used, no image will have been set.)
            if (!newViewFlag) {
                holder.imageView.setImageDrawable(null);
            }
        }
        else {
            // If this Note has an image, only show the image and Note title in the preview (not the whole text)
            // (or if the title is empty, show head of the text in place of the title)
                holder.textView.setVisibility(View.GONE);
                holder.textView.setHeight(0);
                holder.imageView.setMinimumHeight(metrics.widthPixels / 4);


//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.textView.getLayoutParams();
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
//            holder.imageView.setLayoutParams(layoutParams);

                if (getItem(index).getNote_title().isEmpty()) {
                    holder.titleView.setText(note.getNoteHead());
                } else {
                    holder.titleView.setText(note.getNote_title());
                }

            // If the ImageView in the ViewHolder isn't showing the right image for this Note, set the image from file
                if (!holder.imageId.equals(note.getImage_id())) {
                    //Set the minimum size of the imageView based on the Display Metrics of the screen
                    //This size will be used by setBitmapViaBackgroundTask for decoding down the image
                   // holder.imageView.setMinimumWidth(display, R.integer.IM_SCALE_FACTOR_GRID);
                    System.out.println("________________###########DISPLAY WIDTH IS:  " + metrics.widthPixels + "_____________________##########################");
                    holder.imageView.setBitmapViaBackgroundTask(context, note.getImage_id(), metrics.widthPixels, res.getInteger(R.integer.IM_SCALE_FACTOR_GRID));
                }
        }

        if(checkedNotes != null) {
            if (checkedNotes.contains(getItem(index))) {
                notePreview.setBackgroundColor(ContextCompat.getColor(context, R.color.orangered));
            } else {
                notePreview.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            }
        }

        return notePreview;
    }

    public void updateCheckedNoteList(SparseBooleanArray positions) {
        this.checkedNotes = new ArrayList<>();
        for (int i = 0; i < noteArray.size(); i++) {
            int note_position = getPosition(noteArray.get(i));
            if (positions.get(note_position)) {
                checkedNotes.add(noteArray.get(i));
            }
        }
    }

}
