import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class Main {
    private final static String QUEUE_NAME = "chat";
    private final static String QUEUE_HOST = "hawk.rmq.cloudamqp.com";
    private final static String QUEUE_USER = "dmnzyvrm";
    private final static String QUEUE_PASSWORD = "jLEQ9JpcRs7JXzWg4mYM9GwZp2xQ9uqP";

    public static void main(String[] argv) throws Exception {
    //    Chat c = new Chat(QUEUE_HOST, QUEUE_USER, QUEUE_PASSWORD);
    //    c.start();
        var a = new APICommunicator();
        a.getAllChannels();
        System.out.println(a.getAllUsers());
    }

}
