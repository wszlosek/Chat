import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Backend {
    boolean isLoggedIn = false;
    private final String publicExchangeName = "GLOBAL_EXCHANGE";
    private final String QUEUE_HOST;
    private final String QUEUE_USER;
    private final String QUEUE_PASSWORD;
    private final TerminalWriter terminalWriter = new TerminalWriter();
    private final Gson gson = new Gson();
    private Connection connection;
    private Channel channel;
    private String personalUsername;
    private String personalQueueName;
    private String personalConsumeTag;

    public Backend(String QUEUE_HOST, String QUEUE_USER, String QUEUE_PASSWORD){
        this.QUEUE_HOST = QUEUE_HOST;
        this.QUEUE_USER = QUEUE_USER;
        this.QUEUE_PASSWORD = QUEUE_PASSWORD;
    }

    private void configureConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(QUEUE_HOST);
        factory.setUsername(QUEUE_USER);
        factory.setPassword(QUEUE_PASSWORD);
        factory.setVirtualHost(QUEUE_USER);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    private void shutdownConnection() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    private static String getQueueName(String userName) {
        return "msg:" + userName;
    }

    private static String getExchangeName(String channelName) {
        return "msg:" + channelName;
    }

    private void startReceiving() {

        try {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String receivedMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
                gson.fromJson(receivedMessage, Message.class);
                Message formattedMessage = gson.fromJson(receivedMessage, Message.class);
                terminalWriter.writeMessage(formattedMessage);
            };
            personalConsumeTag = channel.basicConsume(personalQueueName, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopReceiving() throws IOException {
        channel.basicCancel(personalConsumeTag);
    }

    public void login(String chosenName) throws Exception {
        configureConnection();
        personalUsername = chosenName;
        personalQueueName = getQueueName(chosenName);
        channel.queueDeclare(personalQueueName, false, false, true, null);
        startReceiving();
        channel.queueBind(personalQueueName, publicExchangeName, "");
        isLoggedIn = true;
    }

    public void logout() throws Exception {
        stopReceiving();
        channel.queueDelete(personalQueueName);
        shutdownConnection();
        isLoggedIn = false;
    }

    public void sendPublicMessage(String messageContent) {
        try {
            channel.exchangeDeclare(publicExchangeName, "fanout");

            Message messageToSend = new Message(personalUsername, ChannelType.GLOBAL.toString(), messageContent);
            channel.basicPublish(publicExchangeName, "", null, gson.toJson(messageToSend).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendChannelMessage(String messageContent, String channelName) {
        try {
            String exchangeName = getExchangeName(channelName);
            channel.exchangeDeclare(exchangeName, "fanout");

            Message messageToSend = new Message(personalUsername, channelName, ChannelType.NORMAL.toString(), messageContent);
            channel.basicPublish(exchangeName, "", null, gson.toJson(messageToSend).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPrivateMessage(String messageContent, String userName) {
        try {
            String queueName = getQueueName(userName);
            Message messageToSend = new Message(personalUsername, ChannelType.DIRECT.toString(), messageContent);
            channel.basicPublish("", queueName, null, gson.toJson(messageToSend).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createChannel(String channelName) throws Exception {
        String exchangeName = getExchangeName(channelName);
        channel.exchangeDeclare(exchangeName, "fanout");
        channel.queueBind(personalQueueName, exchangeName, "");
    }

    public void deleteChannel(String channelName) throws Exception {
        String exchangeName = getExchangeName(channelName);
        channel.exchangeDelete(exchangeName);
    }

    public void addUserToChannel(String channelName, String userName) throws Exception {
        String exchangeName = getExchangeName(channelName);
        String queueName = getQueueName(userName);
        channel.queueBind(queueName, exchangeName, "");
    }

    public void removeUserFromChannel(String channelName, String userName) throws Exception {
        String exchangeName = getExchangeName(channelName);
        String queueName = getQueueName(userName);
        channel.queueUnbind(queueName, exchangeName, "");
    }
}
