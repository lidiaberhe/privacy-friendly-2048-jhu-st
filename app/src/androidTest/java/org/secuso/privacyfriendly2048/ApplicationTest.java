package org.secuso.privacyfriendly2048;

import static androidx.test.espresso.Espresso.onView;
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

/*
 * TODO: Test Undo button
 */

@RunWith(JUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    // this is used to delay starting the activity until ready
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
//
//    @Test
//    public void testMainActivityTryContinuingWhenNoGamePresentAllGames() {
//        continueWhenNoPreviousGameGridSpecific(R.id.main_menu_img1);
//        onView(withId(R.id.btn_next)).perform(click());
//        continueWhenNoPreviousGameGridSpecific(R.id.main_menu_img2);
//        onView(withId(R.id.btn_next)).perform(click());
//        continueWhenNoPreviousGameGridSpecific(R.id.main_menu_img3);
//        onView(withId(R.id.btn_next)).perform(click());
//        continueWhenNoPreviousGameGridSpecific(R.id.main_menu_img4);
//
//        resetGameOptionsTo4x4();
//    }

    @Test
    public void testTutorialLaunchesFromMenu() {
        openTutorialWindow();

        // confirm buttons from tutorial screen are visible
        onView(withId(R.id.btn_next))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.btn_skip))
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testStatisticsLaunchesFromMenu() {
        openStatisticsWindow();

        // check that the statistics tabs are visible
        onView(withId(R.id.tabs))
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testSettingLaunchesFromMenu() {
        openSettingsWindow();

        // check that the settings layout is visible
        onView(allOf(withId(android.R.id.title), withText("Activate Animation"),
                withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                isDisplayed())).check(matches(withText("Activate Animation")));
    }

    @Test
    public void testHelpLaunchesFromMenu() {
        openHelpWindow();

        // check that the list of info is visible
        onView(withId(R.id.generalExpandableListView))
                .check(matches(isCompletelyDisplayed()));
    }

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

    @Test
    public void testTutorialNextGoesToNextPage() {
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

    @Test
    public void testTutorialFinishingTutorialLaunchesMainActivity() {
        // open tutorial
        openTutorialWindow();


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

        // another sanity check; check that the buttons on the MainActivity view are present
        onView(withId(R.id.button_newGame)).check(matches(isDisplayed()));
        onView(withId(R.id.button_continueGame)).check(matches(isDisplayed()));
    }

    @Test
    public void testTutorialSkipLaunchesMainActivity() {
        // open tutorial
        openTutorialWindow();

        // press skip button
        onView(withId(R.id.btn_skip)).perform(click());

        // check that the MainActivity intent has been sent
        // intended works similarly to verify from mockito
//        intended(hasComponent(MainActivity.class.getName()));

        // another sanity check; check that the buttons on the MainActivity view are present
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
        onView(withId(R.id.button_continueGame)).check(matches(isDisplayed()));
    }

    @Test
    public void testMainActivityNextButtonGoesForwardAndPrevButtonGoesBackward() {
        nextButtonGoesForward();
        prevButtonGoesBackward();
    }

    @Test
    public void testSwipeLeftAllGridsSendsAllToLeft() {
        // TODO: Find way to check grid to make sure all at left

        startNewGame(R.id.main_menu_img1);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        backToMainMenu();

        // 5x5 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img2);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        backToMainMenu();

        // 6x6 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img3);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        backToMainMenu();

        // 7x7 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img4);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeLeft());

        backToMainMenu();

        resetGameOptionsTo4x4();
//        Element[][] elements = GameActivity.elements;
//        assertEquals(R.color.button_empty, elements[0][1].getSolidColor());

    }

    @Test
    public void testSwipeRightAllGridsSendsAllToRight() {
        // TODO: Find way to check grid to make sure all at left

        startNewGame(R.id.main_menu_img1);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        backToMainMenu();

        // 5x5 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img2);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        backToMainMenu();

        // 6x6 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img3);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        backToMainMenu();

        // 7x7 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img4);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeRight());

        backToMainMenu();

        resetGameOptionsTo4x4();
//        Element[][] elements = GameActivity.elements;
//        assertEquals(R.color.button_empty, elements[0][1].getSolidColor());
    }

    @Test
    public void testSwipeUpAllGridsSendsAllUp() {
        // TODO: Find way to check grid to make sure all at left

        startNewGame(R.id.main_menu_img1);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        backToMainMenu();

        // 5x5 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img2);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        backToMainMenu();

        // 6x6 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img3);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        backToMainMenu();

        // 7x7 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img4);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeUp());

        backToMainMenu();

        resetGameOptionsTo4x4();
//        Element[][] elements = GameActivity.elements;
//        assertEquals(R.color.button_empty, elements[0][1].getSolidColor());

    }

    @Test
    public void testSwipeDownAllGridsSendsDownUp() {
        // TODO: Find way to check grid to make sure all at left

        startNewGame(R.id.main_menu_img1);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        backToMainMenu();

        // 5x5 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img2);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        backToMainMenu();

        // 6x6 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img3);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        backToMainMenu();

        // 7x7 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img4);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.touch_field)).perform(swipeDown());

        backToMainMenu();

        resetGameOptionsTo4x4();
//        Element[][] elements = GameActivity.elements;
//        assertEquals(R.color.button_empty, elements[0][1].getSolidColor());

    }

    @Test
    public void testMainActivityLaunchesGameAllGrids() {
        // 4x4 GRID
        startNewGame(R.id.main_menu_img1);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        backToMainMenu();

        // 5x5 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img2);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        backToMainMenu();

        // 6x6 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img3);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        backToMainMenu();

        // 7x7 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img4);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));

        backToMainMenu();

        resetGameOptionsTo4x4();
    }
    @Test
    public void testResetGridOnAllGrids() {
        // 4x4 GRID
        startNewGame(R.id.main_menu_img1);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.restartButton)).perform(click());

        backToMainMenu();

        // 5x5 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img2);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.restartButton)).perform(click());

        backToMainMenu();

        // 6x6 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img3);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.restartButton)).perform(click());

        backToMainMenu();

        // 7x7 GRID
        onView(withId(R.id.btn_next)).perform(click());
        startNewGame(R.id.main_menu_img4);
        // check that new game started by looking for grid
        onView(withId(R.id.touch_field)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.restartButton)).perform(click());;

        backToMainMenu();

        resetGameOptionsTo4x4();
    }

    @Test
    public void testStartingGameThenReturningMakesContinueButtonClickable() {
        // 4x4 GRID
        checkContinueEnabled();
        backToMainMenu();

        // 5x5 GRID
        onView(withId(R.id.btn_next)).perform(click());
        checkContinueEnabled();
        backToMainMenu();

        // 6x6 GRID
        onView(withId(R.id.btn_next)).perform(click());
        checkContinueEnabled();
        backToMainMenu();

        // 7x7 GRID
        onView(withId(R.id.btn_next)).perform(click());
        checkContinueEnabled();
        backToMainMenu();

        resetGameOptionsTo4x4();
    }

