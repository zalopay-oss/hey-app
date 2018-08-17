package locust;

import io.vertx.core.json.Json;
import model.UserRequest;

import java.util.UUID;

public class SignUpCron extends ApiCron {

    public SignUpCron(String url, String jwt, String method, String name) {
        super(url, jwt, method, name);
    }

    @Override
    public String getRequestJson() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("puser_" + UUID.randomUUID());
        userRequest.setFullName("Performance User Test");
        userRequest.setPassword("123");
        return Json.encodePrettily(userRequest);
    }
}
