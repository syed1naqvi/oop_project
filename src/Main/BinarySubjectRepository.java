package Main;

import java.io.*;
import java.util.*;

public final class BinarySubjectRepository implements SubjectRepository {
    private final File file;

    public BinarySubjectRepository(String path) {
        this.file = new File(path);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Subject> loadAll() throws Exception {
        if (!file.exists()) return Collections.emptyList();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                List<?> raw = (List<?>) obj;
                List<Subject> out = new ArrayList<>(raw.size());
                for (Object o : raw) out.add((Subject) o);
                return out;
            }
            return Collections.emptyList();
        }
    }

    @Override
    public void saveAll(List<Subject> subjects) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(new ArrayList<>(subjects));
        }
    }
}
