package org.secuso.privacyfriendly2048;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.internal.matchers.ArrayEquals;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
//import org.robolectric.shadows.support.v4.Shadows;
import org.secuso.privacyfriendly2048.R;
import org.secuso.privacyfriendly2048.activities.helper.GameStatistics;
import org.secuso.privacyfriendly2048.activities.helper.GameState;
import org.secuso.privacyfriendly2048.activities.Element;
import org.secuso.privacyfriendly2048.activities.MainActivity;
import org.secuso.privacyfriendly2048.activities.GameActivity;
import org.secuso.privacyfriendly2048.activities.StatsActivity;
import org.secuso.privacyfriendly2048.helpers.FirstLaunchManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.icu.util.ValueIterator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Objects;


@RunWith(RobolectricTestRunner.class)
public class AppTest {

    Context context = ApplicationProvider.getApplicationContext();

    Intent intent = new Intent();

    /*
    Test Helper Functions
     */
    private boolean compareStatistics(GameStatistics gs1, GameStatistics gs2) {
        return gs1.getFilename().equals(gs2.getFilename()) &&
                gs1.toString().equals(gs2.toString());
    }

    private boolean compareStates(GameState gs1, GameState gs2) {
        return gs1.toString().equals(gs2.toString());
    }

    private boolean compareElements(Element elm1, Element elm2) {
        return elm1.getNumber() == elm2.getNumber() &&
                elm1.getdPosX() == elm2.getdPosX() &&
                elm1.getdPosY() == elm2.getdPosY();
    }

    private void pref1ColorWhite(Element elm) {
        elm.drawItem();
        assertEquals(View.VISIBLE, elm.getVisibility());
        assertEquals("" + elm.getNumber(), elm.getText());
        assertEquals(ContextCompat.getColor(context,R.color.white), elm.getCurrentTextColor());
    }

    private void pref1ColorBlack(Element elm) {
        elm.drawItem();
        assertEquals(View.VISIBLE, elm.getVisibility());
        assertEquals("" + elm.getNumber(), elm.getText());
        assertEquals(ContextCompat.getColor(context,R.color.black), elm.getCurrentTextColor());
    }

