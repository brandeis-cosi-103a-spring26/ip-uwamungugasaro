package edu.brandeis.cosi103a.ip1;

import java.util.Scanner;
import java.util.Random;

/**
 * A 2-player command-line dice rolling game.
 * Each player gets up to 10 turns to roll a die.
 * On each turn, a player rolls a 6-sided die and can re-roll up to 2 times.
 * The final die value is added to their score.
 * The player with the highest score wins.
 */
public class App {
    private static final int MAX_TURNS = 10;
    private static final int MAX_REROLLS = 2;
    private static final int DIE_SIDES = 6;

    private static int player1Score = 0;
    private static int player2Score = 0;
    private static Scanner scanner;
    private static Random random;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        random = new Random();

        System.out.println("========================================");
        System.out.println("    Welcome to the Dice Rolling Game!    ");
        System.out.println("========================================");
        System.out.println("Players: Player 1 vs Player 2");
        System.out.println("Max Turns: " + MAX_TURNS);
        System.out.println("Max Re-rolls per turn: " + MAX_REROLLS);
        System.out.println("========================================\n");

        // Play the game for up to 10 turns per player
        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            System.out.println("--- TURN " + turn + " ---");

            // Player 1's turn
            playTurn("Player 1");

            // Player 2's turn
            playTurn("Player 2");

            System.out.println("\nCurrent Scores: Player 1: " + player1Score + " | Player 2: " + player2Score + "\n");
        }

        // Determine the winner
        System.out.println("========================================");
        System.out.println("              GAME OVER!                 ");
        System.out.println("========================================");
        System.out.println("Final Scores:");
        System.out.println("Player 1: " + player1Score);
        System.out.println("Player 2: " + player2Score);
        System.out.println("========================================");

        if (player1Score > player2Score) {
            System.out.println("🎉 Player 1 WINS! 🎉");
        } else if (player2Score > player1Score) {
            System.out.println("🎉 Player 2 WINS! 🎉");
        } else {
            System.out.println("It's a TIE!");
        }

        scanner.close();
    }

    /**
     * Simulates one player's turn.
     * The player rolls a die and can choose to re-roll up to 2 times.
     * 
     * @param playerName the name of the player taking the turn
     */
    public static void playTurn(String playerName) {
        System.out.println("\n" + playerName + "'s Turn:");

        int currentRoll = rollDie();
        System.out.println("You rolled: " + currentRoll);

        int rerollsUsed = 0;

        // Allow up to 2 re-rolls
        while (rerollsUsed < MAX_REROLLS) {
            System.out.print("Do you want to re-roll? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes") || response.equals("y")) {
                rerollsUsed++;
                currentRoll = rollDie();
                System.out.println("You rolled: " + currentRoll);
            } else if (response.equals("no") || response.equals("n")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        if (rerollsUsed == MAX_REROLLS) {
            System.out.println("You've used all your re-rolls. Your final score for this turn: " + currentRoll);
        } else {
            System.out.println("Your final score for this turn: " + currentRoll);
        }

        // Add the score to the player's total
        if (playerName.equals("Player 1")) {
            player1Score += currentRoll;
        } else {
            player2Score += currentRoll;
        }
    }

    /**
     * Set scanner
     * 
     * @param newScanner to use
     */
    public static void setScanner(Scanner newScanner) {
        scanner = newScanner;
    }

    /**
     * Rolls a 6-sided die.
     * 
     * @return a random number between 1 and 6
     */
    public static int rollDie() {
        return random.nextInt(DIE_SIDES) + 1;
    }

    /**
     * Gets player 1's current score.
     * 
     * @return player 1's score
     */
    public static int getPlayer1Score() {
        return player1Score;
    }

    /**
     * Gets player 2's current score.
     * 
     * @return player 2's score
     */
    public static int getPlayer2Score() {
        return player2Score;
    }

    /**
     * Resets both player scores to 0.
     */
    public static void resetScores() {
        player1Score = 0;
        player2Score = 0;
    }

    /**
     * Sets the random number generator for testing purposes.
     * 
     * @param rand the Random object to use
     */
    public static void setRandom(Random rand) {
        random = rand;
    }
}
