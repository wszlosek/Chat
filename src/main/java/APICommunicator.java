import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class APICommunicator {
    private final String login = "dmnzyvrm";
    private final String password = "jLEQ9JpcRs7JXzWg4mYM9GwZp2xQ9uqP";
    private final String apiAddress = "http://hawk.rmq.cloudamqp.com/api/exchanges";

    public List<String> getAllChannels() throws IOException, InterruptedException {
        System.out.println(getJSON(apiAddress, 1000000000));
        return null;
    }

    public List<String> getAllUsers() {
        return null;
    }

    public String getJSON(String url, int timeout) {

        var auth = login + ":" + password;
        var auth2 = String.valueOf(Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8)));
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-Type", "application/json");
            c.setRequestProperty("Authorization", "Basic " + auth2);
            c.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();

        } catch (MalformedURLException ex) {
            // Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //  Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    //   Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
            return null;
        }
    }
}
