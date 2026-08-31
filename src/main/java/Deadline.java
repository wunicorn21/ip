/**
 * Represents a deadline: a chore that must be done by a certain time.
 */
public class Deadline extends Chore {

    /** Text describing when the chore is due. */
    protected String by;

    /**
     * Creates a deadline with the given description and due time.
     *
     * @param description Text describing the chore.
     * @param by Text describing when the chore is due.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the description followed by the due time in parentheses.
     *
     * @return The chore description with its due time appended.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " (by: " + by + ")";
    }
}
