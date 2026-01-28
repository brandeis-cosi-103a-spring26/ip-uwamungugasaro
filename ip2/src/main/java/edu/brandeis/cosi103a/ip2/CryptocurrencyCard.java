package edu.brandeis.cosi103a.ip2;

/**
 * Represents a Cryptocurrency card in the card game.
 * Cryptocurrency cards have a cost in cryptocoins and a value in cryptocoins.
 * The value is worth when the card is played.
 */
public class CryptocurrencyCard extends Card {
    private int value;

    /**
     * Constructs a CryptocurrencyCard with a specified cost and value.
     *
     * @param cost  the number of cryptocoins required to buy this card
     * @param value the number of cryptocoins this card is worth when played
     */
    public CryptocurrencyCard(int cost, int value) {
        super(cost);
        this.value = value;
    }

    /**
     * Gets the value of this cryptocurrency card in cryptocoins.
     *
     * @return the number of cryptocoins this card is worth when played
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of this cryptocurrency card.
     *
     * @param value the number of cryptocoins this card is worth when played
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CryptocurrencyCard{" +
                "cost=" + getCost() +
                ", value=" + value + " cryptocoins" +
                '}';
    }
}
