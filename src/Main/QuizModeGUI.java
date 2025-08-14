package Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizModeGUI extends JFrame {

    private final SubjectService service;

    private final JLabel subjectLabel = new JLabel("Quiz Mode", SwingConstants.CENTER);
    private final JLabel questionLabel = new JLabel("Question will appear here", SwingConstants.CENTER);
    private final JLabel questionNumberLabel = new JLabel("Question 0/0", SwingConstants.LEFT);
    private final JLabel feedbackLabel = new JLabel(" ", SwingConstants.CENTER);

    private final JButton optionA = new JButton("A");
    private final JButton optionB = new JButton("B");
    private final JButton optionC = new JButton("C");
    private final JButton optionD = new JButton("D");

    private final JButton nextBtn = new JButton("Next");
    private final JButton finishBtn = new JButton("Finish");

    private List<Flashcard> quizPool = new ArrayList<>();
    private int currentIndex = -1;
    private int correctCount = 0;

    public QuizModeGUI(SubjectService service) {
        super("Quiz Mode - Multiple Choice");
        this.service = service;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(12,12));

        subjectLabel.setFont(subjectLabel.getFont().deriveFont(Font.BOLD, 18f));
        subjectLabel.setBorder(new EmptyBorder(10,10,10,10));
        add(subjectLabel, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        questionLabel.setFont(questionLabel.getFont().deriveFont(Font.BOLD, 20f));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(questionLabel);
        center.add(Box.createVerticalStrut(12));

        JPanel options = new JPanel(new GridLayout(2,2,12,12));
        options.setBorder(new EmptyBorder(12,12,12,12));
        options.add(optionA);
        options.add(optionB);
        options.add(optionC);
        options.add(optionD);
        center.add(options);
        center.add(Box.createVerticalStrut(6));

        feedbackLabel.setForeground(new Color(0,128,0));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(feedbackLabel);

        add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        questionNumberLabel.setBorder(new EmptyBorder(0,12,0,0));
        south.add(questionNumberLabel);
        south.add(Box.createHorizontalStrut(24));
        south.add(nextBtn);
        south.add(finishBtn);
        add(south, BorderLayout.SOUTH);

        // actions
        optionA.addActionListener(e -> onAnswer(optionA.getText()));
        optionB.addActionListener(e -> onAnswer(optionB.getText()));
        optionC.addActionListener(e -> onAnswer(optionC.getText()));
        optionD.addActionListener(e -> onAnswer(optionD.getText()));

        nextBtn.addActionListener(e -> loadNext());
        finishBtn.addActionListener(e -> dispose());

        // build quiz pool
        if (!showSubjectSelection()) {
            dispose();
            return;
        }

        setSize(680, 520);
        setLocationRelativeTo(null);
        setVisible(true);

        loadNext();
    }

    private boolean showSubjectSelection() {
        ListModel<Subject> model = service.getModel();
        if (model.getSize() == 0) {
            JOptionPane.showMessageDialog(this,
                "No subjects available. Please create subjects and add flashcards first.",
                "No Subjects",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        List<JCheckBox> checks = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            Subject s = model.getElementAt(i);
            JCheckBox cb = new JCheckBox(s.getName() + " (" + s.getFlashcards().size() + " flashcards)");
            cb.putClientProperty("subject", s);
            checks.add(cb);
            panel.add(cb);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(380, 260));

        int result = JOptionPane.showConfirmDialog(this,
            scrollPane,
            "Select Subjects for Quiz",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return false;

        quizPool.clear();
        for (JCheckBox cb : checks) {
            if (cb.isSelected()) {
                Subject s = (Subject) cb.getClientProperty("subject");
                quizPool.addAll(s.getFlashcards());
            }
        }
        if (quizPool.size() < 4) {
            JOptionPane.showMessageDialog(this,
                "Need at least 4 total flashcards across selected subjects.",
                "Not Enough Flashcards",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        subjectLabel.setText("Selected subjects: " + selectedSubjectsText(checks));
        Collections.shuffle(quizPool);
        currentIndex = -1;
        correctCount = 0;
        return true;
    }

    private String selectedSubjectsText(List<JCheckBox> checks) {
        StringBuilder sb = new StringBuilder();
        for (JCheckBox cb : checks) if (cb.isSelected()) {
            if (sb.length() > 0) sb.append(", ");
            String label = cb.getText();
            int idx = label.indexOf(" (");
            sb.append(idx > 0 ? label.substring(0, idx) : label);
        }
        return sb.toString();
    }

    private void loadNext() {
        currentIndex++;
        if (currentIndex >= quizPool.size()) {
            questionLabel.setText("Done! Score: " + correctCount + "/" + quizPool.size());
            feedbackLabel.setText(" ");
            optionA.setEnabled(false);
            optionB.setEnabled(false);
            optionC.setEnabled(false);
            optionD.setEnabled(false);
            nextBtn.setEnabled(false);
            return;
        }
        Flashcard q = quizPool.get(currentIndex);
        questionLabel.setText(q.getMainKey());
        questionNumberLabel.setText("Question " + (currentIndex + 1) + " / " + quizPool.size());

        // build choices (correct + 3 distractors)
        List<String> choices = new ArrayList<>();
        choices.add(q.getDefinition());
        int i = 0;
        for (Flashcard f : quizPool) {
            if (choices.size() == 4) break;
            if (f == q) continue;
            String def = f.getDefinition();
            if (!choices.contains(def)) choices.add(def);
            i++;
        }
        while (choices.size() < 4) choices.add("N/A");
        Collections.shuffle(choices);

        optionA.setText(choices.get(0));
        optionB.setText(choices.get(1));
        optionC.setText(choices.get(2));
        optionD.setText(choices.get(3));

        feedbackLabel.setText(" ");
        optionA.setEnabled(true);
        optionB.setEnabled(true);
        optionC.setEnabled(true);
        optionD.setEnabled(true);
        nextBtn.setEnabled(true);
    }

    private void onAnswer(String chosen) {
        if (currentIndex < 0 || currentIndex >= quizPool.size()) return;
        String correct = quizPool.get(currentIndex).getDefinition();
        boolean ok = correct.equals(chosen);
        if (ok) {
            correctCount++;
            feedbackLabel.setForeground(new Color(0,128,0));
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setForeground(new Color(180,0,0));
            feedbackLabel.setText("Incorrect. Correct answer: " + correct);
        }
        optionA.setEnabled(false);
        optionB.setEnabled(false);
        optionC.setEnabled(false);
        optionD.setEnabled(false);
    }
}
