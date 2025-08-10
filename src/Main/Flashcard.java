package Main;

public class Flashcard {
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
        this.mainKey = mainKey;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
