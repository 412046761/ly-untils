package mp.voice;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

public class HttpsConnection {

    public static HttpsURLConnection getHttpsConnection(String connectingUrl) throws Exception {

        URL url = new URL(connectingUrl);
        return (HttpsURLConnection) url.openConnection();
    }
}