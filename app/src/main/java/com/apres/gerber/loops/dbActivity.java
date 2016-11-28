package com.apres.gerber.loops;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.apres.gerber.loops.R.color.white;

/**
 * Created by cedriclinares on 11/22/16.
 */

public class dbActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout myDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.db);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        myDB = (LinearLayout) findViewById(R.id.dblayout);
        MapDBHelper mDbHelper = new MapDBHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // This is an array of things we want to get out of the DB.
        // We can ask for everything but only use one thing like in this example.
        String[] projection = {
                MapReaderContract.MapEntry._ID,
                MapReaderContract.MapEntry.COLUMN_ROUTE,
               // MapReaderContract.MapEntry.COLUMN_DIST,
                //MapReaderContract.MapEntry.COLUMN_ALT,
        };
        // Here we make a query and actually get the contents of the DB. For us this can also just be copy and pasted.
        Cursor c = db.query(
                MapReaderContract.MapEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort orderadasd
        );
        //When it returns, there is no way to guarantee it's sorted so I just moved the cursor to whatever
        // is in the first row.
        if (c != null) {
            c.moveToFirst();
        }
        else{
            Toast.makeText(this, "Nothing To Display", Toast.LENGTH_LONG).show();
        }

        //for (int i = 0; i < c.getCount(); i++) {
                // Here is where you can pull the actual content out of the cursor, here I took the route, just replace
                // COLUMN_ROUTE with whichever column you need.
           // for(int j = 0; j < c.getColumnNames().length; j++) {
                int itemId = c.getColumnIndexOrThrow(MapReaderContract.MapEntry.COLUMN_ROUTE);
                //Change it to a string so we can actually display it.
                String className = c.getString(itemId);
                final TextView rowTextView = new TextView(this);
              //  if (j == 0) {
                    rowTextView.setText("Route:" + className);
                //}
                //if (j == 1) {
                 //   rowTextView.setText("Distance:" + className);
                //}
                //if (j == 2) {
                 //   rowTextView.setText("Altitude:" + className);
                //}
                rowTextView.setTextColor(getResources().getColor(white));
                // add the textview to the linearlayout
                myDB.addView(rowTextView);
                c.moveToNext();
           // }
        //}
    }

    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Home Button Pressed", Toast.LENGTH_LONG);
                myIntent = new Intent(dbActivity.this, MapsActivity.class);
                startActivityForResult(myIntent,0);
                return true;
            case R.id.option1:
                return true;

            case R.id.option2:
                //TODO add what to do
                return true;

            case R.id.option3:
                //TODO add what to do
                return true;
            default:
                return false;
        }
    }
}

