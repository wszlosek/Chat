public class Test {
    private final static String QUEUE_NAME = "chat";
    private final static String QUEUE_HOST = "hawk.rmq.cloudamqp.com";
    private final static String QUEUE_USER = "dmnzyvrm";
    private final static String QUEUE_PASSWORD = "jLEQ9JpcRs7JXzWg4mYM9GwZp2xQ9uqP";


    public static void main(String[] argv) throws Exception {
//        Backend b = new Backend(QUEUE_HOST, QUEUE_USER, QUEUE_PASSWORD);
   //     b.login("viju");
//        b.sendPublicMessage("hello");
//
    //    b.createChannel("wojak sie nie myje");
//        b.addUserToChannel("duuuuuuuuuuuuuupa2","wojak");
//        b.sendChannelMessage("apud apud","duuuuuuuuuuuuuupa2");
//        b.deleteChannel("duuuuuuuuuuuuuupa2");
      //  b.logout();

        var a = new APICommunicator();
        a.getChannelsFromUser("c1");

    }
}
