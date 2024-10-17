import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.main();
    }
}

class Game {

    // Constants
    final char PLAYER = 'P';
    final char EXIT = 'E';
    final char TREASURE = 'T';
    final char STAIRS = 'S';
    final char LOOPER = 'L';
    final char ZIGZAGGER = 'Z';
    final char EMPTY = '.';
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


    // Main function and game initialization
    void main() {
        Scanner in = new Scanner(System.in);
        readDungeon(in);
        playDungeon(in);
        in.close();
    }

    // Input and command processing
    void readDungeon(Scanner scanner) {
        layoutDungeonLevel1 = new char[MAX_ROOMS];
        layoutDungeonLevel2 = new char[MAX_ROOMS];

        String level1 = scanner.nextLine();
        String level2 = scanner.nextLine();

        layoutDungeonLevel1 = level1.toCharArray();
        layoutDungeonLevel2 = level2.toCharArray();

        processDungeonLevel(layoutDungeonLevel1, 1);
        processDungeonLevel(layoutDungeonLevel2, 2);
    }

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

    void handleQuit(){
        if (!gameOver){
            System.out.printf(NOT_GAME_OVER);
        }else if(isGameEnd() && !enemyCollision()){
            System.out.printf(GAME_WIN_GOODBYE);
        }else {
            System.out.println(GAME_LOOSE_GOODBYE);
        }
        gameOver = true;
    }

    // Game logic
    void handleGameLogic(String dir, Scanner scanner){
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
        }else if(isGameEnd()){
            System.out.println(GAME_WIN);
            gameOver = true;
        }else{
            gameStatus();
        }
    }

    boolean isGameEnd() {
        return treasureCollected == treasureCount &&
                playerPosition == exitPosition &&
                ((playerLevel == 1 && playerPosition == exitPositionLevel1) ||
                (playerLevel == 2 && playerPosition == exitPositionLevel2));
    }

    // Player movement
    // Moving player in N direction, on N steps + checks on stairs/treasure
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
    //Collision player with stairs
    // Here is all understandable, if player at the same position with stairs, just changing theirs positions
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
    //Collision player with treasure. Here I get all array of the current level, based on playerLevel, cuz there is multiple of coins
    //and save all position of them make no sense.
    void treasureCollision(int playerPos) {
        char[] curLevel = playerLevel == 1 ? layoutDungeonLevel1 : layoutDungeonLevel2;
        if (curLevel[playerPos] == TREASURE) {
            treasureCollected++;
            curLevel[playerPos] = EMPTY;
        }
    }

    // Enemy movement
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

    boolean enemyCollision() {
        return (zigzaggerPositionLevel1 == playerPosition && playerLevel == 1) ||
                (zigzaggerPositionLevel2 == playerPosition && playerLevel == 2) ||
                (looperPositionLevel1 == playerPosition && playerLevel == 1) ||
                (looperPositionLevel2 == playerPosition && playerLevel == 2);
    }

    // Game status and output
    void gameStatus() {
        int outputLevel = 3 - playerLevel;

        System.out.printf(playerStatus, outputLevel, playerPosition + 1, treasureCollected);

        printEnemyStatus(looperPositionLevel2, ENEMY_LOOPER, 1);
        printEnemyStatus(zigzaggerPositionLevel2, ENEMY_ZIGZAGGER, 1);
        printEnemyStatus(looperPositionLevel1, ENEMY_LOOPER, 2);
        printEnemyStatus(zigzaggerPositionLevel1, ENEMY_ZIGZAGGER, 2);
    }
    // Checks for enemy which is alive, in other words  which is identifier is not -1
    void printEnemyStatus(int position, String enemyType, int level) {
        if (position >= 0) {
            System.out.printf(enemyStatus, level, enemyType, position + 1);
        }
    }
}