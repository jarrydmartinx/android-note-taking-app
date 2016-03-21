package com.example.jarryd.assignment_1;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
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
    private int layout_id;
    private Note[] noteArray;

    public NoteAdapter(Context context, int layout_id, Note[] array) {
        super(context, layout_id, array);
        this.context = context;
        this.layout_id = layout_id;
        this.noteArray = array;
    }

    public static class ViewHolder {
        public TextView noteTextView;
        public ImageView noteImageView;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        View notePreview;

        if (convertView == null) {
            ViewHolder holder;


            /* Inflate (render) the layout file */
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            notePreview = inflater.inflate(layout_id, parent, false);

            // Instantiate ViewHolder to hold XML elements of notePreview
            holder = new ViewHolder();
            holder.noteTextView = (TextView) notePreview.findViewById(R.id.noteTextView);
            holder.noteImageView = (ImageView) notePreview.findViewById(R.id.noteImageView);

            /* get data from the noteArray to set Text for each Note Preview */
            holder.noteTextView.setText((CharSequence) noteArray[index].note_title);

            /* get data from the noteArray to set Image for each Note Preview */
            int image_res_id = context.getResources().getIdentifier(noteArray[index].image_name, "drawable", context.getPackageName());
            holder.noteImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), image_res_id, null));
        } else {
            notePreview = convertView;
        }

        return notePreview;
    }


}
