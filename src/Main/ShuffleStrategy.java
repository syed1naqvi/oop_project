package Main;

import java.util.List;
import java.util.Random;

public interface ShuffleStrategy {
    void shuffle(List<Flashcard> list, Random rng);
}