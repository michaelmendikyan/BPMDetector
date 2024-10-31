import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BPMDetector extends JFrame {
    private ArrayList<Long> tapTimestamps;
    private JLabel bpmLabel;
    private Timer idleTimer;
    private final int IDLE_TIMEOUT = 15000;

    public BPMDetector() {
        tapTimestamps = new ArrayList<>();
        bpmLabel = new JLabel("BPM: 0", SwingConstants.CENTER);
        bpmLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        setLayout(new BorderLayout());
        add(bpmLabel, BorderLayout.CENTER);
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    long currentTime = System.currentTimeMillis();
                    tapTimestamps.add(currentTime);
                    calculateBPM();
                    resetIdleTimer();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        idleTimer = new Timer(IDLE_TIMEOUT, e -> resetTaps());
        idleTimer.setRepeats(false);
    }

    private void calculateBPM() {
        if (tapTimestamps.size() < 2) {
            return;
        }

        long totalInterval = 0;
        for (int i = 1; i < tapTimestamps.size(); i++) {
            totalInterval += (tapTimestamps.get(i) - tapTimestamps.get(i - 1));
        }

        double averageInterval = totalInterval / (tapTimestamps.size() - 1);
        double bpm = 60000.0 / averageInterval;

        bpmLabel.setText(String.format("BPM: %.2f", bpm));
    }

    private void resetTaps() {
        tapTimestamps.clear();
        bpmLabel.setText("BPM: 0");
    }

    private void resetIdleTimer() {
        idleTimer.restart();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BPMDetector::new);
    }
}
