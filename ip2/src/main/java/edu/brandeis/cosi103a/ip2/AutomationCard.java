package edu.brandeis.cosi103a.ip2;

/**
 * Represents an Automation card in the card game.
 * Automation cards have a cost in cryptocoins and a value in APs (action
 * points).
 * The value is worth at the end of the game.
 */
public class AutomationCard extends Card {
    private int value;

    /**
     * Constructs an AutomationCard with a specified cost and value.
     *
     * @param cost  the number of cryptocoins required to buy this card
     * @param value the number of APs this card is worth at the end of the game
     */
    public AutomationCard(int cost, int value) {
        super(cost);
        this.value = value;
    }

    /**
     * Gets the value of this automation card in APs.
     *
     * @return the number of APs this card is worth at the end of the game
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of this automation card.
     *
     * @param value the number of APs this card is worth at the end of the game
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AutomationCard{" +
                "cost=" + getCost() +
                ", value=" + value + " APs" +
                '}';
    }
}
