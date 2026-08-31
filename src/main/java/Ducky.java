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
        printIntroduction();

        // Read user input line by line, dispatching each command until "bai" is typed.
        Scanner scanner = new Scanner(System.in);
        Chore[] chores = new Chore[MAX_CHORES];
        int choreCount = 0;
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bai")) {
                break;
            } else if (input.equals("list")) {
                printChores(chores, choreCount);
            } else if (input.startsWith("mark")) {
                markChore(input, chores);
            } else if (input.startsWith("unmark")) {
                unmarkChore(input, chores);
            } else if (input.startsWith("todo")) {
                choreCount = addToDo(input, chores, choreCount);
            } else if (input.startsWith("deadline")) {
                choreCount = addDeadline(input, chores, choreCount);
            } else if (input.startsWith("event")) {
                choreCount = addEvent(input, chores, choreCount);
            } else {
                System.out.println("QUACK?! I don't know \"" + input + "\". "
                        + "Try: list, todo, deadline, event, mark, unmark, bai");
            }
        }

        System.out.println("OK BAI. OFF TO BUY SOME LEMONADE"); // reference to the duck song
        scanner.close();
    }

    /** Prints Ducky's greeting and name banner. */
    private static void printIntroduction() {
        String banner = " ____             _          \n"
                + "|  _ \\ _   _  ___| | ___   _ \n"
                + "| | | | | | |/ __| |/ / | | |\n"
                + "| |_| | |_| | (__|   <| |_| |\n"
                + "|____/ \\__,_|\\___|_|\\_\\\\__, |\n"
                + "                        |___/ \n";
        System.out.println("QUACK QUACK!!!! I'M");
        System.out.println(banner);
    }

    /**
     * Prints every stored chore with its type icon, status icon and rank.
     *
     * @param chores Array holding the chores.
     * @param choreCount Number of chores currently stored.
     */
    private static void printChores(Chore[] chores, int choreCount) {
        for (int i = 0; i < choreCount; i++) {
            System.out.println("[" + chores[i].getTypeIcon() + "]" + "[" + chores[i].getStatusIcon() + "] "
                    + (i + 1) + ". " + chores[i].getDescription());
        }
    }

    /**
     * Marks the chore named by a "mark &lt;rank&gt;" command as done.
     *
     * @param input Full command line entered by the user.
     * @param chores Array holding the chores.
     */
    private static void markChore(String input, Chore[] chores) {
        int choreIndex = Integer.parseInt(input.substring("mark".length()).trim()) - 1;
        chores[choreIndex].markAsDone();
        System.out.println("done quacking " + chores[choreIndex].getDescription());
    }

    /**
     * Marks the chore named by an "unmark &lt;rank&gt;" command as not done.
     *
     * @param input Full command line entered by the user.
     * @param chores Array holding the chores.
     */
    private static void unmarkChore(String input, Chore[] chores) {
        int choreIndex = Integer.parseInt(input.substring("unmark".length()).trim()) - 1;
        chores[choreIndex].markAsUndone();
        System.out.println("oh! actl im not done quaking "
                + chores[choreIndex].getDescription());
    }

    /**
     * Adds a to-do chore described by a "todo &lt;description&gt;" command.
     *
     * @param input Full command line entered by the user.
     * @param chores Array holding the chores.
     * @param choreCount Number of chores stored before this call.
     * @return The updated chore count.
     */
    private static int addToDo(String input, Chore[] chores, int choreCount) {
        String description = input.substring("todo".length()).trim();
        chores[choreCount] = new ToDo(description);
        System.out.println("QUACKDDING: " + chores[choreCount].getDescription());
        return choreCount + 1;
    }

    /**
     * Adds a deadline chore described by a "deadline &lt;description&gt; /by &lt;when&gt;" command.
     * If the "/by" separator is missing, an error is printed and nothing is added.
     *
     * @param input Full command line entered by the user.
     * @param chores Array holding the chores.
     * @param choreCount Number of chores stored before this call.
     * @return The updated chore count.
     */
    private static int addDeadline(String input, Chore[] chores, int choreCount) {
        String rest = input.substring("deadline".length()).trim();
        String[] parts = rest.split("/by", 2);
        if (parts.length < 2) {
            System.out.println("QUACK?! A deadline needs a /by, e.g. deadline return book /by Sunday");
            return choreCount;
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        chores[choreCount] = new Deadline(description, by);
        printAddedChore(chores[choreCount], choreCount + 1);
        return choreCount + 1;
    }

    /**
     * Adds an event chore described by an
     * "event &lt;description&gt; /from &lt;start&gt; /to &lt;end&gt;" command.
     * If the "/from" or "/to" separator is missing, an error is printed and nothing is added.
     *
     * @param input Full command line entered by the user.
     * @param chores Array holding the chores.
     * @param choreCount Number of chores stored before this call.
     * @return The updated chore count.
     */
    private static int addEvent(String input, Chore[] chores, int choreCount) {
        String rest = input.substring("event".length()).trim();
        String[] parts = rest.split("/from", 2);
        if (parts.length < 2) {
            System.out.println("QUACK?! An event needs a /from, e.g. event meeting /from Mon 2pm /to 4pm");
            return choreCount;
        }
        String description = parts[0].trim();
        String[] timeParts = parts[1].split("/to", 2);
        if (timeParts.length < 2) {
            System.out.println("QUACK?! An event needs a /to, e.g. event meeting /from Mon 2pm /to 4pm");
            return choreCount;
        }
        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        chores[choreCount] = new Event(description, from, to);
        printAddedChore(chores[choreCount], choreCount + 1);
        return choreCount + 1;
    }

    /**
     * Prints the confirmation shown after a deadline or event chore is added.
     *
     * @param added The chore that was just added.
     * @param choreCount Number of chores stored after the addition.
     */
    private static void printAddedChore(Chore added, int choreCount) {
        System.out.println("QUACKDDING! I've added this chore:");
        System.out.println("  [" + added.getTypeIcon() + "][" + added.getStatusIcon() + "] " + added.getDescription());
        System.out.println("Now you have " + choreCount + " chores in the list.");
    }
}
