package Main;

public class Flashcard implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String mainKey;
    private String definition;
    private String subject;

    public Flashcard(String mainKey, String definition, String subject) {
        if (mainKey == null || mainKey.isBlank() || definition == null || definition.isBlank())
            throw new IllegalArgumentException("Flashcard cannot contain empty information.");
        this.mainKey = mainKey;
        this.definition = definition;
        this.subject = subject;
    }

    public String getMainKey() { return mainKey; }
    public String getDefinition() { return definition; }
    public String getSubject() { return subject; }

    public void setMainKey(String mainKey) {
        if (mainKey == null || mainKey.isBlank())
            throw new IllegalArgumentException("Flashcard cannot contain empty information.");
        this.mainKey = mainKey;
    }
    public void setDefinition(String definition) {
        if (definition == null || definition.isBlank())
            throw new IllegalArgumentException("Flashcard cannot contain empty information.");
        this.definition = definition;
    }
    public void setSubject(String subject) { this.subject = subject; }

    @Override public String toString() {
        return mainKey;
    }
}

