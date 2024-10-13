    import java.util.Scanner;

    //Constants
    private static final char PLAYER = 'P';
    private static final char EXIT = 'E';
    private static final char TREASURE = 'T';
    private static final char STAIRS = 'S';
    private static final char LOOPER = 'L';
    private static final char ZIGZAGER = 'Z';
    private static final char EMPTY = '.';
    private static final int MAX_ROOMS = 100;
    private static final int MIN_ROOMS = 4;
    private static final String LEFT_MOVE = "left";
    private static final String RIGHT_MOVE = "right";
    private static final String ENEMY_LOOPER = "looper";
    private static final String ENEMY_ZIGZAGGER = "zigzagger";
    //Constant strings ...
    private static final String playerStatus = "Player: level %d, room %d, treasures %d%n";
    private static final String enemyStatus = "Level %d enemy: %s, room %d%n";
    //Constant variables
    private static char[] layoutDungeonLevel1;
    private static char[] layoutDungeonLevel2;
    private static int playerPosition;
    private static int exitPosition;
    private static int treasureCount = 0;
    private static int treasureCollected = 0;
    private static int playerLevel;

    private static int looperPositionLevel1 = -1;
    private static int zigzagerPositionLevel1 = -1;
    private static int looperPositionLevel2 = -1;
    private static int zigzagerPositionLevel2 = -1;
    private static int stepLevel1 = 1;
    private static int stepLevel2 = 1;
    private static int stairsPositionLevel2;
    private static int stairsPositionLevel1;





    public static void main() {
        Scanner scanner = new Scanner(System.in);
        readDungeon(scanner);
        playDungeon(scanner);
        movementPlayer("right", 2);
        movementZigzagger();
        movementLooper();
        enemyCollision();
        gameStatus();
        gameEnd();
        scanner.close();
    }

    private static void readDungeon(Scanner scanner) {
        layoutDungeonLevel1 = new char[MAX_ROOMS];
        layoutDungeonLevel2 = new char[MAX_ROOMS];

        String level1 = scanner.nextLine();
        String level2 = scanner.nextLine();

        layoutDungeonLevel1 = level1.toCharArray();
        layoutDungeonLevel2 = level2.toCharArray();

        processDungeonLevel(layoutDungeonLevel1, 2);
        processDungeonLevel(layoutDungeonLevel2, 1);
    }

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
                case ZIGZAGER:
                    if (level == 1) zigzagerPositionLevel1 = i;
                    else zigzagerPositionLevel2 = i;
                    break;
                case STAIRS:
                    if (level == 1) stairsPositionLevel1 = i;
                    else stairsPositionLevel2 = i;
                    break;
            }
        }
    }
    private static void playDungeon(Scanner scanner) {

    }

    private static void movementPlayer(String dir, int steps) {

        int levelLength = playerLevel == 1 ? layoutDungeonLevel1.length : layoutDungeonLevel2.length;
        int newPos = playerPosition;

        if (dir.equals(LEFT_MOVE)) {
            newPos = Math.max(0, playerPosition - steps);
        } else if (dir.equals(RIGHT_MOVE)) {
            newPos = Math.min(levelLength, playerPosition + steps);
        }
        playerPosition = newPos;

        treasureCollision(playerPosition);
        stairsCollision(playerPosition);
    }

    private static void movementLooper(){
        if(looperPositionLevel2 != -1) {
            int length = layoutDungeonLevel1.length;
            looperPositionLevel1 = (looperPositionLevel1 + 1) % length;
        }
        if (looperPositionLevel1 != -1) {
            int length = layoutDungeonLevel2.length;
            looperPositionLevel2 = (looperPositionLevel2 + 1) % length;
        }

    }

    private static void movementZigzagger() {

        if (zigzagerPositionLevel2 != -1) {
            int length = layoutDungeonLevel1.length;
            zigzagerPositionLevel1 = (zigzagerPositionLevel1 + stepLevel1) % length;
            stepLevel1 = (stepLevel1 % 5) + 1;
        }

        if (zigzagerPositionLevel1 != -1) {
            int length = layoutDungeonLevel2.length;
            zigzagerPositionLevel2 = (zigzagerPositionLevel2 + stepLevel2) % length;
            stepLevel2 = (stepLevel2 % 5) + 1;
        }
    }

    private static void gameStatus() {
        System.out.printf(playerStatus, playerLevel, playerPosition + 1, treasureCollected);

        if (looperPositionLevel1 != -1) {
            System.out.printf(enemyStatus, 1, ENEMY_LOOPER, looperPositionLevel1 + 1);
        }
        if (looperPositionLevel2 != -1) {
            System.out.printf(enemyStatus, 2, ENEMY_LOOPER, looperPositionLevel2 + 1);
        }
        if (zigzagerPositionLevel1 != -1) {
            System.out.printf(enemyStatus, 1, ENEMY_ZIGZAGGER, zigzagerPositionLevel1 + 1);
        }
        if (zigzagerPositionLevel2 != -1) {
            System.out.printf(enemyStatus, 2, ENEMY_ZIGZAGGER, zigzagerPositionLevel2 + 1);
        }
    }

    private static void stairsCollision(int playerPos){
        int newPos = playerPos;
        if(newPos == stairsPositionLevel1){
            newPos = stairsPositionLevel2;
            playerLevel = 2;
        } else if (newPos == stairsPositionLevel2) {
            newPos = stairsPositionLevel1;
            playerLevel = 1;
        }
        playerPosition = newPos;
    }

    private static void treasureCollision(int playerPos){
        char[] curLevel = playerLevel == 1 ? layoutDungeonLevel2 : layoutDungeonLevel1;
        if(curLevel[playerPos] == TREASURE){
            treasureCollected++;
            curLevel[playerPos] = EMPTY;
        }
    }

    private static void enemyCollision(){
        if(zigzagerPositionLevel1 == playerPosition || zigzagerPositionLevel2 == playerPosition){
            // function done. just return any statement or print game over
        }else if(looperPositionLevel1 == playerPosition || looperPositionLevel2 == playerPosition){
        }
    }

    private static boolean gameEnd(){
        return treasureCollected == treasureCount && playerPosition == exitPosition;
    }


