package org.secuso.privacyfriendly2048;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.secuso.privacyfriendly2048.activities.helper.GameStatistics;


/**
 * This class contains unit tests for the GameStatistics object
 */
public class GameStatisticsTest {
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

    @Test // test resetTimePlayed actually resets it
    public void testResetTimePlayed() {
        stats.addTimePlayed(25L);
        assertEquals(25L, stats.getTimePlayed());
        stats.resetTimePlayed();
        assertEquals(0L, stats.getTimePlayed());
    }

}
