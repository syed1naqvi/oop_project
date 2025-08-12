package Main;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FisherYatesShuffle implements ShuffleStrategy {
    @Override
    public void shuffle(List<Flashcard> list, Random rng) {
        for (int i = list.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1); // 0..i inclusive
            Collections.swap(list, i, j);
        }
    }
}