package com.example.android.galleryacademyyandex.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.android.galleryacademyyandex.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView authorTextView = findViewById(R.id.about_author);
        authorTextView.setMovementMethod(LinkMovementMethod.getInstance());
        authorTextView.setLinkTextColor(getResources().getColor(R.color.aboutAuthorLink));

        TextView descriptionTextView = findViewById(R.id.about_description);
        descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        descriptionTextView.setLinkTextColor(getResources().getColor(R.color.aboutDescriptionLink));
    }
}
