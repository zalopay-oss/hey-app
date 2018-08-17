package locust;

import com.bigsonata.swarm.Cron;
import com.bigsonata.swarm.Props;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class WSCron extends Cron {
    private static final Logger logger = LoggerFactory.getLogger(WSCron.class);

    private String urlString;
    private String jwt;
    private String requestJson;
    private String name;
    private WebSocketClient mWs;
    private long startTime = System.currentTimeMillis();
    private boolean canProcess = false;

    public WSCron(String url, String jwt, String requestJson, String name) {
        super(
                Props.createAsync()
                        .setType("WS")
                        .setName(name)
        );
        this.urlString = url;
        this.jwt = jwt;
        this.requestJson = requestJson;
        this.name = name;
    }

    @Override
    public void dispose() {
        mWs.close();
    }

    @Override
    public void process() {
        while (canProcess) {
            mWs.send(requestJson);
            startTime = System.currentTimeMillis();
            break;
        }
    }

    @Override
    public Cron clone() {
        return new WSCron(this.urlString, this.jwt, this.requestJson, this.name);
    }

    @Override
    public void initialize() {
        try {
            mWs = new WebSocketClient(new URI(urlString + "?jwt=" + jwt)) {
                @Override
                public void onMessage(String message) {
                    long elapsed = System.currentTimeMillis() - startTime;
                    recordSuccess(elapsed);
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    canProcess = true;

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    long elapsed = System.currentTimeMillis() - startTime;
                    recordFailure(elapsed, ex.getMessage());
                }

            };

            //open websocket
            mWs.connect();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
