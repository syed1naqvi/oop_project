/*
 * disclosure: assistance of AI and internet were used, as allowed by the professor, in some swing and GUI parts due to using
 * elements we did not encounter before or use in class. core object oriented programming functionality was written ourselves. 
 * for anything that we used the assistance of AI or internet in, we made sure to fully understand all the code and libraries, 
 * along with rewriting the code with what we learnt.
 */



package Main;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class StudyGUI extends JFrame {
    private final StudySession session;

    // Label for the term at the top of of the card
    private final JLabel termLabel   = new JLabel("", SwingConstants.CENTER);
    private final JTextArea defArea  = new JTextArea();
    // footer progress (ie "Card 2 / 7")
    private final JLabel footerLabel = new JLabel("", SwingConstants.LEFT);

    // Controls
    private final JToggleButton shuffleToggle = new JToggleButton("Shuffle");
    private final JButton reshuffleBtn = new JButton("Reshuffle");
    private final JButton prevBtn = new JButton("Previous");
    private final JButton nextBtn = new JButton("Next");

    public StudyGUI(List<Flashcard> cards) {
        super("Study");
        this.session = new StudySession(cards);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(12,12));

        // header term specs
        termLabel.setFont(termLabel.getFont().deriveFont(Font.BOLD, 24f));
        termLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        add(termLabel, BorderLayout.NORTH);

        // middle of card specs
        defArea.setEditable(false);
        defArea.setLineWrap(true);
        defArea.setWrapStyleWord(true);
        defArea.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        add(new JScrollPane(defArea), BorderLayout.CENTER);

        // footer progress and buttons
        JPanel south = new JPanel(new BorderLayout());
        footerLabel.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        south.add(footerLabel, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        buttons.add(prevBtn);
        buttons.add(nextBtn);
        buttons.add(shuffleToggle);
        buttons.add(reshuffleBtn);
        south.add(buttons, BorderLayout.CENTER);

        add(south, BorderLayout.SOUTH);

        // go to previous card
        prevBtn.addActionListener(e -> { session.previous(); refresh(); });
        // go to next card
        nextBtn.addActionListener(e -> { session.next(); refresh(); });
        shuffleToggle.addActionListener(e -> {
            boolean on = shuffleToggle.isSelected();
            session.setShuffleEnabled(on);
            shuffleToggle.setText(on ? "Unshuffle" : "Shuffle");
            refresh();
        });
        // initial render
        reshuffleBtn.addActionListener(e -> { session.reshuffle(); refresh(); });

        setSize(560, 440);
        setLocationRelativeTo(null);
        setVisible(true);

        refresh();
    }

    private void refresh() {
        Flashcard c = session.current();
        if (c == null) {
            termLabel.setText("No cards in this subject.");
            defArea.setText("");
            footerLabel.setText("");
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            shuffleToggle.setEnabled(false);
            reshuffleBtn.setEnabled(false);
            return;
        }

        // show current card
        termLabel.setText(c.getMainKey());
        defArea.setText(c.getDefinition());

        // footer 
        int total = session.size();
        int pos1  = session.position() + 1;      // 1-based for display
        footerLabel.setText(String.format("Card %d / %d", pos1, total));

        // only allow if not at the first card
        prevBtn.setEnabled(session.position() > 0);
        // only allow if not at the final card
        nextBtn.setEnabled(session.position() < total - 1);

        // shuffle is allowed only when there are cards
        shuffleToggle.setEnabled(true);
        reshuffleBtn.setEnabled(true);
    }
}
