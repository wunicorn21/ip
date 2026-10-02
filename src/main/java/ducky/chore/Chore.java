package ducky.chore;

/**
 * Represents a single chore (task) tracked by Ducky.
 * A chore has a text description and a completion status.
 */
public class Chore {
    /** Text describing what the chore involves. */
    protected String description;

    /** Whether the chore has been completed. */
    protected boolean isDone;

    /**
     * Creates a chore with the given description.
     * The chore starts out not done.
     *
     * @param description Text describing the chore.
     */
    public Chore(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the icon marking this chore's type.
     * The base chore has no specific type, so a "?" placeholder is returned;
     * subclasses override this with their own single-letter icon.
     *
     * @return A one-character type icon.
     */
    public String getTypeIcon() {
        return "?";
    }

    /**
     * Returns the status icon for this chore.
     *
     * @return "X" if the chore is done, or a single space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the description of this chore.
     *
     * @return The chore description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this chore's own description contains the given keyword.
     * Only the text the user typed as the description is searched, not the
     * date or time details that subclasses append in {@link #getDescription()}.
     * The match is case-sensitive.
     *
     * @param keyword Text to look for.
     * @return {@code true} if the description contains the keyword.
     */
    public boolean hasKeyword(String keyword) {
        return description.contains(keyword);
    }

    /** Marks this chore as done. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this chore as not done. */
    public void markAsUndone() {
        this.isDone = false;
    }
}
