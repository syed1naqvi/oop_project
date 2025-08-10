package Main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainGUI extends JFrame {
    
    private JPanel centerPanel, bottomPanel;
    private JButton subBtn;
    private JButton studyBtn;
    private JLabel titleLabel, info;

    public MainGUI()
    {
        setTitle("Flashcards");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(50, 50));

        titleLabel = new JLabel("Home", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 25f));
        titleLabel.setBorder(new EmptyBorder(50, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        info = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Select 'Subject' if you would like to  <br/> manage your flashcards."
            + " Select 'Study' <br/> if you would like to study them."
            + "</div></html>",
            SwingConstants.CENTER
        );
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setFont(titleLabel.getFont().deriveFont(Font.PLAIN, 20f));
        info.setBorder(new EmptyBorder(0,0,40,0));

        subBtn = new JButton("Subjects");
        studyBtn = new JButton("Study");
        subBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        studyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        Font bigFont = subBtn.getFont().deriveFont(Font.PLAIN, 18f);
        subBtn.setFont(bigFont);
        studyBtn.setFont(bigFont);
        Dimension buttonSize = new Dimension(180, 120);
        subBtn.setPreferredSize(buttonSize);
        studyBtn.setPreferredSize(buttonSize);
        subBtn.setMaximumSize(buttonSize);
        studyBtn.setMaximumSize(buttonSize);
        
        centerPanel.add(info);
        centerPanel.add(Box.createVerticalStrut(12));
        centerPanel.add(subBtn);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(studyBtn);

        bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.add(centerPanel);
        bottomPanel.setBorder(new EmptyBorder(-200,0,0,0));
        add(bottomPanel, BorderLayout.CENTER);

        pack();
        setMinimumSize(new Dimension(420, 500));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
