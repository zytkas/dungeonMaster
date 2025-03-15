import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

/**
 * GameGUI - Graphical User Interface for the Dungeon Master game
 * @author Danylo Zhdanov NOVA FCT 24/25
 */
public class GameGUI extends JFrame {
    public static final String TITLE_GAME = "Dungeon Master";
    public static final String INIT_CONSTANT = "Player: Level 1, Room 1, Treasures 0";
    public static final String LABEL_STEPS = "Steps:";
    public static final String MOVE_LEFT = "left";
    public static final String MOVE_RIGHT = "right";
    public static final String LABEL_QUIT = "Quit";
    public static final String LABEL_RIGHT = "Right";
    public static final String LABEL_LEFT = "Left";
    public static final String GAME_OVER_MSG = "Game Over!";
    public static final String BE_A_POSITIVE_NUMBER = "Steps must be a positive number!";
    public static final String LOST_THE_GAME = "You lost the game!";
    public static final String WON_THE_GAME = "You won the game!";
    public static final String VALID_NUMBER_OF_STEPS = "Please enter a valid number of steps!";
    public static final String ZIGZAGGER_ROOM_1 = "Level 1 enemy: zigzagger, room %d<br>";
    public static final String LOOPER_ROOM_1 = "Level 1 enemy: looper, room %d<br>";
    public static final String LOOPER_ROOM_2 = "Level 2 enemy: looper, room %d<br>";
    public static final String ZIGZAGGER_ROOM_2 = "Level 2 enemy: zigzagger, room %d<br>";
    private final Game game;
    private JPanel dungeonPanel;
    private JLabel statusLabel;
    private JLabel enemyStatusLabel;
    private JTextField stepsField;
    private JButton leftButton;
    private JButton rightButton;

