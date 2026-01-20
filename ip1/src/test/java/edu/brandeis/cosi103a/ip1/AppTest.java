package edu.brandeis.cosi103a.ip1;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Dice Rolling Game App.
 */
public class AppTest {

    @Before
    public void setUp() {
        // Reset scores before each test
        App.resetScores();
    }

    /**
     * Test that rollDie returns a value between 1 and 6.
     */
    @Test
    public void testRollDie() {
        App.setRandom(new Random(42));
        int roll = App.rollDie();
        assertTrue("Roll should be between 1 and 6", roll >= 1 && roll <= 6);
    }

    /**
     * Test that getPlayer1Score returns the correct initial score.
     */
    @Test
    public void testGetPlayer1Score() {
        int score = App.getPlayer1Score();
        assertEquals("Initial score should be 0", 0, score);
    }

    /**
     * Test that getPlayer2Score returns the correct initial score.
     */
    @Test
    public void testGetPlayer2Score() {
        int score = App.getPlayer2Score();
        assertEquals("Initial score should be 0", 0, score);
    }

    /**
     * Test that resetScores resets both players' scores to 0.
     */
    @Test
    public void testResetScores() {
        // Simulate some scores being added (by rolling)
        App.setRandom(new Random(42));
        int roll1 = App.rollDie();
        int roll2 = App.rollDie();

        // Reset the scores
        App.resetScores();

        // Verify both scores are 0
        assertEquals("Player 1 score should be 0 after reset", 0, App.getPlayer1Score());
        assertEquals("Player 2 score should be 0 after reset", 0, App.getPlayer2Score());
    }

    /**
     * Test that multiple die rolls produce valid values.
     */
    @Test
    public void testMultipleDieRolls() {
        App.setRandom(new Random(12345));
        for (int i = 0; i < 20; i++) {
            int roll = App.rollDie();
            assertTrue("Roll " + i + " should be between 1 and 6", roll >= 1 && roll <= 6);
        }
    }

    /**
     * Test that playTurn for Player 1 with no re-rolls adds the roll to Player 1's score.
     */
    @Test
    public void testPlayTurnPlayer1NoRerolls() {
        // Simulate user input: "no" to re-roll
        String input = "no\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        App.setScanner(testScanner);
        App.setRandom(new Random(100)); // Fixed seed for predictable roll
        
        int initialScore = App.getPlayer1Score();
        App.playTurn("Player 1");
        
        // Verify score increased (die was rolled and added)
        assertTrue("Player 1 score should increase after turn", App.getPlayer1Score() > initialScore);
    }

    /**
     * Test that playTurn for Player 2 with no re-rolls adds the roll to Player 2's score.
     */
    @Test
    public void testPlayTurnPlayer2NoRerolls() {
        // Simulate user input: "no" to re-roll
        String input = "no\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        App.setScanner(testScanner);
        App.setRandom(new Random(200)); // Fixed seed for predictable roll
        
        int initialScore = App.getPlayer2Score();
        App.playTurn("Player 2");
        
        // Verify score increased (die was rolled and added)
        assertTrue("Player 2 score should increase after turn", App.getPlayer2Score() > initialScore);
    }

    /**
     * Test that playTurn allows one re-roll when user enters "yes".
     */
    @Test
    public void testPlayTurnWithOneReroll() {
        // Simulate user input: "yes" for first re-roll, "no" to stop
        String input = "yes\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        App.setScanner(testScanner);
        App.setRandom(new Random(300));

        int initialScore = App.getPlayer1Score();
        App.playTurn("Player 1");

        // Verify score increased after one re-roll
        assertTrue("Player 1 score should increase with one re-roll", App.getPlayer1Score() > initialScore);
    }
    
    /**
     * Test that playTurn adds a valid die roll value to the player's score.
     */
    @Test
    public void testPlayTurnAddsValidDieValue() {
        // Simulate user input: "no" to re-roll
        String input = "no\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        App.setScanner(testScanner);

        // Use fixed random to know the roll value
        Random fixedRandom = new Random(42);
        App.setRandom(fixedRandom);

        int scoreBefore = App.getPlayer1Score();
        int expectedRoll = fixedRandom.nextInt(6) + 1; // Simulate what rollDie does

        App.setRandom(new Random(42)); // Reset to get the same roll
        App.playTurn("Player 1");

        int scoreAfter = App.getPlayer1Score();
        int addedScore = scoreAfter - scoreBefore;

        // Verify the added score is between 1 and 6
        assertTrue("Added score should be between 1 and 6", addedScore >= 1 && addedScore <= 6);
    }
    
    /**
     * Test that playTurn correctly identifies and updates Player 1's score.
     */
    @Test
    public void testPlayTurnUpdatesCorrectPlayer1Score() {
        String input = "no\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        App.setScanner(testScanner);
        App.setRandom(new Random(800));
        
        int player1Before = App.getPlayer1Score();
        int player2Before = App.getPlayer2Score();
        
        App.playTurn("Player 1");
        
        // Player 1 should increase, Player 2 should stay the same
        assertTrue("Player 1 score should increase", App.getPlayer1Score() > player1Before);
        assertEquals("Player 2 score should not change", player2Before, App.getPlayer2Score());
    }

    /**
     * Test that playTurn correctly identifies and updates Player 2's score.
     */
    @Test
    public void testPlayTurnUpdatesCorrectPlayer2Score() {
        String input = "no\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        App.setScanner(testScanner);
        App.setRandom(new Random(900));

        int player1Before = App.getPlayer1Score();
        int player2Before = App.getPlayer2Score();

        App.playTurn("Player 2");

        // Player 2 should increase, Player 1 should stay the same
        assertTrue("Player 2 score should increase", App.getPlayer2Score() > player2Before);
        assertEquals("Player 1 score should not change", player1Before, App.getPlayer1Score());
    }
    
    /**
     * Test that playTurn handles invalid input and continues.
     */
    @Test
    public void testPlayTurnWithInvalidInput() {
        // Simulate user input: invalid input, then "no"
        String input = "maybe\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        App.setScanner(testScanner);
        App.setRandom(new Random(700));
        
        int initialScore = App.getPlayer1Score();
        App.playTurn("Player 1");
        
        // Verify score increased (turn should have completed)
        assertTrue("Player 1 score should increase despite invalid input", App.getPlayer1Score() > initialScore);
    }
}
