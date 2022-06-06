import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class APICommunicator {
    private final String login = "dmnzyvrm";
    private final String password = "jLEQ9JpcRs7JXzWg4mYM9GwZp2xQ9uqP";
    private final String apiAddress = "https://hawk.rmq.cloudamqp.com/api/";

    public List<String> getAllChannels() throws IOException {
        var json = getJSON(apiAddress + "exchanges");
        JSONArray jsonArray = new JSONArray(json);
        var result = new ArrayList<String>();

        for(int i = 0; i < jsonArray.length(); i++) {
            var c = jsonArray.getJSONObject(i);
            result.add(c.getString("name"));
        }

        return result;
    }

    public List<String> getAllUsers() throws IOException {
        var json = getJSON(apiAddress + "queues");
        JSONArray jsonArray = new JSONArray(json);
        var result = new ArrayList<String>();

        for(int i = 0; i < jsonArray.length(); i++) {
            var c = jsonArray.getJSONObject(i);
            result.add(c.getString("name").replace("msg:", ""));
        }

        return result;
    }

    public List<String> getUsersFromChannel(String channelName) throws IOException {
        channelName = channelName.replace("msg:", "");
        var json = getJSON(apiAddress + "bindings");
        JSONArray jsonArray = new JSONArray(json);
        var result = new ArrayList<String>();

        for(int i = 0; i < jsonArray.length(); i++) {
            var c = jsonArray.getJSONObject(i);
            if (c.getString("source").replace("msg:", "").equals(channelName)) {
                result.add(c.getString("destination").replace("msg:", ""));
            }
        }
        System.out.println(result);

        return result;
    }

    public List<String> getChannelsFromUser(String userName) throws IOException {
        userName = userName.replace("msg:", "");
        var json = getJSON(apiAddress + "bindings");
        JSONArray jsonArray = new JSONArray(json);
        var result = new ArrayList<String>();

        for(int i = 0; i < jsonArray.length(); i++) {
            var c = jsonArray.getJSONObject(i);
            if (c.getString("destination").replace("msg:", "").equals(userName)) {
                result.add(c.getString("source").replace("msg:", ""));
            }
        }
        System.out.println(result);

        return result;
    }

    public String getJSON(String address) throws IOException {
        var auth = login + ":" + password;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(auth.getBytes()));

        var url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization",  basicAuth);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }
}
