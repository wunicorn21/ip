import java.util.Scanner;

/**
 * Entry point for the Ducky task manager.
 * Ducky reads commands from standard input and manages a list of chores until
 * the user types "bai".
 */
public class Ducky {
    /**
     * Runs the Ducky command loop.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        printIntroduction();

        // Read user input line by line, dispatching each command until "bai" is typed.
        Scanner scanner = new Scanner(System.in);
        ChoreList chores = new ChoreList();
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bai")) {
                break;
            } else if (input.equals("list")) {
                printChores(chores);
            } else if (input.startsWith("mark")) {
                markChore(input, chores);
            } else if (input.startsWith("unmark")) {
                unmarkChore(input, chores);
            } else if (input.startsWith("todo")) {
                addToDo(input, chores);
            } else if (input.startsWith("deadline")) {
                addDeadline(input, chores);
            } else if (input.startsWith("event")) {
                addEvent(input, chores);
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
     * @param chores The chore list.
     */
    private static void printChores(ChoreList chores) {
        for (int i = 0; i < chores.size(); i++) {
            Chore chore = chores.get(i);
            System.out.println("[" + chore.getTypeIcon() + "]" + "[" + chore.getStatusIcon() + "] "
                    + (i + 1) + ". " + chore.getDescription());
        }
    }

    /**
     * Parses the rank following a "mark" or "unmark" keyword and checks that
     * it refers to an existing chore. Prints an error message and returns -1
     * if the rank is missing, not a number, or out of range.
     *
     * @param input Full command line entered by the user.
     * @param keyword The command keyword ("mark" or "unmark") preceding the rank.
     * @param chores The chore list, used to check the rank is in range.
     * @return The zero-based chore index, or -1 if the rank was invalid.
     */
    private static int parseChoreIndex(String input, String keyword, ChoreList chores) {
        String rankText = input.substring(keyword.length()).trim();
        int rank;
        try {
            rank = Integer.parseInt(rankText);
        } catch (NumberFormatException e) {
            System.out.println("QUACK?! \"" + rankText + "\" isn't a chore number.");
            return -1;
        }
        int choreIndex = rank - 1;
        if (choreIndex < 0 || choreIndex >= chores.size()) {
            System.out.println("QUACK?! There's no chore number " + rank + ".");
            return -1;
        }
        return choreIndex;
    }

    /**
     * Marks the chore named by a "mark &lt;rank&gt;" command as done.
     * If the rank is missing, not a number, or out of range, an error is
     * printed and nothing is marked.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     */
    private static void markChore(String input, ChoreList chores) {
        int choreIndex = parseChoreIndex(input, "mark", chores);
        if (choreIndex == -1) {
            return;
        }
        Chore chore = chores.get(choreIndex);
        chore.markAsDone();
        System.out.println("done quacking " + chore.getDescription());
    }

    /**
     * Marks the chore named by an "unmark &lt;rank&gt;" command as not done.
     * If the rank is missing, not a number, or out of range, an error is
     * printed and nothing is unmarked.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     */
    private static void unmarkChore(String input, ChoreList chores) {
        int choreIndex = parseChoreIndex(input, "unmark", chores);
        if (choreIndex == -1) {
            return;
        }
        Chore chore = chores.get(choreIndex);
        chore.markAsUndone();
        System.out.println("oh! actl im not done quaking " + chore.getDescription());
    }

    /**
     * Adds a to-do chore described by a "todo &lt;description&gt;" command.
     * If the description is missing, an error is printed and nothing is added.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     */
    private static void addToDo(String input, ChoreList chores) {
        String description = input.substring("todo".length()).trim();
        if (description.isEmpty()) {
            System.out.println("QUACK?! A todo needs a description, e.g. todo read book");
            return;
        }
        Chore chore = new ToDo(description);
        chores.add(chore);
        printAddedChore(chore, chores.size());
    }

    /**
     * Adds a deadline chore described by a "deadline &lt;description&gt; /by &lt;when&gt;" command.
     * If the "/by" separator, the description or the due time is missing, an
     * error is printed and nothing is added.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     */
    private static void addDeadline(String input, ChoreList chores) {
        String rest = input.substring("deadline".length()).trim();
        String[] parts = rest.split("/by", 2);
        if (parts.length < 2) {
            System.out.println("QUACK?! A deadline needs a /by, e.g. deadline return book /by Sunday");
            return;
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        if (description.isEmpty()) {
            System.out.println("QUACK?! A deadline needs a description, e.g. deadline return book /by Sunday");
            return;
        }
        if (by.isEmpty()) {
            System.out.println("QUACK?! A deadline needs a time after /by, e.g. deadline return book /by Sunday");
            return;
        }
        Chore chore = new Deadline(description, by);
        chores.add(chore);
        printAddedChore(chore, chores.size());
    }

    /**
     * Adds an event chore described by an
     * "event &lt;description&gt; /from &lt;start&gt; /to &lt;end&gt;" command.
     * If the "/from" or "/to" separator, the description, the start time or
     * the end time is missing, an error is printed and nothing is added.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     */
    private static void addEvent(String input, ChoreList chores) {
        String rest = input.substring("event".length()).trim();
        String[] parts = rest.split("/from", 2);
        if (parts.length < 2) {
            System.out.println("QUACK?! An event needs a /from, e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        String description = parts[0].trim();
        if (description.isEmpty()) {
            System.out.println("QUACK?! An event needs a description, e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        String[] timeParts = parts[1].split("/to", 2);
        if (timeParts.length < 2) {
            System.out.println("QUACK?! An event needs a /to, e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        if (from.isEmpty()) {
            System.out.println("QUACK?! An event needs a start time after /from, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        if (to.isEmpty()) {
            System.out.println("QUACK?! An event needs an end time after /to, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        Chore chore = new Event(description, from, to);
        chores.add(chore);
        printAddedChore(chore, chores.size());
    }

    /**
     * Prints the confirmation shown after a chore is added.
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
