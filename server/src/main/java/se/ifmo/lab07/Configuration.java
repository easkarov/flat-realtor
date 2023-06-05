package se.ifmo.lab07;

import java.io.IOException;
import java.util.Properties;


public class Configuration {

    public final static String DB_URL;

    public final static String DB_USER;

    public final static String DB_PASSWORD;

    public final static String DB_INIT;

    public final static String DB_DROP;

    public final static int PROCESSING_THREADS;

    public final static int BATCH;

    public final static int END_SIZE;

    public final static int PORT;

    static {
        try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties")) {
            Properties props = new Properties();
            props.load(stream);

            DB_URL = props.getProperty("DB_URL");
            DB_USER = props.getProperty("DB_USER");
            DB_PASSWORD = props.getProperty("DB_PASSWORD");
            DB_INIT = props.getProperty("DB_INIT");
            DB_DROP = props.getProperty("DB_DROP");
            PROCESSING_THREADS = Integer.parseInt(props.getProperty("PROCESSING_THREADS"));
            BATCH = Integer.parseInt(props.getProperty("BATCH"));
            END_SIZE = Integer.parseInt(props.getProperty("END_SIZE"));
            PORT = Integer.parseInt(props.getProperty("PORT"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
