package edu.brandeis.cosi103a.ip2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for the Player class.
 * Tests focus on core game mechanics: card playing, drawing, purchasing,
 * cleanup, and strategy.
 */
public class PlayerTest {
    private Player player;
    private Card automationCard;
    private Card cryptoCard1;
    private Card cryptoCard2;
    private Card bitcoin;

    @Before
    public void setUp() {
        player = new Player("Alice", 0);
        automationCard = new AutomationCard(10, 5);
        cryptoCard1 = new CryptocurrencyCard(3, 2);
        cryptoCard2 = new CryptocurrencyCard(6, 3);
        bitcoin = new CryptocurrencyCard(0, 1);
    }

    /**
     * Test playCard moves card from hand to discard pile.
     */
    @Test
    public void testPlayCard() {
        player.addCardToHand(cryptoCard1);
        assertEquals(1, player.getHandSize());

        boolean played = player.playCard(cryptoCard1);

        assertTrue(played);
        assertEquals(0, player.getHandSize());
        assertEquals(1, player.getDiscardPileSize());
        assertTrue(player.getDiscardPile().contains(cryptoCard1));
    }

    /**
     * Test playCard fails when card not in hand.
     */
    @Test
    public void testPlayCardNotInHand() {
        player.addCardToHand(cryptoCard1);

        boolean played = player.playCard(cryptoCard2);

        assertFalse(played);
        assertEquals(1, player.getHandSize());
        assertEquals(0, player.getDiscardPileSize());
    }

    /**
     * Test drawCard moves card from draw pile to hand.
     */
    @Test
    public void testDrawCard() {
        player.addCardToDrawPile(cryptoCard1);
        assertEquals(1, player.getDrawPileSize());

        Card drawn = player.drawCard();

        assertEquals(cryptoCard1, drawn);
        assertEquals(0, player.getDrawPileSize());
        assertEquals(1, player.getHandSize());
        assertTrue(player.getHand().contains(cryptoCard1));
    }

    /**
     * Test drawCard reshuffles discard pile when draw pile is empty.
     */
    @Test
    public void testDrawCardReshufflesWhenEmpty() {
        player.addCardToDiscardPile(cryptoCard1);
        player.addCardToDiscardPile(cryptoCard2);
        assertEquals(0, player.getDrawPileSize());
        assertEquals(2, player.getDiscardPileSize());

        Card drawn = player.drawCard();

        assertNotNull(drawn);
        // After reshuffle: 1 card drawn from draw pile, 1 card left in draw pile
        assertEquals(1, player.getDrawPileSize());
        // Discard pile should be empty after reshuffle
        assertEquals(0, player.getDiscardPileSize());
        assertEquals(1, player.getHandSize());
    }

    /**
     * Test drawCard returns null when both piles are empty.
     */
    @Test
    public void testDrawCardReturnsNullWhenBothEmpty() {
        Card drawn = player.drawCard();

        assertNull(drawn);
        assertEquals(0, player.getHandSize());
    }

    /**
     * Test playCardForBuying moves card from hand to played cards.
     */
    @Test
    public void testPlayCardForBuying() {
        player.addCardToHand(cryptoCard1);

        boolean played = player.playCardForBuying(cryptoCard1);

        assertTrue(played);
        assertEquals(0, player.getHandSize());
        assertEquals(0, player.getDiscardPileSize());
        assertEquals(2, player.calculatePlayedCardsValue());
    }

    /**
     * Test calculatePlayedCardsValue sums values of all played cards.
     */
    @Test
    public void testCalculatePlayedCardsValue() {
        player.addCardToHand(cryptoCard1); // value 2
        player.addCardToHand(cryptoCard2); // value 3
        player.addCardToHand(bitcoin); // value 1

        player.playCardForBuying(cryptoCard1);
        player.playCardForBuying(cryptoCard2);
        player.playCardForBuying(bitcoin);

        assertEquals(6, player.calculatePlayedCardsValue());
    }

    /**
     * Test purchaseCard adds card to discard pile.
     */
    @Test
    public void testPurchaseCard() {
        player.purchaseCard(automationCard);

        assertEquals(0, player.getHandSize());
        assertEquals(1, player.getDiscardPileSize());
        assertTrue(player.getDiscardPile().contains(automationCard));
    }

    /**
     * Test cleanupPhase moves all hand and played cards to discard pile and deals
     * new hand.
     */
    @Test
    public void testCleanupPhase() {
        // Setup: hand with some cards, played cards from buy phase
        player.addCardToDrawPile(automationCard);
        player.addCardToDrawPile(cryptoCard1);
        player.addCardToDrawPile(cryptoCard2);
        player.addCardToHand(bitcoin);
        player.playCardForBuying(bitcoin);

        player.cleanupPhase();

        // During cleanup:
        // 1. Hand (empty after playCardForBuying) and played (bitcoin) moved to discard
        // 2. dealNewHand(5) reshuffles discard (1 bitcoin) back to draw
        // 3. Then deals up to 5 cards from draw (we have 3 original + 1 bitcoin = 4
        // total available)
        // Result: discard is empty, hand is 4
        assertEquals(0, player.getDiscardPileSize());
        assertEquals(0, player.calculatePlayedCardsValue());
        assertEquals(4, player.getHandSize());
    }

