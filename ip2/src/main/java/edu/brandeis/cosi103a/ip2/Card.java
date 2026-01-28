package edu.brandeis.cosi103a.ip2;

/**
 * Abstract base class for all card types in the card game.
 * All cards have a cost in cryptocoins required to purchase them.
 */
public abstract class Card {
    private int cost;

    /**
     * Constructs a Card with a specified cost.
     *
     * @param cost the number of cryptocoins required to buy this card
     */
    public Card(int cost) {
        this.cost = cost;
    }

    /**
     * Gets the cost of this card.
     *
     * @return the number of cryptocoins required to buy this card
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the cost of this card.
     *
     * @param cost the number of cryptocoins required to buy this card
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Gets the value of this card.
     * The type of value depends on the card subtype.
     *
     * @return the value of this card
     */
    public abstract int getValue();
}
