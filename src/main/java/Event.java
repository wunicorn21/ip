public class Event extends Chore{

    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getTypeIcon(){
        return "E";
    }

    @Override
    public String getDescription() {
        return super.getDescription() +"(from: " + from + " to: " + to + ")" ;
    }
}
