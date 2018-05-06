package com.example.android.galleryacademyyandex.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.galleryacademyyandex.R;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getResources().getString(R.string.activity_about_title));
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView authorTextView = findViewById(R.id.about_author);
        authorTextView.setMovementMethod(LinkMovementMethod.getInstance());
        authorTextView.setLinkTextColor(getResources().getColor(R.color.aboutAuthorLink));

        TextView descriptionTextView = findViewById(R.id.about_description);
        descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        descriptionTextView.setLinkTextColor(getResources().getColor(R.color.aboutDescriptionLink));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
