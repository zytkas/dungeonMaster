/**
        * @author Danylo Zhdanov NOVA FCT 24/25
*/
    import javax.swing.*;
    import java.util.Scanner;

    public class Main {
        public static void main(String[] args) {
            Game game = new Game();
            Scanner in = new Scanner(System.in);

            // Считываем макет подземелья из консоли
            System.out.println("Введите макет для уровня 2:");
            game.layoutDungeonLevel2 = in.nextLine().toCharArray();

            System.out.println("Введите макет для уровня 1:");
            game.layoutDungeonLevel1 = in.nextLine().toCharArray();

            // Обрабатываем макет подземелья
            game.processDungeonLevel(game.layoutDungeonLevel2, 2);
            game.processDungeonLevel(game.layoutDungeonLevel1, 1);

            // Запускаем GUI
            SwingUtilities.invokeLater(() -> new GameGUI(game));
        }
    }

    class Game {
        // Constants
        static final char PLAYER = 'P';
        static final char EXIT = 'E';
        static final char TREASURE = 'T';
        static final char STAIRS = 'S';
        static final char LOOPER = 'L';
        static final char ZIGZAGGER = 'Z';
        static final char EMPTY = '.';
        final int MAX_ROOMS = 100;
        final String LEFT_MOVE = "left";
        final String RIGHT_MOVE = "right";
        final String ENEMY_LOOPER = "looper";
        final String QUIT = "quit";
        final String ENEMY_ZIGZAGGER = "zigzagger";

        // Constant strings
        final String playerStatus = "Player: level %d, room %d, treasures %d%n";
        final String enemyStatus = "Level %d enemy: %s, room %d%n";
        final String INVALID_COMMAND = "Invalid command%n";
        final String GAME_OVER = "The game is over%n";
        final String NOT_GAME_OVER = "The game was not over yet!%n";
        final String GAME_WIN = "You won the game!";
        final String GAME_LOST = "You lost the game!%n";
        final String GAME_WIN_GOODBYE ="Goodbye: You won the game!%n";
        final String GAME_LOOSE_GOODBYE ="Goodbye: You lost the game!";

        // Global variables
        char[] layoutDungeonLevel1;
        char[] layoutDungeonLevel2;
        int playerPosition;
        int exitPosition;
        int stairsPositionLevel2;
        int stairsPositionLevel1;
        int playerLevel;
        int looperPositionLevel1 = -1;
        int zigzaggerPositionLevel1 = -1;
        int looperPositionLevel2 = -1;
        int zigzaggerPositionLevel2 = -1;
        int stepLevel1 = 1;
        int stepLevel2 = 1;
        int treasureCount = 0;
        int treasureCollected = 0;
        int exitPositionLevel1;
        int exitPositionLevel2;
        boolean gameOver = false;


        /**
         * Processes the dungeon layout for a given level.
         * Initializes player, enemy, treasure, exit, and stairs positions.
         * @param layout The char array representing the dungeon layout
         * @param level The current dungeon level (1 or 2)
         */
        void processDungeonLevel(char[] layout, int level) {
            for (int i = 0; i < layout.length; i++) {
                switch (layout[i]) {
                    case TREASURE:
                        treasureCount++; break;
                    case PLAYER:
                        playerPosition = i;
                        playerLevel = level;
                        break;
                    case EXIT:
                        exitPosition = i;
                        if (level == 1) exitPositionLevel1 = i;
                        else exitPositionLevel2 = i; break;
                    case LOOPER:
                        if (level == 1) looperPositionLevel1 = i;
                        else looperPositionLevel2 = i; break;
                    case ZIGZAGGER:
                        if (level == 1) zigzaggerPositionLevel1 = i;
                        else zigzaggerPositionLevel2 = i; break;
                    case STAIRS:
                        if (level == 1) stairsPositionLevel1 = i;
                        else stairsPositionLevel2 = i; break;
                }
            }
        }

        /**
         * Handles player movement in the specified direction for the given number of steps.
         * Checks for collisions with treasures and stairs after movement.
         * @param dir The direction of movement ("left" or "right")
         * @param steps The number of steps to move
         */
        void movementPlayer(String dir, int steps) {
            int levelLength = playerLevel == 1 ?
                    layoutDungeonLevel1.length :
                    layoutDungeonLevel2.length;
            int newPos = playerPosition;

            if (dir.equals(LEFT_MOVE)) {
                newPos = Math.max(0, playerPosition - steps);
            } else if (dir.equals(RIGHT_MOVE)) {
                newPos = Math.min(levelLength - 1, playerPosition + steps);
            }
            playerPosition = newPos;

            treasureCollision(playerPosition);
            stairsCollision(playerPosition);
        }

        /**
         * Checks if the player has collided with stairs and updates the player's position and
         * level accordingly.
         * @param playerPos The current player position
         */
        void stairsCollision(int playerPos) {
            int newPos = playerPos;
            if (newPos == stairsPositionLevel2 && playerLevel == 2) {
                newPos = stairsPositionLevel1;
                playerLevel = 1;
            } else if (newPos == stairsPositionLevel1 && playerLevel == 1) {
                newPos = stairsPositionLevel2;
                playerLevel = 2;
            }
            playerPosition = newPos;
        }

        /**
         * Checks if the player has collided with a treasure.
         * If so, collects the treasure and updates the dungeon layout.
         * @param playerPos The current player position
         */
        void treasureCollision(int playerPos) {
            char[] curLevel = playerLevel == 1 ? layoutDungeonLevel1 : layoutDungeonLevel2;
            if (curLevel[playerPos] == TREASURE) {
                treasureCollected++;
                curLevel[playerPos] = EMPTY;
            }
        }

        /**
         * Moves the Looper enemy on both levels.
         * The Looper moves one position to the right, wrapping around to the start if it reaches
         * the end.
         */
        void movementLooper() {
            if (looperPositionLevel1 != -1) {
                int length = layoutDungeonLevel1.length;
                looperPositionLevel1 = (looperPositionLevel1 + 1) % length;
            }
            if (looperPositionLevel2 != -1) {
                int length = layoutDungeonLevel2.length;
                looperPositionLevel2 = (looperPositionLevel2 + 1) % length;
            }
        }

        /**
         * Moves the Zigzagger enemy on both levels.
         * The Zigzagger moves in a pattern of 1 to 5 steps, cycling through this pattern.
         */
        void movementZigzagger() {
            if (zigzaggerPositionLevel1 != -1) {
                int length = layoutDungeonLevel1.length;
                zigzaggerPositionLevel1 = (zigzaggerPositionLevel1 + stepLevel1) % length;
                stepLevel1 = (stepLevel1 % 5) + 1;
            }

            if (zigzaggerPositionLevel2 != -1) {
                int length = layoutDungeonLevel2.length;
                zigzaggerPositionLevel2 = (zigzaggerPositionLevel2 + stepLevel2) % length;
                stepLevel2 = (stepLevel2 % 5) + 1;
            }
        }

        /**
         * Checks if the player has collided with any enemy on the current level.
         * @return true if there's a collision, false otherwise
         */
        boolean isEnemyCollision() {
            return (zigzaggerPositionLevel1 == playerPosition && playerLevel == 1) ||
                    (zigzaggerPositionLevel2 == playerPosition && playerLevel == 2) ||
                    (looperPositionLevel1 == playerPosition && playerLevel == 1) ||
                    (looperPositionLevel2 == playerPosition && playerLevel == 2);
        }

        /**
         * Checks if the game has ended (player has collected all treasures and reached the exit).
         * @return true if the game has ended, false otherwise
         */
        boolean isGameEnd() {
            return treasureCollected == treasureCount &&
                    playerPosition == exitPosition &&
                    ((playerLevel == 1 && playerPosition == exitPositionLevel1) ||
                            (playerLevel == 2 && playerPosition == exitPositionLevel2));
        }

        /**
         * Reads the dungeon layout for both levels from user input.
         * @param scanner scans arrays from user input
         */
        void readDungeon(Scanner scanner) {
            layoutDungeonLevel2 = new char[MAX_ROOMS];
            layoutDungeonLevel1 = new char[MAX_ROOMS];

            String level2 = scanner.nextLine();
            String level1 = scanner.nextLine();

            layoutDungeonLevel2 = level2.toCharArray();
            layoutDungeonLevel1 = level1.toCharArray();

            processDungeonLevel(layoutDungeonLevel2, 2);
            processDungeonLevel(layoutDungeonLevel1, 1);
        }

        /**
         * Main game loop that processes user commands and updates game state.
         * @param in scans command from the user input
         */
        void playDungeon(Scanner in) {
            while(true){
                String command = in.next();
                switch(command){
                    case QUIT:
                        handleQuit();
                        return;
                    case LEFT_MOVE, RIGHT_MOVE:
                        handleGameLogic(command, in);
                        break;
                    default:
                        System.out.printf(INVALID_COMMAND);
                        break;
                }
                in.nextLine();
            }
        }

        /**
         * Handles the game logic for player movement, enemy movement, and collision checks.
         * @param dir The direction of player movement from user input
         * @param scanner scans N steps from user input
         */
        void handleGameLogic(String dir, Scanner scanner){
            if (gameOver){
                System.out.printf(GAME_OVER);
                return;
            }

            int steps = scanner.nextInt();
            movementPlayer(dir, steps);
            movementZigzagger();
            movementLooper();

            if(isEnemyCollision()){
                System.out.printf(GAME_LOST);
                gameOver = true;
            }else if(isGameEnd()){
                System.out.println(GAME_WIN);
                gameOver = true;
            }else{
                gameStatus();
            }
        }

        /**
         * Prints the current game status, including player and enemy positions.
         */
        void gameStatus() {

            System.out.printf(playerStatus, playerLevel, playerPosition + 1, treasureCollected);

            printEnemyStatus(looperPositionLevel1, ENEMY_LOOPER, 1);
            printEnemyStatus(zigzaggerPositionLevel1, ENEMY_ZIGZAGGER, 1);
            printEnemyStatus(looperPositionLevel2, ENEMY_LOOPER, 2);
            printEnemyStatus(zigzaggerPositionLevel2, ENEMY_ZIGZAGGER, 2);
        }

        /**
         * Prints the status of a specific enemy if it exists on the given level.
         * in other words, which argument not -1
         * @param position The position of the enemy
         * @param enemyType The type of the enemy ("looper" or "zigzagger"), which are constants
         * @param level The current dungeon level, which is basically static number
         */
        void printEnemyStatus(int position, String enemyType, int level) {
            if (position >= 0) {
                System.out.printf(enemyStatus, level, enemyType, position + 1);
            }
        }

        /**
         * Handles the quit command, determining the game outcome and printing appropriate messages.
         */
        void handleQuit(){
            if (!gameOver){
                System.out.printf(NOT_GAME_OVER);
            }else if(isGameEnd() && !isEnemyCollision()){
                System.out.printf(GAME_WIN_GOODBYE);
            }else {
                System.out.println(GAME_LOOSE_GOODBYE);
            }
            gameOver = true;
        }

        void main() {
            Scanner in = new Scanner(System.in);
            readDungeon(in);
            playDungeon(in);
            in.close();
        }

    }