package com.apres.gerber.loops;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Huimee on 11/4/2016.
 */

public class ShareTextActivity extends AppCompatActivity {

    private EditText textEntry;
    private Button shareButton;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use activity_share_text.xml
        setContentView(R.layout.activity_share_text);

        textEntry = (EditText) findViewById(R.id.share_text_entry);
        shareButton = (Button) findViewById(R.id.share_text_button);

        setupEvents();
    }

    private void setupEvents() {
        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                String userEntry = textEntry.getText().toString();
                Intent textShareIntent = new Intent (Intent.ACTION_SEND);
                textShareIntent.putExtra (Intent.EXTRA_TEXT, userEntry);
                textShareIntent.setType("text/plain");
                startActivity(Intent.createChooser(textShareIntent, "Share with:"));
            }
        });
    }
}
