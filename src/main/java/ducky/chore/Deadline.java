package ducky.chore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Represents a deadline: a chore that must be done by a certain date.
 * The due date is stored as a {@link LocalDate} rather than as free text,
 * so Ducky understands it as a real calendar date.
 */
public class Deadline extends Chore {
    /**
     * Format of a due date everywhere Ducky handles one: typed by the user,
     * shown to the user and written to the save file, e.g. "15-10-2019" for
     * 15 October 2019. Using one shared format keeps all three consistent.
     * "uuuu" (not "yyyy") is needed for STRICT resolving, which rejects
     * impossible dates like 30-02-2019 instead of silently adjusting them.
     */
    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

    /** Date by which the chore is due. */
    protected LocalDate by;

    /**
     * Creates a deadline with the given description and due date.
     *
     * @param description Text describing the chore.
     * @param by Date by which the chore is due.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the description followed by the due date in parentheses,
     * with the date shown in {@link #DATE_FORMAT}.
     *
     * @return The chore description with its due date appended.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " (by: " + by.format(DATE_FORMAT) + ")";
    }
}
