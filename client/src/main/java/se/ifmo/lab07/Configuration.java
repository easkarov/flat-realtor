package se.ifmo.lab07;

import java.io.IOException;
import java.util.Properties;


public class Configuration {

    public final static String HOST;

    public final static int PORT;

    public final static int BATCH;

    public final static int MAX_DELAY;

    public final static int MAX_REC_DEPTH;

    static {
        try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("client.properties")) {
            Properties props = new Properties();
            props.load(stream);

            HOST = props.getProperty("HOST");
            PORT = Integer.parseInt(props.getProperty("PORT"));
            BATCH = Integer.parseInt(props.getProperty("BATCH"));
            MAX_DELAY = Integer.parseInt(props.getProperty("MAX_DELAY"));
            MAX_REC_DEPTH = Integer.parseInt(props.getProperty("MAX_REC_DEPTH"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
