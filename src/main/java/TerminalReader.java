import java.io.IOException;
import java.util.Scanner;

public class TerminalReader {

    private final Backend backend;
    private final APICommunicator APICommunicator;
    private final Scanner in = new Scanner(System.in);

    public TerminalReader(Backend backend) {
        this.backend = backend;
        APICommunicator = new APICommunicator();
    }

    public void start() throws IOException {
        System.out.println("Witaj w naszym komunikatorze.");
        System.out.println("Najpierw zdefiniuj swoją unikalną nazwę użytkownika: ");

        String username = in.nextLine();

        while (APICommunicator.getAllUsers().contains(username)) {
            System.out.println("Podana nazwa użytkownika istnieje już w systemie. Podaj inny nick: ");
            username = in.nextLine();
        }

        System.out.println("Cześć, " + username + "! " +
                "Wpisz dany numer użytkownika, by napisać do niego wiadomość.");


        if (APICommunicator.getAllUsers().isEmpty()) {
            System.out.println("<Brak innych użytkowników>");
        }

        int i = 1;
        for (var user : APICommunicator.getAllUsers()) {
            System.out.println(String.valueOf(i) + ": " + user);
            i += 1;
        }

        System.out.print("Numer użytkownika: ");
        int numberOfUser = in.nextInt();

        while (numberOfUser <= 0 || numberOfUser > APICommunicator.getAllUsers().size()) {
            System.out.print("Nieprawidłowy numer użytkownika! Ponów próbę: ");
            numberOfUser = in.nextInt();
        }

        System.out.println();
        System.out.println("Napisz treść wiadomości do " + APICommunicator.getAllUsers().get(numberOfUser) + ":");

        String message = in.nextLine();
    }
}
