package se.ifmo.lab07.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.dto.response.Response;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Map;


public class Server implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final DatagramSocket connection;

    private final ReceivingManager receivingManager;

    private final SendingManager sendingManager;

    public Server(int port) throws SocketException {
        this.connection = new DatagramSocket(port);
        this.receivingManager = new ReceivingManager(connection);
        this.sendingManager = new SendingManager(connection);
        logger.info("Server started on {} port", port);
    }

    @Override
    public void close() {
        this.connection.close();
        logger.info("Server has been stopped. Connection closed");
    }

    public Map.Entry<SocketAddress, Request> receiveRequest() throws IOException, ClassNotFoundException {
        return receivingManager.receiveRequest();
    }

    public void send(SocketAddress address, Response response) throws IOException {
        sendingManager.send(address, response);
    }
}