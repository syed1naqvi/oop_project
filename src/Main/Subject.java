package Main;

import java.util.*;

public class Subject {

    private final String name;
    private final java.util.List<Flashcard> flashcards = new ArrayList<>();

    public Subject(String name) {
        if (name == null || name.isBlank()) // making sure subject has a name
        {
            throw new IllegalArgumentException("Subject name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getName() { return name; }

    public List<Flashcard> getFlashcards() {
        return Collections.unmodifiableList(flashcards); // disclosure: recommended by AI to use unmodifiableList to prevent data tampering
    }

    public void addFlashcard(Flashcard card) {
        flashcards.add(Objects.requireNonNull(card)); // prevent having any nulls in list to deal with later on
    }

    public boolean removeFlashcard(Flashcard card) {
        return flashcards.remove(card);
    }

    @Override public String toString() { return name; }
}
