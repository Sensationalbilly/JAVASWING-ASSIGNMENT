import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * SwingAssignment is the main class for this PROG209 assignment.
 *
 * Inheritance:
 *   SwingAssignment extends BaseWindow, so it reuses the colour palette,
 *   icon loading, and colour utilities from the parent class.
 *
 * Constructor chaining:
 *   SwingAssignment() -> SwingAssignment(String) -> super(String, String)
 *
 * Event driven programming:
 *   Buttons, links, a text field, and keyboard shortcuts each wire an
 *   ActionListener that runs when the matching event fires.
 *
 * Author: Mohamed Barry
 */
public class SwingAssignment extends BaseWindow {

    // Required by the assignment: created and instantiated here, not inherited
    private JFrame frame;
    private JPanel panel;
    private JLabel label;

    // Texts cycled by the Change Text button
    private static final String[] LABEL_TEXTS = {
        "Welcome to Java Swing First Individual Assignment by Mohamed Barry",
        "Java Swing — Event Driven Programming by Mohamed Barry",
        "PROG209 — Assignment by Mohamed Barry",
        "Hello from SwingAssignment! by Mohamed Barry"
    };
    private int labelTextIndex = 0;

    // Background colours cycled by the Change Background button
    private static final Color[] BG_COLORS = {
        BG_LIGHT,
        new Color(220, 245, 232),
        new Color(245, 228, 246),
        new Color(255, 240, 220)
    };
    private int bgColorIndex = 0;

    private String currentLabelText;
    private Color currentBgColor = BG_LIGHT;
    private Timer fadeTimer;
    private int eventCount = 0;

    private JPanel contentPanel;
    private JPanel footerPanel;
    private JTextField messageField;
    private JLabel statusLabel;

    /** No-arg constructor chains to the named-author constructor. */
    public SwingAssignment() {
        this("Mohamed Barry");
    }

    /** Named-author constructor chains up to BaseWindow via super(...). */
    public SwingAssignment(String authorName) {
        super("Java Swing Assignment", authorName);
        currentLabelText = LABEL_TEXTS[0];
        initFrame();
        buildUI();
        frame.setVisible(true);
    }

    /** Creates and configures the JFrame required by the assignment. */
    private void initFrame() {
        frame = new JFrame(windowTitle);
        frame.setIconImage(loadApplicationIcon().getImage());
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    /**
     * Builds the window to match the Figma wireframe:
     * header wave + badge, centred title/author/buttons, footer info + wave.
     */
    private void buildUI() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(currentBgColor);
        frame.add(panel);

        panel.add(buildHeader(), BorderLayout.NORTH);

        contentPanel = buildContent();
        panel.add(contentPanel, BorderLayout.CENTER);

        footerPanel = buildFooter();
        panel.add(footerPanel, BorderLayout.SOUTH);
    }

    /** Header: navy wave ribbon only (logo lives in content so it is never clipped). */
    private JPanel buildHeader() {
        return new WaveBanner(true);
    }

    /**
     * Centre area: logo + title + message on top, buttons locked in the middle
     * so BoxLayout can never squash them away.
     */
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(currentBgColor);

