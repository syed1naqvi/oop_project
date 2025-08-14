/*
 * disclosure: assistance of AI and internet were used, as allowed by the professor, in some swing and GUI parts due to using
 * elements we did not encounter before or use in class. core object oriented programming functionality was written ourselves. 
 * for anything that we used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, 
 * along with rewriting the code with what we learnt.
 */

package Main;

import java.awt.*;
import javax.swing.*;

public class SubjectsGUI extends JFrame {
    private final SubjectService service;
    private final JList<Subject> subjectsList;
    private JButton addBtn, removeBtn, backBtn, openBtn, studyBtn;

    public SubjectsGUI(SubjectService service) {
        this.service = service;

        setTitle("Subjects");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(12,12));

        JLabel title = new JLabel("Subjects", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        subjectsList = new JList<>(service.getModel());
        subjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(subjectsList), BorderLayout.CENTER);

        JPanel btnBar = new JPanel();
        btnBar.setLayout(new BoxLayout(btnBar, BoxLayout.Y_AXIS));
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        btnBar.add(topRow);
        btnBar.add(bottomRow);
        add(btnBar, BorderLayout.SOUTH);

        addBtn    = new JButton("Add");
        removeBtn = new JButton("Remove");
        openBtn   = new JButton("Open");
        studyBtn  = new JButton("Study");
        backBtn   = new JButton("Back");

        Font big = addBtn.getFont().deriveFont(Font.PLAIN, 18f);
        for (JButton b : new JButton[]{addBtn, removeBtn, openBtn, studyBtn, backBtn}) {
            b.setFont(big);
            b.setPreferredSize(new Dimension(120, 40));
        }

        topRow.add(addBtn);
        topRow.add(removeBtn);
        bottomRow.add(openBtn);
        bottomRow.add(studyBtn);
        bottomRow.add(backBtn);

        addBtn.addActionListener(e -> onAdd());
        removeBtn.addActionListener(e -> onRemove());
        backBtn.addActionListener(e -> dispose());
        openBtn.addActionListener(e -> onOpen());
        studyBtn.addActionListener(e -> onStudy());

        setSize(520, 420);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onAdd() {
        String name = JOptionPane.showInputDialog(this, "New subject name:");
        if (name == null) return;
        try {
            Subject created = service.addSubject(name);
            subjectsList.setSelectedValue(created, true);
            service.save(); // optional immediate save
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Name", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onRemove() {
        Subject selected = subjectsList.getSelectedValue();
        if (selected == null) return;
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove subject \"" + selected.getName() + "\"?",
                "Confirm",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (confirm == JOptionPane.OK_OPTION) {
            service.removeSubject(selected);
            service.save(); // optional immediate save
        }
    }

    private void onOpen() {
        Subject selected = subjectsList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a subject first.");
            return;
        }
        new FlashcardsGUI(service, selected).setVisible(true);
    }

    private void onStudy() {
        Subject selected = subjectsList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a subject first.");
            return;
        }
        if (selected.getFlashcards().isEmpty()) {
            JOptionPane.showMessageDialog(this, "This subject has no flashcards.");
            return;
        }
        new StudyGUI(service, selected.getFlashcards()).setVisible(true);
    }
}