    // Constants for UI
    private static final int CELL_SIZE = 30;
    private static final int PANEL_HEIGHT = 120;
    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);
    private static final Color PLAYER_COLOR = new Color(0, 200, 0);
    private static final Color ENEMY_COLOR = new Color(200, 0, 0);
    private static final Color TREASURE_COLOR = new Color(255, 215, 0);
    private static final Color EXIT_COLOR = new Color(0, 128, 255);
    private static final Color STAIRS_COLOR = new Color(139, 69, 19);
    private static final Color EMPTY_COLOR = new Color(100, 100, 100);

    public GameGUI(Game game) {
        this.game = game;
        setupUI();
        updateUI();
    }

    /**
     * Sets up the UI components
     */
    private void setupUI() {
        setTitle(TITLE_GAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Calculate width based on the longest dungeon level
        int maxLength = Math.max(
                game.layoutDungeonLevel1.length,
                game.layoutDungeonLevel2.length
        );
        int width = maxLength * CELL_SIZE;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Dungeon visualization panel
        dungeonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawDungeon(g);
            }
        };
        dungeonPanel.setPreferredSize(new Dimension(width, PANEL_HEIGHT * 2));
        dungeonPanel.setBackground(BACKGROUND_COLOR);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(BACKGROUND_COLOR);

        statusLabel = new JLabel(INIT_CONSTANT);
        statusLabel.setForeground(Color.WHITE);

        enemyStatusLabel = new JLabel("");
        enemyStatusLabel.setForeground(Color.WHITE);

        statusPanel.add(statusLabel);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statusPanel.add(enemyStatusLabel);

        // Controls panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());
        controlsPanel.setBackground(BACKGROUND_COLOR);

        JLabel stepsLabel = new JLabel(LABEL_STEPS);
        stepsLabel.setForeground(Color.WHITE);

        stepsField = new JTextField("1", 3);

        leftButton = new JButton(LABEL_LEFT);
        leftButton.addActionListener(e -> movePlayer(MOVE_LEFT));

        rightButton = new JButton(LABEL_RIGHT);
        rightButton.addActionListener(e -> movePlayer(MOVE_RIGHT));

        JButton quitButton = new JButton(LABEL_QUIT);
        quitButton.addActionListener(e -> handleQuit());

        controlsPanel.add(stepsLabel);
        controlsPanel.add(stepsField);
        controlsPanel.add(leftButton);
        controlsPanel.add(rightButton);
        controlsPanel.add(quitButton);

        // Add panels to the main layout
        mainPanel.add(dungeonPanel, BorderLayout.NORTH);
        mainPanel.add(statusPanel, BorderLayout.CENTER);
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);

        // Set up the frame
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Draws both dungeon levels
     * @param g Graphics context
     */
    private void drawDungeon(Graphics g) {
        // Draw level 2 (top)
        drawLevel(g, game.layoutDungeonLevel2, 2, 0);

        // Draw level 1 (bottom)
        drawLevel(g, game.layoutDungeonLevel1, 1, PANEL_HEIGHT);
    }

    /**
     * Draws a single dungeon level
     * @param g Graphics context
     * @param layout The dungeon layout as a char array
     * @param level The level number (1 or 2)
     * @param yOffset The vertical offset for drawing
     */
    private void drawLevel(Graphics g, char[] layout, int level, int yOffset) {
        // Draw level label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Level " + level, 10, yOffset + 20);

        // Draw cells
        for (int i = 0; i < layout.length; i++) {
            int x = i * CELL_SIZE;
            int y = yOffset + 30;

            // Fill the cell
            g.setColor(getCellColor(layout[i]));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            // Draw cell border
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            // Draw cell content
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(layout[i]), x + CELL_SIZE/2 - 4, y + CELL_SIZE/2 + 4);
        }

        // Draw player if on this level
        if (game.playerLevel == level) {
            int x = game.playerPosition * CELL_SIZE;
            int y = yOffset + 30;

            g.setColor(PLAYER_COLOR);
            g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }

        // Draw looper if on this level
        int looperPosition = (level == 1) ? game.looperPositionLevel1 : game.looperPositionLevel2;
        if (looperPosition != -1) {
            int x = looperPosition * CELL_SIZE;
            int y = yOffset + 30;

            g.setColor(ENEMY_COLOR);
            g.fillRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            g.setColor(Color.WHITE);
            g.drawString("L", x + CELL_SIZE/2 - 4, y + CELL_SIZE/2 + 4);
        }

        // Draw zigzagger if on this level
        int zigzaggerPosition = (level == 1) ? game.zigzaggerPositionLevel1 : game.zigzaggerPositionLevel2;
        if (zigzaggerPosition != -1) {
            int x = zigzaggerPosition * CELL_SIZE;
            int y = yOffset + 30;

            g.setColor(ENEMY_COLOR);
            int[] xPoints = {x + CELL_SIZE/2, x + CELL_SIZE - 5, x + 5};
            int[] yPoints = {y + 5, y + CELL_SIZE - 5, y + CELL_SIZE - 5};
            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(Color.WHITE);
            g.drawString("Z", x + CELL_SIZE/2 - 4, y + CELL_SIZE/2 + 4);
        }
    }

    /**
     * Gets the color for a cell based on its type
     * @param cell The cell character
     * @return The corresponding color
     */
    private Color getCellColor(char cell) {
        return switch (cell) {
            case Game.PLAYER -> PLAYER_COLOR;
            case Game.EXIT -> EXIT_COLOR;
            case Game.TREASURE -> TREASURE_COLOR;
            case Game.STAIRS -> STAIRS_COLOR;
            case Game.LOOPER, Game.ZIGZAGGER -> ENEMY_COLOR;
            default -> EMPTY_COLOR;
        };
    }

    /**
     * Handles player movement
     * @param direction The direction to move ("left" or "right")
     */
    private void movePlayer(String direction) {
        if (game.gameOver) {
            JOptionPane.showMessageDialog(this, GAME_OVER_MSG);
            return;
        }

        try {
            int steps = Integer.parseInt(stepsField.getText());
            if (steps <= 0) {
                JOptionPane.showMessageDialog(this, BE_A_POSITIVE_NUMBER);
                return;
            }

            game.movementPlayer(direction, steps);
            game.movementZigzagger();
            game.movementLooper();

            if (game.isEnemyCollision()) {
                JOptionPane.showMessageDialog(this, LOST_THE_GAME);
                game.gameOver = true;
            } else if (game.isGameEnd()) {
                JOptionPane.showMessageDialog(this, WON_THE_GAME);
                game.gameOver = true;
            }

            updateUI();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, VALID_NUMBER_OF_STEPS);
        }
    }

    /**
     * Handles the quit button action
     */
    private void handleQuit() {
        game.handleQuit();
        System.exit(0);
    }

    /**
     * Updates the UI to reflect the current game state
     */
    private void updateUI() {
        // Update status label
        statusLabel.setText(String.format("Player: Level %d, Room %d, Treasures %d",
                game.playerLevel, game.playerPosition + 1, game.treasureCollected));

        // Update enemy status
        StringBuilder enemyStatus = new StringBuilder("<html>");

        if (game.looperPositionLevel1 != -1) {
            enemyStatus.append(String.format(LOOPER_ROOM_1, game.looperPositionLevel1 + 1));
        }
        if (game.zigzaggerPositionLevel1 != -1) {
            enemyStatus.append(String.format(ZIGZAGGER_ROOM_1, game.zigzaggerPositionLevel1 + 1));
        }
        if (game.looperPositionLevel2 != -1) {
            enemyStatus.append(String.format(LOOPER_ROOM_2, game.looperPositionLevel2 + 1));
        }
        if (game.zigzaggerPositionLevel2 != -1) {
            enemyStatus.append(String.format(ZIGZAGGER_ROOM_2, game.zigzaggerPositionLevel2 + 1));
        }

        enemyStatus.append("</html>");
        enemyStatusLabel.setText(enemyStatus.toString());

        // Update buttons status based on game state
        leftButton.setEnabled(!game.gameOver);
        rightButton.setEnabled(!game.gameOver);

        // Repaint the dungeon
        dungeonPanel.repaint();
    }
}