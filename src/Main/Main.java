package Main;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // compose dependencies (DIP)
        SubjectRepository repo = new BinarySubjectRepository("flashcards.txt"); // <- plain string
        Validator<String> validator = new SubjectNameValidator();                   
        SubjectService service = new SubjectService(repo, validator);

        // load before UI
        service.load();

        // auto-save on exit
        Runtime.getRuntime().addShutdownHook(new Thread(service::save));

        // launch UI
        SwingUtilities.invokeLater(() -> {
            MainGUI mgu = new MainGUI(service);
            mgu.setVisible(true);   // <- no named arg
        });
    }
}
