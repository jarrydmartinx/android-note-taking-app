package com.example.jarryd.assignment_1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jarryd on 21/03/16.
 */
     public class NoteGridAdapter extends ArrayAdapter<Note> {

    private ArrayList<Note> noteArray;
    public ArrayList<Note> checkedNotes;
    private final Context context;
    private int layout_id;


    private static class ViewHolder {
        protected TextView titleView;
        protected MyImageView imageView;
    }

    public NoteGridAdapter(Context context, int layout_id, ArrayList<Note> noteArray) {
        super(context, layout_id, noteArray);
        System.out.println("################# New NoteGridAdapter Created #######################");
        this.context = context;
        this.layout_id = layout_id;
        this.noteArray = noteArray;
        this.checkedNotes= null;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {

        //          System.out.println("############getView Called by Adapter, index in noteArray = " + index + ", convertView type: "+ convertView +"#############");
            /* Declare a ViewHolder object that will hold all the View objects for the Note */
        ViewHolder holder;
        View notePreview = (View) convertView;

            /* Check that a usable View object doesn't already exist */
        if (notePreview == null) {
            /* Inflate (render) the layout file */
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            notePreview = inflater.inflate(layout_id, parent, false);
            holder = new ViewHolder();
            holder.titleView = (TextView) notePreview.findViewById(R.id.noteTextView);
            holder.imageView = (MyImageView) notePreview.findViewById(R.id.noteImageView);

            notePreview.setTag(
                    context.getResources().getInteger(R.integer.view_tag_for_viewholder),
                    holder);
        } else {
            holder = (ViewHolder) notePreview.getTag(
                    context.getResources().getInteger(R.integer.view_tag_for_viewholder));
        }


            /* Sets the title of the Note preview (if the title is empty it displays the head of the note text */
        if(noteArray.get(index).getNote_title().isEmpty() && noteArray.get(index).getNote_text() != null){
            holder.titleView.setText(noteArray.get(index).getNoteHead());
        }
        else if (noteArray.get(index).note_title != null) {
            holder.titleView.setText(noteArray.get(index).note_title);
        }
        if (noteArray.get(index).image_id != null) {
            notePreview.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            holder.imageView.setBitmapViaBackgroundTask(context, noteArray.get(index).image_id);
            System.out.println("############ON GETVIEW NOTE GRIDADAPTER#######  width is :    " + holder.imageView.getMeasuredWidth() +
                    "   height is :    " + holder.imageView.getMeasuredHeight());
        }
        if(checkedNotes != null){
            if(checkedNotes.contains(noteArray.get(index))){
                notePreview.setBackgroundColor(ContextCompat.getColor(context,R.color.orangered));
            } else{
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
