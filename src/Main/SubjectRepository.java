package Main;

import java.util.List;

public interface SubjectRepository {
    List<Subject> loadAll() throws Exception;
    void saveAll(List<Subject> subjects) throws Exception;
}
