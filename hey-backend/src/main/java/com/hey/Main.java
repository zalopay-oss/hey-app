package com.hey;

import com.hey.util.PropertiesUtils;
import com.hey.verticle.HeyVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.emirbuc.randomsentence.RandomSentences;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String PROP_FILE_NAME = "system.properties";
    public static final String RESOURCE_PATH = "src/main/resources/";

    private static String env;

    public static void main(String[] args) throws IOException {
        env = System.getenv("env");
        if (StringUtils.isBlank(env)) {
            LOGGER.error("Missing env");
            System.exit(1);
        }
        System.setProperty("env", env);
        initSystemProperty();
        initVertx();
    }

    private static void initSystemProperty() {
        Properties p = new Properties();
        try {
            p.load(FileUtils.openInputStream(new File(RESOURCE_PATH + env + "." + PROP_FILE_NAME)));
        } catch (IOException e) {
            LOGGER.error("Cannot load System Property");
            System.exit(1);
        }
        for (String name : p.stringPropertyNames()) {
            String value = p.getProperty(name);
            System.setProperty(name, value);
        }
    }

    private static void initVertx() {
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setWorkerPoolSize(PropertiesUtils.getInstance().getIntValue("worker.size"));
        vertxOptions.setMaxEventLoopExecuteTime(Long.MAX_VALUE);

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(vertxOptions.getEventLoopPoolSize());

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(
                HeyVerticle.class,
                deploymentOptions);
    }
}
