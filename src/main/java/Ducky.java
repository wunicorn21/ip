import java.util.Scanner;

public class Ducky {
    public static void main(String[] args) {
        String banner = " ____             _          \n"
                + "|  _ \\ _   _  ___| | ___   _ \n"
                + "| | | | | | |/ __| |/ / | | |\n"
                + "| |_| | |_| | (__|   <| |_| |\n"
                + "|____/ \\__,_|\\___|_|\\_\\\\__, |\n"
                + "                        |___/ \n";
        System.out.println("QUACK QUACK!!!! I'M"); //prints the introduction using the banner as the "name"
        System.out.println(banner);

        // Reads user input line by line, echoing each command back until "bye" is typed.
        Scanner scanner = new Scanner(System.in);
        chore[] chorelist = new chore[100];
        int counter = 0;
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bai")) {
                break;
            }

            if (input.equals("list")){
                for (int i = 0; i<counter; i++){
                    System.out.println("[" + chorelist[i].getStatusIcon() + "] " + String.valueOf(i+1) + ". "+ chorelist[i].getDescription());
                }
                continue;
            }

            if (input.startsWith("mark")){ //might add array accessing outside of index thing later
                String filterChoreListRank = input.substring(5).trim();
                int choreListRankIdx = Integer.parseInt(filterChoreListRank)-1;
                chorelist[choreListRankIdx].markAsDone();
                System.out.println ("done quacking " + chorelist[choreListRankIdx].getDescription());
                continue;
            }

            if (input.startsWith("unmark")){
                String filterChoreListRank = input.substring(7).trim();
                int choreListRankIdx = Integer.parseInt(filterChoreListRank)-1;
                chorelist[choreListRankIdx].markAsUndone();
                System.out.println ("oh! actl im not done quaking " + chorelist[choreListRankIdx].getDescription());
                continue;
            }
            chorelist[counter] = new chore(input);
            counter++;
            System.out.println("QUACKDDING: " + input);

        }

        System.out.println("OK BAI. OFF TO BUY SOME LEMONADE"); //reference to the duck song
        scanner.close();
    }
}