    /**
     * Test selectCardsForBuying picks a cryptocurrency card when available.
     */
    @Test
    public void testSelectCardsForBuyingWithCryptoInHand() {
        player.addCardToHand(cryptoCard1);
        player.addCardToHand(bitcoin);

        Map<String, List<Card>> supply = new HashMap<>();
        supply.put("Framework", new ArrayList<>());
        supply.put("Module", new ArrayList<>());
        supply.put("Method", new ArrayList<>());

        boolean selected = player.selectCardsForBuying(supply);

        assertTrue(selected);
        assertNotNull(player.getSelectedCardToPlay());
        assertTrue(player.getSelectedCardToPlay() instanceof CryptocurrencyCard);
    }

    /**
     * Test selectCardsForBuying prefers non-Bitcoin cards when available.
     */
    @Test
    public void testSelectCardsForBuyingPreferencesNonBitcoin() {
        player.addCardToHand(cryptoCard1);
        player.addCardToHand(bitcoin);

        Map<String, List<Card>> supply = new HashMap<>();
        supply.put("Framework", new ArrayList<>());
        supply.put("Module", new ArrayList<>());
        supply.put("Method", new ArrayList<>());

        // Run selection multiple times - should sometimes select non-Bitcoin
        boolean selectedNonBitcoin = false;
        for (int i = 0; i < 10; i++) {
            player = new Player("Test", 0);
            player.addCardToHand(cryptoCard1);
            player.addCardToHand(bitcoin);
            player.selectCardsForBuying(supply);

            if (player.getSelectedCardToPlay() == cryptoCard1) {
                selectedNonBitcoin = true;
                break;
            }
        }
        assertTrue("Should prefer non-Bitcoin cards", selectedNonBitcoin);
    }

    /**
     * Test selectCardsForBuying returns false when no crypto cards in hand.
     */
    @Test
    public void testSelectCardsForBuyingNoCardsInHand() {
        Map<String, List<Card>> supply = new HashMap<>();
        supply.put("Framework", new ArrayList<>());

        boolean selected = player.selectCardsForBuying(supply);

        assertFalse(selected);
        assertNull(player.getSelectedCardToPlay());
    }

    /**
     * Test addAP and removeAP modify player's AP correctly.
     */
    @Test
    public void testAPModification() {
        player.addAP(10);
        assertEquals(10, player.getAP());

        player.addAP(5);
        assertEquals(15, player.getAP());

        player.removeAP(3);
        assertEquals(12, player.getAP());
    }

    /**
     * Test resetCardSelections clears selected cards.
     */
    @Test
    public void testResetCardSelections() {
        player.addCardToHand(cryptoCard1);
        player.playCardForBuying(cryptoCard1);

        Map<String, List<Card>> supply = new HashMap<>();
        supply.put("Framework", new ArrayList<>());
        supply.put("Module", new ArrayList<>());
        supply.put("Method", new ArrayList<>());
        player.selectCardsForBuying(supply);

        player.resetCardSelections();

        assertNull(player.getSelectedCardToPlay());
        assertNull(player.getSelectedCardToBuy());
    }

    /**
     * Test full game turn sequence: draw, play, cleanup.
     */
    @Test
    public void testFullTurnSequence() {
        // Initial draw pile setup
        player.addCardToDrawPile(automationCard);
        player.addCardToDrawPile(cryptoCard1);
        player.addCardToDrawPile(cryptoCard2);
        player.addCardToDrawPile(bitcoin);

        // Draw to hand
        player.drawCard();
        player.drawCard();
        assertEquals(2, player.getHandSize());
        assertEquals(2, player.getDrawPileSize());

        // Play a card for buying
        player.playCardForBuying(cryptoCard1);
        assertEquals(1, player.getHandSize());
        assertEquals(2, player.calculatePlayedCardsValue());

        // Purchase a card
        player.purchaseCard(automationCard);
        assertEquals(1, player.getDiscardPileSize());

        // Cleanup:
        // 1. Add hand (automationCard) and played (cryptoCard1) to discard = 3 total in
        // discard
        // 2. dealNewHand(5) reshuffles the 3 from discard into draw (which had 2)
        // 3. Deals up to 5 from draw: we have 5 total (2 original + 3 from reshuffle),
        // so hand gets 5
        // 4. After dealing 5, draw pile still has 0, discard is empty
        player.cleanupPhase();
        assertEquals(0, player.calculatePlayedCardsValue());
        assertEquals(5, player.getHandSize());
    }
}
