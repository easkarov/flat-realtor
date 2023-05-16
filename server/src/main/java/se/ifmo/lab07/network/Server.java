package se.ifmo.lab07.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.request.GetCommandsRequest;
import se.ifmo.lab07.dto.request.PingRequest;
import se.ifmo.lab07.dto.request.ValidationRequest;
import se.ifmo.lab07.dto.response.GetCommandsResponse;
import se.ifmo.lab07.dto.response.PingResponse;
import se.ifmo.lab07.manager.AuthManager;
import se.ifmo.lab07.manager.CommandManager;
import se.ifmo.lab07.manager.ReceivingManager;
import se.ifmo.lab07.manager.SendingManager;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;


public class Server implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final DatagramSocket connection;
    private final CommandManager commandManager;
    private final ReceivingManager receivingManager;
    private final SendingManager sendingManager;
    private final AuthManager authManager;

    public Server(CommandManager commandManager, AuthManager authManager, int port) throws SocketException {
        this.connection = new DatagramSocket(port);
        this.commandManager = commandManager;
        this.receivingManager = new ReceivingManager(connection);
        this.sendingManager = new SendingManager(connection);
        this.authManager = authManager;
        logger.info("Server started on {} port", port);
    }

    @Override
    public void close() {
        this.connection.close();
        logger.info("Server has been stopped. Connection closed");
    }

    public void run() {
        while (true) {
            try {
                var pair = receivingManager.receiveRequest();
                var address = pair.getKey();
                var request = pair.getValue();

                if (request instanceof GetCommandsRequest) {
                    sendingManager.send(address, new GetCommandsResponse(commandManager.getCommands()));
                }

                var user = Integer.toHexString(new Random().nextInt());
                var password = Integer.toHexString(new Random().nextInt());
                authManager.register(user, password);
                var token = authManager.authorize(user, password);
                authManager.logout(token);

                if (request instanceof CommandRequest commandRequest) {
                    sendingManager.send(address, commandManager.execute(commandRequest));
                }

                if (request instanceof ValidationRequest validationRequest) {
                    sendingManager.send(address, commandManager.validate(validationRequest));
                }

                if (request instanceof PingRequest) {
                    sendingManager.send(address, new PingResponse());
                }

            } catch (IOException e) {
                logger.error("Error occurred while I/O.\n{}", e.getMessage());
            } catch (ClassNotFoundException e) {
                logger.error("Error. Invalid response format from server");
            }
        }
    }
}