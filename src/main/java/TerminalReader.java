import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TerminalReader {

    private final Backend backend;
    private final APICommunicator APICommunicator;

    public TerminalReader(Backend backend) throws IOException, TimeoutException {
        this.backend = backend;
        APICommunicator = new APICommunicator();
    }

    public void start() {
        System.out.println("Komunikator ");
    }
}
