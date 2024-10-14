import java.util.Scanner;
public class Main {
    //Constants
    private static final char PLAYER = 'P';
    private static final char EXIT = 'E';
    private static final char TREASURE = 'T';
    private static final char STAIRS = 'S';
    private static final char LOOPER = 'L';
    private static final char ZIGZAGGER = 'Z';
    private static final char EMPTY = '.';
    private static final int MAX_ROOMS = 100;
    // private static final int MIN_ROOMS = 4; no sense in use, at least for now
    private static final String LEFT_MOVE = "left";
    private static final String RIGHT_MOVE = "right";
    private static final String ENEMY_LOOPER = "looper";
    private static final String QUIT = "quit";
    private static final String ENEMY_ZIGZAGGER = "zigzagger";
    //Constant strings
    private static final String playerStatus = "Player: level %d, room %d, treasures %d%n";
    private static final String enemyStatus = "Level %d enemy: %s, room %d%n";
    private static final String INVALID_COMMAND = "Invalid command%n";
    private static final String GAME_OVER = "The game is over%n";
    private static final String NOT_GAME_OVER = "The game was not over yet%n";
    private static final String GAME_WIN = "You win the game!";
    private static final String GAME_LOST = "You lost the game!%n";
    private static final String GAME_WIN_GOODBYE ="Goodbye: You win the game!";
    private static final String GAME_LOOSE_GOODBYE ="Goodbye: You lost the game!";
    //Constant variables
    private static char[] layoutDungeonLevel1;
    private static char[] layoutDungeonLevel2;
    private static int playerPosition;
    private static int exitPosition;
    private static int stairsPositionLevel2;
    private static int stairsPositionLevel1;
    private static int playerLevel;

    private static int looperPositionLevel1 = -1;
    private static int zigzaggerPositionLevel1 = -1;
    private static int looperPositionLevel2 = -1;
    private static int zigzaggerPositionLevel2 = -1;
    private static int stepLevel1 = 1; //Made for zigzagger cals
    private static int stepLevel2 = 1;
    private static int treasureCount = 0;
    private static int treasureCollected = 0;

    private static boolean gameOver = false;

