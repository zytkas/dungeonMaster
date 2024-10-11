import java.util.Scanner;

public class Main {

        //Constants
        private static final char PLAYER = 'P';
        private static final char EXIT = 'E';
        private static final char TREASURE = 'T';
        private static final char STAIRS= 'S';
        private static final char LOOPER = 'L';
        private static final char ZIGZAGER = 'Z';
        private static final char EMPTY = '.';
        private static final int MAX_ROOMS = 100;
        private static final int MIN_ROOMS = 4;

        //Constant strings ...

        //Constant variables
        private static char[] layoutDungeonLevel1;
        private static char[] layoutDungeonLevel2;
        private static int playerPosition;
        private static int exitPosition;
        private static int treasureCount;
        private static int treasureCollected;
        private static int zigzagerPosition;
        private static int looperPosition;



        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            readDungeon(scanner);
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
                    case PLAYER -> playerPosition = i;
                    case EXIT -> exitPosition = i;
                    case TREASURE -> treasureCount = i++;
                    case LOOPER -> looperPosition = i;
                }
            }
            for (int i = 0; i < layoutDungeonLevel2.length; i++) {
                switch (layoutDungeonLevel2[i]) {
                    case EXIT -> exitPosition = i;
                    case TREASURE -> treasureCount = i++;
                    case ZIGZAGER -> zigzagerPosition = i;
                }
            }
        }


}
