package edu.brandeis.cosi103a.ip2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents a player in the Automation card game.
 * Each player has a name, AP (action points/victory points),
 * and collections of cards in a draw pile and discard pile.
 * The draw pile contains both starting hand and purchased cards.
 * The discard pile starts empty and accumulates cards as they are played.
 */
public class Player {
    private String name;
    private int ap;
    private List<Card> hand;
    private List<Card> drawPile;
    private List<Card> discardPile;
    private List<Card> playedCards;
    private Card selectedCardToBuy;
    private Card selectedCardToPlay;
    private Random random;

    /**
     * Constructs a Player with a name and initial AP.
     *
     * @param name      the player's name
     * @param initialAP the starting number of AP (action points)
     */
    public Player(String name, int initialAP) {
        this.name = name;
        this.ap = initialAP;
        this.hand = new ArrayList<>();
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.selectedCardToBuy = null;
        this.selectedCardToPlay = null;
        this.random = new Random();
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name.
     *
     * @param name the player's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the player's current AP (action points/victory points).
     *
     * @return the number of AP the player has
     */
    public int getAP() {
        return ap;
    }

    /**
     * Sets the player's AP.
     *
     * @param ap the number of AP the player has
     */
    public void setAP(int ap) {
        this.ap = ap;
    }

    /**
     * Adds AP to the player's total.
     *
     * @param amount the amount of AP to add
     */
    public void addAP(int amount) {
        this.ap += amount;
    }

    /**
     * Removes AP from the player's total.
     *
     * @param amount the amount of AP to remove
     */
    public void removeAP(int amount) {
        this.ap -= amount;
    }

    /**
     * Gets the player's draw pile.
     *
     * @return the list of cards in the draw pile
     */
    public List<Card> getDrawPile() {
        return drawPile;
    }

    /**
     * Adds a card to the player's draw pile.
     *
     * @param card the card to add
     */
    public void addCardToDrawPile(Card card) {
        drawPile.add(card);
    }

    /**
     * Removes a card from the player's draw pile.
     *
     * @param card the card to remove
     * @return true if the card was removed, false otherwise
     */
    public boolean removeCardFromDrawPile(Card card) {
        return drawPile.remove(card);
    }

    /**
     * Gets the player's discard pile.
     *
     * @return the list of cards in the discard pile
     */
    public List<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     * Adds a card to the player's discard pile.
     *
     * @param card the card to add to the discard pile
     */
    public void addCardToDiscardPile(Card card) {
        discardPile.add(card);
    }

    /**
     * Removes a card from the player's discard pile.
     *
     * @param card the card to remove
     * @return true if the card was removed, false otherwise
     */
    public boolean removeCardFromDiscardPile(Card card) {
        return discardPile.remove(card);
    }

    /**
     * Gets the number of cards in the player's draw pile.
     *
     * @return the size of the draw pile
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }

    /**
     * Gets the number of cards in the player's discard pile.
     *
     * @return the size of the discard pile
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }

    /**
     * Gets the player's hand.
     *
     * @return the list of cards in the player's hand
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add to the hand
     */
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param card the card to remove
     * @return true if the card was removed, false otherwise
     */
    public boolean removeCardFromHand(Card card) {
        return hand.remove(card);
    }

    /**
     * Gets the number of cards in the player's hand.
     *
     * @return the size of the hand
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Plays a card from the hand to the discard pile.
     * Cards played during a turn go to the discard pile and can be reshuffled back
     * into the draw pile for reuse.
     *
     * @param card the card to play
     * @return true if the card was successfully played, false if not in hand
     */
    public boolean playCard(Card card) {
        if (removeCardFromHand(card)) {
            addCardToDiscardPile(card);
            return true;
        }
        return false;
    }

    /**
     * Draws a card from the draw pile to the hand.
     * If the draw pile is empty, reshuffles the discard pile back into the draw
     * pile first.
     *
     * @return the card drawn, or null if both draw and discard piles are empty
     */
    public Card drawCard() {
        // If draw pile is empty, reshuffle discard pile
        if (drawPile.isEmpty()) {
            reshuffleDiscardIntoDraw();
        }

        // Draw from draw pile if available
        if (!drawPile.isEmpty()) {
            Card card = drawPile.remove(0);
            addCardToHand(card);
            return card;
        }

        return null;
    }

    /**
     * Plays a cryptocurrency card during the buy phase.
     * The card is moved from hand to played cards list for calculating buying
     * power.
     *
     * @param card the cryptocurrency card to play
     * @return true if successfully played, false if not in hand
     */
    public boolean playCardForBuying(Card card) {
        if (removeCardFromHand(card)) {
            playedCards.add(card);
            return true;
        }
        return false;
    }

    /**
     * Calculates the total value of all played cryptocurrency cards.
     * This value represents the player's buying power during the buy phase.
     *
     * @return the sum of values of all played cards
     */
    public int calculatePlayedCardsValue() {
        int totalValue = 0;
        for (Card card : playedCards) {
            totalValue += card.getValue();
        }
        return totalValue;
    }

    /**
     * Purchases a card and adds it directly to the discard pile.
     * This is called during the buy phase.
     *
     * @param card the card to purchase
     */
    public void purchaseCard(Card card) {
        addCardToDiscardPile(card);
    }

    /**
     * Executes the cleanup phase of a turn:
     * 1. Moves all cards from hand to discard pile
     * 2. Moves all played cards to discard pile
     * 3. Deals a new hand (up to 5 cards) from draw pile with automatic reshuffle
     */
    public void cleanupPhase() {
        // Add hand to discard pile
        discardPile.addAll(hand);
        hand.clear();

        // Add played cards to discard pile
        discardPile.addAll(playedCards);
        playedCards.clear();

        // Deal new hand (up to 5 cards)
        dealNewHand(5);
    }

    /**
     * Deals a new hand from the draw pile.
     * If the draw pile is empty, reshuffles the discard pile first.
     * Continues dealing until hand reaches target size or no cards are available.
     *
     * @param targetHandSize the desired size of the new hand
     */
    private void dealNewHand(int targetHandSize) {
        while (hand.size() < targetHandSize) {
            // If draw pile is empty, reshuffle discard pile
            if (drawPile.isEmpty()) {
                reshuffleDiscardIntoDraw();
            }

            // If still no cards available, stop dealing
            if (drawPile.isEmpty()) {
                break;
            }

            // Draw a card
            Card card = drawPile.remove(0);
            hand.add(card);
        }
    }

    /**
     * Reshuffles all cards from the discard pile back into the draw pile.
     * This is called when the draw pile runs out, allowing cards to be played
     * multiple times.
     */
    private void reshuffleDiscardIntoDraw() {
        if (!discardPile.isEmpty()) {
            Collections.shuffle(discardPile);
            drawPile.addAll(discardPile);
            discardPile.clear();
        }
    }

    /**
     * Determines the best card to play and buy based on game strategy.
     * Strategy: Play cryptocoins that will get us an Automation Card worth at least
     * 10 APs.
     * If no such card is affordable, select a random Cryptocurrency card,
     * preferring non-Bitcoin cards.
     *
     * @param availableAutomationCards map of available automation cards by type
     * @return true if a card was selected, false if no suitable cards available
     */
    public boolean selectCardsForBuying(java.util.Map<String, java.util.List<Card>> availableAutomationCards) {
        // First, try to find an automation card that will give us at least 10 APs
        selectedCardToBuy = null;
        selectedCardToPlay = null;

        // Check each crypto card in hand for what automation card it could afford
        for (Card card : hand) {
            if (card instanceof CryptocurrencyCard) {
                // Check if this card alone could buy an automation card worth 10+ APs
                AutomationCard bestCard = findBestAffordableAutomationCard(card.getValue(), availableAutomationCards);
                if (bestCard != null && bestCard.getValue() >= 3) {
                    selectedCardToPlay = card;
                    selectedCardToBuy = bestCard;
                    return true;
                }
            }
        }

        // If no single card can get us 10+ AP automation card, pick a random
        // cryptocurrency card
        // Prefer non-Bitcoin cards
        List<Card> cryptocurrencyCards = new ArrayList<>();
        List<Card> bitcoinCards = new ArrayList<>();

        for (Card card : hand) {
            if (card instanceof CryptocurrencyCard) {
                // Check if it's a Bitcoin (cost 0, value 1)
                if (card.getCost() == 0 && card.getValue() == 1) {
                    bitcoinCards.add(card);
                } else {
                    cryptocurrencyCards.add(card);
                }
            }
        }

        // Prefer non-Bitcoin cards
        if (!cryptocurrencyCards.isEmpty()) {
            selectedCardToPlay = cryptocurrencyCards.get(random.nextInt(cryptocurrencyCards.size()));
            selectedCardToBuy = null; // Will buy random crypto card later
            return true;
        } else if (!bitcoinCards.isEmpty()) {
            selectedCardToPlay = bitcoinCards.get(random.nextInt(bitcoinCards.size()));
            selectedCardToBuy = null;
            return true;
        }

        return false;
    }

    /**
     * Finds the best (highest value) automation card that can be afforded with the
     * given amount.
     *
     * @param buyingPower              the amount of cryptocoins available
     * @param availableAutomationCards map of available automation cards by type
     * @return the best affordable automation card, or null if none are affordable
     */
    private AutomationCard findBestAffordableAutomationCard(int buyingPower,
            java.util.Map<String, java.util.List<Card>> availableAutomationCards) {
        AutomationCard bestCard = null;
        int bestValue = 0;

        // Check Framework, Module, Method in that order (highest value first)
        String[] priorityOrder = { "Framework", "Module", "Method" };
        for (String cardType : priorityOrder) {
            java.util.List<Card> cards = availableAutomationCards.get(cardType);
            if (cards != null && !cards.isEmpty()) {
                Card card = cards.get(0);
                if (card instanceof AutomationCard) {
                    AutomationCard automationCard = (AutomationCard) card;
                    if (automationCard.getCost() <= buyingPower && automationCard.getValue() > bestValue) {
                        bestCard = automationCard;
                        bestValue = automationCard.getValue();
                    }
                }
            }
        }

        return bestCard;
    }

    /**
     * Gets the selected card to play for buying.
     *
     * @return the selected cryptocurrency card to play, or null if none selected
     */
    public Card getSelectedCardToPlay() {
        return selectedCardToPlay;
    }

    /**
     * Gets the selected card to buy.
     *
     * @return the selected automation or cryptocurrency card to buy, or null if
     *         none selected
     */
    public Card getSelectedCardToBuy() {
        return selectedCardToBuy;
    }

    /**
     * Resets the selected cards for the next turn.
     */
    public void resetCardSelections() {
        selectedCardToBuy = null;
        selectedCardToPlay = null;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", ap=" + ap +
                ", handSize=" + hand.size() +
                ", playedCardsValue=" + calculatePlayedCardsValue() +
                ", drawPileSize=" + drawPile.size() +
                ", discardPileSize=" + discardPile.size() +
                '}';
    }
}
