/*
 * disclosure: assistance of AI and internet were used in some swing and GUI parts due to using elements we did not encounter 
 * before or use in class. core object oriented programming functionality was written ourselves. for anything that we
 * used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, along with rewriting
 * the code with what we learnt.
 */

package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
  Controls traversal + shuffle for a deck of Flashcards.
  - Toggle with setShuffleEnabled(true/false)
  - next()/previous() to navigate
  - progress() returns 0..1
 */
public class StudySession {
    private final List<Flashcard> allCards; // original order inserted
    private final List<Flashcard> order;    // current traversal order
    private int index = 0;
    private boolean shuffleEnabled = false;

    private final Random rng;
    private final ShuffleStrategy shuffler;

    public StudySession(List<Flashcard> cards) {
        this(cards, System.currentTimeMillis(), new FisherYatesShuffle());
    }

    public StudySession(List<Flashcard> cards, long seed) {
        this(cards, seed, new FisherYatesShuffle());
    }

    public StudySession(List<Flashcard> cards, long seed, ShuffleStrategy shuffler) {
        Objects.requireNonNull(cards, "cards");
        this.allCards = new ArrayList<>(cards);   // get a copy
        this.order    = new ArrayList<>(allCards);
        this.rng      = new Random(seed);
        this.shuffler = Objects.requireNonNull(shuffler, "shuffler");
    }

    // enable/disable shuffle. Disabling restores original order added to the subject
    public void setShuffleEnabled(boolean enabled) {
        if (this.shuffleEnabled == enabled) return;
        this.shuffleEnabled = enabled;
        if (enabled) {
            reshuffle();
        } else {
            order.clear();
            order.addAll(allCards);
            index = 0;
        }
    }

    public boolean isShuffleEnabled() { return shuffleEnabled; }

    // shuffle again using the current cards
    public void reshuffle() {
        order.clear();
        order.addAll(allCards);
        shuffler.shuffle(order, rng);
        index = 0;
    }

    public Flashcard current() {
        if (order.isEmpty()) return null;
        return order.get(index);
    }

    public Flashcard next() {
        if (order.isEmpty()) return null;
        if (index < order.size() - 1) {
            index++;
            return order.get(index);
        }
        return null;
    }

    public Flashcard previous() {
        if (order.isEmpty()) return null;
        if (index > 0) {
            index--;
            return order.get(index);
        }
        return null;
    }

    // Create a progress bar for the flash card (i.e 1/4 or 2/6)
    // to see how far along you are through the flash cards
    // Total cards in the current traversal order. 
    public int size() { return order.size(); }

    //Position of the current card
    public int position() { return order.isEmpty() ? -1 : index; }

}
