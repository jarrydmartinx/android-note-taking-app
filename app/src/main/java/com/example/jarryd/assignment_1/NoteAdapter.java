package com.example.jarryd.assignment_1;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
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

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {

        /* Inflate (render) the layout file */
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View notePreview = inflater.inflate(layout_id, parent, false);

        /* Instantiate the View objects for each note preview */
        TextView noteText = (TextView) notePreview.findViewById(R.id.noteText);
        ImageView noteImage = (ImageView) notePreview.findViewById(R.id.noteImage);


        /* get data from the noteArray to set Text for each Note Preview */
        noteText.setText((CharSequence) noteArray[index].note_title);

        /* get data from the noteArray to set Image for each Note Preview */
      //  int image_res_id = context.getResources().getIdentifier(noteArray[index].image_name,"drawable",context.getPackageName());
        // String image_reference = "R.drawable." + noteArray[index].image_name;
        Path path = Paths.get(image_reference)
        int image_res_id = R.drawable.john;
        noteImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), image_res_id, null));



        return notePreview;
    }


}
