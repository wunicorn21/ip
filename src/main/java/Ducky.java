import java.util.Scanner;

/**
 * Entry point for the Ducky task manager.
 * Ducky reads commands from standard input and manages a list of chores until
 * the user types "bai".
 */
public class Ducky {
    /** Maximum number of chores Ducky can store. */
    private static final int MAX_CHORES = 100;

    /**
     * Runs the Ducky command loop.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        String banner = " ____             _          \n"
                + "|  _ \\ _   _  ___| | ___   _ \n"
                + "| | | | | | |/ __| |/ / | | |\n"
                + "| |_| | |_| | (__|   <| |_| |\n"
                + "|____/ \\__,_|\\___|_|\\_\\\\__, |\n"
                + "                        |___/ \n";
        // Print the introduction, using the banner as Ducky's "name".
        System.out.println("QUACK QUACK!!!! I'M");
        System.out.println(banner);

        // Read user input line by line, handling each command until "bai" is typed.
        Scanner scanner = new Scanner(System.in);
        Chore[] chorelist = new Chore[MAX_CHORES];
        int counter = 0;
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bai")) {
                break;
            }

            if (input.equals("list")) {
                for (int i = 0; i < counter; i++) {
                    System.out.println("[" + chorelist[i].getStatusIcon() + "] "
                            + (i + 1) + ". " + chorelist[i].getDescription());
                }
                continue;
            }

            if (input.startsWith("mark")) {
                String filterChoreListRank = input.substring(5).trim();
                int choreListRankIdx = Integer.parseInt(filterChoreListRank) - 1;
                chorelist[choreListRankIdx].markAsDone();
                System.out.println("done quacking " + chorelist[choreListRankIdx].getDescription());
                continue;
            }

            if (input.startsWith("unmark")) {
                String filterChoreListRank = input.substring(7).trim();
                int choreListRankIdx = Integer.parseInt(filterChoreListRank) - 1;
                chorelist[choreListRankIdx].markAsUndone();
                System.out.println("oh! actl im not done quaking "
                        + chorelist[choreListRankIdx].getDescription());
                continue;
            }

            chorelist[counter] = new Chore(input);
            counter++;
            System.out.println("QUACKDDING: " + input);
        }

        System.out.println("OK BAI. OFF TO BUY SOME LEMONADE"); // reference to the duck song
        scanner.close();
    }
}
