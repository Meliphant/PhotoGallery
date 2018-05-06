package com.example.android.galleryacademyyandex;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.galleryacademyyandex.ui.PhotoActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class PhotoActivityShareClickTest {
    private static final String shareMsg = "https://vignette.wikia.nocookie.net/meme/images/9/95/%D0%A3%D0%98%D0%A1%D0%9C4.jpg/revision/latest?cb=20170507085307&path-prefix=ru";

    @Rule
    public final IntentsTestRule<PhotoActivity> mActivityTestRule =
            new IntentsTestRule<PhotoActivity>(PhotoActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent(InstrumentationRegistry.getTargetContext(),
                            PhotoActivity.class);
                    intent.putExtra(PhotoActivity.INTENT_PHOTO, shareMsg);
                    return intent;
                }
            };

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickShareButton() {
        Espresso.onView(withId(R.id.action_share)).perform(click());
//        intended(allOf(hasAction(Intent.ACTION_CHOOSER), hasExtra(Intent.EXTRA_TEXT, shareMsg)));

        intended(allOf(hasAction(Intent.ACTION_CHOOSER),
                hasExtra((Intent.EXTRA_INTENT), allOf(hasAction(Intent.ACTION_SEND), hasExtra(Intent.EXTRA_TEXT, shareMsg) ))));
    }
}
