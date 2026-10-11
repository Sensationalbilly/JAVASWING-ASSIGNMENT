import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

/**
 * SwingAssignment is the main class for this PROG209 assignment.
 *
 * Inheritance:
 *   SwingAssignment extends BaseWindow, so it reuses the JFrame setup,
 *   colours, and icon drawing from the parent class.
 *
 * Constructor:
 *   The constructor below calls super(...) first, then builds the GUI.
 *
 * Event driven programming:
 *   Each button has an ActionListener that runs when the button is clicked.
 *
 *
 * Author: Mohamed Barry
 */
public class SwingAssignment extends BaseWindow {

    // Texts shown when the Change Text button is clicked
    private static final String[] LABEL_TEXTS = {
        "Welcome to Java Swing First Individual Assignment by Mohamed Barry",
        "Java Swing — Event Driven Programming",
        "PROG209 — Assignment by Mohamed Barry",
        "Hello from SwingAssignment!"
    };
    private int labelTextIndex = 0;

    // Background colours shown when the Change Background button is clicked
    private static final Color[] BG_COLORS = {
        BG_LIGHT,
        new Color(220, 245, 232),
        new Color(245, 228, 246),
        new Color(255, 240, 220)
    };
    private int bgColorIndex = 0;

    // Centre area (icon, heading, buttons, footer info)
    private JPanel centrePanel;
    private JPanel buttonRow;
    private JPanel infoRow;

    /**
     * Constructor for the child class.
     * super(...) runs the BaseWindow constructor first.
     */
    public SwingAssignment() {
        super("Java Swing Assignment");
        buildUI();
        frame.setVisible(true);
    }

