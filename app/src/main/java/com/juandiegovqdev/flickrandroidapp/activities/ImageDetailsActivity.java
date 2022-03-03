package com.juandiegovqdev.flickrandroidapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.juandiegovqdev.flickrandroidapp.R;
import com.squareup.picasso.Picasso;

public class ImageDetailsActivity extends AppCompatActivity {

    Context context;
    String author, title, description, url, link, date;
    ImageView imageView;
    AppCompatTextView imageTitle;
    AppCompatTextView imageDate;
    AppCompatTextView imageAuthor;
    AppCompatTextView imageDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        initializeActionBar();
        context = this;
        initializeUI();
        initializeIntents();
        initializeData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share_button) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String body = "Hey! Check this image: " + url;
            String sub = "What an image!";
            myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
            myIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(myIntent, "Share Using"));
        }
        return super.onOptionsItemSelected(item);
    }

    public void initializeActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initializeUI() {
        imageView = findViewById(R.id.image);
        imageTitle = findViewById(R.id.image_title);
        imageDate = findViewById(R.id.image_date);
        imageAuthor = findViewById(R.id.image_author);
        imageDescription = findViewById(R.id.image_description);
    }

    public void initializeIntents() {
        author = getIntent().getStringExtra("author");
        date = getIntent().getStringExtra("date");
        description = getIntent().getStringExtra("description");
        link = getIntent().getStringExtra("link");
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
    }

    public void initializeData() {
        Picasso.get().load(url).into(imageView);
        imageTitle.setText(title);
        imageAuthor.setText(author);
        imageDate.setText(date);
        imageDescription.setText(description);
    }
}
