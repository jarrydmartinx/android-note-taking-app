package com.example.jarryd.assignment_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Just dummy code coz I need a StringArray */

        String[] myStringArray = {"Abacus", "Boris", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson", "Catson"};

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /* Click listener for responding to notePreview click, for launching EditNoteActivity */
        AdapterView.OnItemClickListener notePreviewClickedListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent launchEditNoteIntent = new Intent(MainActivity.this, EditNoteActivity.class);
                startActivity(launchEditNoteIntent);
            }

            public void onItemLongClick(AdapterView parent, View view, int position, long id) {
                /* I want the context menu to show up here */
            }
        };

        /* Constructor for main GridView of note previews */
        GridView noteGridView = (GridView) findViewById(R.id.noteGridView);
        /* Instantiate ArrayAdapter that liases bw data and GridView */
        ArrayAdapter<String> noteGridAdapter = new ArrayAdapter<String>(this, R.layout.note_preview, R.id.noteText, myStringArray);
        noteGridView.setAdapter(noteGridAdapter);
        noteGridView.setOnItemClickListener(notePreviewClickedListener);

          /* Click handler    id is items id number in container  */



}/* THis is just for updating the data in the previews in my grid. must notify after edit or won't show up.
    ListView myList=
            (ListView) findViewById(R.id.listView);
    ArrayAdapter myAdapt=
            (ArrayAdapter)myList.getAdapter();
    myAdapt.notifyDataSetChanged();  */





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    public void PrintFirstMessage(View view) {
//        Log.v(TAG, "First Button Was Pressed");
//    }
}