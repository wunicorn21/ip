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
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bai")) {
                break;
            }
            System.out.println("QUACKDDING: " + input);

        }

        System.out.println("OK BAI. OFF TO BUY SOME LEMONADE"); //reference to the duck song
        scanner.close();
    }
}
