package org.secuso.privacyfriendly2048;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.app.Application;
import android.content.Intent;
import android.test.ApplicationTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.secuso.privacyfriendly2048.activities.MainActivity;

@RunWith(JUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    // this is used to delay starting the activity until ready to run test
    @Rule
    public ActivityTestRule<MainActivity> activityRuleMain
            = new ActivityTestRule<>(
            MainActivity.class,
            false,
            false);

    @Before
    public void setup() {
        Intents.init();
        startMainActivity();
    }

    @After
    public void teardown() {
        Intents.release();
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(ApplicationProvider.getApplicationContext(),
                MainActivity.class);

        activityRuleMain.launchActivity(mainIntent);
    }

    /*
     * TUTORIALACTIVITY TESTS
     */

    /*
     * test that accessing the tutorial page from the menu opens up the tutorial page with
     * preference, color 1 testing TutorialActivity to help improve branch coverage coverage
     * (also calls MainActivity)
     */
    @Test
    public void testTutorialLaunchesFromMenu() {
        // open tutorial window from menu
        openTutorialWindow();

        // confirm buttons from tutorial screen are visible (this is the way one would check
        // visually that something works, which makes sense with GUI testing)
        onView(withId(R.id.btn_next))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.btn_skip))
                .check(matches(isCompletelyDisplayed()));
    }

    /*
     * test that changing the color scheme changes the colors of the tutorial
     * testing TutorialActivity to help improve branch and statement coverage coverage
     * (also calls MainActivity)
     */
    @Test
    public void testTutorialLaunchesFromMenuPref2() {
        // change the color
        changeColorSchemeToOriginal();

        // open tutorial window from menu
        openTutorialWindow();

        // confirm buttons from tutorial screen are visible (this is the way one would check
        // visually that something works, which makes sense with GUI testing)
        onView(withId(R.id.btn_next))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.btn_skip))
                .check(matches(isCompletelyDisplayed()));
    }

    /*
     * test that pressing the next button from the first page on the tutorial moves to the second page,
     * testing tutorial activity by swtiching pages to help improve branch coverage
     */
    @Test
    public void testTutorialNextButtonOnFirstPageGoesToSecondPage() {
        // open tutorial
        openTutorialWindow();

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
    }

    /*
     * test that pressing the next button from the second page on the tutorial moves to the third page,
     * testing tutorial activity by swtiching pages to help improve branch coverage
     */
    @Test
    public void testTutorialNextButtonOnSecondPageGoesToThirdPage() {
        // open tutorial
        openTutorialWindow();

        // get to second tutorial image
        clickButtonOnce(R.id.btn_next);
        // sanity check, make sure current image being displayed is the second tutorial image
        onView(withId(R.id.image2))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 2 is no longer visible, but image 3 is
        onView(withId(R.id.image2))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.image3))
                .check(matches(isCompletelyDisplayed()));

    }

    /*
     * test that pressing the next button from the third page on the tutorial moves to the fourth page,
     * testing tutorial activity by swtiching pages to help improve branch coverage
     */
    @Test
    public void testTutorialNextButtonOnThirdPageGoesToFourthPage() {
        // open tutorial
        openTutorialWindow();

        // get to third tutorial image
        clickButtonTwice(R.id.btn_next);
        // sanity check, make sure current image being displayed is the third tutorial image
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

    /*
     * test that getting to the last page of the tutorial changes the
     * next button text from "next" to "okay",
     * testing tutorial activity paths (i.e. fully going through the tutorial)to help improve
     * branch coverage
     */
    @Test
    public void testTutorialFinishingTutorialChangesNextButtonText() {
        // open tutorial
        openTutorialWindow();

        // there are three pages of tutorial before the last one,
        // so click "next" three times
        onView(withId(R.id.btn_next))
                .perform(click())
                .perform(click())
                .perform(click());

        // text of the next button should become "Okay"
        onView(withId(R.id.btn_next)).check(matches(withText("Okay")));
    }

    /*
     * test that getting to the last page of the tutorial changes the
     * previous button to invisible, testing tutorial activity to help improve
     * branch coverage
     */
    @Test
    public void testTutorialFinishingTutorialMakesSkipButtonInvisible() {
        // open tutorial
        openTutorialWindow();

        // there are three pages of tutorial before the last one,
        // so click "next" three times
        onView(withId(R.id.btn_next))
                .perform(click())
                .perform(click())
                .perform(click());

        // the skip button should become invisible
        onView(withId(R.id.btn_skip)).check(matches(withEffectiveVisibility(GONE)));
    }

    /*
     * test that finishing the tutorial launches the main game activity,
     * testing tutorial activity starting MainActivity via next button to improve branch coverage
     */
    @Test
    public void testTutorialFinishingTutorialLaunchesMainActivity() {
        // open tutorial
        openTutorialWindow();

        // there are three pages of tutorial before the last one,
        // so click "next" three times
        clickButtonThrice(R.id.btn_next);
        // text of the next button should become "Okay"
        onView(withId(R.id.btn_next)).check(matches(withText("Okay")));

        // now click okay and check that the MainActivity intent has been sent
        onView(withId(R.id.btn_next)).perform(click());

        // another sanity check; check that the buttons on the MainActivity view are present
        onView(withId(R.id.button_newGame)).check(matches(isDisplayed()));
        onView(withId(R.id.button_continueGame)).check(matches(isDisplayed()));
    }

    /*
     * test that skipping the tutorial launches the main game activity,
     * testing tutorial activity starting MainActivity via skip to help improve branch coverage
     */
    @Test
    public void testTutorialSkipLaunchesMainActivity() {
        // open tutorial
        openTutorialWindow();

        // press skip button
        onView(withId(R.id.btn_skip)).perform(click());

        // another sanity check; check that the buttons on the MainActivity view are present
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
        onView(withId(R.id.button_continueGame)).check(matches(isDisplayed()));
    }

    /*
     * test that changing the color scheme to preference 1 (original) changes the tutorial layout
     * testing tutorial activity to help reach code based on color scheme
     */
    @Test
    public void testTutorialWithOriginalColorSchemeDisplaysOriginalColorScheme() {
        // change color to original then open up the tutorial slides
        changeColorSchemeToOriginal();
        openTutorialWindow();

        // differences start between color preferences begin on second page
        onView(withId(R.id.btn_next)).perform(click());

        // check that second original color scheme picture is being displayed
        onView(withId(R.id.image2)).check(matches(isDisplayed()));
        clickButtonOnce(R.id.btn_next);

        // check that third original color scheme picture is being displayed
        onView(withId(R.id.image3)).check(matches(isDisplayed()));
        clickButtonOnce(R.id.btn_next);

        // check that fourth original color scheme picture is being displayed
        onView(withId(R.id.image4)).check(matches(isDisplayed()));
    }
    /*
     * test that changing the color scheme to preference 2 (default) changes the tutorial layout,
     * testing tutorial activity to help reach code based on color scheme
     */
    @Test
    public void testTutorialWithDefaultColorSchemeDisplaysDefaultColorScheme() {
        // change color to original then open up the tutorial slides
        changeColorSchemeToDefault();
        openTutorialWindow();

        // differences start between color preferences begin on second page
        onView(withId(R.id.btn_next)).perform(click());

        // check that second original color scheme picture is being displayed
        onView(withId(R.id.image2)).check(matches(isDisplayed()));
        clickButtonOnce(R.id.btn_next);

        // check that third original color scheme picture is being displayed
        onView(withId(R.id.image3)).check(matches(isDisplayed()));
        clickButtonOnce(R.id.btn_next);

        // check that fourth original color scheme picture is being displayed
        onView(withId(R.id.image4)).check(matches(isDisplayed()));
    }



    /*
     * STATSACTIVITY TESTS
     */

    /*
     * test that accessing the statistics page from the menu opens up the tutorial page,
     * with the default colorscheme
     * testing StatsActivity to help improve branch coverage coverage (also calls MainActivity)
     */
    @Test
    public void testStatisticsLaunchesFromMenuDefaultPreferences() {
        changeColorSchemeToDefault();
        openStatisticsWindow();

        // open 5x5 tab
        openGridTabInStatistics(1);
        // open 6x6 tab
        openGridTabInStatistics(2);
        // open 7x7 tab
        openGridTabInStatistics(3);
    }

    /*
     * test that accessing the statistics page from the menu opens up the tutorial page,
     * with the original colorscheme
     * testing StatsActivity to help improve branch coverage coverage (also calls MainActivity)
     */
    @Test
    public void testStatisticsLaunchesFromMenuOriginalPreferences() {
        changeColorSchemeToOriginal();
        openStatisticsWindow();

        // check that the statistics tabs are visible
        onView(withId(R.id.tabs))
                .check(matches(isCompletelyDisplayed()));

        // open 5x5 tab
        openGridTabInStatistics(1);
        // open 6x6 tab
        openGridTabInStatistics(2);
        // open 7x7 tab
        openGridTabInStatistics(3);
    }

    /*
     * test that swiping in the StatisticsActivity switches tab
     */
    @Test
    public void testStatisticsSwipeWorks() {
        openStatisticsWindow();

        // swipe!
        onView(allOf(withId(R.id.main_content),
                childAtPosition(
                        childAtPosition(
                                withId(R.id.drawer_layout),
                                0),
                        1),
                isDisplayed())).perform(swipeLeft());
    }

    /*
     * test that swiping in the StatisticsActivity switches tab
     */
    @Test
    public void testStatisticsTapTabWorks() {
        openStatisticsWindow();

        // tap!
        onView(allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.tabs),
                                        0),
                                1),
                        isDisplayed()))
                .perform(click());
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the highest number
     */
    @Test
    public void testStatisticsResetResetsHighestNumber4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();
        onView(withId(R.id.highest_number1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the highest score
     */
    @Test
    public void testStatisticsResetResetsHighestScore4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.highest_score1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the playing time
     */
    @Test
    public void testStatisticsResetResetsPlayingTime4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.time_played1))
                .check(matches(withText("0.00 h")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the undos
     */
    @Test
    public void testStatisticsResetResetsUndos4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.undo_number1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the total swipes
     */
    @Test
    public void testStatisticsResetResetsTotalSwipes4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_All1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the time per swipe
     */
    @Test
    public void testStatisticsResetResetsTimePerSwipe4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.time_swipes1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the up swipes
     */
    @Test
    public void testStatisticsResetResetsUpSwipes4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_T1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the down swipes
     */
    @Test
    public void testStatisticsResetResetsDownSwipes4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_D1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the left swipes
     */
    @Test
    public void testStatisticsResetResetsLeftSwipes4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_L1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 4x4 grid resets the right swipes
     */
    @Test
    public void testStatisticsResetResetsRightSwipes4x4() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_R1))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the highest number
     */
    @Test
    public void testStatisticsResetResetsHighestNumber5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();
        onView(withId(R.id.highest_number2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the highest score
     */
    @Test
    public void testStatisticsResetResetsHighestScore5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.highest_score2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the playing time
     */
    @Test
    public void testStatisticsResetResetsPlayingTime5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.time_played2))
                .check(matches(withText("0.00 h")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the undos
     */
    @Test
    public void testStatisticsResetResetsUndos5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.undo_number2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the total swipes
     */
    @Test
    public void testStatisticsResetResetsTotalSwipes5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_All2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the time per swipe
     */
    @Test
    public void testStatisticsResetResetsTimePerSwipe5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.time_swipes2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the up swipes
     */
    @Test
    public void testStatisticsResetResetsUpSwipes5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_T2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the down swipes
     */
    @Test
    public void testStatisticsResetResetsDownSwipes5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_D2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the left swipes
     */
    @Test
    public void testStatisticsResetResetsLeftSwipes5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_L2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 5x5 grid resets the right swipes
     */
    @Test
    public void testStatisticsResetResetsRightSwipes5x5() {
        // open 5x5 tab
        openGridTabInStatistics(1);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_R2))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the highest number
     */
    @Test
    public void testStatisticsResetResetsHighestNumber6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();
        onView(withId(R.id.highest_number3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the highest score
     */
    @Test
    public void testStatisticsResetResetsHighestScore6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.highest_score3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the playing time
     */
    @Test
    public void testStatisticsResetResetsPlayingTime6x6() {
        openStatisticsWindow();

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.time_played3))
                .check(matches(withText("0.00 h")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the undos
     */
    @Test
    public void testStatisticsResetResetsUndos6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.undo_number3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the total swipes
     */
    @Test
    public void testStatisticsResetResetsTotalSwipes6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_All3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the time per swipe
     */
    @Test
    public void testStatisticsResetResetsTimePerSwipe6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.time_swipes3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the up swipes
     */
    @Test
    public void testStatisticsResetResetsUpSwipes6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_T3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the down swipes
     */
    @Test
    public void testStatisticsResetResetsDownSwipes6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_D3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the left swipes
     */
    @Test
    public void testStatisticsResetResetsLeftSwipes6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_L3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 6x6 grid resets the right swipes
     */
    @Test
    public void testStatisticsResetResetsRightSwipes6x6() {
        // open 6x6 tab
        openGridTabInStatistics(2);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_R3))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the highest number
     */
    @Test
    public void testStatisticsResetResetsHighestNumber7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();
        onView(withId(R.id.highest_number4))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the highest score
     */
    @Test
    public void testStatisticsResetResetsHighestScore7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.highest_score4))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the playing time
     */
    @Test
    public void testStatisticsResetResetsPlayingTime7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.time_played4))
                .check(matches(withText("0.00 h")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the undos
     */
    @Test
    public void testStatisticsResetResetsUndos7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.undo_number4))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the total swipes
     */
    @Test
    public void testStatisticsResetResetsTotalSwipes7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_All4))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the time per swipe
     */
    @Test
    public void testStatisticsResetResetsTimePerSwipe7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.time_swipes4))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the up swipes
     */
    @Test
    public void testStatisticsResetResetsUpSwipes7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_T4))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the down swipes
     */
    @Test
    public void testStatisticsResetResetsDownSwipes7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_D4))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the left swipes
     */
    @Test
    public void testStatisticsResetResetsLeftSwipes7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_L4))
                .check(matches(withText("0")));
    }

    /*
     * test that resetting the statistics for a 7x7 grid resets the right swipes
     */
    @Test
    public void testStatisticsResetResetsRightSwipes7x7() {
        // open 7x7 tab
        openGridTabInStatistics(3);

        // select the menu and reset statistics button
        resetStatistics();

        // check that stat is 0
        onView(withId(R.id.moves_R4))
                .check(matches(withText("0")));
    }



    /*
     * SETTINGSACTIVITY TESTS
     */

    /*
     * test that accessing the settings page from the menu opens up the tutorial page,
     * testing SettingsActivity to help improve branch coverage coverage (also calls MainActivity)
     */
    @Test
    public void testSettingLaunchesFromMenu() {
        openSettingsWindow();

        // check that the settings layout is visible
        onView(allOf(withId(android.R.id.title), withText("Activate Animation"),
                withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                isDisplayed())).check(matches(withText("Activate Animation")));
    }

    /*
     * test that makes sure that changing the color preferences in settings updates the settings option
     * testing SettingsActivity to help improve branch coverage coverage (also calls MainActivity)
     */
    @Test
    public void testSettingChangeColorSchemeToOriginalUpdatesInSetting() {
        // change color scheme to original
        changeColorSchemeToOriginal();

        // check that the color scheme now says original instead of default
        onView(withText("Original"))
                .check(matches(isCompletelyDisplayed()));
    }

    /*
     * test that makes sure that changing the color preferences in settings updates the settings option
     * testing SettingsActivity to help improve branch coverage coverage (also calls MainActivity)
     */
    @Test
    public void testSettingChangeColorSchemeToDefaultUpdatesInSetting() {
        // change color scheme to original
        changeColorSchemeToDefault();

        // check that the color scheme now says original instead of default
        onView(withText("Default"))
                .check(matches(isCompletelyDisplayed()));
    }

    /*
     * test that changing the animation switches it in GameActivity
     * testing SettingsActivity to help improve branch coverage coverage (also calls MainActivity
     * and GameActivity)
     */
    @Test
    public void testSettingChangeAnimationActivation() {
        openSettingsWindow();

        // click on the activate animation portion, should switch toggle off
        onView(allOf(withId(android.R.id.title), withText("Activate Animation"),
                withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                isDisplayed())).perform(click());

        // start new game to view animation
        // open menu
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // switch back animation
        backToMainMenu();
        openSettingsWindow();

        // click on the activate animation portion, should switch toggle on
        onView(allOf(withId(android.R.id.title), withText("Activate Animation"),
                withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                isDisplayed())).perform(click());
    }

    private void openMainMenu() {
        openMenuNavigation();
        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                isDisplayed())).perform(click());
    }

    /*
     * HELPACTIVITY TESTS
     */

    /*
     * test that launching the HelpActivity launches the page with the list of information
     */
    @Test
    public void testHelpLaunchesFromMenu() {
        openHelpWindow();

        // check that the list of info is visible
        onView(withId(R.id.generalExpandableListView))
                .check(matches(isCompletelyDisplayed()));
    }



    /*
     * ABOUTACTIVITY TESTS
     */

    /*
     * test that launching the AboutActivity launches the page with all the information
     */
    @Test
    public void testAboutLaunchesFromMenu() {
        openAboutWindow();

        // check that the information options are visible
        onView(withId(R.id.secusoWebsite))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.githubURL))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.textFieldVersionName))
                .check(matches(isCompletelyDisplayed()));
    }



    /*
     * MAINACTIVITY TESTS
     */

    /*
     * test that pressing the next button from the first page on the main menu moves to the second,
     * testing MainActivity by switching pages to help improve branch coverage
     */
    @Test
    public void testMainMenuNextButtonOnFirstGridGoesToSecondGrid() {
        // sanity check, make sure current image being displayed is the first menu image
        onView(withId(R.id.main_menu_img1))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 1 is no longer visible, but image 2 is
        onView(withId(R.id.main_menu_img1))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img2))
                .check(matches(isCompletelyDisplayed()));

        // set back to original menu page for consistency between tests
        clickButtonOnce(R.id.btn_prev);
        onView(withId(R.id.main_menu_img1))
                .check(matches(isCompletelyDisplayed()));
    }

    /*
     * test that pressing the next button from the second page on the main menu moves to the third,
     * testing MainActivity by switching pages to help improve branch coverage
     */
    @Test
    public void testMainMenuNextButtonOnSecondGridGoesToThirdGrid() {
        // get to second main menu image
        clickButtonOnce(R.id.btn_next);
        // sanity check, make sure current image being displayed is the second grid image
        onView(withId(R.id.main_menu_img2))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 2 is no longer visible, but image 3 is
        onView(withId(R.id.main_menu_img2))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img3))
                .check(matches(isCompletelyDisplayed()));

        // set back to original menu page for consistency between tests
        clickButtonTwice(R.id.btn_prev);
        onView(withId(R.id.main_menu_img1))
                .check(matches(isCompletelyDisplayed()));
    }

    /*
     * test that pressing the next button from the third page on the main menu moves to the fourth,
     * testing MainActivity by switching pages to help improve branch coverage
     */
    @Test
    public void testMainMenuNextButtonOnThirdGridGoesToFourthGrid() {
        // get to third menu image
        clickButtonTwice(R.id.btn_next);
        // sanity check, make sure current image being displayed is the third grid image
        onView(withId(R.id.main_menu_img3))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 3 is no longer visible, but image 4 is
        onView(withId(R.id.main_menu_img3))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img4))
                .check(matches(isCompletelyDisplayed()));

        // set back to original menu page for consistency between tests
        clickButtonThrice(R.id.btn_prev);
        onView(withId(R.id.main_menu_img1))
                .check(matches(isCompletelyDisplayed()));
    }


    /*
     * test functionality of the continue button in MainActivity by starting new games
     * for 4x4 grid size. also partially tests the gameActivity
     */
    @Test
    public void testStartingGameThenReturningMakesContinueButtonClickable4x4() {
        // 4x4 GRID automatically

        // perform the test! check that starting a new game enables the continue button (that was
        // previously greyed out
        checkContinueEnabled();

        // navigate back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test functionality of the continue button in MainActivity by starting new games
     * for 5x5 grid size. also partially tests the gameActivity
     */
    @Test
    public void testStartingGameThenReturningMakesContinueButtonClickable5x5() {
        // get to 5x5 GRID
        clickButtonOnce(R.id.btn_next);

        // perform the test! check that starting a new game enables the continue button (that was
        // previously greyed out
        checkContinueEnabled();

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test functionality of the continue button in MainActivity by starting new games
     * for 6x6 grid size. also partially tests the gameActivity
     */
    @Test
    public void testStartingGameThenReturningMakesContinueButtonClickable6x6() {
        // get to 6x6 GRID
        clickButtonTwice(R.id.btn_next);

        // perform the test! check that starting a new game enables the continue button (that was
        // previously greyed out
        checkContinueEnabled();

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test functionality of the continue button in MainActivity by starting new games
     * for 7x7 grid size. also partially tests the gameActivity
     */
    @Test
    public void testStartingGameThenReturningMakesContinueButtonClickable7x7() {
        // get to 7x7 GRID
        clickButtonThrice(R.id.btn_next);

        // perform the test! check that starting a new game enables the continue button (that was
        // previously greyed out
        checkContinueEnabled();

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that launching a game on a 4x4 grid with default color scheme is successful
     * testing the MainActivity (and partially GameActivity)
     */
    @Test
    public void testMainActivityLaunchesGame4x4GridDefaultPreferences() {
        changeColorSchemeToDefault();
        // start game with 4x4 GRID
        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // navigate back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that launching a game on a 5x5 grid with default color scheme is successful
     * testing the MainActivity (and partially GameActivity)
     */
    @Test
    public void testMainActivityLaunchesGame5x5GridDefaultPreferences() {
        changeColorSchemeToDefault();
        // start game with 5x5 GRID
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that launching a game on a 6x6 grid with default color scheme is successful
     * testing the MainActivity (and partially GameActivity)
     */
    @Test
    public void testMainActivityLaunchesGame6x6GridDefaultPreferences() {
        changeColorSchemeToDefault();
        // start game with 6x6 GRID
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that launching a game on a 7x7 grid with default color scheme is successful
     * testing the MainActivity (and partially GameActivity)
     */
    @Test
    public void testMainActivityLaunchesGame7x7GridDefaultPreferences() {
        changeColorSchemeToDefault();
        // start game with 7x7 GRID
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that launching a game on a 4x4 grid with original color scheme is successful
     * testing the MainActivity (and partially GameActivity)
     */
    @Test
    public void testMainActivityLaunchesGame4x4GridOriginalPreferences() {
        changeColorSchemeToOriginal();
        // start game with 4x4 GRID
        openMainMenu();
        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // navigate back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that launching a game on a 5x5 grid with default color scheme is successful
     * testing the MainActivity (and partially GameActivity)
     */
    @Test
    public void testMainActivityLaunchesGame5x5GridOriginalPreferences() {
        changeColorSchemeToOriginal();
        // start game with 5x5 GRID
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that launching a game on a 6x6 grid with default color scheme is successful
     * testing the MainActivity (and partially GameActivity)
     */
    @Test
    public void testMainActivityLaunchesGame6x6GridOriginalPreferences() {
        changeColorSchemeToOriginal();
        // start game with 6x6 GRID
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that launching a game on a 7x7 grid with original color scheme is successful
     * testing the MainActivity (and partially GameActivity)
     */
    @Test
    public void testMainActivityLaunchesGame7x7GridOriginalPreferences() {
        changeColorSchemeToOriginal();
        // start game with 7x7 GRID
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }



    /*
     * GAMEACTIVITY TESTS
     */

    /*
     * test that swiping left on a 4x4 grid with default colorscheme sends all to the left
     */
    @Test
    public void testSwipeLeftSendsAllToLeft4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping left on a 5x5 grid with default colorscheme sends all to the left
     */
    @Test
    public void testSwipeLeftSendsAllToLeft5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping left on a 6x6 grid with default colorscheme sends all to the left
     */
    @Test
    public void testSwipeLeftSendsAllToLeft6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game

        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping left on a 7x7 grid with default colorscheme sends all to the left
     */
    @Test
    public void testSwipeLeftSendsAllToLeft7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping left on a 4x4 grid with original colorscheme sends all to the left
     */
    @Test
    public void testSwipeLeftSendsAllToLeft4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping left on a 5x5 grid with original colorscheme sends all to the left
     */
    @Test
    public void testSwipeLeftSendsAllToLeft5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping left on a 6x6 grid with original colorscheme sends all to the left
     */
    @Test
    public void testSwipeLeftSendsAllToLeft6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping left on a 7x7 grid with original colorscheme sends all to the left
     */
    @Test
    public void testSwipeLeftSendsAllToLeft7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeRightSendsAllToRight4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeRightSendsAllToRight5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeRightSendsAllToRight6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeRightSendsAllToRight7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeRightSendsAllToRight4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeRightSendsAllToRight5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeRightSendsAllToRight6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeRightSendsAllToRight7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeUpSendsAllToUp4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping up multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeUpSendsAllToUp5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeUpSendsAllToUp6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeUpSendsAllToUp7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeUpSendsAllToUp4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping up multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeUpSendsAllToUp5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeUpSendsAllToUp6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeUpSendsAllToUp7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeDownSendsAllDown4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping down multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeDownSendsAllDown5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeDownSendsAllDown6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeDownSendsAllDown7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeDownSendsAllDown4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping down multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeDownSendsAllDown5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeDownSendsAllDown6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testSwipeDownSendsAllDown7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping left multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeLefts4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping left multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeLeft5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping left multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeLeft6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping left multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeLeft7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping left multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeLeft4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping left multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeLeft5x5originalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping left multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeLeft6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping left multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeLeft7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft())
                .perform(swipeLeft());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeRight4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeRight5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeRight6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeRight7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeRight4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeRight5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeRight6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping right multiple times on the specific sized grid with the colorscheme works
     */
    @Test
    public void testMultipleSwipeRight7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeRight());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }


    /*
     * test that swiping up multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeUp4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping up multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeUp5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeUp6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeUp7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeUp4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping up multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeUp5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeUp6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping up multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeUps7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp())
                .perform(swipeUp());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeDowns4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping down multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeDowns5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeDowns6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with default colorscheme works
     */
    @Test
    public void testMultipleSwipeDowns7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testMultipleSwipeDowns4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping down multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testMultipleSwipeDowns5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testMultipleSwipeDowns6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping down multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testMultipleSwipeDowns7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that swiping multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testCombinationOfMultipleSwipe4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testCombinationOfMultipleSwipe5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testCombinationOfMultipleSwipe6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testCombinationOfMultipleSwipe7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }


    /*
     * test that swiping multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testCombinationOfMultipleSwipe4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that swiping multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testCombinationOfMultipleSwipe5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that swiping multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testCombinationOfMultipleSwipe6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that swiping multiple times on the specific sized grid with original colorscheme works
     */
    @Test
    public void testCombinationOfMultipleSwipe7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field))
                .perform(swipeDown())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeRight())
                .perform(swipeRight())
                .perform(swipeLeft())
                .perform(swipeUp())
                .perform(swipeDown());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that undo button on a 4x4 grid with default colorscheme works
     */
    @Test
    public void testUndoButton4x4DefaultColor() {
        changeColorSchemeToDefault();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());
        onView(withId(R.id.undoButton)).perform(click());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that undo button on a 5x5 grid with default colorscheme works
     */
    @Test
    public void testUndoButton5x5DefaultColor() {
        changeColorSchemeToDefault();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());
        onView(withId(R.id.undoButton)).perform(click());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that undo button on a 6x6 grid with default colorscheme works
     */
    @Test
    public void testUndoButton6x6DefaultColor() {
        changeColorSchemeToDefault();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());
        onView(withId(R.id.undoButton)).perform(click());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that undo button on a 7x7 grid with default colorscheme works
     */
    @Test
    public void testUndoButton7x7DefaultColor() {
        changeColorSchemeToDefault();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());
        onView(withId(R.id.undoButton)).perform(click());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test that undo button on a 4x4 grid with original colorscheme works
     */
    @Test
    public void testUndoButton4x4OriginalColor() {
        changeColorSchemeToOriginal();

        // start 4x4 grid game
        openMainMenu();

        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());
        onView(withId(R.id.undoButton)).perform(click());

        // go back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test that undo button on a 5x5 grid with original colorscheme works
     */
    @Test
    public void testUndoButton5x5OriginalColor() {
        changeColorSchemeToOriginal();

        // start 5x5 grid game
        openMainMenu();
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());
        onView(withId(R.id.undoButton)).perform(click());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test that undo button on a 6x6 grid with original colorscheme works
     */
    @Test
    public void testUndoButton6x6OriginalColor() {
        changeColorSchemeToOriginal();

        // start 6x6 grid game
        openMainMenu();
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());
        onView(withId(R.id.undoButton)).perform(click());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test that undo button on a 7x7 grid with original colorscheme works
     */
    @Test
    public void testUndoButton7x7OriginalColor() {
        changeColorSchemeToOriginal();
        // start 4x4 grid game
        openMainMenu();
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());
        onView(withId(R.id.undoButton)).perform(click());

        // go back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }

    /*
     * test restart on 4x4 grid;
     * helps improve coverage in GameActivity
     */
    @Test
    public void testRestartGridOnAllGrids() {
        // 4x4 GRID automatically, so start a new game
        startNewGame(R.id.main_menu_img1);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // restart
        onView(withId(R.id.restartButton)).perform(click());

        // navigate back to main menu for consistency between tests
        backToMainMenu();
    }

    /*
     * test restart on 5x5 grid;
     * helps improve coverage in GameActivity
     */
    @Test
    public void testRestartGridOn5x5Grid() {
        // navigate to 5x5 GRID and start a new game
        clickButtonOnce(R.id.btn_next);
        startNewGame(R.id.main_menu_img2);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // restart
        onView(withId(R.id.restartButton)).perform(click());

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonOnce(R.id.btn_prev);
    }

    /*
     * test restart on 4x4 grid;
     * helps improve coverage in GameActivity
     */
    @Test
    public void testRestartGridOn6x6Grid() {
        // navigate to 6x6 GRID and start a new game
        clickButtonTwice(R.id.btn_next);
        startNewGame(R.id.main_menu_img3);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // restart
        onView(withId(R.id.restartButton)).perform(click());

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonTwice(R.id.btn_prev);
    }

    /*
     * test restart on 7x7 grid;
     * helps improve coverage in GameActivity
     */
    @Test
    public void testRestartGridOn7x7Grid() {
        // navigate to 7x7 GRID and start a new game
        clickButtonThrice(R.id.btn_next);
        startNewGame(R.id.main_menu_img4);

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        // restart
        onView(withId(R.id.restartButton)).perform(click());

        // navigate back to main menu for consistency between tests
        backToMainMenu();
        clickButtonThrice(R.id.btn_prev);
    }


    /*
     * HELPER FUNCTIONS
     */

    /*
     * helper function to open the tutorial window from the main activity
     */
    private void openTutorialWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                2),
                        isDisplayed())).perform(click());

    }


    /*
     * helper function to open the statistics window from the main activity
     */
    private void openStatisticsWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(
                                allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                3),
                        isDisplayed())).perform(click());

    }

    /*
     * helper function to open the settings window from the main activity
     */
    private void openSettingsWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                5),
                        isDisplayed())).perform(click());

    }

    /*
     * helper function to open the help window from the main activity
     */
    private void openHelpWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                6),
                        isDisplayed())).perform(click());
    }

    /*
     * helper function to open the about window from the main activity
     */
    private void openAboutWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                7),
                        isDisplayed())).perform(click());

    }

    /*
     * helper function to open menu to get to all of the menu activities above
     */
    private void openMenuNavigation() {
        onView(allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed())).perform(click());
    }

    /*
     * helper function to open the specific tab for any grid in statisitcs
     */
    private void openGridTabInStatistics(int position) {
        openStatisticsWindow();
        onView(allOf(childAtPosition(childAtPosition(
                        withId(R.id.tabs),
                        0),position),
                isDisplayed()))
                .perform(click());
    }

    /*
     * helper function to start a new game for a grid selection on the main menu
     */
    private void startNewGame(int img) {
        onView(withId(img))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.button_newGame)).perform(click());
    }

    /*
     * helper function to navigate back to the main page (with all of the game board selections)
     * via the menu
     */
    private void backToMainMenu() {
        onView(allOf(withContentDescription("Navigate up"),
                childAtPosition(
                        allOf(withId(android.support.design.R.id.action_bar),
                                childAtPosition(
                                        withId(android.support.design.R.id.action_bar_container),
                                        0)),
                        1),
                isDisplayed())).perform(click());
    }

    /*
     * helper function to change the users color preferences to the original
     */
    private void changeColorSchemeToOriginal() {
        openSettingsWindow();

        // click on color scheme
        onData(anything()).inAdapterView(allOf(withId(android.R.id.list),
                        childAtPosition(withId(android.R.id.list_container),0)))
                .atPosition(2)
                .perform(click());

        // select the original option
        onData(anything()).inAdapterView(allOf(withClassName(
                                is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)))
                .atPosition(1).perform(click());
    }

    /*
     * helper function to change the users color preferences to the default
     */
    private void changeColorSchemeToDefault() {
        openSettingsWindow();

        // click on color scheme
        onData(anything()).inAdapterView(allOf(withId(android.R.id.list),
                        childAtPosition(withId(android.R.id.list_container),0)))
                .atPosition(2)
                .perform(click());

        // select the original option
        onData(anything()).inAdapterView(allOf(withClassName(
                                is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)))
                .atPosition(0).perform(click());
    }

    /*
     * helper function to check that, for whatever grid this was called on, the continue button
     * is enabled after starting a new game
     */
    private void checkContinueEnabled() {
        onView(withId(R.id.button_newGame)).perform(click());

        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        backToMainMenu();

        // check we're back on main page by looking for buttons
        onView(withId(R.id.button_newGame)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.button_continueGame)).check(matches(isCompletelyDisplayed()));


        // check that continue game is possible by trying to click
        onView(withId(R.id.button_continueGame)).perform(click());

        // check game restarted by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
    }

    /*
     * helper function to click a button once
     * used primarily to click through pages, both forward and back
     * takes in the button to press once
     */
    private void clickButtonOnce(int btn) {
        onView(withId(btn))
                .perform(click());
    }

    /*
     * helper function to click a button twice
     * used primarily to click through pages, both forward and back
     * takes in the button to press twice
     */
    private void clickButtonTwice(int btn) {
        onView(withId(btn))
                .perform(click())
                .perform(click());
    }

    /*
     * helper function to click a button thrice
     * used primarily to click through pages, both forward and back
     * takes in the button to press thrice
     */
    private void clickButtonThrice(int btn) {
        onView(withId(btn))
                .perform(click())
                .perform(click())
                .perform(click());
    }

    /*
     * helper method to reset the game statistics
     */
    private void resetStatistics() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(allOf(withId(android.support.coreui.R.id.title), withText("Reset Statistics"),
                        childAtPosition(childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),0), isDisplayed()))
                .perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}