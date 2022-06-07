import java.io.IOException;
import java.util.Locale;
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

            String nick;
            String command;

            do {
                System.out.println("---");
                System.out.print("Your nickname: ");
                nick = in.nextLine();
            } while (APICommunicator.getAllUsers().contains(nick));

            backend.login(nick);

            do {
                System.out.println("---");
                System.out.print("Command: ");
                command = in.nextLine();
                System.out.println(command);

                if (command.toLowerCase(Locale.ROOT).contains("-users")) {
                    System.out.println(APICommunicator.getAllUsers());
                } else if (command.toLowerCase(Locale.ROOT).contains("-channels")) {
                    System.out.println(APICommunicator.getChannelsFromUser(nick));
                } else if (command.toLowerCase(Locale.ROOT).contains("-private")) {
                    var par = command.split(" ");
                    var user = par[1];
                    var message = par[2];
                    if (APICommunicator.getAllUsers().contains(user)) {
                        backend.sendPrivateMessage(message, user);
                    } else {
                        System.out.println("User " + user + "does not exist!");
                    }
                } else if (command.toLowerCase(Locale.ROOT).contains("-public")) {
                    var par = command.split(" ");
                    var message = par[1];
                    backend.sendPublicMessage(message);
                } else if (command.toLowerCase(Locale.ROOT).contains("-new channel")) {
                    var par = command.split(" ");
                    var newChannelName = par[2];
                    if (APICommunicator.getAllChannels().contains(newChannelName)) {
                        backend.createChannel(newChannelName);
                    } else {
                        System.out.println("Channel " + newChannelName + "does not exist!");
                    }
                } else if (command.toLowerCase(Locale.ROOT).contains("-remove channel")) {
                    var par = command.split(" ");
                    var channelToRemoveName = par[2];
                    if (APICommunicator.getAllChannels().contains(channelToRemoveName)
                            && APICommunicator.getChannelsFromUser(nick).contains(channelToRemoveName)) {
                        backend.deleteChannel(channelToRemoveName);
                    } else {
                        System.out.println("Channel " + channelToRemoveName + "does not exist!");
                    }
                } else if (command.toLowerCase(Locale.ROOT).contains("-add user")) {
                    var par = command.split(" ");
                    var user = par[2];
                    var channel = par[4];

                    if (APICommunicator.getChannelsFromUser(nick).contains(channel) && APICommunicator.getAllUsers().contains(user)) {
                        backend.addUserToChannel(channel, user);
                    } else {
                        System.out.println("Error");
                    }
                } else if (command.toLowerCase(Locale.ROOT).contains("-remove user")) {
                    var par = command.split(" ");
                    var user = par[2];
                    var channel = par[4];

                    if (APICommunicator.getChannelsFromUser(nick).contains(channel)
                            && APICommunicator.getAllUsers().contains(user) && APICommunicator.getChannelsFromUser(user).contains(channel)) {
                        backend.removeUserFromChannel(channel, user);
                    } else {
                        System.out.println("Error");
                    }
                } else if (command.toLowerCase(Locale.ROOT).contains("-message")) {
                    var par = command.split(" ");
                    var message = par[1];
                    var channel = par[2];

                    if (APICommunicator.getChannelsFromUser(nick).contains(channel)) {
                        backend.sendChannelMessage(message, channel);
                    } else {
                        System.out.println("Error");
                    }
                }

            } while (!command.toLowerCase(Locale.ROOT).contains("-logout"));
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
