package Main;

import javax.swing.*;
import java.awt.*;

public class SubjectsGUI extends JFrame {
    
    public SubjectsGUI() 
    {
        super("Subjects");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes just this window
        setLayout(new BorderLayout(16,16));

        add(new JLabel("Subjects manager goes here", SwingConstants.CENTER), BorderLayout.CENTER);

        setSize(420, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}