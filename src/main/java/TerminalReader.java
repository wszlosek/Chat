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

    public void start() throws Exception {

        while (true) {
            System.out.println("Witaj w naszym komunikatorze.");
            System.out.println("Najpierw zdefiniuj swoją unikalną nazwę użytkownika: ");
            var users = APICommunicator.getAllUsers();
            String username = in.nextLine();

            while (users.contains(username)) {
                System.out.println("Podana nazwa użytkownika istnieje już w systemie. Podaj inny nick: ");
                username = in.nextLine();
            }
            backend.login(username);

            System.out.println("Cześć, " + username + "! W naszej aplikacji możesz skorzystać z " +
                    "kilku trybów:");

            var choice = 1;
            while (choice == 1 || choice == 2 || choice == 3 || choice == 4 ||
                    choice == 5 || choice == 6 || choice == 7 || choice == 8) {
                System.out.println("1. Napisz prywatnie do jednego z dostępnych użytkowników \n" +
                        "2. Napisz publiczną wiadomość \n" +
                        "3. Stwórz nowy kanał \n" +
                        "4. Dodaj użytkownika do kanału \n" +
                        "5. Usuń użytkownika z kanału \n" +
                        "6. Napisz wiadomość na danym kanale \n" +
                        "7. Usuń kanał \n" +
                        "8. Wyloguj się");

                choice = in.nextInt();
                switch (choice) {
                    case 1 -> sendPrivateMessage();
                    case 2 -> sendPublicMessage();
                    case 3 -> createNewChannel();
                    case 4 -> addNewUserToChannel(username);
                    case 5 -> removeUserFromChannel(username);
                    case 6 -> sendMessageOnChannel(username);
                    case 7 -> removeChannel(username);
                    case 8 -> logout();
                    default -> System.out.println("Nieprawidłowe polecenie!");
                }
            }
        }
    }

    private void removeChannel(String username) throws Exception {
        var channelsOfUser = APICommunicator.getChannelsFromUser(username);

        System.out.println("O który kanał Ci chodzi?");
        int i = 1;
        for (var v : channelsOfUser) {
            System.out.println(i + ": " + v);
            i += 1;
        }
        int numberOfChannel = in.nextInt() - 1;
        var channel = channelsOfUser.get(numberOfChannel);

        backend.deleteChannel(channel);
        System.out.println("Pomyślnie usunięto kanał!");
    }

    private void sendMessageOnChannel(String username) throws IOException {
        var channelsOfUser = APICommunicator.getChannelsFromUser(username);

        System.out.println("O który kanał Ci chodzi?");
        int i = 1;
        for (var v : channelsOfUser) {
            System.out.println(i + ": " + v);
            i += 1;
        }
        int numberOfChannel = in.nextInt() - 1;
        var channel = channelsOfUser.get(numberOfChannel);

        System.out.println("Podaj treść wiadomości: ");
        String message = in.next();

        backend.sendChannelMessage(message, channel);
        System.out.println("Pomyślnie wysłano wiadomość!");
    }

    private void removeUserFromChannel(String username) throws Exception {
        var channelsOfUser = APICommunicator.getChannelsFromUser(username);

        System.out.println("O który kanał Ci chodzi?");
        int i = 1;
        for (var v : channelsOfUser) {
            System.out.println(i + ": " + v);
            i += 1;
        }
        int numberOfChannel = in.nextInt() - 1;

        var usersOfChannel = APICommunicator.getUsersFromChannel(channelsOfUser.get(numberOfChannel));

        System.out.println("Wybierz użytkownika, którego chcesz usunac z tego kanału: ");
        i = 1;
        for (var user : usersOfChannel) {
            System.out.println(i + ": " + user);
            i += 1;
        }
        int numberOfUser = in.nextInt() - 1;

        backend.removeUserFromChannel(channelsOfUser.get(numberOfChannel), usersOfChannel.get(numberOfUser));
        System.out.println("Usunięto użytkownika z tego kanału!");
    }

    private void logout() throws Exception {
        backend.logout();
        System.out.println("Wylogowano pomyślnie!");
    }

    private void addNewUserToChannel(String username) throws Exception {
        var users = APICommunicator.getAllUsers();
        var channelsOfUser = APICommunicator.getChannelsFromUser(username);

        System.out.println("O który kanał Ci chodzi?");
        int i = 1;
        for (var v : channelsOfUser) {
            System.out.println(i + ": " + v);
            i += 1;
        }
        int numberOfChannel = in.nextInt() - 1;

        System.out.println("Wybierz użytkownika, którego chcesz dodać do tego kanału: ");
        i = 1;
        for (var user : users) {
            System.out.println(i + ": " + user);
            i += 1;
        }
        int numberOfUser = in.nextInt() - 1;

        backend.addUserToChannel(channelsOfUser.get(numberOfChannel), users.get(numberOfUser));
        System.out.println("Dodano użytkownika do tego kanału!");
    }

    private void createNewChannel() throws Exception {
        var channels = APICommunicator.getAllChannels();
        System.out.println("Podaj nazwę nowego kanału: ");
        String channelName = in.next();

        while (channels.contains(channelName)) {
            System.out.println("Podany kanał już istnieje, podaj inną nazwę: ");
            channelName = in.next();
        }

        backend.createChannel(channelName);
    }

    private void sendPublicMessage() {
        System.out.println("Podaj treść wiadomości: ");

        backend.sendPublicMessage(in.nextLine());

        System.out.println("Wiadomość wysłana!");
    }

    private void sendPrivateMessage() throws IOException {
        var users = APICommunicator.getAllUsers();
        System.out.println("Wpisz dany numer użytkownika, by napisać do niego wiadomość.");

        if (users.isEmpty()) {
            System.out.println("<Brak innych użytkowników>");
        }

        int i = 1;
        for (var user : users) {
            System.out.println(i + ": " + user);
            i += 1;
        }

        System.out.print("Numer użytkownika: ");
        int numberOfUser = in.nextInt() - 1;

        while (numberOfUser < 0 || numberOfUser >= users.size()) {
            System.out.print("Nieprawidłowy numer użytkownika! Ponów próbę: ");
            numberOfUser = in.nextInt();
        }

        System.out.println();
        System.out.println("Napisz treść wiadomości do " + users.get(numberOfUser) + ":");

        String message = in.next();
        backend.sendPrivateMessage(message, users.get(numberOfUser));

        System.out.println("Wiadomość do " + users.get(numberOfUser) + " wysłana!");
    }
}
