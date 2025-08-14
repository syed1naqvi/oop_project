/*
 * disclosure: assistance of AI and internet were used, as allowed by the professor, in some swing and GUI parts due to using
 * elements we did not encounter before or use in class. core object oriented programming functionality was written ourselves. 
 * for anything that we used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, 
 * along with rewriting the code with what we learnt.
 */

package Main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainGUI extends JFrame {

    private final SubjectService service;
    private JPanel centerPanel, bottomPanel;
    private JButton subBtn;
    private JButton studyBtn;
    private JLabel titleLabel, info;

    public MainGUI(SubjectService service) {
        this.service = service;
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

        subBtn.addActionListener(e -> new SubjectsGUI(service).setVisible(true));

        studyBtn.addActionListener(e -> {
            ListModel<Subject> model = service.getModel();
            if (model.getSize() == 0) {
                JOptionPane.showMessageDialog(this,
                    "No subjects available. Please create subjects and add flashcards first.",
                    "No Subjects",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int totalFlashcards = 0;
            for (int i = 0; i < model.getSize(); i++) {
                totalFlashcards += model.getElementAt(i).getFlashcards().size();
            }

            String[] options = {"Flashcard Study", "Multiple Choice Quiz", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this,
                "Select study mode:",
                "Study Mode Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

            if (choice == 0) {
                showSubjectSelectionForStudy();
            } else if (choice == 1) {
                if (totalFlashcards < 4) {
                    JOptionPane.showMessageDialog(this,
                        "You need at least 4 flashcards total to start a quiz.\n" +
                        "Current total: " + totalFlashcards + " flashcards",
                        "Not Enough Flashcards",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                new QuizModeGUI(service).setVisible(true);
            }
        });

        bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.add(centerPanel);
        bottomPanel.setBorder(new EmptyBorder(-200,0,0,0));
        add(bottomPanel, BorderLayout.CENTER);

        pack();
        setMinimumSize(new Dimension(420, 500));
        setLocationRelativeTo(null);
    }

    private void showSubjectSelectionForStudy() {
        ListModel<Subject> model = service.getModel();
        JList<Subject> subjectList = new JList<>(model);
        subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        subjectList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Subject) {
                    Subject s = (Subject) value;
                    setText(s.getName() + " (" + s.getFlashcards().size() + " flashcards)");
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(subjectList);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        int result = JOptionPane.showConfirmDialog(this,
            scrollPane,
            "Select a Subject to Study",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Subject selected = subjectList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this,
                    "Please select a subject to study.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
                showSubjectSelectionForStudy();
            } else if (selected.getFlashcards().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "The selected subject has no flashcards.",
                    "No Flashcards",
                    JOptionPane.WARNING_MESSAGE);
                showSubjectSelectionForStudy();
            } else {
                new StudyGUI(service, selected.getFlashcards()).setVisible(true);
            }
        }
    }
}
