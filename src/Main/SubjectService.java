/*
 * disclosure: assistance of AI and internet were used, as allowed by the professor, in some swing and GUI parts due to using
 * elements we did not encounter before or use in class. core object oriented programming functionality was written ourselves. 
 * for anything that we used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, 
 * along with rewriting the code with what we learnt.
 */


package Main;

import javax.swing.*;
import java.util.*;

public final class SubjectService {
    private final DefaultListModel<Subject> model = new DefaultListModel<>();
    private final SubjectRepository repository;
    private final Validator<String> nameValidator;

    public SubjectService(SubjectRepository repository, Validator<String> nameValidator) {
        this.repository = Objects.requireNonNull(repository);
        this.nameValidator = Objects.requireNonNull(nameValidator);
    }

    public ListModel<Subject> getModel() {
        return model;
    }

    public Subject addSubject(String name) {
        nameValidator.validate(name);
        for (int i = 0; i < model.size(); i++) {
            if (model.get(i).getName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("A subject with this name already exists.");
            }
        }
        Subject s = new Subject(name);
        model.addElement(s);
        return s;
    }

    public boolean removeSubject(Subject s) {
        return model.removeElement(s);
    }

    public void load() {
        try {
            model.clear();
            for (Subject s : repository.loadAll()) model.addElement(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            List<Subject> snapshot = Collections.list(model.elements());
            repository.saveAll(snapshot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
