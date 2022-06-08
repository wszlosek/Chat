public class Chat {

    private final TerminalReader terminalReader;

    public Chat(String QUEUE_HOST, String QUEUE_USER, String QUEUE_PASSWORD) {
        Backend backend = new Backend(QUEUE_HOST, QUEUE_USER, QUEUE_PASSWORD);
        terminalReader = new TerminalReader(backend, QUEUE_HOST, QUEUE_USER, QUEUE_PASSWORD);
    }

    public void start() throws Exception {
        terminalReader.start();
    }
}
