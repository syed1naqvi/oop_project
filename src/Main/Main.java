package Main;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) throws Exception {

        SubjectManager.loadFromDisk();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> SubjectManager.saveToDisk()));

        SwingUtilities.invokeLater(() -> {
            MainGUI mgu = new MainGUI();
            mgu.show();
        });
    }
}
