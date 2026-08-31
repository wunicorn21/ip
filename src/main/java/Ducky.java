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
                    System.out.println("[" + chorelist[i].getTypeIcon() + "]" + "[" + chorelist[i].getStatusIcon() + "] "
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

            if (input.startsWith("todo")) {
                String description = input.substring(5).trim();
                chorelist[counter] = new ToDo(description);
                counter++;
                System.out.println("QUACKDDING: " + chorelist[counter - 1].getDescription());
                continue;
            }

            if (input.startsWith("deadline")) {
                String rest = input.substring(9).trim();
                String[] parts = rest.split("/by", 2);
                if (parts.length < 2) {
                    System.out.println("QUACK?! A deadline needs a /by, e.g. deadline return book /by Sunday");
                    continue;
                }
                String description = parts[0].trim();
                String by = parts[1].trim();
                chorelist[counter] = new Deadlines(description, by);
                counter++;
                Chore added = chorelist[counter - 1];
                System.out.println("QUACKDDING! I've added this chore:");
                System.out.println("  [" + added.getTypeIcon() + "][" + added.getStatusIcon() + "] " + added.getDescription());
                System.out.println("Now you have " + counter + " chores in the list.");
                continue;
            }

            if (input.startsWith("event")) {
                String rest = input.substring(6).trim();
                String[] parts = rest.split("/from", 2);
                if (parts.length < 2) {
                    System.out.println("QUACK?! An event needs a /from, e.g. event meeting /from Mon 2pm /to 4pm");
                    continue;
                }
                String description = parts[0].trim();
                String[] timeParts = parts[1].split("/to", 2);
                if (timeParts.length < 2) {
                    System.out.println("QUACK?! An event needs a /to, e.g. event meeting /from Mon 2pm /to 4pm");
                    continue;
                }
                String from = timeParts[0].trim();
                String to = timeParts[1].trim();
                chorelist[counter] = new Events(description, from, to);
                counter++;
                Chore added = chorelist[counter - 1];
                System.out.println("QUACKDDING! I've added this chore:");
                System.out.println("  [" + added.getTypeIcon() + "][" + added.getStatusIcon() + "] " + added.getDescription());
                System.out.println("Now you have " + counter + " chores in the list.");
                continue;
            }
        }

        System.out.println("OK BAI. OFF TO BUY SOME LEMONADE"); // reference to the duck song
        scanner.close();
    }
}
