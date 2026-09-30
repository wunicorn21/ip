package ducky.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ducky.chore.Chore;
import ducky.chore.ChoreList;
import ducky.chore.Deadline;
import ducky.chore.Event;
import ducky.chore.ToDo;

/**
 * Loads and saves the chore list to a fixed file on disk, so chores survive
 * between runs of Ducky. The save path is relative to the working directory
 * the JVM is launched from (the project root, when run normally).
 * Only the happy path is handled: a well-formed save file written by this
 * same class, and a data directory that can be created if missing.
 */
public class Storage {
    /** Relative path, from the project root, of the save file. */
    private static final Path DATA_FILE_PATH = Paths.get(".", "data", "ducky.txt");

    /** Matches one saved line: "[typeIcon][statusIcon] description". */
    private static final Pattern LINE_PATTERN = Pattern.compile("^\\[(.)\\]\\[(.)\\] (.*)$");

    /** Matches a deadline's rendered description: "description (by: when)". */
    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^(.*) \\(by: (.*)\\)$");

    /** Matches an event's rendered description: "description (from: start to: end)". */
    private static final Pattern EVENT_PATTERN = Pattern.compile("^(.*) \\(from: (.*) to: (.*)\\)$");

    /**
     * Loads the chores previously saved to disk.
     * If the save file does not exist yet (e.g. on the very first run),
     * an empty list is returned instead of failing.
     *
     * @return The chores read from disk, or an empty list if none are saved.
     * @throws IOException If the save file exists but cannot be read.
     */
    public List<Chore> load() throws IOException {
        List<Chore> chores = new ArrayList<>();
        if (!Files.exists(DATA_FILE_PATH)) {
            return chores;
        }
        for (String line : Files.readAllLines(DATA_FILE_PATH)) {
            try {
                chores.add(parseChore(line));
            } catch (DateTimeParseException e) {
                // Reported as an IOException so the caller handles it like any other unreadable save file.
                throw new IOException("bad date in saved line \"" + line + "\"", e);
            }
        }
        return chores;
    }

    /**
     * Saves every chore in the given list to disk, overwriting any previous
     * save file. The data directory is created first if it does not already
     * exist.
     *
     * @param chores The chore list to save.
     * @throws IOException If the data directory or save file cannot be written.
     */
    public void save(ChoreList chores) throws IOException {
        Files.createDirectories(DATA_FILE_PATH.getParent());
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < chores.size(); i++) {
            lines.add(formatChore(chores.get(i)));
        }
        Files.write(DATA_FILE_PATH, lines);
    }

    /**
     * Renders a chore the same way it appears in "list", minus the leading
     * rank, since the rank is just the chore's position and need not be
     * stored.
     *
     * @param chore The chore to render.
     * @return The chore rendered as one save-file line.
     */
    private String formatChore(Chore chore) {
        return "[" + chore.getTypeIcon() + "][" + chore.getStatusIcon() + "] " + chore.getDescription();
    }

    /**
     * Parses one save-file line, previously written by {@link #formatChore},
     * back into a chore.
     *
     * @param line One save-file line.
     * @return The chore the line represents.
     * @throws DateTimeParseException If a deadline's saved date cannot be read.
     */
    private Chore parseChore(String line) {
        Matcher lineMatcher = LINE_PATTERN.matcher(line);
        lineMatcher.matches();
        String typeIcon = lineMatcher.group(1);
        boolean isDone = lineMatcher.group(2).equals("X");
        String description = lineMatcher.group(3);

        Chore chore = buildChore(typeIcon, description);
        if (isDone) {
            chore.markAsDone();
        }
        return chore;
    }

    /**
     * Builds the right chore subtype from its type icon and rendered
     * description, undoing the "(by: ...)" / "(from: ... to: ...)" suffix
     * that {@link Deadline} and {@link Event} add when rendering.
     *
     * @param typeIcon Single-letter type icon ("T", "D" or "E").
     * @param description The rendered description, as written by {@link #formatChore}.
     * @return The reconstructed chore.
     * @throws DateTimeParseException If a deadline's saved date cannot be read.
     */
    private Chore buildChore(String typeIcon, String description) {
        switch (typeIcon) {
        case "D":
            Matcher deadlineMatcher = DEADLINE_PATTERN.matcher(description);
            deadlineMatcher.matches();
            // The date was saved in the same format it is displayed in, so read it back with that format.
            LocalDate by = LocalDate.parse(deadlineMatcher.group(2), Deadline.DISPLAY_FORMAT);
            return new Deadline(deadlineMatcher.group(1), by);
        case "E":
            Matcher eventMatcher = EVENT_PATTERN.matcher(description);
            eventMatcher.matches();
            return new Event(eventMatcher.group(1), eventMatcher.group(2), eventMatcher.group(3));
        default:
            return new ToDo(description);
        }
    }
}
