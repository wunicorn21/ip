public class Deadline extends Chore{

    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getTypeIcon(){
        return "D";
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " (by: " + by + ")";
    }
}