        // ── Top: logo, welcome label, message field ──────────────────────
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(10, 48, 0, 48));

        JLabel badge = new JLabel(new ImageIcon(renderBadge(112)));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        top.add(badge);
        top.add(Box.createVerticalStrut(16));

        // Required JLabel — full welcome text, styled to match the wireframe
        label = new JLabel(formatWelcome(currentLabelText), SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        // Tall enough for title + full author line (avoids clipping "Mohamed Barry")
        label.setPreferredSize(new Dimension(700, 110));
        label.setMinimumSize(new Dimension(500, 110));
        label.setMaximumSize(new Dimension(700, 110));
        top.add(label);

        top.add(Box.createVerticalStrut(28));
        top.add(buildMessageRow());

        content.add(top, BorderLayout.NORTH);

        // ── Middle: buttons + links + status (always visible) ────────────
        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.setOpaque(false);
        middle.setBorder(BorderFactory.createEmptyBorder(20, 48, 8, 48));

        middle.add(buildButtonRow());
        middle.add(Box.createVerticalStrut(14));
        middle.add(buildLinkRow());
        middle.add(Box.createVerticalStrut(10));
        middle.add(buildStatusRow());
        middle.add(Box.createVerticalGlue());

        content.add(middle, BorderLayout.CENTER);
        return content;
    }

    /** Footer: divider, course/year row, bottom wave. */
    private JPanel buildFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(currentBgColor);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JSeparator line = new JSeparator();
        line.setForeground(DIVIDER);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        footer.add(line);

        footer.add(Box.createVerticalStrut(12));
        footer.add(buildInfoRow());
        footer.add(Box.createVerticalStrut(8));
        footer.add(new WaveBanner(false));
        return footer;
    }

    /** Text box: type a custom message, press Enter to show it in the label. */
    private JPanel buildMessageRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel hint = new JLabel("Type a message and press Enter:");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(TEXT_MUTED);

        messageField = new JTextField(22);
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageField.setPreferredSize(new Dimension(260, 30));
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 215), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        messageField.addActionListener(e -> {
            String typed = messageField.getText().trim();
            if (!typed.isEmpty()) {
                currentLabelText = typed.contains(" by ")
                        ? typed
                        : typed + " by " + authorName;
                refreshLabel();
                recordEvent("Custom message shown");
            }
        });

        row.add(hint);
        row.add(messageField);
        return row;
    }

    /** Change Text / Change Background — equal size, centred, evenly spaced. */
    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setPreferredSize(new Dimension(700, 56));
        row.setMinimumSize(new Dimension(500, 56));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        Dimension buttonSize = new Dimension(240, 50);

        RoundedButton changeTextBtn = new RoundedButton("Change Text", BTN_BLUE, true);
        changeTextBtn.setPreferredSize(buttonSize);
        changeTextBtn.setMinimumSize(buttonSize);
        changeTextBtn.setMaximumSize(buttonSize);
        changeTextBtn.setMnemonic(KeyEvent.VK_T);
        changeTextBtn.addActionListener((ActionEvent e) -> {
            labelTextIndex = (labelTextIndex + 1) % LABEL_TEXTS.length;
            currentLabelText = LABEL_TEXTS[labelTextIndex];
            refreshLabel();
            recordEvent("Change Text clicked");
        });

        RoundedButton changeBgBtn = new RoundedButton("Change Background", BTN_PURPLE, false);
        changeBgBtn.setPreferredSize(buttonSize);
        changeBgBtn.setMinimumSize(buttonSize);
        changeBgBtn.setMaximumSize(buttonSize);
        changeBgBtn.setMnemonic(KeyEvent.VK_B);
        changeBgBtn.addActionListener((ActionEvent e) -> {
            bgColorIndex = (bgColorIndex + 1) % BG_COLORS.length;
            animateBackgroundTo(BG_COLORS[bgColorIndex]);
            recordEvent("Change Background clicked");
        });

        row.add(changeTextBtn);
        row.add(changeBgBtn);
        return row;
    }

    /** "Pick a custom colour..." and "Reset" links, centred under the buttons. */
    private JPanel buildLinkRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 28, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        LinkButton pickColorLink = new LinkButton("Pick a custom colour…");
        pickColorLink.setMnemonic(KeyEvent.VK_P);
        pickColorLink.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(frame, "Choose a background colour", currentBgColor);
            if (chosen != null) {
                animateBackgroundTo(chosen);
                recordEvent("Custom colour picked");
            }
        });

        LinkButton resetLink = new LinkButton("Reset");
        resetLink.setMnemonic(KeyEvent.VK_R);
        resetLink.addActionListener(e -> {
            messageField.setText("");
            labelTextIndex = 0;
            bgColorIndex = 0;
            currentLabelText = LABEL_TEXTS[0];
            refreshLabel();
            animateBackgroundTo(BG_LIGHT);
            recordEvent("Reset clicked");
        });

        row.add(pickColorLink);
        row.add(resetLink);
        return row;
    }

    private JPanel buildStatusRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(TEXT_MUTED);
        updateStatusLabel();

        row.add(statusLabel);
        return row;
    }

    /** Footer information: course code (left) and academic year (right). */
    private JPanel buildInfoRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

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

    /** Re-renders the label using colours that fit the current background. */
    private void refreshLabel() {
        label.setText(formatWelcome(currentLabelText));
    }

    /**
     * Formats the required welcome label: bold title + author name under it,
     * matching the Figma wireframe alignment.
     */
    private String formatWelcome(String text) {
        String title = text;
        String author = authorName;

        if (text.contains(" by ")) {
            int at = text.lastIndexOf(" by ");
            title = text.substring(0, at);
            author = text.substring(at + 4).trim();
        }

        boolean dark = isDark(currentBgColor);
        String titleColor = dark ? "#FFFFFF" : "#12264E";
        String authorColor = dark ? "#E6E9F2" : "#697691";

        return "<html><div style='text-align:center;'>"
                + "<span style='font-family:Segoe UI; font-size:24px; font-weight:bold; color:"
                + titleColor + ";'>"
                + title
                + "</span><br>"
                + "<span style='font-family:Segoe UI; font-size:16px; font-weight:normal; color:"
                + authorColor + ";'>"
                + author
                + "</span></div></html>";
    }

    /** Smoothly fades panel/content/footer from the current colour to the target. */
    private void animateBackgroundTo(Color target) {
        if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();

        final Color start = currentBgColor;
        final int steps = 20;
        final int[] step = {0};

        currentBgColor = target;
        refreshLabel();

        fadeTimer = new Timer(15, e -> {
            step[0]++;
            float t = step[0] / (float) steps;
            Color blended = blend(start, target, t);
            panel.setBackground(blended);
            contentPanel.setBackground(blended);
            footerPanel.setBackground(blended);
            if (step[0] >= steps) {
                ((Timer) e.getSource()).stop();
            }
        });
        fadeTimer.start();
    }

    private void recordEvent(String description) {
        eventCount++;
        updateStatusLabel();
    }

    private void updateStatusLabel() {
        statusLabel.setText("Events handled: " + eventCount
                + "   |   Shortcuts: Alt+T, Alt+B, Alt+P, Alt+R");
    }

    // ── Wave header / footer ─────────────────────────────────────────────

    /** Custom panel that paints the navy S-curve from the Figma wireframe. */
    private class WaveBanner extends JPanel {
        private final boolean header;

        WaveBanner(boolean header) {
            this.header = header;
            setOpaque(false);
            int h = header ? 110 : 96;
            setPreferredSize(new Dimension(800, h));
            setMinimumSize(new Dimension(800, h));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
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

    /** Rounded / pill button with hover and press colour feedback. */
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
            setMargin(new Insets(0, 0, 0, 0));

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

                @Override
                public void mousePressed(MouseEvent e) {
                    currentColor = baseColor.darker();
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    currentColor = contains(e.getPoint()) ? baseColor.brighter() : baseColor;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int arc = getHeight();
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            FontMetrics fm = g2.getFontMetrics(getFont());
            int iconGap = 10;
            int iconW = 16;
            int textW = fm.stringWidth(getText());
            int totalW = iconW + iconGap + textW;
            int startX = (getWidth() - totalW) / 2;
            int cy = getHeight() / 2;

            g2.setColor(Color.WHITE);
            if (pencilIcon) {
                int ix = startX + 2;
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(ix, cy + 6, ix + 12, cy - 6);
                g2.fillPolygon(
                        new int[]{ix + 12, ix + 16, ix + 14},
                        new int[]{cy - 6, cy - 8, cy - 3},
                        3);
            } else {
                g2.fill(new Ellipse2D.Double(startX + 2, cy - 6, 12, 12));
            }

            int textX = startX + iconW + iconGap;
            int textY = cy + (fm.getAscent() - fm.getDescent()) / 2;
            g2.setFont(getFont());
            g2.drawString(getText(), textX, textY);
            g2.dispose();
        }
    }

    // ── Link-styled button (Pick a custom colour / Reset) ────────────────

    private static class LinkButton extends JButton {
        LinkButton(String text) {
            super("<html><u>" + text + "</u></html>");
            setForeground(new Color(20, 110, 220));
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    /** Starts the application on the Swing event-dispatch thread. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingAssignment::new);
    }
}
