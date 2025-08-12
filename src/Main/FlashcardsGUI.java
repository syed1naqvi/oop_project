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

public class FlashcardsGUI extends JFrame {

    private final Subject subject;
    private DefaultListModel<Flashcard> MODEL;
    private JList<Flashcard> flashcardList;
    private JButton addBtn, editBtn, removeBtn, backBtn;
    private JLabel title;
    private JPanel btnBar, topRow, bottomRow;

    public FlashcardsGUI(Subject subject) 
    {
        setTitle("Flashcards — " + subject.getName());
        this.subject = subject;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // completely exit program
        setLayout(new BorderLayout(12, 12));

        title = new JLabel("Subject: " + subject.getName(), SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // list model + populate from subject
        MODEL = new DefaultListModel<>();
        for (Flashcard fc : subject.getFlashcards()) {
            MODEL.addElement(fc);
        }

        
        /* 
         * flashcards list with custom renderer - this is needed so that the user can see both the word and the definition within the list,
         * which is why it is much more complicated than the subjectsGUI -> needed AI and online assistance to figure out how to display all 
         * the elements on the JList as i have no experience with JList
         */

        flashcardList = new JList<>(MODEL);
        flashcardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flashcardList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel lbl = new JLabel(value.getMainKey() + " — " + value.getDefinition());
            lbl.setOpaque(true);
            if (isSelected) {
                lbl.setBackground(list.getSelectionBackground());
                lbl.setForeground(list.getSelectionForeground());
            } else {
                lbl.setBackground(list.getBackground());
                lbl.setForeground(list.getForeground());
            }
            lbl.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            return lbl;
        });
        add(new JScrollPane(flashcardList), BorderLayout.CENTER);

        btnBar = new JPanel();
        btnBar.setLayout(new BoxLayout(btnBar, BoxLayout.Y_AXIS));

        addBtn = new JButton("Add");
        removeBtn = new JButton("Remove");
        editBtn = new JButton("Edit");
        backBtn = new JButton("Back");

        Font bigFont = addBtn.getFont().deriveFont(Font.PLAIN, 18f);
        addBtn.setFont(bigFont);
        removeBtn.setFont(bigFont);
        editBtn.setFont(bigFont);
        backBtn.setFont(bigFont);

        Dimension buttonSize = new Dimension(120, 40);
        addBtn.setPreferredSize(buttonSize);
        addBtn.setMaximumSize(buttonSize);
        removeBtn.setPreferredSize(buttonSize);
        removeBtn.setMaximumSize(buttonSize);
        editBtn.setPreferredSize(buttonSize);
        editBtn.setMaximumSize(buttonSize);
        backBtn.setPreferredSize(buttonSize);
        backBtn.setMaximumSize(buttonSize);

        btnBar.setBorder(new EmptyBorder(0,0,50,0));

        topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        topRow.add(addBtn);
        topRow.add(removeBtn);

        bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        bottomRow.add(editBtn);
        bottomRow.add(backBtn);

        btnBar.add(topRow);
        btnBar.add(bottomRow);

        add(btnBar, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        removeBtn.addActionListener(e -> onRemove());
        backBtn.addActionListener(e -> dispose());

        setSize(420, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /* 
     * disclosure: assistance of AI was used in these functions for inserting and formatting forms 
     * which were not ever used in class before
     */

    private void onAdd() 
    {
        JTextField termField = new JTextField(20);
        JTextField defField = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.add(new JLabel("Main term:"));
        form.add(termField);
        form.add(new JLabel("Definition:"));
        form.add(defField);

        int result = JOptionPane.showConfirmDialog(
                this, form, "Add Flashcard", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) return;

        String term = termField.getText().trim();
        String def = defField.getText().trim();
        if (term.isEmpty() || def.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Both fields are required.", "Missing data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Flashcard card = new Flashcard(term, def, subject.getName());
        subject.addFlashcard(card);
        MODEL.addElement(card);
        flashcardList.setSelectedValue(card, true);
    }

    // almost copy + paste of onAdd()
    private void onEdit() 
    {
        Flashcard selected = flashcardList.getSelectedValue();
        if (selected == null) return;

        JTextField termField = new JTextField(selected.getMainKey(), 20);
        JTextField defField = new JTextField(selected.getDefinition(), 20);

        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.add(new JLabel("Main term:"));
        form.add(termField);
        form.add(new JLabel("Definition:"));
        form.add(defField);

        int result = JOptionPane.showConfirmDialog(
                this, form, "Edit Flashcard", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) return;

        String term = termField.getText().trim();
        String def = defField.getText().trim();
        if (term.isEmpty() || def.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Both fields are required.", "Missing data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        selected.setMainKey(term);
        selected.setDefinition(def);
        flashcardList.repaint();
    }

    private void onRemove() 
    {
        Flashcard selected = flashcardList.getSelectedValue();
        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove this flashcard?\n" + selected.getMainKey(),
                "Confirm",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (confirm == JOptionPane.OK_OPTION) 
        {
            subject.removeFlashcard(selected);
            MODEL.removeElement(selected);
        }
    }
}
