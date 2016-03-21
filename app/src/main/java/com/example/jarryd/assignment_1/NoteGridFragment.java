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

public class NoteGridFragment extends Fragment /*implements AdapterView.OnItemClickListener ???*/ {
    private NoteAdapter noteAdapter;

    // Fragment Constructor (intended to be empty, see documentation)
    public NoteGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteAdapter = new NoteAdapter(getActivity(), R.layout.fragment_note_grid);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View noteFragment = inflater.inflate(R.layout.fragment_note_grid, container, false);
        final GridView noteGridView = (GridView) noteFragment.findViewById(R.id.noteGridView);
        noteGridView.setAdapter(noteAdapter);
        //noteGridView.setOnItemClickListener(this); NOT SURE WHAT TO DO WITH THIS
        return noteFragment;
    }

//    @Override LOOK INTO WHAT THIS IS
//    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//        final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
//        i.putExtra(ImageDetailActivity.EXTRA_IMAGE, position);
//        startActivity(i);
//    }

    public class NoteAdapter extends ArrayAdapter<Note> {

        private final Context context;
        private int layout_id;

        private static class ViewHolder {
            protected TextView textView;
            protected ImageView imageView;
        }

        public NoteAdapter(Context context, int layout_id) {
            super(context, layout_id);
            this.context = context;
            this.layout_id = layout_id;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            System.out.println("getView " + index + " " + convertView);
            ViewHolder holder;

            if (convertView == null) {

            /* Inflate (render) the layout file */
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layout_id, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.noteTextView);
                holder.imageView = (ImageView) convertView.findViewById(R.id.noteImageView);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

         /* get data from the noteArray to set Text for each Note Preview */

            holder.textView.setText(noteArray[index].note_title);

        /* get data from the noteArray to set Image for each Note Preview */
            if (noteArray[index].image_name != context.getResources().getString(R.string.no_image)) {
                String image_name = noteArray[index].image_name;
                int image_res_id = context.getResources().getIdentifier(image_name, "drawable", context.getPackageName());
                System.out.println("before setImage yo, "", ");
                holder.imageView.bgSetBitmap(context.getResources(), image_res_id, null));
            }
            return convertView;
        }

        public void bgSetBitmap(int resId, ImageView noteImageView) {
            if (cancelExistingBackgroundTask(resId, noteImageView)) {
                final BackgroundBitmapTask bmpTask = new BackgroundBitmapTask(noteImageView);
                final BackgroundDrawable bgDrawable = new BackgroundDrawable(getResources(), null, bmpTask);
                noteImageView.setImageDrawable(bgDrawable);
                bmpTask.execute(resId);
            }
        }


    }
}