    private void setDefaultPreferenceTo2() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("pref_color", "2"); // change preference value
        editor.commit();
    }

    /*
    GameStatistics Test
     */

    private final GameStatistics stats = new GameStatistics(2);

    @Test
    public void testSetRecordMinValue() {
        stats.setRecord(Long.MIN_VALUE);
        assertTrue(stats.getRecord() >=0);
    }

    @Test
    public void setHighestNumberMinLong() {
        long high = stats.getHighestNumber();
        stats.setHighestNumber(Long.MIN_VALUE);
        assertEquals(high, stats.getHighestNumber());
    }

    @Test
    public void setHighestNumberMaxLong() {
        stats.setHighestNumber(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, stats.getHighestNumber());
    }

    @Test
    public void setHighestNumberEqual() {
        stats.setHighestNumber(2L);
        assertEquals(2L, stats.getHighestNumber());
    }

    @Test // how does it handle when time played exceeds MAX_VALUE
    public void addTimePlayedExceedMaxLong() {
        stats.addTimePlayed(10L);
        assertEquals(10L, stats.getTimePlayed());
        stats.addTimePlayed(Long.MAX_VALUE);
        assertTrue(stats.getTimePlayed() >= 0);
    }

    @Test // how does it handle getting a negative value
    public void addTimePlayedMinLong() {
        stats.addTimePlayed(Long.MIN_VALUE);
        assertTrue(stats.getTimePlayed() >= 0);
    }

    @Test
    public void addTimePlayedAddsValues() {
        stats.addTimePlayed(10L);
        stats.addTimePlayed(34L);
        assertEquals(44L, stats.getTimePlayed());
    }

    @Test
    public void testUndo() {
        stats.undo();
        assertEquals(1, stats.getUndo());
    }

    @Test
    public void testMoveL() {
        stats.moveL();
        assertEquals(1, stats.getMoves_l());
    }

    @Test
    public void testMoveR() {
        stats.moveR();
        assertEquals(1, stats.getMoves_r());
    }

    @Test
    public void testMoveT() {
        stats.moveT();
        assertEquals(1, stats.getMoves_t());
    }

    @Test
    public void testMoveD() {
        stats.moveD();
        assertEquals(1, stats.getMoves_d());
    }

    @Test // add moves can add 1 move
    public void testAddMovesOneMove() {
        stats.addMoves(1);
        assertEquals(1L, stats.getMoves());
    }

    @Test // add moves can add 2 move
    public void testAddMovesTwoMoves() {
        stats.addMoves(1L);
        stats.addMoves(32L);
        assertEquals(33L, stats.getMoves());
    }

    @Test // add move don't take negative values
    public void testAddMovesMinValue() {
        stats.addMoves(Long.MIN_VALUE);
        assertTrue(stats.getMoves()>=0);
    }

    @Test // test to see how add moves handle overflow
    public void testAddMovesExceedsMaxValue() {
        stats.addMoves(1L);
        stats.addMoves(Long.MAX_VALUE);
        assertTrue(stats.getMoves()>=0);
    }


    /*
    GameState Tests
     */

    private final GameState state = new GameState(2);

    @Test
    public void testGetNumberReturnsValidValue() {
        state.numbers = new int[] {1,2,3,4};
        assertEquals(2, state.getNumber(0,1));

    }

    @Test // when array out of bounds returns 0
    public void testGetNumberReturnsExceptionReturns0() {
        state.numbers = new int[] {1,2,3,4};
        assertEquals(0, state.getNumber(0,4));
    }

    @Test
    public void testGetLastNumberReturnsValidValue() {
        state.last_numbers = new int[] {1,2,3,4};
        assertEquals(2, state.getLastNumber(0,1));

    }

    @Test // when array out of bounds returns 0
    public void testGetLastNumberReturnsExceptionReturns0() {
        state.last_numbers = new int[] {1,2,3,4};
        assertEquals(0, state.getLastNumber(0,4));
    }

    @Test // test nothing gets set in constructor when elements are empty
    public void testGameStateEmptyElements() {
        GameState gameState = new GameState(new Element[][]{}, new Element[][]{});
        assertArrayEquals(new int[]{}, gameState.numbers);
        assertArrayEquals(new int[]{}, gameState.last_numbers);
        assertEquals(0, gameState.n);
    }

    @Test
    public void testGameStateSecondElementArrEmpty() {
        Element elm = new Element(context);
        elm.setNumber(2);
        GameState gameState = new GameState(new Element[][]{new Element[]{elm}}, new Element[][]{});
        assertArrayEquals(new int[]{2}, gameState.numbers);
        assertArrayEquals(new int[]{}, gameState.last_numbers);
        assertEquals(1, gameState.n);
    }

    @Test // reach branch coverage in first loop
    public void testGameState1SecondElementArrEmpty() {
        Element elm = new Element(context); elm.setNumber(2);
        GameState gameState = new GameState(
                new Element[][]{new Element[]{elm, elm}}, new Element[][]{});
//        assertArrayEquals(new int[]{2,2}, gameState.numbers);
//        assertArrayEquals(new int[]{}, gameState.last_numbers);
        assertEquals(1, gameState.n);
    }

    @Test // reach branch coverage in first loop
    public void testGameStateFistElementArrEmpty() {
        Element elm = new Element(context); elm.setNumber(2);
        GameState gameState = new GameState(
                new Element[][]{}, new Element[][]{new Element[]{elm}});
        assertArrayEquals(new int[]{}, gameState.numbers);
        assertArrayEquals(new int[]{2}, gameState.last_numbers);
        assertEquals(0, gameState.n);
    }

    @Test // reach branch coverage in first loop
    public void testGameState2FistElementArrEmpty() {
        Element elm = new Element(context); elm.setNumber(2);
        GameState gameState = new GameState(
                new Element[][]{}, new Element[][]{new Element[]{elm, elm}});
//        assertArrayEquals(new int[]{}, gameState.numbers);
//        assertArrayEquals(new int[]{2}, +gameState.last_numbers);
        assertEquals(0, gameState.n);
    }

    /*
    Element Tests
     */


    @Test
    public void testElementSetsTextSize() {
        Element elm = new Element(context);
        assertEquals(elm.textSize, elm.getTextSize(), 0.0);
//        assertEquals(context.getResources().getDrawable(R.drawable.game_brick), elm.getBackground());
    }

    @Test
    public void testToString() {
        Element elm = new Element(context);
        elm.setNumber(4);

        assertEquals("number: 4", elm.toString());
    }

    @Test
    public void testElementCopyEquals() {
        Element elm = new Element(context);
        elm.setNumber(5);
        elm.setDPosition(2,5);
        elm.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
        Element elm2 = elm.copy();

        assertTrue(compareElements(elm, elm2));
    }

    @Test
    public void testUpdateFontSizeUpdates() {
        Element elm = new Element(context);
        elm.setLayoutParams(new ViewGroup.LayoutParams(14, 14));
        float size = elm.getTextSize();
        elm.updateFontSize();
        assertTrue(size != elm.getTextSize());
    }

    @Test // when element is initially invisible, set it to visible
    public void testDrawItemInvisibleNumber2 () {
        Element elm = new Element(context);
        elm.setVisibility(View.INVISIBLE);
        elm.setNumber(2);
        elm.drawItem();
        assertEquals(View.VISIBLE, elm.getVisibility());
    }

    @Test
    public void testElementDrawItemNumber0() {
        Element elm = new Element(context);
        elm.drawItem();
        assertEquals(View.INVISIBLE, elm.getVisibility());
        assertEquals("", elm.getText());
        assertEquals(ContextCompat.getColor(context,R.color.black), elm.getCurrentTextColor());
    }

    @Test
    public void testElementDrawItemNumber1() {
        Element elm = new Element(context);
        elm.setNumber(2);
        elm.drawItem();
        assertEquals(View.VISIBLE, elm.getVisibility());
        assertEquals("" + elm.getNumber(), elm.getText());
        assertEquals(ContextCompat.getColor(context,R.color.black), elm.getCurrentTextColor());
    }

    @Test
    public void testElementDrawItemNumber4() {
        Element elm = new Element(context);
        elm.setNumber(4);
        pref1ColorBlack(elm);
//        int color = 0;
//        Drawable background = elm.getBackground();
//        if (background instanceof ShapeDrawable) {
//            color = ((ShapeDrawable)background).getPaint().getColor();
//        } else if (background instanceof GradientDrawable) {
//            color = Objects.requireNonNull(((GradientDrawable) background).getColor().getColorForState(elm.getDrawableState(), elm.getDrawingCacheBackgroundColor()));
//        } else if (background instanceof ColorDrawable) {
//            color = ((ColorDrawable)background).getColor();
//        }
//
//        assertEquals(color, ContextCompat.getColor(context, R.color.button_empty));
    }

    @Test // test drawItem when number is negative
    public void testElementDrawItemNumberNeg() {
        Element elm = new Element(context);
        elm.setNumber(-1);
        int color = elm.getCurrentTextColor();
        elm.drawItem();
        assertEquals(color, elm.getCurrentTextColor());
    }

    @Test
    public void testElementDrawItemNumber8() {
        Element elm = new Element(context);
        elm.setNumber(8);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber16() {
        Element elm = new Element(context);
        elm.setNumber(16);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber32() {
        Element elm = new Element(context);
        elm.setNumber(32);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber64() {
        Element elm = new Element(context);
        elm.setNumber(64);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber128() {
        Element elm = new Element(context);
        elm.setNumber(128);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber256() {
        Element elm = new Element(context);
        elm.setNumber(256);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber512() {
        Element elm = new Element(context);
        elm.setNumber(512);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber1024() {
        Element elm = new Element(context);
        elm.setNumber(1024);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber2048() {
        Element elm = new Element(context);
        elm.setNumber(2048);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber4096() {
        Element elm = new Element(context);
        elm.setNumber(4096);
        pref1ColorBlack(elm);
    }

    @Test
    public void testElementDrawItemNumber8192() {
        Element elm = new Element(context);
        elm.setNumber(8192);
        pref1ColorBlack(elm);
    }

    @Test
    public void testElementDrawItemNumber16384() {
        Element elm = new Element(context);
        elm.setNumber(16384);
        float size = elm.getTextSize();
        pref1ColorWhite(elm);
        assertTrue(size != elm.getTextSize());
    }

    @Test
    public void testElementDrawItemNumber32768() {
        Element elm = new Element(context);
        elm.setNumber(32768);
        float size = elm.getTextSize();
        pref1ColorWhite(elm);
        assertTrue(size != elm.getTextSize());
    }

    @Test // test drawItem when number is negative and pref is changed
    public void testElementDrawItemPref2NumberNeg() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(-1);
        int color = elm.getCurrentTextColor();
        elm.drawItem();
        assertEquals(color, elm.getCurrentTextColor());
    }

    @Test
    public void testElementDrawItemPref2Number0() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.drawItem();
        assertEquals(View.INVISIBLE, elm.getVisibility());
        assertEquals("", elm.getText());
        assertEquals(ContextCompat.getColor(context,R.color.black), elm.getCurrentTextColor());
    }

    @Test
    public void testElementDrawItemPref2Number2() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(2);
        elm.drawItem();
        assertEquals(View.VISIBLE, elm.getVisibility());
        assertEquals("" + elm.getNumber(), elm.getText());
        assertEquals(ContextCompat.getColor(context,R.color.black), elm.getCurrentTextColor());
    }

    @Test
    public void testElementDrawItemPref2Number4() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(4);
        pref1ColorBlack(elm);
    }

    @Test
    public void testElementDrawItemPref2Number8() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(8);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number16() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(16);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number32() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(32);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number64() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(64);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number128() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(128);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number256() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(256);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number512() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(512);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number1024() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(1024);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number2048() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(2048);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number4096() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(4096);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number8192() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(8192);
        pref1ColorWhite(elm);
    }

    @Test
    public void testElementDrawItePref2mNumber16384() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(16384);
        float size = elm.getTextSize();
        pref1ColorWhite(elm);
        assertTrue(size != elm.getTextSize());
    }

    @Test
    public void testElementDrawItemPref2Number32768() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(32768);
        float size = elm.getTextSize();
        pref1ColorWhite(elm);
        assertTrue(size != elm.getTextSize());
    }



    /*
    GameActivity Tests
     */

    @Test (expected = NullPointerException.class)
    public void testInitializeWithUndoFalse() {
        Intent intent = new Intent().putExtra("undo", true);
        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent).setup()) {
            GameActivity activity = controller.get();
            activity.initialize();

            assertEquals(View.VISIBLE, activity.findViewById(R.id.undoButton).getVisibility());
        }
    }


    @Test // test createNewGame function initializes certain fields
    public void testCreateNewGame() {
        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class).setup()) {
            GameActivity activity = controller.get();
            activity.createNewGame();

            assertEquals("0" ,activity.textFieldPoints.getText());
            assertEquals("0" ,activity.textFieldRecord.getText());
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.undoButton).getVisibility());
        }
    }

    @Test // test elements actually switch places
    public void testSwitchElementPositions() {
        Element e1 = new Element(context);
        e1.dPosX = 2; e1.dPosY = 3;

        Element e2 = new Element(context);
        e2.dPosX = 1; e2.dPosY = 4;

        GameActivity gameActivity = new GameActivity();
        gameActivity.switchElementPositions(e1, e2);

        assertTrue(e1.getdPosX() == 1 && e1.getdPosY() ==4);
        assertTrue(e1.animateMoving);
        assertTrue(e2.getdPosX() == 2 && e2.getdPosY() ==3);
        assertFalse(e2.animateMoving);
    }

    @Test // test function when elem arr empty
    public void testDrawAllElementsEmpty() {
        GameActivity activity = new GameActivity();
        activity.drawAllElements(new Element[][]{});
    }

    @Test // run for loop with one element
    public void testDrawAllElementsRunOnce() {
        Element e1 = new Element(context);
        e1.setVisibility(View.VISIBLE);

        GameActivity activity = new GameActivity();
        activity.drawAllElements(new Element[][]{new Element[]{e1}});

        assertEquals(View.INVISIBLE, e1.getVisibility());
    }

    @Test // run for loop with 2 element
    public void testDrawAllElementsRunTwice() {
        Element e1 = new Element(context);
        e1.setVisibility(View.VISIBLE);

        Element e2 = new Element(context);
        e2.setVisibility(View.INVISIBLE);
        e2.setNumber(4);

        GameActivity activity = new GameActivity();
        activity.drawAllElements(new Element[][]{new Element[]{e1, e2}});

        assertEquals(View.INVISIBLE, e1.getVisibility());
        assertEquals(View.VISIBLE, e2.getVisibility());
    }

    @Test // when the player has not won yet, stats is not saved
    public void testCheck2048Won2048False() {
        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class).setup()) {
            GameActivity activity = controller.get();
            activity.initialize();

            activity.won2048 = false;
            activity.check2048();

            File file = new File(activity.getFilesDir(), "statistics4.txt");
            assertFalse(file.exists());

            activity.getFilesDir().delete();
        }

    }

    @Test // when the player has reaches threshold, stats is saved
    public void testCheck2048Won2048PlayerReachThreshold() {
        int n = 4;
        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("new", false);
        intent.putExtra("filename", "state4.txt");

        GameState gst = new GameState(2);
        gst.points = 3;
        gst.numbers = new int[] {2,4,8,2048,2,2,2,2,2,2,2,2,2,2,2,2,};

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent).setup()) {
            GameActivity activity = controller.get();
            String [] files  = activity.getFilesDir().list();

            File stateFile = new File(activity.getFilesDir(), "state4.txt");
            File statsFile = new File(activity.getFilesDir(), "statistics4.txt");

            assertFalse(statsFile.exists());
            assertFalse(stateFile.exists());

            // create GameState object file
            FileOutputStream fileOut = new FileOutputStream(stateFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gst);
            out.close();
            fileOut.close();

            // make sure it exists
            assertTrue(stateFile.exists());

            // now initialize with intent
            activity.initialize();
            activity.won2048 = false;
            activity.check2048();

            assertTrue(statsFile.exists());
            assertTrue(activity.won2048);

            activity.getFilesDir().delete();
            
        } catch (IOException e) {
//                e.printStackTrace();
        }
    }

    @Test
    public void testCheck2048Won2048NoFileCreated() {
        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class).setup()) {
            GameActivity activity = controller.get();
            activity.initialize();

            activity.won2048 = true;
            activity.check2048();

            File file = new File(activity.getFilesDir(), "statistics4.txt");
            assertFalse(file.exists());

            activity.getFilesDir().delete();
            
        }
    }

    @Test // calling game over should save the current game stats
    public void testGameOverSavesGameStatistics() {
        int n = 3;
        Intent intent = new Intent().putExtra("n", n);
        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent).setup()) {
            GameActivity activity = controller.get();

            File file = new File(activity.getFilesDir(), "statistics"+n+".txt");
            assertFalse(file.exists());

            activity.initialize();
            activity.gameOver();

            assertTrue(file.exists());
            activity.getFilesDir().delete();
            
        }
    }

    @Test // calling readStatisticsFromFile on empty file should return a new GameStatistics object
    public void readStatisticsFromFileEmpty() {
        int n = 2;

        Intent intent = new Intent();
        intent.putExtra("n", n);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent).setup()) {
            GameActivity activity = controller.get();
            activity.initializeState();

            File file = new File(activity.getFilesDir(), "statistics" + n + ".txt");
            file.createNewFile();
            assertTrue(file.exists());

            GameStatistics gs1 = activity.readStatisticsFromFile();
            GameStatistics gs2 = new GameStatistics(n);
            assertTrue(compareStatistics(gs1, gs2));
            System.out.println(activity.getFilesDir());

            activity.getFilesDir().delete();

            

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test // calling readStatisticsFromFile on nonexistent file should return a new GameStatistics object
    public void readStatisticsFromFileOnNonexistentFile() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = controller.get();
            activity.initializeState();

            GameStatistics gs1 = activity.readStatisticsFromFile();
            GameStatistics gs2 = new GameStatistics(n);
            assertTrue(compareStatistics(gs1, gs2));

            System.out.println(activity.getFilesDir());

            activity.getFilesDir().delete();

            

        }
    }

    @Test // calling readStatisticsFromFile on corrupted file should return new GameStatistics object
    public void readStatisticsFromFileOnCorruptedFile() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            File file = new File(activity.getFilesDir(), "statistics" + n + ".txt");
            file.createNewFile();

            String str = "Hello";
            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
            byte[] strToBytes = str.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();

            GameStatistics gs1 = activity.readStatisticsFromFile();
            GameStatistics gs2 = new GameStatistics(n);
            assertTrue(compareStatistics(gs1, gs2));

            activity.getFilesDir().delete();

            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test // writes stats to file and file exists
    public void saveStatisticsToFile() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = controller.get();
            activity.initializeState();

            GameStatistics gs1 = new GameStatistics(n);
            gs1.setHighestNumber(25);
            gs1.setRecord(100);

            activity.saveStatisticsToFile(gs1);

            File file = new File(activity.getFilesDir(), gs1.getFilename());
            assertTrue(file.exists());

            activity.getFilesDir().delete();

            
        }
    }

    @Test(expected = NullPointerException.class)
    // saving null object throws NullPointerException exception
    public void saveStatisticsToFileNullStats() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = controller.get();
            activity.initializeState();

            activity.saveStatisticsToFile(null);
            activity.getFilesDir().delete();

            

        }
    }

    @Test // calling saveStatisticsToFile on a GameStatistics object then calling readStatisticsFromFile should return same object
    public void integrationTestOnSaveStatisticsToFileAndReadStatisticsFromFile() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = controller.get();
            activity.initializeState();

            GameStatistics gs1 = new GameStatistics(n);
            gs1.setHighestNumber(25);
            gs1.setRecord(100);

            activity.saveStatisticsToFile(gs1);
            GameStatistics gs2 = activity.readStatisticsFromFile();
            assertTrue(compareStatistics(gs1, gs2));

            activity.getFilesDir().delete();

            
        }
    }


    @Test // reading state from nonexistent file should return new GameState object
    public void readStateFromFileNonExistent() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            GameState ms1 = new GameState(n);
            GameState ms2 = activity.readStateFromFile();
            assertTrue(compareStates(ms1, ms2));

            activity.getFilesDir().delete();

            
        }
    }

    @Test // reading state from empty file should return new GameState object
    public void readStateFromFileEmpty() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            File file = new File(activity.getFilesDir(), filename);
            file.createNewFile();

            GameState ms1 = new GameState(n);
            GameState ms2 = activity.readStateFromFile();
            assertTrue(compareStates(ms1, ms2));

            activity.getFilesDir().delete();

            
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

    @Test // reading state from corrupted file should return new GameState object
    public void readStateFromFileCorrupted() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            File file = new File(activity.getFilesDir(), filename);
            file.createNewFile();

            String str = "Hello";
            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
            byte[] strToBytes = str.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();

            GameState ms1 = new GameState(n);
            GameState ms2 = activity.readStateFromFile();
            assertTrue(compareStates(ms1, ms2));

            activity.getFilesDir().delete();

            
        } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    @Test // saving state to file when file name provided actually creates file under given name
    public void saveStateToFileFilenameGiven() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = controller.get();
            activity.initializeState();

            GameState ms = new GameState(n);
            activity.saveStateToFile(ms);

            File file = new File(activity.getFilesDir(), filename);
            assertTrue(file.exists());

            activity.getFilesDir().delete();

            
        }
    }


    @Test // saving state to file when file name not provided actually creates file under new name
    public void saveStateToFileFilenameNotGiven() {
        int n = 2;

        Intent intent = new Intent();
        intent.putExtra("n", n);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = controller.get();
            activity.initializeState();

            GameState ms = new GameState(n);
            activity.saveStateToFile(ms);

            File file = new File(activity.getFilesDir(), "state"+n+".txt");
            assertTrue(file.exists());

            activity.getFilesDir().delete();

            
        }
    }

    @Test(expected = NullPointerException.class) // writes to file
    public void saveStateToFileNullState() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = controller.get();

            activity.initializeState();
            activity.saveStateToFile(null);

            File file = new File(activity.getFilesDir(), filename);
            assertTrue(file.exists());

            activity.getFilesDir().delete();

        }
    }


    @Test // calling saveStateToFile on a GameState object then calling readStateFromFile should return same object
    public void integrationTestOnSaveStateToFileAndReadStateFromFile() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initialize();

            GameState ms1 = new GameState(n);
            ms1.points = 30;
            ms1.n = n;
            ms1.undo = true;
            ms1.numbers = new int[]{-1,1,2};

            activity.saveStateToFile(ms1);
            GameState ms2 = activity.readStateFromFile();
            assertTrue(compareStates(ms1, ms2));

            activity.getFilesDir().delete();

            
        }
    }

    @Test // calling saveStateToFile on a GameState object then calling readStateFromFile should return same object
    // case: gamestate.numbers = []
    public void integrationTestOnSaveStateToFileAndReadStateFromFile2() {
        int n = 4;
        String filename = "state"+n+".txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            GameState ms1 = new GameState(n);
            ms1.points = 30;
            ms1.n = n;
            ms1.undo = true;
            ms1.numbers = new int[]{};

            activity.saveStateToFile(ms1);
            GameState ms2 = activity.readStateFromFile();
            assertTrue(compareStates(new GameState(n), ms2));

            activity.getFilesDir().delete();
        }
    }

    @Test // calling saveStateToFile on a GameState object then calling readStateFromFile should return same object
    // case: gameState.n != n
    public void integrationTestOnSaveStateToFileAndReadStateFromFile3() {
        int n = 3;
        String filename = "state"+n+".txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initialize();

            GameState ms1 = new GameState(3);
            ms1.points = 30;
            ms1.n = n;
            ms1.undo = true;
            ms1.numbers = new int[]{};

            activity.saveStateToFile(ms1);
            GameState ms2 = activity.readStateFromFile();
            assertFalse(compareStates(ms1, ms2));

            activity.getFilesDir().delete();

            
        }
    }

    @Test // reading file with null object should not return null object
    public void integrationTestOnSaveStateToFileAndReadStateFromFileWithNullState() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            activity.saveStateToFile(null);
            GameState ms1 = new GameState(n);
            GameState ms2 = activity.readStateFromFile();
            assertTrue(compareStates(ms1, ms2));

            activity.getFilesDir().delete();

            
        }
    }

    @Test // calling readStatisticsFRomFile on GameState Object file should recognize wrong object and return new Stats object
    public void integrationTestOnSaveStateToFileAndReadStatisticsFromFileWithStateObject() {
        int n = 2;
        String filename = "statistics2.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            activity.saveStateToFile(new GameState(n));
            GameStatistics gs1 = new GameStatistics(n);
            GameStatistics gs2 = activity.readStatisticsFromFile();
            assertTrue(compareStatistics(gs1, gs2));

            activity.getFilesDir().delete();

            
        }
    }

    @Test // calling readStateFromFile on GameStatistics Object file should recognize wrong object and return new State object
    public void integrationTestOnSaveStatisticsToFileAndReadStateFromFileWithStatsObject() {
        int n = 2;
        String filename = "statistics2.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            activity.saveStatisticsToFile(new GameStatistics(n));
            GameState ms1 = new GameState(n);
            GameState ms2 = activity.readStateFromFile();
            assertTrue(compareStates(ms1, ms2));

            activity.getFilesDir().delete();

            
        }
    }

    @Test // reading file with null object should not return null object
    public void integrationTestOnSaveStateToFileAndReadStatisticsFromFileWithNullState() {
        int n = 2;
        String filename = "statistics2.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            activity.saveStateToFile(null);
            GameStatistics gs1 = new GameStatistics(n);
            GameStatistics gs2 = activity.readStatisticsFromFile();
            assertTrue(compareStatistics(gs1, gs2));

            activity.getFilesDir().delete();

            
        }
    }

    @Test // deleting a nonexistent state file should return false
    public void deleteStateFileNonexistentFilenameGiven() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            assertFalse(activity.deleteStateFile());
            activity.getFilesDir().delete();

            
        }
    }

    @Test // deleting a nonexistent state file should return false
    public void deleteStateFileNonexistentFilenameNotGiven() {
        int n = 2;

        Intent intent = new Intent();
        intent.putExtra("n", n);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            assertFalse(activity.deleteStateFile());
            activity.getFilesDir().delete();
        }
    }

    @Test // deleting a state file that exist should return true
    public void deleteStateFileExist() {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            File file = new File(activity.getFilesDir(), filename);
            file.createNewFile();
            assertTrue(activity.deleteStateFile());

            activity.getFilesDir().delete();

            
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

    @Test // saving a state to file and then deleting that file should return true
    public void integrationTestSaveStateToFileAndDeleteStateFile () {
        int n = 2;
        String filename = "stateFile.txt";

        Intent intent = new Intent();
        intent.putExtra("n", n);
        intent.putExtra("filename", filename);

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class, intent)) {
            controller.setup();
            GameActivity activity = (controller.get());
            activity.initializeState();

            GameState ms = new GameState(n);
            activity.saveStateToFile(ms);
            assertTrue(activity.deleteStateFile());

            activity.getFilesDir().delete();

            
        }
    }


    @Test // test no exception or anything breaks when called
    public void testSetDPositionsAnimationTrue() {
        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class).setup()) {
            GameActivity activity = controller.get();
            activity.initialize();
            activity.setDPositions(true);
        }
    }

    @Test // test no exception or anything breaks when called
    public void testSetDPositionsAnimationFalse() {
        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class).setup()) {
            GameActivity activity = controller.get();
            activity.initialize();
            activity.setDPositions(false);
        }
    }

    @Test // stats file created and saved when restart button clicked
    public void testInitResourcesRestartButtonSavesStats() {

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class)) {
            controller.setup();
            GameActivity activity = controller.get();

            activity.initialize();
            activity.initResources();

            // make sure file don't exist
            File file = new File(activity.getFilesDir(), "statistics4.txt");
            assertFalse(file.exists());

            // click button
            activity.findViewById(R.id.restartButton).performClick();

            // should exist now
            assertTrue(file.exists());
        }
    }

    @Test // test undo button visibility not update when undo is false
    public void testInitResourcesUndoButtonFalse() {

        try (ActivityController<GameActivity> controller = Robolectric.buildActivity(GameActivity.class)) {
            controller.setup();
            GameActivity activity = controller.get();

            activity.initialize();
            activity.initResources();

            // click button
            ImageButton ib = activity.findViewById(R.id.undoButton);
            ib.setVisibility(View.VISIBLE);
            ib.performClick();
            assertEquals(View.INVISIBLE, ib.getVisibility());
        }
    }


    /*
    FirstLaunchManager Tests
     */

    @Test
    public void testIsFirstTimeLaunchTrue() {
        FirstLaunchManager manager = new FirstLaunchManager(context);
        manager.setFirstTimeLaunch(true);
        assertTrue(manager.isFirstTimeLaunch());
    }

    @Test
    public void testIsFirstTimeLaunchFalse() {
        FirstLaunchManager manager = new FirstLaunchManager(context);
        manager.setFirstTimeLaunch(false);
        assertFalse(manager.isFirstTimeLaunch());
    }

    // the following 2 tests are to reach branch coverage the FirstLauncherManager class
    @Test
    public void initFirstTimeLaunchFalse() {
        FirstLaunchManager manager = new FirstLaunchManager(context);
        manager.setFirstTimeLaunch(false);
        manager.initFirstTimeLaunch();
    }

    @Test
    public void initFirstTimeLaunchTrue() {
        FirstLaunchManager manager = new FirstLaunchManager(context);
        manager.setFirstTimeLaunch(true);
        manager.initFirstTimeLaunch();
    }

    /*
    Main Activity Tests
     */

    // the following three tests simply tests when the next or prev button are visible

    @Test
    public void testUpdateMovingButtonsBtnPrevInvisibleBtnNextVisible() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();

            activity.updateMovingButtons(0);
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.btn_prev).getVisibility());
            assertEquals(View.VISIBLE, activity.findViewById(R.id.btn_next).getVisibility());
        }
    }

    @Test
    public void testUpdateMovingButtonsBtnPrevVisibleBtnNextInvisible() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();

            activity.updateMovingButtons(3);
            assertEquals(View.VISIBLE, activity.findViewById(R.id.btn_prev).getVisibility());
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.btn_next).getVisibility());
        }

    }

    @Test
    public void testUpdateMovingButtonsBtnPrevVisibleBtnNextVisible() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();

            activity.updateMovingButtons(1);
            assertEquals(View.VISIBLE, activity.findViewById(R.id.btn_prev).getVisibility());
            assertEquals(View.VISIBLE, activity.findViewById(R.id.btn_next).getVisibility());
        }

    }



    // MainActivity.MyViewPagerAdapter tests

