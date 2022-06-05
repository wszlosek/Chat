package notImp;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Send {
    private final static String QUEUE_NAME = "chat";
    private final static String QUEUE_HOST = "hawk.rmq.cloudamqp.com";
    private final static String QUEUE_USER = "dmnzyvrm";
    private final static String QUEUE_PASSWORD = "jLEQ9JpcRs7JXzWg4mYM9GwZp2xQ9uqP";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(QUEUE_HOST);
        factory.setUsername(QUEUE_USER);
        factory.setPassword(QUEUE_PASSWORD);
        factory.setVirtualHost(QUEUE_USER);
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(QUEUE_NAME, "fanout");
            Gson gson = new Gson();
            String content = "hello!";
            // MESSAGE m = new MESSAGE("Wojtek", content, "f");
            //channel.basicPublish(QUEUE_NAME, "", null, gson.toJson(m).getBytes(StandardCharsets.UTF_8));
            //System.out.println(" [x] Sent '" + m + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
