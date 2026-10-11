import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * BaseWindow is the parent / base class for the application window.
 *
 * It creates the JFrame, sets the required window properties, and
 * prepares shared colours used by the child class.
 *
 * SwingAssignment inherits from this class (inheritance) and calls
 * this constructor with super(...) (constructor chaining).
 */
public class BaseWindow extends JFrame {

    // Required by the assignment: a JFrame variable named frame
    protected JFrame frame;

    // Required by the assignment: a JPanel variable named panel
    protected JPanel panel;

    // Required by the assignment: a JLabel variable named label
    protected JLabel label;

    // Colour palette taken from the Figma wireframe
    protected static final Color BG_LIGHT     = new Color(236, 241, 248);
    protected static final Color NAVY         = new Color(24, 72, 122);
    protected static final Color WAVE_ACCENT  = new Color(168, 204, 232);
    protected static final Color CIRCLE_NAVY  = new Color(22, 58, 112);
    protected static final Color TEXT_DARK    = new Color(18, 38, 78);
    protected static final Color TEXT_MUTED   = new Color(105, 118, 145);
    protected static final Color BTN_BLUE     = new Color(20, 140, 245);
    protected static final Color BTN_PURPLE   = new Color(132, 48, 196);
    protected static final Color DIVIDER      = new Color(200, 210, 222);
    protected static final Color CUP_CREAM    = new Color(248, 248, 250);
    protected static final Color COFFEE_BROWN = new Color(92, 52, 28);
    protected static final Color STEAM_RED    = new Color(210, 32, 38);

    /**
     * Constructor: sets up the main window.
     *
     * @param title text shown in the JFrame title bar
     */
    public BaseWindow(String title) {
        super(title);

        // Assignment requirements: 800 x 800 and close the app on exit
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // The assignment asks for a JFrame variable named frame
        this.frame = this;

        loadIcon();
    }

    /**
     * Loads (or creates) the application icon from the assets folder
     * using ImageIcon, then applies it to the JFrame.
     */
    private void loadIcon() {
        File iconFile = new File("assets/icon.png");
        iconFile.getParentFile().mkdirs();

        try {
            ImageIO.write(createCoffeeIcon(64), "png", iconFile);
        } catch (Exception e) {
            System.err.println("Could not save icon: " + e.getMessage());
        }

        try {
            this.setIconImage(new ImageIcon(iconFile.getAbsolutePath()).getImage());
        } catch (Exception e) {
            System.err.println("Could not load icon: " + e.getMessage());
        }
    }

    /**
     * Draws a small circular coffee-cup icon used as the window icon.
     */
    private BufferedImage createCoffeeIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawCoffeeBadge(g, 0, 0, size);
        g.dispose();
        return img;
    }

    /**
     * Shared drawing routine for the circular coffee badge.
     * Used both for the window icon and the large centre icon.
     */
    protected void drawCoffeeBadge(Graphics2D g, int x, int y, int size) {
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
}
