package Main;

import javax.swing.DefaultListModel;
import java.io.*;
import java.util.*;

public final class DataStorage {
    private static final String FILE_NAME = "flashcards.txt";

    public static void save(DefaultListModel<Subject> model) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(Collections.list(model.elements()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadInto(DefaultListModel<Subject> model) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            List<Subject> subjects = (List<Subject>) in.readObject();
            model.clear();
            subjects.forEach(model::addElement);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
