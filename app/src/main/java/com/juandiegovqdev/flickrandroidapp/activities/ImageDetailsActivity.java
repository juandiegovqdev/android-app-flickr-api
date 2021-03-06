package com.juandiegovqdev.flickrandroidapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.juandiegovqdev.flickrandroidapp.R;
import com.juandiegovqdev.flickrandroidapp.views.ZoomableImageView;
import com.squareup.picasso.Picasso;

public class ImageDetailsActivity extends AppCompatActivity {

    Context context;
    String author, title, description, url, link, date;
    ImageView imageView;
    ZoomableImageView fullScreenImg;
    AppCompatTextView imageTitle, imageDate, imageAuthor, imageDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        initializeActionBar();
        context = this;
        initializeUI();
        initializeIntents();
        initializeData();
        initializeListeners();
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
            String body = getString(R.string.share_body) + url;
            String sub = getString(R.string.share_sub);
            myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
            myIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(myIntent, getString(R.string.share_using)));
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
        fullScreenImg = findViewById(R.id.full_screen_img);
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

    public void initializeListeners() {
        imageView.setOnClickListener(view -> {
            fullScreenImg.setVisibility(View.VISIBLE);
            Picasso.get().load(url).into(fullScreenImg);
        });
    }

    public void initializeData() {
        Picasso.get().load(url).into(imageView);
        imageTitle.setText(title);
        imageAuthor.setText(author);
        imageDate.setText(date);
        imageDescription.setText(description);
    }

    public void hideFullScreenImg(View view) {
        view.setVisibility(View.GONE);
        try {
            getSupportActionBar().show();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }
}
