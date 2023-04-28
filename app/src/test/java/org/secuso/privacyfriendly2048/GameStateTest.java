package org.secuso.privacyfriendly2048;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.os.SystemClock;
import android.view.ActionProvider;
import android.view.MotionEvent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.secuso.privacyfriendly2048.activities.Element;
import org.secuso.privacyfriendly2048.activities.helper.GameState;
import org.secuso.privacyfriendly2048.activities.helper.Gestures;

/**
 * This class contains unit tests for the GameState object
 */
@RunWith(AndroidJUnit4.class)
public class GameStateTest {

    Context context = ApplicationProvider.getApplicationContext();

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

    /*
    the following tests are to test the GameState constructor. Note that this
    constructor has no  documentation and so there is no information on how the states
    of the inputs should be or what behavior to expect
     */

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

    @Test // reach branch coverage in second loop
    public void testGameStateFistElementArrEmpty() {
        Element elm = new Element(context); elm.setNumber(2);
        GameState gameState = new GameState(
                new Element[][]{}, new Element[][]{new Element[]{elm}});
        assertArrayEquals(new int[]{}, gameState.numbers);
        assertArrayEquals(new int[]{2}, gameState.last_numbers);
        assertEquals(0, gameState.n);
    }

    @Test // reach branch coverage in second loop
    public void testGameState2FistElementArrEmpty() {
        Element elm = new Element(context); elm.setNumber(2);
        GameState gameState = new GameState(
                new Element[][]{}, new Element[][]{new Element[]{elm, elm}});
//        assertArrayEquals(new int[]{}, gameState.numbers);
//        assertArrayEquals(new int[]{2}, +gameState.last_numbers);
        assertEquals(0, gameState.n);
    }

}
