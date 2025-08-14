/*
 * disclosure: assistance of AI and internet were used, as allowed by the professor, in some swing and GUI parts due to using
 * elements we did not encounter before or use in class. core object oriented programming functionality was written ourselves. 
 * for anything that we used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, 
 * along with rewriting the code with what we learnt.
 */

package Main;

import java.util.*;

public class Subject implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Flashcard> flashcards = new ArrayList<>();

    public Subject(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards; // kept mutable for GUI editing convenience
    }

    public void addFlashcard(Flashcard card) {
        flashcards.add(Objects.requireNonNull(card));
    }

    public boolean removeFlashcard(Flashcard card) {
        return flashcards.remove(card);
    }

    @Override public String toString() {
        return name;
    }
}
