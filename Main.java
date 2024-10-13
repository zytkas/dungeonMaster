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
    //Constant strings ...

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

        movementPlayer("right", 6);
        movementZigzagger();
        movementLooper();
        enemyCollision();
        gameStatus();

        movementPlayer("right", 2);
        movementZigzagger();
        movementLooper();
        enemyCollision();
        gameStatus();

        movementPlayer("right", 2);
        movementZigzagger();
        movementLooper();
        enemyCollision();
        gameStatus();

        movementPlayer("right", 5);
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

        for (int i = 0; i < layoutDungeonLevel1.length; i++) {
            switch (layoutDungeonLevel1[i]) {
                case TREASURE:
                    treasureCount++;
                    break;
                case PLAYER:
                    playerPosition = i;
                    playerLevel = 2;
                    break;
                case EXIT:
                    exitPosition = i;
                    break;
                case LOOPER:
                    looperPositionLevel2 = i;
                    break;
                case ZIGZAGER:
                    zigzagerPositionLevel2 = i;
                    break;
                case STAIRS:
                    stairsPositionLevel2 = i;
            }
        }

        for (int i = 0; i < layoutDungeonLevel2.length; i++) {
            switch (layoutDungeonLevel2[i]) {
                case PLAYER:
                    playerPosition = i;
                    playerLevel = 1;
                    break;
                case EXIT:
                    exitPosition = i;
                    break;
                case TREASURE:
                    treasureCount++;
                    break;
                case LOOPER:
                    looperPositionLevel1= i;
                    break;
                case ZIGZAGER:
                    zigzagerPositionLevel1 = i;
                    break;
                case STAIRS:
                    stairsPositionLevel1 = i;
            }
        }
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
        System.out.println("Player: level " + playerLevel + ", room " + (playerPosition + 1) + ", treasures " + treasureCollected  + "/"+ treasureCount);
        if (looperPositionLevel1 != -1) {
            System.out.println("Level 1 enemy: looper, room " + (looperPositionLevel1 + 1));
        }
        if (looperPositionLevel2 != -1) {
            System.out.println("Level 2 enemy: looper, room " + (looperPositionLevel2 + 1));
        }
        if (zigzagerPositionLevel1 != -1) {
            System.out.println("Level 1 enemy: zigzager, room " + (zigzagerPositionLevel1 + 1));
        }
        if (zigzagerPositionLevel1 != -1) {
            System.out.println("Level 2 enemy: zigzager, room " + (zigzagerPositionLevel1 + 1));
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
            // to finish basically end of game
        }else if(looperPositionLevel1 == playerPosition || looperPositionLevel2 == playerPosition){
           // to finish basically end of game
        }
    }

    private static boolean gameEnd(){
        return treasureCollected == treasureCount && playerPosition == exitPosition;
    }


