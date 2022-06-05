import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Chat {

    private final Backend backend;
    private final TerminalReader terminalReader;

    public Chat(String QUEUE_HOST, String QUEUE_USER, String QUEUE_PASSWORD) throws IOException, TimeoutException {
        backend = new Backend(QUEUE_HOST, QUEUE_USER, QUEUE_PASSWORD);
        terminalReader = new TerminalReader(backend);
    }

    public void start() {
        terminalReader.start();
    }
}
