/*
 * disclosure: assistance of AI and internet were used, as allowed by the professor, in some swing and GUI parts due to using
 * elements we did not encounter before or use in class. core object oriented programming functionality was written ourselves. 
 * for anything that we used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, 
 * along with rewriting the code with what we learnt.
 */

package Main;

import java.awt.*;
import javax.swing.*;

public class FlashcardsGUI extends JFrame {
    private final SubjectService service;
    private final Subject subject;

    private DefaultListModel<Flashcard> model;
    private JList<Flashcard> flashcardList;
    private JButton addBtn, editBtn, removeBtn, backBtn;

    public FlashcardsGUI(SubjectService service, Subject subject) {
        super("Flashcards — " + subject.getName());
        this.service = service;
        this.subject = subject;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel("Subject: " + subject.getName(), SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // Build model from subject cards
        model = new DefaultListModel<>();
        for (Flashcard c : subject.getFlashcards()) model.addElement(c);

        flashcardList = new JList<>(model);
        flashcardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flashcardList.setVisibleRowCount(10);
        add(new JScrollPane(flashcardList), BorderLayout.CENTER);

        JPanel btnBar = new JPanel();
        btnBar.setLayout(new BoxLayout(btnBar, BoxLayout.Y_AXIS));
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        btnBar.add(topRow);
        btnBar.add(bottomRow);
        add(btnBar, BorderLayout.SOUTH);

        addBtn    = new JButton("Add");
        editBtn   = new JButton("Edit");
        removeBtn = new JButton("Remove");
        backBtn   = new JButton("Back");

        Font big = addBtn.getFont().deriveFont(Font.PLAIN, 18f);
        for (JButton b : new JButton[]{addBtn, editBtn, removeBtn, backBtn}) {
            b.setFont(big);
            b.setPreferredSize(new Dimension(120, 40));
        }

        topRow.add(addBtn);
        topRow.add(editBtn);
        bottomRow.add(removeBtn);
        bottomRow.add(backBtn);

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        removeBtn.addActionListener(e -> onRemove());
        backBtn.addActionListener(e -> dispose());

        setSize(600, 480);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onAdd() {
        JTextField keyField = new JTextField();
        JTextArea defArea = new JTextArea(5, 20);
        defArea.setLineWrap(true);
        defArea.setWrapStyleWord(true);
        JTextField subjField = new JTextField(subject.getName());
        subjField.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout(8,8));
        JPanel top = new JPanel(new GridLayout(0,1,6,6));
        top.add(new JLabel("Term:"));
        top.add(keyField);
        top.add(new JLabel("Definition:"));
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(defArea), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new GridLayout(0,1,6,6));
        bottom.add(new JLabel("Subject:"));
        bottom.add(subjField);
        panel.add(bottom, BorderLayout.SOUTH);

        int res = JOptionPane.showConfirmDialog(this, panel, "Add Flashcard",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String key = keyField.getText();
        String def = defArea.getText();
        try {
            Flashcard c = new Flashcard(key, def, subject.getName());
            subject.addFlashcard(c);
            model.addElement(c);
            service.save(); // optional
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onEdit() {
        Flashcard sel = flashcardList.getSelectedValue();
        if (sel == null) return;

        JTextField keyField = new JTextField(sel.getMainKey());
        JTextArea defArea = new JTextArea(sel.getDefinition(), 5, 20);
        defArea.setLineWrap(true);
        defArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout(8,8));
        JPanel top = new JPanel(new GridLayout(0,1,6,6));
        top.add(new JLabel("Term:"));
        top.add(keyField);
        top.add(new JLabel("Definition:"));
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(defArea), BorderLayout.CENTER);

        int res = JOptionPane.showConfirmDialog(this, panel, "Edit Flashcard",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            sel.setMainKey(keyField.getText());
            sel.setDefinition(defArea.getText());
            // refresh cell
            int idx = flashcardList.getSelectedIndex();
            model.set(idx, sel);
            service.save(); // optional
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onRemove() {
        Flashcard sel = flashcardList.getSelectedValue();
        if (sel == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove this flashcard?\n\n" + sel.getMainKey(),
                "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        subject.removeFlashcard(sel);
        model.removeElement(sel);
        service.save(); // optional
    }
}
