package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Controls traversal + shuffle for a deck of Flashcards.
 * - Toggle with setShuffleEnabled(true/false)
 * - next()/previous() to navigate
 * - progress() returns 0..1
 */
public class StudySession {
    private final List<Flashcard> allCards; // original insertion order
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
        this.allCards = new ArrayList<>(cards);  // copy so we own the list
        this.order    = new ArrayList<>(allCards);
        this.rng      = new Random(seed);
        this.shuffler = Objects.requireNonNull(shuffler, "shuffler");
    }

    /** Enable/disable shuffle. Disabling restores original order. */
    public void setShuffleEnabled(boolean enabled) {
        if (this.shuffleEnabled == enabled) return;
        this.shuffleEnabled = enabled;
        if (enabled) reshuffle();
        else {
            order.clear();
            order.addAll(allCards);
            index = 0;
        }
    }

    public boolean isShuffleEnabled() { return shuffleEnabled; }

    /** Shuffle again using the current cards. */
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

    public void reset() { index = 0; }

    /** In [0,1]; 1 means you're on the last card. */
    public double progress() {
        if (order.isEmpty()) return 0.0;
        return (index + 1) / (double) order.size();
    }

    // Optional helpers if you allow edits during study:
    public void addCard(Flashcard c) {
        allCards.add(Objects.requireNonNull(c));
        if (shuffleEnabled) reshuffle(); else order.add(c);
    }
    public boolean removeCard(Flashcard c) {
        boolean removed = allCards.remove(c);
        if (removed) {
            int inOrder = order.indexOf(c);
            if (inOrder >= 0) {
                order.remove(inOrder);
                if (index > 0 && inOrder <= index) index--;
            }
        }
        return removed;
    }
}
