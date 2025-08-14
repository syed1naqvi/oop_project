package Main;

public class Flashcard implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String mainKey;
    private String definition;
    private String subject;

    public Flashcard(String mainKey, String definition, String subject) {
        this.mainKey = mainKey;
        this.definition = definition;
        this.subject = subject;
    }

    // getters
    public String getMainKey() {
        return mainKey;
    }

    public String getDefinition() {
        return definition;
    }

    public String getSubject() {
        return subject;
    }

    // setters
    public void setMainKey(String mainKey) {
        if (mainKey == null || mainKey.isBlank()) // making sure mainKey has information
        {
            throw new IllegalArgumentException("Flashcard cannot contain empty information.");
        }
        this.mainKey = mainKey;
    }

    public void setDefinition(String definition) {
        if (definition == null || definition.isBlank()) // making sure definition has information
        {
            throw new IllegalArgumentException("Flashcard cannot contain empty information.");
        }
        this.definition = definition;
    }

    public void setSubject(String subject) {
        if (subject == null || subject.isBlank()) // making sure subject has information
        {
            throw new IllegalArgumentException("Flashcard cannot contain empty information.");
        }
        this.subject = subject;
    }
}
