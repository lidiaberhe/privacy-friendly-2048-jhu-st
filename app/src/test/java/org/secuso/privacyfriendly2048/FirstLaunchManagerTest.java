package org.secuso.privacyfriendly2048;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.secuso.privacyfriendly2048.helpers.FirstLaunchManager;

/**
 * This class contains unit tests for the FirstLaunchManager class
 */
@RunWith(AndroidJUnit4.class)
public class FirstLaunchManagerTest {
    Context context = ApplicationProvider.getApplicationContext();

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
}
