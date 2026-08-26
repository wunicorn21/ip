public class chore {
    protected String description;
    protected boolean isDone;

    public chore(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    } //mark the blank with X or nothing

    public String getDescription(){
        return description;
    }
    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }
}
