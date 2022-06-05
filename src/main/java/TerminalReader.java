public class TerminalReader {

    private final Backend backend;
    private final APICommunicator APICommunicator;

    public TerminalReader(Backend backend) {
        this.backend = backend;
        APICommunicator = new APICommunicator();
    }

    public void start() {
        System.out.println("Komunikator ");
    }
}
