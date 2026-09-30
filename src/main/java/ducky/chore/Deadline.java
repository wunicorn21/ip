package ducky.chore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a deadline: a chore that must be done by a certain date.
 * The due date is stored as a {@link LocalDate} rather than as free text,
 * so Ducky understands it as a real calendar date.
 */
public class Deadline extends Chore {
    /**
     * Format used to show the due date to the user, e.g. "Oct 15 2019".
     * The locale is fixed to English so month names stay the same on every
     * machine, which also keeps the save file readable across machines.
     */
    public static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

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
     * with the date shown in {@link #DISPLAY_FORMAT}.
     *
     * @return The chore description with its due date appended.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