    /**
     * Builds the full window layout to match the Figma wireframe:
     * navy wave header, centred content, info bar, navy wave footer.
     */
    private void buildUI() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_LIGHT);
        frame.setContentPane(panel);

        panel.add(new WaveBanner(true), BorderLayout.NORTH);

        centrePanel = buildCentre();
        panel.add(centrePanel, BorderLayout.CENTER);

        panel.add(new WaveBanner(false), BorderLayout.SOUTH);
    }

    /**
     * Creates the middle of the window: coffee icon, welcome label,
     * two action buttons, and the course / year information.
     */
    private JPanel buildCentre() {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBackground(BG_LIGHT);
        centre.setBorder(BorderFactory.createEmptyBorder(10, 50, 8, 50));

        centre.add(Box.createVerticalGlue());

        JLabel iconLabel = new JLabel(new ImageIcon(renderCentreIcon(168)));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centre.add(iconLabel);

        centre.add(Box.createVerticalStrut(28));

        // Required JLabel — wrapped in a FlowLayout panel so BoxLayout centres it properly
        label = new JLabel(formatWelcome(LABEL_TEXTS[0]), SwingConstants.CENTER);
        JPanel labelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        labelWrapper.setOpaque(false);
        labelWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelWrapper.add(label);
        centre.add(labelWrapper);

        centre.add(Box.createVerticalStrut(36));

        buttonRow = buildButtonRow();
        buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        centre.add(buttonRow);

        centre.add(Box.createVerticalGlue());

        JSeparator line = new JSeparator();
        line.setForeground(DIVIDER);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        centre.add(line);

        centre.add(Box.createVerticalStrut(14));

        infoRow = buildInfoRow();
        infoRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        centre.add(infoRow);

        centre.add(Box.createVerticalStrut(6));
        return centre;
    }

    /**
     * Formats the welcome heading so it looks like the Figma layout:
     * large bold title, smaller grey author line underneath.
     */
    private String formatWelcome(String text) {
        String title = text;
        String author = "";

        if (text.contains(" by ")) {
            int at = text.lastIndexOf(" by ");
            title = text.substring(0, at);
            author = "by " + text.substring(at + 4);
        }

        // Match the Figma two-line heading
        title = title.replace("First Individual", "First<br>Individual");

        return "<html><div style='text-align:center; width:640px;'>"
                + "<span style='font-family:Segoe UI; font-size:22px; font-weight:bold; color:#12264E;'>"
                + title
                + "</span>"
                + (author.isEmpty()
                    ? ""
                    : "<br><span style='font-family:Segoe UI; font-size:14px; font-weight:normal; color:#697691;'>"
                        + author + "</span>")
                + "</div></html>";
    }

    /** Draws the large circular coffee icon used in the centre of the window. */
    private BufferedImage renderCentreIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawCoffeeBadge(g, 0, 0, size);
        g.dispose();
        return img;
    }

    /**
     * Creates the two event-driven buttons from the wireframe.
     */
    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 22, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(700, 58));

        RoundedButton changeTextBtn = new RoundedButton("Change Text", BTN_BLUE, true);
        changeTextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Event: Change Text was clicked → update the JLabel
                labelTextIndex = (labelTextIndex + 1) % LABEL_TEXTS.length;
                label.setText(formatWelcome(LABEL_TEXTS[labelTextIndex]));
            }
        });

        RoundedButton changeBgBtn = new RoundedButton("Change Background", BTN_PURPLE, false);
        changeBgBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Event: Change Background was clicked → update the JPanel colour
                bgColorIndex = (bgColorIndex + 1) % BG_COLORS.length;
                applyBackground(BG_COLORS[bgColorIndex]);
            }
        });

        row.add(changeTextBtn);
        row.add(changeBgBtn);
        return row;
    }

    /**
     * Footer information from the Figma design, drawn on the light background
     * (not on top of the navy wave).
     */
    private JPanel buildInfoRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        left.add(new JLabel(new CapIcon()));
        JLabel course = new JLabel("PROG209 – Event Driven Programming");
        course.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        course.setForeground(TEXT_DARK);
        left.add(course);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(new JLabel(new CalendarIcon()));
        JLabel year = new JLabel("2026/2027");
        year.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        year.setForeground(TEXT_DARK);
        right.add(year);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    /** Updates the content background after the Change Background click. */
    private void applyBackground(Color color) {
        panel.setBackground(color);
        centrePanel.setBackground(color);
        for (Component c : centrePanel.getComponents()) {
            if (c instanceof JPanel) ((JPanel) c).setBackground(color);
        }
        panel.repaint();
    }

    // ── Wave header / footer ─────────────────────────────────────────────

    /**
     * Custom panel that paints the navy S-curve from the Figma wireframe.
     * header = true  → wave at the top
     * header = false → wave at the bottom
     */
    private class WaveBanner extends JPanel {
        private final boolean header;

        WaveBanner(boolean header) {
            this.header = header;
            setOpaque(false);
            setPreferredSize(new Dimension(800, header ? 128 : 96));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int w = getWidth();
            int h = getHeight();

            Path2D navy = new Path2D.Double();
            Path2D accent = new Path2D.Double();

            if (header) {
                // Smooth S-curve: lower on the left, rising toward the right
                navy.moveTo(0, 0);
                navy.lineTo(w, 0);
                navy.lineTo(w, h * 0.36);
                navy.curveTo(w * 0.78, h * 0.08, w * 0.52, h * 0.98, w * 0.26, h * 0.62);
                navy.curveTo(w * 0.12, h * 0.42, w * 0.05, h * 0.82, 0, h * 0.74);
                navy.closePath();

                accent.moveTo(0, h * 0.74 + 7);
                accent.curveTo(w * 0.05, h * 0.82 + 7, w * 0.12, h * 0.42 + 7, w * 0.26, h * 0.62 + 7);
                accent.curveTo(w * 0.52, h * 0.98 + 7, w * 0.78, h * 0.08 + 7, w, h * 0.36 + 7);
            } else {
                // Mirrored footer wave
                navy.moveTo(0, h);
                navy.lineTo(w, h);
                navy.lineTo(w, h * 0.42);
                navy.curveTo(w * 0.78, h * 0.12, w * 0.52, h * 1.05, w * 0.28, h * 0.38);
                navy.curveTo(w * 0.14, h * 0.08, w * 0.06, h * 0.55, 0, h * 0.34);
                navy.closePath();

                accent.moveTo(0, h * 0.34 - 7);
                accent.curveTo(w * 0.06, h * 0.55 - 7, w * 0.14, h * 0.08 - 7, w * 0.28, h * 0.38 - 7);
                accent.curveTo(w * 0.52, h * 1.05 - 7, w * 0.78, h * 0.12 - 7, w, h * 0.42 - 7);
            }

            g2.setColor(NAVY);
            g2.fill(navy);

            g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(WAVE_ACCENT);
            g2.draw(accent);

            g2.setStroke(new BasicStroke(1.8f));
            g2.setColor(NAVY.darker());
            g2.draw(accent);

            g2.dispose();
        }
    }

    // ── Pill-shaped button ───────────────────────────────────────────────

    /**
     * Rounded / pill button that matches the Figma buttons.
     * The pencil or circle mark is painted beside the label.
     */
    private class RoundedButton extends JButton {
        private final Color baseColor;
        private final boolean pencilIcon;
        private Color currentColor;

        RoundedButton(String text, Color color, boolean pencilIcon) {
            super(text);
            this.baseColor = color;
            this.currentColor = color;
            this.pencilIcon = pencilIcon;

            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(230, 48));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    currentColor = baseColor.brighter();
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    currentColor = baseColor;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = getHeight();
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            g2.setColor(Color.WHITE);
            int iconX = 22;
            int cy = getHeight() / 2;

            if (pencilIcon) {
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(iconX, cy + 6, iconX + 12, cy - 6);
                g2.fillPolygon(
                        new int[]{iconX + 12, iconX + 16, iconX + 14},
                        new int[]{cy - 6, cy - 8, cy - 3},
                        3);
            } else {
                g2.fill(new Ellipse2D.Double(iconX, cy - 6, 12, 12));
            }

            FontMetrics fm = g2.getFontMetrics(getFont());
            int textX = iconX + 26;
            int textY = cy + fm.getAscent() / 2 - 2;
            g2.setFont(getFont());
            g2.drawString(getText(), textX, textY);
            g2.dispose();
        }
    }

    // ── Small footer icons (avoids missing emoji boxes on Windows) ───────

    private static class CapIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(18, 38, 78));
            g2.fillPolygon(new int[]{x + 1, x + 9, x + 17}, new int[]{y + 8, y + 2, y + 8}, 3);
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawLine(x + 9, y + 8, x + 9, y + 15);
            g2.drawArc(x + 4, y + 8, 10, 6, 0, -180);
            g2.dispose();
        }

        @Override public int getIconWidth() { return 18; }
        @Override public int getIconHeight() { return 16; }
    }

    private static class CalendarIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(18, 38, 78));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x + 1, y + 4, 14, 11, 3, 3);
            g2.drawLine(x + 5, y + 2, x + 5, y + 6);
            g2.drawLine(x + 11, y + 2, x + 11, y + 6);
            g2.drawLine(x + 3, y + 8, x + 13, y + 8);
            g2.dispose();
        }

        @Override public int getIconWidth() { return 18; }
        @Override public int getIconHeight() { return 16; }
    }

    /**
     * Starts the application on the Swing event-dispatch thread.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingAssignment();
            }
        });
    }
}
