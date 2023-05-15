package se.ifmo.lab07;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.util.Properties;

public class Main {

    private static final String PROPS = "interlayer.properties";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            var stream = new FileReader(PROPS);
            Properties props = new Properties();
            props.load(stream);
            var addresses = props.getProperty("SERVER_PORTS").split("\\s*,\\s*");
            var port = Integer.parseInt(props.getProperty("PORT", "7777"));
            var interlayer = new Interlayer(port);
            for (String address : addresses) {
                var host = address.split(":")[0];
                port = Integer.parseInt(address.split(":")[1]);
                interlayer.register(new InetSocketAddress(host, port));
            }
            interlayer.run();
        } catch (FileNotFoundException e) {
            logger.error("File not found or access denied (read):\n{}", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
//            logger.error("Error. Something went wrong:\n{}", e.getMessage());
        }
    }
}