    //Initialising of game
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        readDungeon(in);
        playDungeon(in);
        in.close();
    }

    // Read and initialise dungeon levels
    private static void readDungeon(Scanner scanner) {
        layoutDungeonLevel1 = new char[MAX_ROOMS];
        layoutDungeonLevel2 = new char[MAX_ROOMS];

        String level1 = scanner.nextLine();
        String level2 = scanner.nextLine();

        layoutDungeonLevel1 = level1.toCharArray();
        layoutDungeonLevel2 = level2.toCharArray();

        processDungeonLevel(layoutDungeonLevel1, 1);
        processDungeonLevel(layoutDungeonLevel2, 2);
    }

    //Processing level elements and setting initial positions
    private static void processDungeonLevel(char[] layout, int level) {
        for (int i = 0; i < layout.length; i++) {
            switch (layout[i]) {
                case TREASURE:
                    treasureCount++;
                    break;
                case PLAYER:
                    playerPosition = i;
                    playerLevel = level;
                    break;
                case EXIT:
                    exitPosition = i;
                    break;
                case LOOPER:
                    if (level == 1) looperPositionLevel1 = i;
                    else looperPositionLevel2 = i;
                    break;
                case ZIGZAGGER:
                    if (level == 1) zigzaggerPositionLevel1 = i;
                    else zigzaggerPositionLevel2 = i;
                    break;
                case STAIRS:
                    if (level == 1) stairsPositionLevel1 = i;
                    else stairsPositionLevel2 = i;
                    break;
            }
        }
    }

    //Main game loop for processing user commands
    private static void playDungeon(Scanner in) {
        while(true){
            String command = readCommand(in);
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
        }
    }

    //Checking input on validity, to prevent double input + quit conditions
    private static String readCommand(Scanner in) {
        String command = in.next();
        if (gameOver && !command.equals(QUIT)) {
            System.out.printf(GAME_OVER);
            in.nextLine();
            return readCommand(in);
        }
        if (!command.equals(QUIT) && !command.equals(LEFT_MOVE) && !command.equals(RIGHT_MOVE)) {
            in.nextLine();
        }
        return command;
    }

    // Processing game logic for movement of player && enemies
    private static void handleGameLogic(String dir, Scanner scanner){
        if (gameOver){
            System.out.printf(GAME_OVER);
            return;
        }

        int steps = scanner.nextInt();
        movementPlayer(dir, steps);
        movementZigzagger();
        movementLooper();

        if(enemyCollision()){
            System.out.printf(GAME_LOST);
            gameOver = true;
        }else if(gameEnd()){
            System.out.println(GAME_WIN);
            gameOver = true;
        }else{
            gameStatus();
        }
    }

    //Quit command processing
    private static void handleQuit(){
        if (!gameOver){
            System.out.printf(NOT_GAME_OVER);
        }else if(gameEnd()){
            System.out.printf(GAME_WIN_GOODBYE);
        }else {
            System.out.println(GAME_LOOSE_GOODBYE);
        }
        gameOver = true;
    }

    //Main logic of player movement + checks for stairs or treasures
    private static void movementPlayer(String dir, int steps) {
        int levelLength = playerLevel == 1 ? layoutDungeonLevel1.length : layoutDungeonLevel2.length;
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

    //Main logic of looper movement
    private static void movementLooper() {
        if (looperPositionLevel1 != -1) {
            int length = layoutDungeonLevel1.length;
            looperPositionLevel1 = (looperPositionLevel1 + 1) % length;
        }
        if (looperPositionLevel2 != -1) {
            int length = layoutDungeonLevel2.length;
            looperPositionLevel2 = (looperPositionLevel2 + 1) % length;
        }
    }

    //Main logic of zigzagger movement
    private static void movementZigzagger() {
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

    //Printing status of game
    private static void gameStatus() {
        int outputPlayerLevel = 3 - playerLevel;

        System.out.printf(playerStatus, outputPlayerLevel, playerPosition + 1, treasureCollected);

        printEnemyStatus(looperPositionLevel2, ENEMY_LOOPER, 1);
        printEnemyStatus(looperPositionLevel1, ENEMY_LOOPER, 2);
        printEnemyStatus(zigzaggerPositionLevel2, ENEMY_ZIGZAGGER, 1);
        printEnemyStatus(zigzaggerPositionLevel1, ENEMY_ZIGZAGGER, 2);
    }

    //Printing enemy status
    private static void printEnemyStatus(int position, String enemyType, int level) {
        if (position >= 0) {
            System.out.printf(enemyStatus, level, enemyType, position + 1);
        }
    }

    //Checks for collision enemy with stairs
    private static void stairsCollision(int playerPos) {
        int newPos = playerPos;
        if (newPos == stairsPositionLevel1) {
            newPos = stairsPositionLevel2;
            playerLevel = 2;
        } else if (newPos == stairsPositionLevel2) {
            newPos = stairsPositionLevel1;
            playerLevel = 1;
        }
        playerPosition = newPos;
    }

    //Checks for treasure collision enemy with stairs
    private static void treasureCollision(int playerPos) {
        char[] curLevel = playerLevel == 1 ? layoutDungeonLevel1 : layoutDungeonLevel2;
        if (curLevel[playerPos] == TREASURE) {
            treasureCollected++;
            curLevel[playerPos] = EMPTY;
        }
    }

    //Checks on enemy collision with player
    private static boolean enemyCollision() {
        return (zigzaggerPositionLevel1 == playerPosition && playerLevel == 1) ||
                (zigzaggerPositionLevel2 == playerPosition && playerLevel == 2) ||
                (looperPositionLevel1 == playerPosition && playerLevel == 1) ||
                (looperPositionLevel2 == playerPosition && playerLevel == 2);
    }

    //Checks for conditions on winning game
    private static boolean gameEnd() {
        return treasureCollected == treasureCount && playerPosition == exitPosition;
    }
}