package Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class SubjectsGUI extends JFrame {

    private JList<Subject> subjectsList;
    private JButton addBtn, removeBtn, closeBtn, openBtn;
    private JLabel title;
    private JPanel btnBar, topRow, bottomRow;

    public SubjectsGUI() {
        setTitle("Subjects");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(50, 50));

        title = new JLabel("Subjects", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        subjectsList = new JList<>(SubjectManager.getModel());
        subjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(subjectsList), BorderLayout.CENTER);

        btnBar = new JPanel();
        btnBar.setLayout(new BoxLayout(btnBar, BoxLayout.Y_AXIS));

        addBtn = new JButton("Add");
        removeBtn = new JButton("Remove");
        openBtn = new JButton("Open");
        closeBtn = new JButton("Close");

        Font bigFont = addBtn.getFont().deriveFont(Font.PLAIN, 18f);
        addBtn.setFont(bigFont);
        removeBtn.setFont(bigFont);
        openBtn.setFont(bigFont);
        closeBtn.setFont(bigFont);

        Dimension buttonSize = new Dimension(120, 40);
        addBtn.setPreferredSize(buttonSize);
        addBtn.setMaximumSize(buttonSize);
        removeBtn.setPreferredSize(buttonSize);
        removeBtn.setMaximumSize(buttonSize);
        openBtn.setPreferredSize(buttonSize);
        openBtn.setMaximumSize(buttonSize);
        closeBtn.setPreferredSize(buttonSize);
        closeBtn.setMaximumSize(buttonSize);

        btnBar.setBorder(new EmptyBorder(0,0,50,0));

        topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        topRow.add(addBtn);
        topRow.add(removeBtn);

        bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        bottomRow.add(openBtn);
        bottomRow.add(closeBtn);

        btnBar.add(topRow);
        btnBar.add(bottomRow);

        add(btnBar, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> onAdd());
        removeBtn.addActionListener(e -> onRemove());
        closeBtn.addActionListener(e -> System.exit(0));

        // window setup
        setSize(420, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // add subject action
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

    // remove subject action
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
