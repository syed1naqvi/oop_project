/*
 * disclosure: assistance of AI and internet were used in some swing and GUI parts due to using elements we did not encounter 
 * before or use in class. core object oriented programming functionality was written ourselves. for anything that we
 * used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, along with rewriting
 * the code with what we learnt.
 */

package Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class SubjectsGUI extends JFrame {

    private JList<Subject> subjectsList;
    private JButton addBtn, removeBtn, backBtn, openBtn;
    private JLabel title;
    private JPanel btnBar, topRow, bottomRow;

    public SubjectsGUI() {
        setTitle("Subjects");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        title = new JLabel("Subjects", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        subjectsList = new JList<>(SubjectManager.getModel());
        subjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane subjectScroll = new JScrollPane(subjectsList);
        subjectScroll.setPreferredSize(new Dimension(0, 400));
        add(subjectScroll, BorderLayout.CENTER);

        btnBar = new JPanel();
        btnBar.setLayout(new BoxLayout(btnBar, BoxLayout.Y_AXIS));

        addBtn = new JButton("Add");
        removeBtn = new JButton("Remove");
        openBtn = new JButton("Open");
        backBtn = new JButton("Back");

        Font bigFont = addBtn.getFont().deriveFont(Font.PLAIN, 18f);
        addBtn.setFont(bigFont);
        removeBtn.setFont(bigFont);
        openBtn.setFont(bigFont);
        backBtn.setFont(bigFont);

        Dimension buttonSize = new Dimension(120, 40);
        addBtn.setPreferredSize(buttonSize);
        addBtn.setMaximumSize(buttonSize);
        removeBtn.setPreferredSize(buttonSize);
        removeBtn.setMaximumSize(buttonSize);
        openBtn.setPreferredSize(buttonSize);
        openBtn.setMaximumSize(buttonSize);
        backBtn.setPreferredSize(buttonSize);
        backBtn.setMaximumSize(buttonSize);

        btnBar.setBorder(new EmptyBorder(0,0,50,0));

        topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        topRow.add(addBtn);
        topRow.add(removeBtn);

        bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        bottomRow.add(openBtn);
        bottomRow.add(backBtn);

        btnBar.add(topRow);
        btnBar.add(bottomRow);

        add(btnBar, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> onAdd());
        removeBtn.addActionListener(e -> onRemove());
        backBtn.addActionListener(e -> dispose());
        openBtn.addActionListener(e -> {
        Subject selected = subjectsList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select a subject to open.",
                    "No Subject Selected",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            new FlashcardsGUI(selected);
        });

        // window setup
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
