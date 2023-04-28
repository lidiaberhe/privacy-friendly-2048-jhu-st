package org.secuso.privacyfriendly2048;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.secuso.privacyfriendly2048.activities.helper.Gestures;

@RunWith(AndroidJUnit4.class)
public class GesturesTest {

    Context context = ApplicationProvider.getApplicationContext();

    /*
   Gesture Tests
     */

    @Test // test the event is consumed
    public void testOnTouchReturnsTrue() {
        Gestures gestures = new Gestures(context);

        // MotionEvent parameters
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        int action = MotionEvent.ACTION_DOWN;
        int x = 0;
        int y = 1;
        int metaState = 0;

        // dispatch the event
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
        assertTrue(gestures.onTouch(new View(context), event));
    }

    @Test // assert the event is not consumed
    public void testOnTouchReturnsFalse() {
        Gestures gestures = new Gestures(context);

        // MotionEvent parameters
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        int action = MotionEvent.ACTION_CANCEL;
        int x = 0;
        int y = 1;
        int metaState = 0;

        // dispatch the event
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
        assertFalse(gestures.onTouch(new View(context), event));
    }

    @Test
    public void testOnSwipeRight() {
        Gestures gestures = new Gestures(context);
        assertFalse(gestures.onSwipeRight());
    }

    @Test
    public void testOnSwipeLeft() {
        Gestures gestures = new Gestures(context);
        assertFalse(gestures.onSwipeLeft());
    }

    @Test
    public void testNichts() {
        Gestures gestures = new Gestures(context);
        assertFalse(gestures.nichts());
    }

    @Test
    public void testOnSwipeTop() {
        Gestures gestures = new Gestures(context);
        assertFalse(gestures.onSwipeTop());
    }

    @Test
    public void testOnSwipeBottom() {
        Gestures gestures = new Gestures(context);
        assertFalse(gestures.onSwipeBottom());
    }
}
