package com.example.jarryd.assignment_1;

import android.app.Activity;
import android.content.Context;
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


            /* Sets the title and image (if any) of the notePreview*/
        if (noteArray.get(index).note_title != null)
            holder.titleView.setText(noteArray.get(index).note_title);
        if (noteArray.get(index).image_id != null) {
            holder.imageView.setBitmapViaBackgroundTask(context, context.getFilesDir() + "/" + noteArray.get(index).image_id + ".jpg");
        }
        // Sets a Tag in order to keep the data of the associated Note object with this view for later use
//            notePreview.setTag(
//                    context.getResources().getInteger(R.integer.view_tag_for_note),
//                    noteArray.get(index));

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
