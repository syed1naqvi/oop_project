/*
 * disclosure: assistance of AI and internet were used in some swing and GUI parts due to using elements we did not encounter 
 * before or use in class. core object oriented programming functionality was written ourselves. for anything that we
 * used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, along with rewriting
 * the code with what we learnt.
 */

package Main;

import java.awt.*;
import javax.swing.*;

public class SubjectsGUI extends JFrame {

    private JList<Subject> subjectsList;
    private JButton addBtn, removeBtn, backBtn, openBtn, studyBtn;
    private JLabel title;
    private JPanel btnBar, topRow, bottomRow;

public SubjectsGUI() {
    setTitle("Subjects");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout(12,12));

    JLabel title = new JLabel("Subjects", SwingConstants.CENTER);
    title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
    add(title, BorderLayout.NORTH);

    subjectsList = new JList<>(SubjectManager.getModel());
    subjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    add(new JScrollPane(subjectsList), BorderLayout.CENTER);

    JPanel btnBar = new JPanel();
    btnBar.setLayout(new BoxLayout(btnBar, BoxLayout.Y_AXIS));
    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
    JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
    btnBar.add(topRow);
    btnBar.add(bottomRow);
    add(btnBar, BorderLayout.SOUTH);

    // --- CREATE BUTTONS (fields, no shadowing) ---
    addBtn    = new JButton("Add");
    removeBtn = new JButton("Remove");
    openBtn   = new JButton("Open");
    studyBtn  = new JButton("Study");   // <-- created before listeners
    backBtn   = new JButton("Back");

    Font bigFont = addBtn.getFont().deriveFont(Font.PLAIN, 18f);
    for (JButton b : new JButton[]{addBtn, removeBtn, openBtn, studyBtn, backBtn}) {
        b.setFont(bigFont);
        b.setPreferredSize(new Dimension(120, 40));
        b.setMaximumSize(new Dimension(120, 40));
    }

    topRow.add(addBtn);
    topRow.add(removeBtn);
    bottomRow.add(openBtn);
    bottomRow.add(studyBtn);            // <-- add to layout
    bottomRow.add(backBtn);

    // --- LISTENERS (attach AFTER creation) ---
    addBtn.addActionListener(e -> onAdd());
    removeBtn.addActionListener(e -> onRemove());
    backBtn.addActionListener(e -> dispose());

    openBtn.addActionListener(e -> {
        Subject selected = subjectsList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject to open.",
                    "No subject selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new FlashcardsGUI(selected);
    });

    studyBtn.addActionListener(e -> {
        Subject selected = subjectsList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject to study.",
                    "No subject selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new StudyGUI(selected.getFlashcards());
    });

    // nice UX: enable Study only when a subject is selected
    studyBtn.setEnabled(false);
    subjectsList.addListSelectionListener(e ->
        studyBtn.setEnabled(!subjectsList.isSelectionEmpty())
    );

    setSize(420, 500);
    setLocationRelativeTo(null);
    setVisible(true);
}


    // add subject
    private void onAdd() {
        String name = JOptionPane.showInputDialog(this, "New subject name:");
        if (name == null) return; // user canceled
        try {
            Subject created = SubjectManager.addSubject(name);
            subjectsList.setSelectedValue(created, true); // highlight new one
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Invalid Name",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // remove subject
    private void onRemove() {
        Subject selected = subjectsList.getSelectedValue();
        if (selected == null) return; // nothing selected
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove subject \"" + selected.getName() + "\"?",
                "Confirm",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (confirm == JOptionPane.OK_OPTION) {
            SubjectManager.removeSubject(selected);
        }
    }
}
