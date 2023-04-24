package org.secuso.privacyfriendly2048;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.app.Application;
import android.content.Intent;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.secuso.privacyfriendly2048.activities.MainActivity;
import org.secuso.privacyfriendly2048.activities.SplashActivity;

@RunWith(JUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

//    @Rule public ActivityScenarioRule<TutorialActivity> activityScenarioRule
//            = new ActivityScenarioRule<TutorialActivity>(TutorialActivity.class, true, false);

    // this is used to delay starting the activity until ready
    @Rule
    public ActivityTestRule<SplashActivity> activityRule
            = new ActivityTestRule<>(
        SplashActivity.class,
            false,
            false);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void teardown() {
        Intents.release();
//        ((ActivityManager) Objects.requireNonNull(ApplicationProvider.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE))).clearApplicationUserData();
//        Runtime runtime = Runtime.getRuntime();
//        runtime.exec("pm clear org.secuso.privacyfriendly2048");
    }

    private void startSplashActivity() {
        Intent mainIntent = new Intent(ApplicationProvider.getApplicationContext(),
                SplashActivity.class);

        activityRule.launchActivity(mainIntent);
    }

    @Test
    public void testTutorialSkipLaunchesMainActivity() {

        startSplashActivity();

        // press skip button
        onView(withId(R.id.btn_skip)).perform(click());

        // check that the MainActivity intent has been sent
        // intended works similarly to verify from mockito
        intended(hasComponent(MainActivity.class.getName()));

        // another sanity check; check that the buttons on the MainActivity view are present
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
        onView(withId(R.id.button_continueGame)).check(matches(isDisplayed()));
    }

    @Test
    public void testTutorialNextGoesToNextPage() {
        startSplashActivity();

        // sanity check, make sure current image being displayed is the first tutorial image
        onView(withId(R.id.image1))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 1 is no longer visible, but image 2 is
        onView(withId(R.id.image1))
                .check(matches(not(isCompletelyDisplayed())));
         onView(withId(R.id.image2))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 2 is no longer visible, but image 3 is
        onView(withId(R.id.image2))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.image3))
                .check(matches(isCompletelyDisplayed()));
        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 3 is no longer visible, but image 4 is
        onView(withId(R.id.image3))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.image4))
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testTutorialFinishingTutorialChangesNextButtonText() {
        startSplashActivity();

        // there are three pages of tutorial before the last one,
        // so click "next" three times
        onView(withId(R.id.btn_next))
                .perform(click())
                .perform(click())
                .perform(click());

        // text of the next button should become "Okay"
        onView(withId(R.id.btn_next)).check(matches(withText("Okay")));
    }

    @Test
    public void testTutorialFinishingTutorialMakesSkipButtonInvisible() {
        startSplashActivity();

        // there are three pages of tutorial before the last one,
        // so click "next" three times
        onView(withId(R.id.btn_next))
                .perform(click())
                .perform(click())
                .perform(click());

        // the skip button should become invisible
        onView(withId(R.id.btn_skip)).check(matches(withEffectiveVisibility(GONE)));
    }

    @Test
    public void testTutorialFinishingTutorialLaunchesMainActivity() {
        startSplashActivity();

        // there are three pages of tutorial before the last one,
        // so click "next" three times
        onView(withId(R.id.btn_next))
                .perform(click())
                .perform(click())
                .perform(click())
                // text of the next button should become "Okay"
                .check(matches(withText("Okay")));

        // now click okay and check that the MainActivity intent has been sent
        onView(withId(R.id.btn_next)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));

        // another sanity check; check that the buttons on the MainActivity view are present
        onView(withId(R.id.button_newGame)).check(matches(isDisplayed()));
        onView(withId(R.id.button_continueGame)).check(matches(isDisplayed()));
    }
}