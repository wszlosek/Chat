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
                    if (!APICommunicator.getAllChannels().contains(newChannelName)) {
                        backend.createChannel(newChannelName);
                    } else {
                        System.out.println("Channel " + newChannelName + " does not exist!");
                    }
                } else if (command.toLowerCase(Locale.ROOT).contains("-remove channel")) {
                    var par = command.split(" ");
                    var channelToRemoveName = par[2];
                    if (APICommunicator.getAllChannels().contains(channelToRemoveName)
                            && APICommunicator.getChannelsFromUser(nick).contains(channelToRemoveName)) {
                        backend.deleteChannel(channelToRemoveName);
                    } else {
                        System.out.println("Channel " + channelToRemoveName + " does not exist!");
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

            backend.logout();
        }
    }
}
