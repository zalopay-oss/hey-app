package locust;

import com.bigsonata.swarm.Cron;
import com.bigsonata.swarm.Props;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;

public class ApiCron extends Cron {
    private static final Logger logger = LoggerFactory.getLogger(ApiCron.class);

    private String urlString;
    private String jwt;
    private String method;
    private String name;
    private String requestJson = "";
    OkHttpClient client = new OkHttpClient();

    public ApiCron(String url, String jwt, String method, String name) {
        super(
                Props.createAsync()
                        .setType(method)
                        .setName(name)
        );
        this.urlString = url;
        this.jwt = jwt;
        this.method = method;
        this.name = name;
    }

    public ApiCron(String url, String jwt, String method, String name, String requestJson) {
        super(
                Props.createAsync()
                        .setType(method)
                        .setName(name)
        );
        this.urlString = url;
        this.jwt = jwt;
        this.method = method;
        this.name = name;
        this.requestJson = requestJson;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void process() {
        RequestBody body = RequestBody.create(MediaType.get("application/json"), getRequestJson());
        Request request;
        if (getRequestJson() != null && !getRequestJson().equals("")) {
            request = new Request.Builder()
                    .url(urlString)
                    .addHeader("Authorization", jwt)
                    .method(method, body)
                    .build();

        } else {
            request = new Request.Builder()
                    .url(urlString)
                    .addHeader("Authorization", jwt)
                    .build();
        }
        long startTime = System.currentTimeMillis();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                long elapsed = System.currentTimeMillis() - startTime;
                recordSuccess(elapsed);
                response.close();
            }
        } catch (Exception ex) {
            long elapsed = System.currentTimeMillis() - startTime;
            recordFailure(elapsed, ex.getMessage());
            ex.printStackTrace();

        }
    }

    @Override
    public Cron clone() {
        return new ApiCron(this.urlString, this.jwt, this.method, this.name, this.requestJson);
    }

    @Override
    public void initialize() {
    }

    public String getRequestJson() {
        return requestJson;
    }
}
