package edu.brandeis.cosi103a.ip2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Main entry point and game manager for the Automation card game.
 * Handles card supply, player initialization, turn management, and game state.
 */
public class App {
    private static final int INITIAL_BITCOINS = 7;
    private static final int INITIAL_METHODS = 3;
    private static final int INITIAL_HAND_SIZE = 5;
    private static final int NUM_PLAYERS = 2;

    private List<Player> players;
    private Map<String, List<Card>> cardSupply;
    private int currentPlayerIndex;
    private Random random;

    /**
     * Constructs the App and initializes the game state.
     */
    public App() {
        this.players = new ArrayList<>();
        this.cardSupply = new HashMap<>();
        this.random = new Random();
        initializeGame();
    }

    /**
     * Initializes the entire game: creates supply, players, and deals initial
     * hands.
     */
    private void initializeGame() {
        createCardSupply();
        createPlayers();
        dealInitialHands();
        selectStartingPlayer();
    }

    /**
     * Creates the common card supply with all card types and quantities.
     */
    private void createCardSupply() {
        // Automation cards
        List<Card> methods = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            methods.add(new AutomationCard(2, 1)); // Method: cost 2, value 1
        }
        cardSupply.put("Method", methods);

        List<Card> modules = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            modules.add(new AutomationCard(5, 3)); // Module: cost 5, value 3
        }
        cardSupply.put("Module", modules);

        List<Card> frameworks = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            frameworks.add(new AutomationCard(8, 6)); // Framework: cost 8, value 6
        }
        cardSupply.put("Framework", frameworks);

        // Cryptocurrency cards
        List<Card> bitcoins = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            bitcoins.add(new CryptocurrencyCard(0, 1)); // Bitcoin: cost 0, value 1
        }
        cardSupply.put("Bitcoin", bitcoins);

        List<Card> ethereums = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            ethereums.add(new CryptocurrencyCard(3, 2)); // Ethereum: cost 3, value 2
        }
        cardSupply.put("Ethereum", ethereums);

        List<Card> dogecoins = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dogecoins.add(new CryptocurrencyCard(6, 3)); // Dogecoin: cost 6, value 3
        }
        cardSupply.put("Dogecoin", dogecoins);
    }

    /**
     * Creates the two players with initial resources.
     * Each player starts with 7 Bitcoins and 3 Methods.
     */
    private void createPlayers() {
        for (int i = 1; i <= NUM_PLAYERS; i++) {
            Player player = new Player("Player " + i, 0);

            // Add 7 Bitcoins to draw pile
            for (int j = 0; j < INITIAL_BITCOINS; j++) {
                player.addCardToDrawPile(cardSupply.get("Bitcoin").remove(0));
            }

            // Add 3 Methods to draw pile
            for (int j = 0; j < INITIAL_METHODS; j++) {
                player.addCardToDrawPile(cardSupply.get("Method").remove(0));
            }

            players.add(player);
        }
    }

    /**
     * Deals initial hands to each player.
     * Each player shuffles their starter deck and deals 5 face down cards as their
     * initial hand.
     */
    private void dealInitialHands() {
        for (Player player : players) {
            // Shuffle the draw pile
            Collections.shuffle(player.getDrawPile());

            // Deal 5 cards from draw pile to hand
            for (int i = 0; i < INITIAL_HAND_SIZE && player.getDrawPileSize() > 0; i++) {
                Card card = player.getDrawPile().remove(0);
                player.addCardToHand(card);
            }

            System.out.println(player.getName() + " dealt initial hand of " + player.getHandSize() + " cards");

        }

    }

    /**
     * Randomly selects the starting player.
     */
    private void selectStartingPlayer() {
        currentPlayerIndex = random.nextInt(NUM_PLAYERS);
        System.out.println(players.get(currentPlayerIndex).getName() + " goes first!");
    }

    /**
     * Gets the current player.
     *
     * @return the player whose turn it is
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Gets the other player (opponent).
     *
     * @return the player who is not currently taking a turn
     */
    public Player getOpponentPlayer() {
        int opponentIndex = (currentPlayerIndex + 1) % NUM_PLAYERS;
        return players.get(opponentIndex);
    }

    /**
     * Gets all players in the game.
     *
     * @return list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the card supply.
     *
     * @return map of card types to available cards
     */
    public Map<String, List<Card>> getCardSupply() {
        return cardSupply;
    }

    /**
     * Ends the current player's turn and switches to the next player.
     */
    public void endTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % NUM_PLAYERS;
    }

    /**
     * Prints the current game state for debugging.
     */
    public void printGameState() {
        System.out.println("\n=== GAME STATE ===");
        System.out.println("Current Turn: " + getCurrentPlayer().getName());
        System.out.println("Card Supply Remaining:");
        cardSupply.forEach((type, cards) -> System.out.println("  " + type + ": " + cards.size()));
        System.out.println("==================\n");
    }

    /**
     * Executes the buy phase for the current player.
     * The player plays cryptocurrency cards from their hand and can purchase up to
     * 1 card
     * using the total value of played cryptocoins.
     *
     * @return true if a card was purchased, false otherwise
     */
    public boolean executeBuyPhase() {
        Player currentPlayer = getCurrentPlayer();
        System.out.println("\n--- " + currentPlayer.getName() + " Buy Phase ---");

        // Play all cryptocurrency cards for maximum buying power
        List<Card> hand = new ArrayList<>(currentPlayer.getHand());
        List<Card> playedCards = new ArrayList<>();
        for (Card card : hand) {
            if (card instanceof CryptocurrencyCard) {
                if (currentPlayer.playCardForBuying(card)) {
                    playedCards.add(card);
                }
            }
        }
        if (!playedCards.isEmpty()) {
            System.out.println("Played Cards: " + playedCards + "\n");
        }

        int buyingPower = currentPlayer.calculatePlayedCardsValue();
        System.out.println("Buying power: " + buyingPower + " cryptocoins" + "\n");

        // Find the best affordable Automation card (prioritize 10+ AP cards)
        AutomationCard targetAutomationCard = findBestAutomationCardToBuy(buyingPower);

        if (targetAutomationCard != null && targetAutomationCard.getCost() <= buyingPower) {
            // Buy the target automation card
            removeFromSupply(targetAutomationCard);
            currentPlayer.purchaseCard(targetAutomationCard);
            System.out.println("Purchased: " + targetAutomationCard);
            return true;
        }

        // If no good automation card found, buy a random cryptocurrency card
        return purchaseRandomCryptocurrencyCard(currentPlayer, buyingPower);
    }

    /**
     * Finds the best Automation card to buy given buying power.
     * Prioritizes Framework cards worth 6 APs, then Module cards worth 3 APs, then
     * Method cards worth 1 AP.
     *
     * @param buyingPower the amount of cryptocoins available
     * @return the best affordable automation card, or null if none affordable
     */
    private AutomationCard findBestAutomationCardToBuy(int buyingPower) {
        // Check in order of value (Framework > Module > Method)
        String[] priorityOrder = { "Framework", "Module", "Method" };

        for (String cardType : priorityOrder) {
            List<Card> cards = cardSupply.get(cardType);
            if (cards != null && !cards.isEmpty()) {
                Card card = cards.get(0);
                if (card instanceof AutomationCard && card.getCost() <= buyingPower) {
                    return (AutomationCard) card;
                }
            }
        }

        return null;
    }

    /**
     * Removes a specific card from the supply.
     *
     * @param cardToRemove the card to remove
     */
    private void removeFromSupply(AutomationCard cardToRemove) {
        for (String type : new String[] { "Framework", "Module", "Method" }) {
            List<Card> cards = cardSupply.get(type);
            if (cards != null && !cards.isEmpty() && cards.get(0) == cardToRemove) {
                cards.remove(0);
                return;
            }
        }
    }

    /**
     * Purchases a random cryptocurrency card, preferring non-Bitcoin cards.
     *
     * @param player      the current player
     * @param buyingPower the total value of played cryptocurrency cards
     * @return true if a card was purchased, false otherwise
     */
    private boolean purchaseRandomCryptocurrencyCard(Player player, int buyingPower) {
        List<String> cryptocurrencyTypes = new ArrayList<>();
        List<String> allCryptoTypes = new ArrayList<>();

        // Categorize cryptocurrency cards
        String[] cryptoOrder = { "Dogecoin", "Ethereum", "Bitcoin" };
        for (String type : cryptoOrder) {
            List<Card> cards = cardSupply.get(type);
            if (cards != null && !cards.isEmpty()) {
                Card card = cards.get(0);
                if (card.getCost() <= buyingPower) {
                    allCryptoTypes.add(type);
                    // Don't add Bitcoin to the preferred list (it has lower value)
                    if (!type.equals("Bitcoin")) {
                        cryptocurrencyTypes.add(type);
                    }
                }
            }
        }

        // Prefer non-Bitcoin cards
        String chosenType = null;
        if (!cryptocurrencyTypes.isEmpty()) {
            chosenType = cryptocurrencyTypes.get(random.nextInt(cryptocurrencyTypes.size()));
        } else if (!allCryptoTypes.isEmpty()) {
            chosenType = allCryptoTypes.get(random.nextInt(allCryptoTypes.size()));
        }

        if (chosenType != null) {
            Card purchasedCard = cardSupply.get(chosenType).remove(0);
            player.purchaseCard(purchasedCard);
            System.out.println("Purchased: " + purchasedCard);
            return true;
        }

        System.out.println("No affordable cryptocurrency cards to purchase.");
        return false;
    }

    /**
     * Executes the cleanup phase for the current player.
     * Discards hand and played cards, then deals a new hand.
     */
    public void executeCleanupPhase() {
        Player currentPlayer = getCurrentPlayer();
        System.out.println("\n--- " + currentPlayer.getName() + " Cleanup Phase ---");

        int previousHandSize = currentPlayer.getHandSize();
        currentPlayer.cleanupPhase();
        currentPlayer.resetCardSelections();

        System.out.println("Discarded " + previousHandSize + " cards from hand");
        System.out.println("Dealt new hand of " + currentPlayer.getHandSize() + " cards");
    }

    /**
     * Executes a complete turn for the current player (buy phase + cleanup phase).
     */
    public void executeTurn() {
        System.out.println("\n========== " + getCurrentPlayer().getName() + " TURN ==========");
        executeBuyPhase();
        executeCleanupPhase();
        System.out.println("========== TURN COMPLETE ==========");
    }

    public static void main(String[] args) {
        System.out.println("=== Automation Card Game ===\n");

        // INITIAL PHASE: Create and initialize game
        App game = new App();
        game.printGameState();

        // SECOND PHASE: Game loop - continue until all Framework cards are purchased
        int turn = 0;
        while (!game.cardSupply.get("Framework").isEmpty()) {
            turn++;
            System.out.println("\n========== TURN " + turn + " ==========");
            game.executeTurn();
            game.endTurn();
            game.printGameState();
        }

        // THIRD PHASE: Game over - determine winner
        System.out.println("\n\n========== GAME OVER ==========");

        // Calculate final APs for each player from all their Automation cards
        List<Player> players = game.getPlayers();
        Player winner = null;
        int maxAP = -1;

        for (Player player : players) {
            int totalAP = 0;

            // Count APs from Automation cards in hand
            for (Card card : player.getHand()) {
                if (card instanceof AutomationCard) {
                    totalAP += card.getValue();
                }
            }

            // Count APs from Automation cards in draw pile
            for (Card card : player.getDrawPile()) {
                if (card instanceof AutomationCard) {
                    totalAP += card.getValue();
                }
            }

            // Count APs from Automation cards in discard pile
            for (Card card : player.getDiscardPile()) {
                if (card instanceof AutomationCard) {
                    totalAP += card.getValue();
                }
            }

            System.out.println(player.getName() + " Final Score: " + totalAP + " APs");

            if (totalAP > maxAP) {
                maxAP = totalAP;
                winner = player;
            }
        }

        System.out.println("\n*** " + winner.getName() + " WINS with " + maxAP + " APs! ***");
    }
}
