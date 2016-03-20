package com.example.jarryd.assignment_1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jarryd on 21/03/16.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    private Context context;
    private int resource;
    private Note[] note_array;

    public NoteAdapter(Context context, int resource, Note[] array) {
        super(context, resource, array);
        this.context = context;
        this.resource = resource;
        this.note_array = array;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {

        /* Inflate (render) the layout file */
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View note_preview = inflater.inflate(resource, parent, false);

        /* Instantiate the View objects for each note preview */
        TextView noteText = (TextView) note_preview.findViewById(R.id.noteText);
        ImageView noteImage = (ImageView) note_preview.findViewById(R.id.noteImage);

        /* get data from the stored array of Note objects for the preview view */
        noteText.setText((CharSequence) note_array[index].note_title);

        return note_preview;
    }


}
