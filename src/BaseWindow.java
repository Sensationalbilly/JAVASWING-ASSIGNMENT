import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * BaseWindow is the parent / base class for the application.
 *
 * It does not create the JFrame itself — SwingAssignment (the child class)
 * declares and instantiates frame/panel/label directly, as the assignment
 * asks. BaseWindow instead holds what the two classes share: the window
 * title/author passed up through the constructor, the colour palette, and
 * the drawing/colour utilities reused by the child.
 *
 * SwingAssignment inherits from this class (inheritance) and reaches it
 * through super(...) (constructor chaining).
 */
public class BaseWindow {

    protected final String windowTitle;
    protected final String authorName;

    // Colour palette taken from the Figma wireframe
    protected static final Color BG_LIGHT     = new Color(236, 241, 248);
    protected static final Color NAVY         = new Color(24, 72, 122);
    protected static final Color WAVE_ACCENT  = new Color(168, 204, 232);
    protected static final Color CIRCLE_NAVY  = new Color(22, 58, 112);
    protected static final Color TEXT_DARK    = new Color(18, 38, 78);
    protected static final Color TEXT_MUTED   = new Color(105, 118, 145);
    protected static final Color TEXT_ON_DARK = new Color(255, 255, 255);
    protected static final Color BTN_BLUE     = new Color(20, 140, 245);
    protected static final Color BTN_PURPLE   = new Color(132, 48, 196);
    protected static final Color DIVIDER      = new Color(200, 210, 222);
    protected static final Color CUP_CREAM    = new Color(248, 248, 250);
    protected static final Color COFFEE_BROWN = new Color(92, 52, 28);
    protected static final Color STEAM_RED    = new Color(210, 32, 38);

    private static final File ICON_FILE = new File("assets/icon.png");

    /**
     * Constructor: stores the shared title/author used by the child class.
     *
     * @param windowTitle text the child class will give the JFrame
     * @param authorName  name shown in the welcome label
     */
    public BaseWindow(String windowTitle, String authorName) {
        this.windowTitle = windowTitle;
        this.authorName = authorName;
    }

    /**
     * Loads the application icon from assets/icon.png using ImageIcon.
     * The icon is only drawn and written to disk if that file is missing —
     * a real icon ships with the project, so normal runs just load it.
     */
    protected ImageIcon loadApplicationIcon() {
        if (!ICON_FILE.exists()) {
            File parent = ICON_FILE.getParentFile();
            if (parent != null) parent.mkdirs();
            try {
                ImageIO.write(renderBadge(64), "png", ICON_FILE);
            } catch (IOException e) {
                System.err.println("Could not create a default icon: " + e.getMessage());
            }
        }
        return new ImageIcon(ICON_FILE.getAbsolutePath());
    }

    /**
     * Renders the circular coffee badge at the given size.
     * Used for both the window icon and the header badge.
     */
    protected BufferedImage renderBadge(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawCoffeeBadge(g, 0, 0, size);
        g.dispose();
        return img;
    }

    /** Shared drawing routine for the circular coffee badge. */
    private void drawCoffeeBadge(Graphics2D g, int x, int y, int size) {
        g.setColor(CIRCLE_NAVY);
        g.fill(new Ellipse2D.Double(x, y, size, size));

        int cupW = (int) (size * 0.42);
        int cupH = (int) (size * 0.30);
        int cupX = x + (size - cupW) / 2 - 2;
        int cupY = y + (int) (size * 0.50);

        // Steam
        g.setColor(STEAM_RED);
        g.setStroke(new BasicStroke(Math.max(2f, size / 18f),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int[] steamXs = {
            cupX + cupW / 4,
            cupX + cupW / 2,
            cupX + (int) (cupW * 0.78)
        };
        for (int i = 0; i < steamXs.length; i++) {
            int sx = steamXs[i];
            GeneralPath steam = new GeneralPath();
            int base = cupY - 4;
            int top = y + (int) (size * 0.18);
            int mid = (base + top) / 2;
            int dir = (i == 1) ? -1 : 1;
            steam.moveTo(sx, base);
            steam.curveTo(sx + dir * 8, mid + 6, sx - dir * 8, mid - 6, sx, top);
            g.draw(steam);
        }

        // Cup body
        g.setColor(CUP_CREAM);
        g.fill(new RoundRectangle2D.Double(cupX, cupY, cupW, cupH, 8, 8));

        // Coffee surface
        g.setColor(COFFEE_BROWN);
        g.fillOval(cupX + 4, cupY + 3, cupW - 8, cupH / 3);

        // Handle
        g.setStroke(new BasicStroke(Math.max(2.5f, size / 16f),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(CUP_CREAM);
        g.drawArc(cupX + cupW - 4, cupY + 4, (int) (size * 0.18), cupH - 8, 270, 180);

        // Saucer
        g.setStroke(new BasicStroke(Math.max(2f, size / 22f)));
        g.drawLine(cupX - 4, cupY + cupH + 3, cupX + cupW + 6, cupY + cupH + 3);
    }

    /** Linear-interpolates between two colours; t is clamped to [0, 1]. */
    protected static Color blend(Color from, Color to, float t) {
        float clamped = Math.max(0f, Math.min(1f, t));
        int r = Math.round(from.getRed()   + (to.getRed()   - from.getRed())   * clamped);
        int g = Math.round(from.getGreen() + (to.getGreen() - from.getGreen()) * clamped);
        int b = Math.round(from.getBlue()  + (to.getBlue()  - from.getBlue())  * clamped);
        return new Color(r, g, b);
    }

    /** True if a colour is dark enough that white text reads better on it than navy. */
    protected static boolean isDark(Color c) {
        double luminance = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255.0;
        return luminance < 0.55;
    }
}
