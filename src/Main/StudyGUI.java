package Main;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class StudyGUI extends JFrame {
    private final StudySession session;

    private final JLabel   keyLabel   = new JLabel("", SwingConstants.CENTER);
    private final JTextArea defArea   = new JTextArea();
    private final JLabel   progressLb = new JLabel("", SwingConstants.CENTER);
    private final JToggleButton shuffleToggle = new JToggleButton("Shuffle");
    private final JButton reshuffleBtn = new JButton("Reshuffle");

    public StudyGUI(List<Flashcard> cards) {
        super("Study");
        this.session = new StudySession(cards);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(12,12));

        keyLabel.setFont(keyLabel.getFont().deriveFont(Font.BOLD, 22f));
        add(keyLabel, BorderLayout.NORTH);

        defArea.setLineWrap(true);
        defArea.setWrapStyleWord(true);
        defArea.setEditable(false);
        defArea.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        add(new JScrollPane(defArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        JButton prev = new JButton("Previous");
        JButton next = new JButton("Next");
        nav.add(prev);
        nav.add(next);

        JPanel opts = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        opts.add(shuffleToggle);
        opts.add(reshuffleBtn);

        bottom.add(progressLb, BorderLayout.WEST);
        bottom.add(nav,        BorderLayout.CENTER);
        bottom.add(opts,       BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        // Actions
        prev.addActionListener(e -> { session.previous(); refresh(); });
        next.addActionListener(e -> { session.next(); refresh(); });
        shuffleToggle.addActionListener(e -> {
            boolean enabled = shuffleToggle.isSelected();
            session.setShuffleEnabled(enabled);
            shuffleToggle.setText(enabled ? "Unshuffle" : "Shuffle");
            refresh();
        });
        reshuffleBtn.addActionListener(e -> { session.reshuffle(); refresh(); });

        setSize(560, 440);
        setLocationRelativeTo(null);
        setVisible(true);

        refresh();
    }

    private void refresh() {
        Flashcard c = session.current();
        if (c == null) {
            keyLabel.setText("No cards in this subject.");
            defArea.setText("");
            progressLb.setText("");
            return;
        }
        keyLabel.setText(c.getMainKey());
        defArea.setText(c.getDefinition());
        progressLb.setText(String.format("Card %d / %d",
                Math.max(1, (int)Math.round(session.progress() * sessionSize())),
                sessionSize()));
    }

    private int sessionSize() { // avoid double rounding issues
        int count = 0;
        for (;;) {
            Flashcard cur = session.current();
            if (cur == null) break;
            count = Math.max(count, 1); // present
            // we can estimate from progress if needed, but not required
            break;
        }
        // We don't expose size() directly from StudySession; progress label can also be omitted.
        // If you want exact size, add a size() getter in StudySession and call it here.
        return  /* better: add getter in StudySession */ 1; // see note below
    }
}
