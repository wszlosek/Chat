import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Backend {

    private static final Gson gson = new Gson();
    private static Channel channel;
    private static String username;
    private final String QUEUE_HOST;
    private final String QUEUE_USER;
    private final String QUEUE_PASSWORD;
    private final TerminalWriter terminalWriter;

    public Backend(String QUEUE_HOST, String QUEUE_USER, String QUEUE_PASSWORD) throws IOException, TimeoutException {
        this.QUEUE_HOST = QUEUE_HOST;
        this.QUEUE_USER = QUEUE_USER;
        this.QUEUE_PASSWORD = QUEUE_PASSWORD;
        terminalWriter = new TerminalWriter();
        configureChannel();
    }

    private void configureChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(QUEUE_HOST);
        factory.setUsername(QUEUE_USER);
        factory.setPassword(QUEUE_PASSWORD);
        factory.setVirtualHost(QUEUE_USER);
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void start() {

    }

    private String getQueueName(String userName) {
        return "msg:" + userName;
    }

    public String getExchangeName(String channelName) {
        return "msg:" + channelName;
    }

    public void messageReceiver(String messageContent) throws Exception {
        try {

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String m = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + m + "'" + ", " + Instant.now());
                MESSAGE m2 = gson.fromJson(m, MESSAGE.class);
                terminalWriter.writeMessage(m2);
            };

            channel.basicConsume("msg:" + username, true, deliverCallback, consumerTag -> {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPublicMessage(String messageContent) throws Exception {
        try {
//            channel.exchangeDeclare("GLOBAL_EXCHANGE", "fanout");

            MESSAGE m = new MESSAGE(username, ChannelType.GLOBAL.toString(), messageContent);
            channel.basicPublish("GLOBAL_EXCHANGE", "", null, gson.toJson(m).getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendChannelMessage(String messageContent, String channelName) throws Exception {
        try {
            channel.exchangeDeclare("msg:" + channelName, "fanout");

            MESSAGE m = new MESSAGE(username, "msg:" + channelName, ChannelType.NORMAL.toString(), messageContent);
            channel.basicPublish("msg:" + channelName, "", null, gson.toJson(m).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPrivateMessage(String messageContent, String userName) throws Exception {
        try {
//            channel.queueDeclare(userName,false,false,true, null);

            MESSAGE m = new MESSAGE(username, ChannelType.DIRECT.toString(), messageContent);
            channel.basicPublish("", userName, null, gson.toJson(m).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createChannel(String channelName) throws Exception {
        channel.exchangeDeclare("msg:" + channelName, "fanout");
    }

    public void deleteChannel(String channelName) throws Exception {
        channel.exchangeDelete("msg:" + channelName);
    }

    public void addUserToChannel(String channelName, String userName) throws Exception {
        channel.queueBind("msg:" + userName, "msg:" + channelName, "");
    }

    public void removeUserFromChannel(String channelName, String userName) throws Exception {
        channel.queueUnbind("msg:" + userName, "msg:" + channelName, "");
    }

    public void login(String chosenName) throws Exception {
        channel.queueDeclare("msg:" + chosenName, false, true, true, null);
    }

    public void logout() throws Exception {
        channel.queueDelete("msg:" + username);
    }
}
