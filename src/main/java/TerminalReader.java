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
                command = in.nextLine();

                if (command.toLowerCase(Locale.ROOT).contains("-users")) {
                    System.out.println(APICommunicator.getAllUsers());
                } else if (command.toLowerCase(Locale.ROOT).contains("-channels")) {
                    System.out.println(APICommunicator.getChannelsFromUser(nick));
                } else if (command.toLowerCase(Locale.ROOT).contains("-private")) {
                    privateMessage(command);
                } else if (command.toLowerCase(Locale.ROOT).contains("-public")) {
                    publicMessage(command);
                } else if (command.toLowerCase(Locale.ROOT).contains("-new channel")) {
                    createNewChannel(command);
                } else if (command.toLowerCase(Locale.ROOT).contains("-delete channel")) {
                    deleteChannel(nick, command);
                } else if (command.toLowerCase(Locale.ROOT).contains("-add user")) {
                    addUserToChannel(nick, command);
                } else if (command.toLowerCase(Locale.ROOT).contains("-delete user")) {
                    deleteUserFromChannel(nick, command);
                } else if (command.toLowerCase(Locale.ROOT).contains("-message")) {
                    sendMessageOnChannel(nick, command);
                }

            } while (!command.toLowerCase(Locale.ROOT).contains("-logout"));

            backend.logout();
        }
    }

    private void sendMessageOnChannel(String nick, String command) throws IOException {
        var par = command.replace("-message ", "").split(" ");
        var message = par[0];
        var channel = par[1];

        if (APICommunicator.getChannelsFromUser(nick).contains(channel)) {
            backend.sendChannelMessage(message, channel);
        } else {
            System.out.println("Error");
        }
    }

    private void deleteUserFromChannel(String nick, String command) throws Exception {
        var par = command.replace("-delete user ", "").split(", ");
        var user = par[0];
        var channel = par[1];

        if (APICommunicator.getChannelsFromUser(nick).contains(channel)
                && APICommunicator.getAllUsers().contains(user) && APICommunicator.getChannelsFromUser(user).contains(channel)) {
            backend.removeUserFromChannel(channel, user);
        } else {
            System.out.println("Error");
        }
    }

    private void addUserToChannel(String nickname, String command) throws Exception {
        var par = command.replace("-add user ", "").split(", ");
        var user = par[0];
        var channel = par[1];
        if (APICommunicator.getChannelsFromUser(nickname).contains(channel) && APICommunicator.getAllUsers().contains(user)) {
            backend.addUserToChannel(channel, user);
        } else {
            System.out.println("Error");
        }
    }

    private void deleteChannel(String nickname, String command) throws Exception {
        var channelToDelete = command.replace("-delete channel ", "");
        if (APICommunicator.getChannelsFromUser(nickname).contains(channelToDelete)) {
            backend.deleteChannel(channelToDelete);
        } else {
            System.out.println("Channel " + channelToDelete+ " does not exist!");
        }
    }

    private void createNewChannel(String command) throws Exception {
        var newChannelName = command.replace("-new channel ", "");
        if (!APICommunicator.getAllChannels().contains(newChannelName)) {
            backend.createChannel(newChannelName);
        } else {
            System.out.println("Channel " + newChannelName + " does not exist!");
        }
    }

    private void publicMessage(String command) {
        var message = command.replace("-public ", "");
        backend.sendPublicMessage(message);
    }

    private void privateMessage(String command) throws IOException {
        var par = command.replace("-private ", "").split(", ");
        var user = par[0];
        var message = par[1];
        if (APICommunicator.getAllUsers().contains(user)) {
            backend.sendPrivateMessage(message, user);
        } else {
            System.out.println("User " + user + "does not exist!");
        }
    }
}
