package Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizModeGUI extends JFrame {
    
    private JLabel subjectLabel, questionLabel, questionNumberLabel, feedbackLabel;
    private JButton optionA, optionB, optionC, optionD;
    private JButton nextBtn, finishBtn;
    private JPanel mainPanel, questionPanel, optionsPanel, navigationPanel;
    private JProgressBar progressBar;
    
    private List<Subject> selectedSubjects;
    private List<Flashcard> allFlashcards;
    private List<Flashcard> quizFlashcards;
    private int currentQuestionIndex;
    private int correctAnswers;
    private int incorrectAnswers;
    private boolean hasAnswered;
    private Flashcard currentCard;
    private String correctAnswer;
    
    public QuizModeGUI() {
        super("Quiz Mode - Multiple Choice");
        selectedSubjects = new ArrayList<>();
        allFlashcards = new ArrayList<>();
        quizFlashcards = new ArrayList<>();
        currentQuestionIndex = 0;
        correctAnswers = 0;
        incorrectAnswers = 0;
        hasAnswered = false;
        
        initializeComponents();
        layoutComponents();
        addListeners();
        
        // Start by showing subject selection
        if (!showSubjectSelection()) {
            // If subject selection failed, don't show the window
            dispose();
            return;
        }
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 600);  // Increased window size
        setLocationRelativeTo(null);
        
        // Ensure window appears on top
        setVisible(true);
        toFront();
        requestFocus();
    }
    
    private void initializeComponents() {
        // Labels
        subjectLabel = new JLabel("", SwingConstants.CENTER);
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        questionNumberLabel = new JLabel("", SwingConstants.CENTER);
        questionNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        questionLabel.setPreferredSize(new Dimension(700, 80));  // Set preferred size for question label
        
        feedbackLabel = new JLabel("", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16));
        feedbackLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Option buttons
        optionA = new JButton();
        optionB = new JButton();
        optionC = new JButton();
        optionD = new JButton();
        
        Font optionFont = new Font("Arial", Font.PLAIN, 14);
        Dimension optionSize = new Dimension(650, 50);  // Increased width for options
        
        JButton[] options = {optionA, optionB, optionC, optionD};
        for (JButton btn : options) {
            btn.setFont(optionFont);
            btn.setPreferredSize(optionSize);
            btn.setMaximumSize(optionSize);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
        }
        
        // Navigation buttons
        nextBtn = new JButton("Next Question");
        finishBtn = new JButton("Finish Quiz");
        nextBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        finishBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        nextBtn.setEnabled(false);
        finishBtn.setVisible(false);
        
        // Progress bar - make it bigger
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(650, 35));  // Much bigger progress bar
        progressBar.setFont(new Font("Arial", Font.BOLD, 14));  // Bigger, bold font for percentage
    }
    
    private void layoutComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Top panel with subject and question number
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(subjectLabel, BorderLayout.NORTH);
        topPanel.add(questionNumberLabel, BorderLayout.CENTER);
        
        // Add some spacing before progress bar
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(new EmptyBorder(10, 0, 0, 0));  // Add spacing above progress bar
        progressPanel.add(progressBar, BorderLayout.CENTER);
        topPanel.add(progressPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Question panel
        questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.CENTER);
        questionPanel.add(feedbackLabel, BorderLayout.SOUTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        
        // Options panel
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        optionA.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionB.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionC.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionD.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        optionsPanel.add(optionA);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(optionB);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(optionC);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(optionD);
        
        mainPanel.add(optionsPanel, BorderLayout.SOUTH);
        
        // Navigation panel
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navigationPanel.add(nextBtn);
        navigationPanel.add(finishBtn);
        
        add(mainPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }
    
    private void addListeners() {
        optionA.addActionListener(e -> checkAnswer("A", optionA));
        optionB.addActionListener(e -> checkAnswer("B", optionB));
        optionC.addActionListener(e -> checkAnswer("C", optionC));
        optionD.addActionListener(e -> checkAnswer("D", optionD));
        
        nextBtn.addActionListener(e -> nextQuestion());
        finishBtn.addActionListener(e -> showResults());
    }
    
    private boolean showSubjectSelection() {
        // Get all subjects from SubjectManager
        DefaultListModel<Subject> model = SubjectManager.getModel();
        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No subjects available. Please create subjects and add flashcards first.",
                "No Subjects",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Create checkbox list for subject selection
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        
        List<JCheckBox> checkBoxes = new ArrayList<>();
        for (int i = 0; i < model.size(); i++) {
            Subject subject = model.getElementAt(i);
            int flashcardCount = subject.getFlashcards().size();
            JCheckBox checkBox = new JCheckBox(subject.getName() + " (" + flashcardCount + " flashcards)");
            checkBox.putClientProperty("subject", subject);
            checkBoxes.add(checkBox);
            checkBoxPanel.add(checkBox);
        }
        
        JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        
        int result = JOptionPane.showConfirmDialog(this,
            scrollPane,
            "Select Subjects for Quiz",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (result != JOptionPane.OK_OPTION) {
            return false;
        }
        
        // Collect selected subjects
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                Subject subject = (Subject) checkBox.getClientProperty("subject");
                selectedSubjects.add(subject);
                allFlashcards.addAll(subject.getFlashcards());
            }
        }
        
        if (selectedSubjects.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one subject for the quiz.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return showSubjectSelection();  // Recursive call to show selection again
        }
        
        // Check if we have enough flashcards from selected subjects
        if (allFlashcards.size() < 4) {
            JOptionPane.showMessageDialog(this,
                "The selected subject(s) don't have enough flashcards for a quiz.\n" +
                "You need at least 4 flashcards total.\n" +
                "Current total from selected subjects: " + allFlashcards.size() + " flashcards\n\n" +
                "Please select more subjects or add more flashcards.",
                "Not Enough Flashcards",
                JOptionPane.WARNING_MESSAGE);
            
            // Clear selections and try again
            selectedSubjects.clear();
            allFlashcards.clear();
            return false;  // Don't open the window
        }
        
        // Shuffle and prepare quiz
        Collections.shuffle(allFlashcards);
        quizFlashcards = new ArrayList<>(allFlashcards);
        
        // Update subject label
        if (selectedSubjects.size() == 1) {
            subjectLabel.setText("Subject: " + selectedSubjects.get(0).getName());
        } else {
            subjectLabel.setText("Quiz on " + selectedSubjects.size() + " subjects");
        }
        
        // Start the quiz
        loadQuestion();
        return true;  // Successfully started quiz
    }
    
    private void loadQuestion() {
        if (currentQuestionIndex >= quizFlashcards.size()) {
            showResults();
            return;
        }
        
        hasAnswered = false;
        nextBtn.setEnabled(false);
        feedbackLabel.setText("");
        
        // Reset button colors
        optionA.setBackground(null);
        optionB.setBackground(null);
        optionC.setBackground(null);
        optionD.setBackground(null);
        
        // Enable all buttons
        optionA.setEnabled(true);
        optionB.setEnabled(true);
        optionC.setEnabled(true);
        optionD.setEnabled(true);
        
        currentCard = quizFlashcards.get(currentQuestionIndex);
        correctAnswer = currentCard.getDefinition();
        
        // Update question
        questionLabel.setText("<html><center>What is the definition of:<br><b>" + 
                            currentCard.getMainKey() + "</b></center></html>");
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + 
                                   " of " + quizFlashcards.size());
        
        // Update progress bar
        progressBar.setMaximum(quizFlashcards.size());
        progressBar.setValue(currentQuestionIndex);
        
        // Create options - allow duplicate definitions
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);
        
        // Get other definitions (may include duplicates which could also be correct)
        List<String> allDefinitions = new ArrayList<>();
        for (Flashcard card : allFlashcards) {
            if (!card.equals(currentCard)) {
                allDefinitions.add(card.getDefinition());
            }
        }
        
        // Shuffle all other definitions
        Collections.shuffle(allDefinitions);
        
        // Add up to 3 more definitions (may include duplicates of the correct answer)
        int optionsNeeded = Math.min(3, allDefinitions.size());
        for (int i = 0; i < optionsNeeded; i++) {
            options.add(allDefinitions.get(i));
        }
        
        // If we still don't have 4 options (very few flashcards), duplicate some
        if (options.size() < 4 && !allDefinitions.isEmpty()) {
            while (options.size() < 4) {
                // Add random definitions from what we have
                options.add(allDefinitions.get((int)(Math.random() * allDefinitions.size())));
            }
        }
        
        // As a last resort, if we still don't have enough (shouldn't happen with 4+ cards)
        while (options.size() < 4) {
            options.add("No definition available");
        }
        
        // Shuffle options
        Collections.shuffle(options);
        
        // Set button texts
        optionA.setText("A. " + options.get(0));
        optionB.setText("B. " + options.get(1));
        optionC.setText("C. " + options.get(2));
        optionD.setText("D. " + options.get(3));
        
        // Check if this is the last question
        if (currentQuestionIndex == quizFlashcards.size() - 1) {
            nextBtn.setVisible(false);
            finishBtn.setVisible(true);
        }
    }
    
    private void checkAnswer(String option, JButton button) {
        if (hasAnswered) return;
        
        hasAnswered = true;
        String selectedAnswer = button.getText().substring(3); // Remove "A. " prefix
        
        // Disable all buttons
        optionA.setEnabled(false);
        optionB.setEnabled(false);
        optionC.setEnabled(false);
        optionD.setEnabled(false);
        
        // Check if the selected answer matches the correct definition
        // This allows for multiple correct answers if definitions are the same
        if (selectedAnswer.equals(correctAnswer)) {
            correctAnswers++;
            button.setBackground(Color.GREEN);
            feedbackLabel.setText("✓ Correct!");
            feedbackLabel.setForeground(new Color(0, 128, 0));
        } else {
            incorrectAnswers++;
            button.setBackground(Color.RED);
            feedbackLabel.setText("✗ Incorrect! The correct answer was: " + correctAnswer);
            feedbackLabel.setForeground(Color.RED);
            
            // Highlight ALL buttons with the correct answer in green
            // (in case there are multiple correct answers with same definition)
            JButton[] buttons = {optionA, optionB, optionC, optionD};
            for (JButton btn : buttons) {
                if (btn.getText().substring(3).equals(correctAnswer)) {
                    btn.setBackground(Color.GREEN);
                }
            }
        }
        
        nextBtn.setEnabled(true);
        if (currentQuestionIndex == quizFlashcards.size() - 1) {
            finishBtn.setEnabled(true);
        }
    }
    
    private void nextQuestion() {
        currentQuestionIndex++;
        loadQuestion();
    }
    
    private void showResults() {
        // Update progress bar to complete
        progressBar.setValue(progressBar.getMaximum());
        
        double percentage = (correctAnswers * 100.0) / quizFlashcards.size();
        
        String message = String.format(
            "<html><center>" +
            "<h2>Quiz Complete!</h2>" +
            "<br>" +
            "<b>Total Questions:</b> %d<br>" +
            "<b>Correct Answers:</b> %d<br>" +
            "<b>Incorrect Answers:</b> %d<br>" +
            "<b>Score:</b> %.1f%%<br>" +
            "<br>" +
            "%s" +
            "</center></html>",
            quizFlashcards.size(),
            correctAnswers,
            incorrectAnswers,
            percentage,
            getPerformanceMessage(percentage)
        );
        
        Object[] options = {"Take Quiz Again", "Back to Home"};
        int choice = JOptionPane.showOptionDialog(this,
            message,
            "Quiz Results",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[1]);
        
        if (choice == 0) {
            // Reset and start again
            dispose();
            new QuizModeGUI();
        } else {
            dispose();
        }
    }
    
    private String getPerformanceMessage(double percentage) {
        if (percentage >= 90) {
            return "<font color='green'><b>Excellent work! Outstanding performance!</b></font>";
        } else if (percentage >= 80) {
            return "<font color='green'><b>Great job! Keep it up!</b></font>";
        } else if (percentage >= 70) {
            return "<font color='blue'><b>Good effort! Room for improvement.</b></font>";
        } else if (percentage >= 60) {
            return "<font color='orange'><b>Fair performance. More practice recommended.</b></font>";
        } else {
            return "<font color='red'><b>Keep studying! You'll improve with practice.</b></font>";
        }
    }
}