public class Events extends Chore{

    protected String from;
    protected String to;

    public Events(String description, String from, String to) {
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