//    @Test
//    public void testInstantiateObject() {
//        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
//            controller.setup();
//            MainActivity activity = controller.get();
//
//            activity.myViewPagerAdapter.instantiateItem(new View(context), 0);
//            ImageView imageView = (ImageView) activity.findViewById(R.id.main_menu_img1);
//            assertEquals(activity.getResources().getDrawable(R.drawable.layout4x4_s), imageView.getDrawable());
//        }
//    }



    /*
    Stats Activity Tests
     */

    @Test // test it does not delete necessary files
    public void testResetGameStatisticsNotDeleteNonStatsFile() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            File file = new File(activity.getFilesDir(), "Statistics3.txt");
            file.createNewFile();
            activity.resetGameStatistics();
            assertTrue(file.exists());

        } catch (IOException e) {
//            throw new RuntimeException(e);
        }

    }

    @Test // test resetGameStatistics deletes both files
    public void testResetGameStatisticsDeleteStatsFile() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            File file = new File(activity.getFilesDir(), "Statistics4.txt");
            File file2 = new File(activity.getFilesDir(), "Statistics7.txt");
            file.createNewFile();
            file2.createNewFile();

            activity.resetGameStatistics();
            assertFalse(file.exists());
            assertFalse(file2.exists());

        } catch (IOException e) {
//            throw new RuntimeException(e);
        }

    }

    @Test // test nothing happens when you call on nonexistent file
    public void testResetGameStatisticsNonExistentStatsFile() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            activity.resetGameStatistics();
        }

    }

    // the next 6 tests just checks that no exception is thrown when the functions take the boundary values
    @Test
    public void testFormatSmallMillis0() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            assertEquals("0.00 s", activity.mSectionsPagerAdapter.formatSmallMillis(0L));
        }
    }

    @Test
    public void testFormatSmallMillisMaxLong() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            activity.mSectionsPagerAdapter.formatSmallMillis(Long.MAX_VALUE);
        }
    }

    @Test
    public void testFormatSmallMillisMinLong() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            activity.mSectionsPagerAdapter.formatSmallMillis(Long.MIN_VALUE);
        }
    }

    @Test
    public void testFormatMillis0() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            assertEquals("0.00 h", activity.mSectionsPagerAdapter.formatMillis(0L));
        }
    }

    @Test
    public void testFormatMillisMaxLong() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            activity.mSectionsPagerAdapter.formatMillis(Long.MAX_VALUE);
        }
    }

    @Test
    public void testFormatMillisMinLong() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            activity.mSectionsPagerAdapter.formatMillis(Long.MIN_VALUE);
        }
    }

    @Test // calling readStatisticsFromFile in Statistics.MyViewPagerAdapter when theres no file to load returns new GameStatistics object
    public void testReadStatsFromFilePagerNonexistentFile() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            GameStatistics gs1 = activity.mSectionsPagerAdapter.readStatisticsFromFile(2);
            GameStatistics gs2 = new GameStatistics(2);
            assertTrue(compareStatistics(gs1, gs2));
        }
    }

    @Test // calling readStatisticsFromFile in Statistics.MyViewPagerAdapter when theres is file to load returns that stats object
    public void testReadStatsFromFilePagerEmptyFile() {
        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();
            int n = 2;

            File file = new File(activity.getFilesDir(), "Statistics"+n+".txt");
            file.createNewFile();

            GameStatistics gs1 = activity.mSectionsPagerAdapter.readStatisticsFromFile(n);
            GameStatistics gs2 = new GameStatistics(n);
            assertTrue(compareStatistics(gs1, gs2));
        } catch (IOException e) {

        }
    }

    @Test // saving GameStatistics object to file and then calling readStatisticsFromFile in Statistics.MyViewPagerAdapter should return stats object
    public void testReadStatsFromFileValidObject() {
        int n = 2;

        GameStatistics gs1 = new GameStatistics(n);
        gs1.setHighestNumber(100);
        gs1.setRecord(500);

        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            File file = new File (activity.getFilesDir(), gs1.getFilename());
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gs1);
            out.close();
            fileOut.close();

            GameStatistics gs2 = activity.mSectionsPagerAdapter.readStatisticsFromFile(n);

            assertTrue(compareStatistics(gs1, gs2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test // saving null object to file and then readStatisticsFromFile in Statistics.MyViewPagerAdapter should not return null object
    public void testReadStatsFromFileFromFileNullObject() {
        int n = 2;

        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            GameStatistics gs1 = new GameStatistics(n);
            File file = new File (activity.getFilesDir(), "statistics"+n+".txt");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(null);
            out.close();
            fileOut.close();

            GameStatistics gs2 = activity.mSectionsPagerAdapter.readStatisticsFromFile(n);
            assertTrue(compareStatistics(gs1, gs2));
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

    @Test // saving GmeState object to file and then calling readStatisticsFromFile in Statistics.MyViewPagerAdapter should return stats object
    public void testReadStatsFromFileStatsActivityOnFileWithGameStateObject() {
        int n = 2;

        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            GameStatistics gs1 = new GameStatistics(n);
            File file = new File (activity.getFilesDir(), "statistics"+n+".txt");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new GameState(2));
            out.close();
            fileOut.close();

            GameStatistics gs2 = activity.mSectionsPagerAdapter.readStatisticsFromFile(n);
            assertTrue(compareStatistics(gs1, gs2));
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

    @Test // saving corrupted file and then calling readStatisticsFromFile in Statistics.MyViewPagerAdapter should return stats object
    public void testReadStatsFromFileStatsActivityOnCorruptedFile() {
        int n = 2;

        try (ActivityController<StatsActivity> controller = Robolectric.buildActivity(StatsActivity.class)) {
            controller.setup();
            StatsActivity activity = controller.get();

            GameStatistics gs1 = new GameStatistics(n);
            File file = new File (activity.getFilesDir(), "statistics"+n+".txt");
            file.createNewFile();

            String str = "jbnfij\rnokdm \nduji";
            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
            byte[] strToBytes = str.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();


            GameStatistics gs2 = activity.mSectionsPagerAdapter.readStatisticsFromFile(n);
            assertTrue(compareStatistics(gs1, gs2));
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

}