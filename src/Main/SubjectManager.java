package Main;

import javax.swing.*;

public final class SubjectManager {

    private static final DefaultListModel<Subject> MODEL = new DefaultListModel<>(); // updates JList automatically

    private SubjectManager() {}

    // getter
    public static DefaultListModel<Subject> getModel() 
    {
        return MODEL;
    }

    // add subject
    public static Subject addSubject(String name) 
    {
        String key = name == null ? "" : name.trim();
        if (key.isEmpty()) 
        {
            throw new IllegalArgumentException("Subject name cannot be empty."); // cannot be null
        }

        for (int i = 0; i < MODEL.size(); i++) 
        {
            if (MODEL.get(i).getName().equalsIgnoreCase(key)) //check if already exists
            {
                return MODEL.get(i);
            }
        }

        Subject created = new Subject(key);
        MODEL.addElement(created); // JList updates automatically
        return created;
    }

    // remove subject
    public static boolean removeSubject(Subject s) 
    {
        return MODEL.removeElement(s); // JList updates automatically
    }
}