//    @Test
//    public void testMainActivitySwipeLeftGoesForwardAndSwipeRightGoesBackward() throws InterruptedException {
//        startMainActivity();
//
//
//        ViewInteraction viewPager = onView(
//                allOf(withId(R.id.view_pager),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.main_content),
//                                        0),
//                                0),
//                        isDisplayed()));
//            viewPager.perform(swipeLeft());
//
//        ViewInteraction viewPager2 = onView(
//                allOf(withId(R.id.view_pager),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.main_content),
//                                        0),
//                                0),
//                        isDisplayed()));
//            viewPager2.perform(swipeLeft());
//
//        ViewInteraction viewPager3 = onView(
//                allOf(withId(R.id.view_pager),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.main_content),
//                                        0),
//                                0),
//                        isDisplayed()));
//            viewPager3.perform(swipeLeft());
//
//        onView(withId(R.id.main_menu_img4))
//                .check(matches(isCompletelyDisplayed()));
//
//        ViewInteraction viewPager4 = onView(
//                allOf(withId(R.id.view_pager),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.main_content),
//                                        0),
//                                0),
//                        isDisplayed()));
//            viewPager4.perform(swipeRight());
//
//        ViewInteraction viewPager5 = onView(
//                allOf(withId(R.id.view_pager),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.main_content),
//                                        0),
//                                0),
//                        isDisplayed()));
//            viewPager5.perform(swipeRight());
//
//        ViewInteraction viewPager6 = onView(
//                allOf(withId(R.id.view_pager),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.main_content),
//                                        0),
//                                0),
//                        isDisplayed()));
//            viewPager6.perform(swipeRight());
//
//        onView(withId(R.id.main_menu_img4))
//                .check(matches(isCompletelyDisplayed()));
//}

    private void openTutorialWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                2),
                        isDisplayed())).perform(click());

    }

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

    private void openSettingsWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                5),
                        isDisplayed())).perform(click());

    }

    private void openHelpWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                6),
                        isDisplayed())).perform(click());
    }

    private void openAboutWindow() {
        openMenuNavigation();

        onView(allOf(childAtPosition(allOf(withId(android.support.design.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                7),
                        isDisplayed())).perform(click());

    }

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

    private void resetGameOptionsTo4x4() {
        onView(withId(R.id.btn_prev))
                .perform(click())
                .perform(click())
                .perform(click());
    }

    private void startNewGame(int img) {
        onView(withId(img))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.button_newGame)).perform(click());
    }

//    private void continueWhenNoPreviousGameGridSpecific(int img) {
//        onView(withId(img))
//                .check(matches(isCompletelyDisplayed()));
//
//        // press next button
//        onView(withId(R.id.button_continueGame)).perform(click());
//
//        // sanity check; check that the buttons on the MainActivity view are still present
//        onView(withId(R.id.view_pager)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_continueGame)).check(matches(isDisplayed()));
//    }

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

    private void nextButtonGoesForward() {
        // sanity check, make sure current image being displayed is the first tutorial image
        onView(withId(R.id.main_menu_img1))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 1 is no longer visible, but image 2 is
        onView(withId(R.id.main_menu_img1))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img2))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 2 is no longer visible, but image 3 is
        onView(withId(R.id.main_menu_img2))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img3))
                .check(matches(isCompletelyDisplayed()));
        // press next button
        onView(withId(R.id.btn_next)).perform(click());

        // now check that image 3 is no longer visible, but image 4 is
        onView(withId(R.id.main_menu_img3))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img4))
                .check(matches(isCompletelyDisplayed()));
    }

    private void prevButtonGoesBackward() {
        // sanity check, make sure current image being displayed is the first tutorial image
        onView(withId(R.id.main_menu_img4))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_prev)).perform(click());

        // now check that image 1 is no longer visible, but image 2 is
        onView(withId(R.id.main_menu_img4))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img3))
                .check(matches(isCompletelyDisplayed()));

        // press next button
        onView(withId(R.id.btn_prev)).perform(click());

        // now check that image 2 is no longer visible, but image 3 is
        onView(withId(R.id.main_menu_img3))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img2))
                .check(matches(isCompletelyDisplayed()));
        // press next button
        onView(withId(R.id.btn_prev)).perform(click());

        // now check that image 3 is no longer visible, but image 4 is
        onView(withId(R.id.main_menu_img2))
                .check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.main_menu_img1))
                .check(matches(isCompletelyDisplayed()));
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