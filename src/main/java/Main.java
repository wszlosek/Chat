import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] argv) throws Exception {
        var props = new Properties();
        props.load(new FileInputStream("local.properties"));
        Chat c = new Chat(props.getProperty("QUEUE_HOST"), props.getProperty("QUEUE_USER"), props.getProperty("QUEUE_PASSWORD"));
        c.start();
    }
}
