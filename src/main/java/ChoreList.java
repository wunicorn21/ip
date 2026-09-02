import java.util.ArrayList;
import java.util.List;

/**
 * Stores the chores tracked by Ducky and provides operations to add and
 * retrieve them. Backed by a resizable list, so it has no fixed capacity.
 */
public class ChoreList {
    /** The chores held by this list, in the order they were added. */
    private final List<Chore> chores = new ArrayList<>();

    /**
     * Adds a chore to the end of the list.
     *
     * @param chore Chore to add.
     */
    public void add(Chore chore) {
        chores.add(chore);
    }

    /**
     * Returns the chore at the given zero-based index.
     *
     * @param index Zero-based position of the chore.
     * @return The chore at that position.
     */
    public Chore get(int index) {
        return chores.get(index);
    }

    /**
     * Returns how many chores are currently stored.
     *
     * @return The number of chores.
     */
    public int size() {
        return chores.size();
    }
}
