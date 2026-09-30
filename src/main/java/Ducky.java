import java.io.IOException;

import ducky.chore.Chore;
import ducky.chore.ChoreList;
import ducky.chore.Deadline;
import ducky.chore.Event;
import ducky.chore.ToDo;
import ducky.storage.Storage;
import ducky.ui.Ui;


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
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage();
        ChoreList chores = loadChores(storage, ui);

        // Read user input line by line, dispatching each command until "bai" is typed.
        while (true) {
            String input = ui.readCommand();

            if (input.equals("bai")) {
                break;
            } else if (input.equals("list")) {
                ui.showChores(chores);
            } else if (input.startsWith("mark")) {
                markChore(input, chores, storage, ui);
            } else if (input.startsWith("unmark")) {
                unmarkChore(input, chores, storage, ui);
            } else if (input.startsWith("delete")) {
                deleteChore(input, chores, storage, ui);
            } else if (input.startsWith("todo")) {
                addToDo(input, chores, storage, ui);
            } else if (input.startsWith("deadline")) {
                addDeadline(input, chores, storage, ui);
            } else if (input.startsWith("event")) {
                addEvent(input, chores, storage, ui);
            } else {
                ui.showMessage("QUACK?! I don't know \"" + input + "\". "
                        + "Try: list, todo, deadline, event, mark, unmark, delete, bai");
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    /**
     * Loads previously saved chores from disk at startup.
     * If nothing is saved yet, or the save file cannot be read, Ducky starts
     * with an empty chore list instead of failing.
     *
     * @param storage The storage used to read the save file.
     * @param ui The ui used to report a loading error, if any.
     * @return A chore list containing whatever was loaded (possibly empty).
     */
    private static ChoreList loadChores(Storage storage, Ui ui) {
        ChoreList chores = new ChoreList();
        try {
            for (Chore chore : storage.load()) {
                chores.add(chore);
            }
        } catch (IOException e) {
            ui.showMessage("QUACK?! Couldn't read saved chores, starting with an empty list: " + e.getMessage());
        }
        return chores;
    }

    /**
     * Saves the current chore list to disk.
     * Called after every command that changes the list, so the save file
     * always reflects what is currently shown. If saving fails, an error is
     * printed but the chore list already in memory is left untouched.
     *
     * @param chores The chore list to save.
     * @param storage The storage used to write the save file.
     * @param ui The ui used to report a saving error, if any.
     */
    private static void saveChores(ChoreList chores, Storage storage, Ui ui) {
        try {
            storage.save(chores);
        } catch (IOException e) {
            ui.showMessage("QUACK?! Couldn't save chores: " + e.getMessage());
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
     * @param ui The ui used to report an invalid rank, if any.
     * @return The zero-based chore index, or -1 if the rank was invalid.
     */
    private static int parseChoreIndex(String input, String keyword, ChoreList chores, Ui ui) {
        String rankText = input.substring(keyword.length()).trim();
        int rank;
        try {
            rank = Integer.parseInt(rankText);
        } catch (NumberFormatException e) {
            ui.showMessage("QUACK?! \"" + rankText + "\" isn't a chore number.");
            return -1;
        }
        int choreIndex = rank - 1;
        if (choreIndex < 0 || choreIndex >= chores.size()) {
            ui.showMessage("QUACK?! There's no chore number " + rank + ".");
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
     * @param storage The storage used to persist the change.
     * @param ui The ui used to print the confirmation or error message.
     */
    private static void markChore(String input, ChoreList chores, Storage storage, Ui ui) {
        int choreIndex = parseChoreIndex(input, "mark", chores, ui);
        if (choreIndex == -1) {
            return;
        }
        Chore chore = chores.get(choreIndex);
        chore.markAsDone();
        saveChores(chores, storage, ui);
        ui.showMessage("done quacking " + chore.getDescription());
    }

    /**
     * Marks the chore named by an "unmark &lt;rank&gt;" command as not done.
     * If the rank is missing, not a number, or out of range, an error is
     * printed and nothing is unmarked.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     * @param storage The storage used to persist the change.
     * @param ui The ui used to print the confirmation or error message.
     */
    private static void unmarkChore(String input, ChoreList chores, Storage storage, Ui ui) {
        int choreIndex = parseChoreIndex(input, "unmark", chores, ui);
        if (choreIndex == -1) {
            return;
        }
        Chore chore = chores.get(choreIndex);
        chore.markAsUndone();
        saveChores(chores, storage, ui);
        ui.showMessage("oh! actl im not done quaking " + chore.getDescription());
    }

    /**
     * Deletes the chore named by a "delete &lt;rank&gt;" command.
     * If the rank is missing, not a number, or out of range, an error is
     * printed and nothing is deleted. Otherwise the deleted chore is
     * announced (using the rank it had before deletion) and the updated
     * chore list, now renumbered with no gaps, is printed.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     * @param storage The storage used to persist the change.
     * @param ui The ui used to print the confirmation, updated list, or error message.
     */
    private static void deleteChore(String input, ChoreList chores, Storage storage, Ui ui) {
        int choreIndex = parseChoreIndex(input, "delete", chores, ui);
        if (choreIndex == -1) {
            return;
        }
        int rank = choreIndex + 1;
        Chore deleted = chores.get(choreIndex);
        String deletedLine = "[" + deleted.getTypeIcon() + "][" + deleted.getStatusIcon() + "] "
                + rank + ". " + deleted.getDescription();
        chores.remove(choreIndex);
        ui.showMessage(deletedLine + " waddled away!");
        ui.showMessage("Now chorelist is:");
        ui.showChores(chores);
        ui.showMessage("Now you have " + chores.size() + " chores in the list.");
        saveChores(chores, storage, ui);
    }

    /**
     * Adds a to-do chore described by a "todo &lt;description&gt;" command.
     * If the description is missing, an error is printed and nothing is added.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     * @param storage The storage used to persist the change.
     * @param ui The ui used to print the confirmation or error message.
     */
    private static void addToDo(String input, ChoreList chores, Storage storage, Ui ui) {
        String description = input.substring("todo".length()).trim();
        if (description.isEmpty()) {
            ui.showMessage("QUACK?! A todo needs a description, e.g. todo read book");
            return;
        }
        Chore chore = new ToDo(description);
        chores.add(chore);
        saveChores(chores, storage, ui);
        printAddedChore(chore, chores.size(), ui);
    }

    /**
     * Adds a deadline chore described by a "deadline &lt;description&gt; /by &lt;when&gt;" command.
     * If the "/by" separator, the description or the due time is missing, an
     * error is printed and nothing is added.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     * @param storage The storage used to persist the change.
     * @param ui The ui used to print the confirmation or error message.
     */
    private static void addDeadline(String input, ChoreList chores, Storage storage, Ui ui) {
        String rest = input.substring("deadline".length()).trim();
        String[] parts = rest.split("/by", 2);
        if (parts.length < 2) {
            ui.showMessage("QUACK?! A deadline needs a /by, e.g. deadline return book /by Sunday");
            return;
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        if (description.isEmpty()) {
            ui.showMessage("QUACK?! A deadline needs a description, e.g. deadline return book /by Sunday");
            return;
        }
        if (by.isEmpty()) {
            ui.showMessage("QUACK?! A deadline needs a time after /by, e.g. deadline return book /by Sunday");
            return;
        }
        Chore chore = new Deadline(description, by);
        chores.add(chore);
        saveChores(chores, storage, ui);
        printAddedChore(chore, chores.size(), ui);
    }

    /**
     * Adds an event chore described by an
     * "event &lt;description&gt; /from &lt;start&gt; /to &lt;end&gt;" command.
     * If the "/from" or "/to" separator, the description, the start time or
     * the end time is missing, an error is printed and nothing is added.
     *
     * @param input Full command line entered by the user.
     * @param chores The chore list.
     * @param storage The storage used to persist the change.
     * @param ui The ui used to print the confirmation or error message.
     */
    private static void addEvent(String input, ChoreList chores, Storage storage, Ui ui) {
        String rest = input.substring("event".length()).trim();
        String[] parts = rest.split("/from", 2);
        if (parts.length < 2) {
            ui.showMessage("QUACK?! An event needs a /from, e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        String description = parts[0].trim();
        if (description.isEmpty()) {
            ui.showMessage("QUACK?! An event needs a description, e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        String[] timeParts = parts[1].split("/to", 2);
        if (timeParts.length < 2) {
            ui.showMessage("QUACK?! An event needs a /to, e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        if (from.isEmpty()) {
            ui.showMessage("QUACK?! An event needs a start time after /from, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        if (to.isEmpty()) {
            ui.showMessage("QUACK?! An event needs an end time after /to, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm");
            return;
        }
        Chore chore = new Event(description, from, to);
        chores.add(chore);
        saveChores(chores, storage, ui);
        printAddedChore(chore, chores.size(), ui);
    }

    /**
     * Prints the confirmation shown after a chore is added.
     *
     * @param added The chore that was just added.
     * @param choreCount Number of chores stored after the addition.
     * @param ui The ui used to print the confirmation.
     */
    private static void printAddedChore(Chore added, int choreCount, Ui ui) {
        ui.showMessage("QUACKDDING! I've added this chore:");
        ui.showMessage("  [" + added.getTypeIcon() + "][" + added.getStatusIcon() + "] " + added.getDescription());
        ui.showMessage("Now you have " + choreCount + " chores in the list.");
    }
}
