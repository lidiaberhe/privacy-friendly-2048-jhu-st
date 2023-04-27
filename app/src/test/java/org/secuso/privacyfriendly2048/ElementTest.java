package org.secuso.privacyfriendly2048;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.secuso.privacyfriendly2048.activities.Element;


/**
 * This class contains unit tests for the Element class
 */
@RunWith(AndroidJUnit4.class)
public class ElementTest {
    // compares 2 Element objects
    private boolean compareElements(Element elm1, Element elm2) {
        return elm1.getNumber() == elm2.getNumber() &&
                elm1.getdPosX() == elm2.getdPosX() &&
                elm1.getdPosY() == elm2.getdPosY() &&
                elm1.getdNumber() == elm2.getdNumber() &&
                elm1.getPosY() == elm2.getPosY() &&
                elm1.getPosX() == elm2.getPosX();
    }

    // helps to minimize repeated code in Elements tests
    private void prefColorWhite(Element elm) {
        elm.drawItem();
        assertEquals(View.VISIBLE, elm.getVisibility());
        assertEquals("" + elm.getNumber(), elm.getText());
        assertEquals(ContextCompat.getColor(context,R.color.white), elm.getCurrentTextColor());
    }

    // helps to minimize repeated code in Elements tests
    private void prefColorBlack(Element elm) {
        elm.drawItem();
        assertEquals(View.VISIBLE, elm.getVisibility());
        assertEquals("" + elm.getNumber(), elm.getText());
        assertEquals(ContextCompat.getColor(context,R.color.black), elm.getCurrentTextColor());
    }

    // helps to minimize repeated code in Elements test
    // changes preference from 1 to 2 to help reach branch coverage
    private void setDefaultPreferenceTo2() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("pref_color", "2"); // change preference value
        editor.commit();
    }

    Context context = ApplicationProvider.getApplicationContext();

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

    /*
    the following tests are to reach branch coverage in Element class
     */

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
        prefColorBlack(elm);
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
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber16() {
        Element elm = new Element(context);
        elm.setNumber(16);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber32() {
        Element elm = new Element(context);
        elm.setNumber(32);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber64() {
        Element elm = new Element(context);
        elm.setNumber(64);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber128() {
        Element elm = new Element(context);
        elm.setNumber(128);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber256() {
        Element elm = new Element(context);
        elm.setNumber(256);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber512() {
        Element elm = new Element(context);
        elm.setNumber(512);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber1024() {
        Element elm = new Element(context);
        elm.setNumber(1024);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber2048() {
        Element elm = new Element(context);
        elm.setNumber(2048);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemNumber4096() {
        Element elm = new Element(context);
        elm.setNumber(4096);
        prefColorBlack(elm);
    }

    @Test
    public void testElementDrawItemNumber8192() {
        Element elm = new Element(context);
        elm.setNumber(8192);
        prefColorBlack(elm);
    }

    @Test
    public void testElementDrawItemNumber16384() {
        Element elm = new Element(context);
        elm.setNumber(16384);
        float size = elm.getTextSize();
        prefColorWhite(elm);
        assertTrue(size != elm.getTextSize());
    }

    @Test
    public void testElementDrawItemNumber32768() {
        Element elm = new Element(context);
        elm.setNumber(32768);
        float size = elm.getTextSize();
        prefColorWhite(elm);
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
        prefColorBlack(elm);
    }

    @Test
    public void testElementDrawItemPref2Number8() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(8);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number16() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(16);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number32() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(32);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number64() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(64);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number128() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(128);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number256() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(256);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number512() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(512);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number1024() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(1024);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number2048() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(2048);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number4096() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(4096);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItemPref2Number8192() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(8192);
        prefColorWhite(elm);
    }

    @Test
    public void testElementDrawItePref2mNumber16384() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(16384);
        float size = elm.getTextSize();
        prefColorWhite(elm);
        assertTrue(size != elm.getTextSize());
    }

    @Test
    public void testElementDrawItemPref2Number32768() {
        setDefaultPreferenceTo2();

        Element elm = new Element(context);
        elm.setNumber(32768);
        float size = elm.getTextSize();
        prefColorWhite(elm);
        assertTrue(size != elm.getTextSize());

    }


}

