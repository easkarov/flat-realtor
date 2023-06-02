package se.ifmo.lab07.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.request.GetCommandsRequest;
import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.dto.request.ValidationRequest;
import se.ifmo.lab07.dto.response.GetCommandsResponse;
import se.ifmo.lab07.dto.response.PingResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.manager.CommandManager;
import se.ifmo.lab07.manager.ReceivingManager;
import se.ifmo.lab07.manager.SendingManager;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int PROCESSING_THREADS = 8;

    private final DatagramSocket connection;
    private final CommandManager commandManager;
    private final ReceivingManager receivingManager;
    private final SendingManager sendingManager;
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(PROCESSING_THREADS);

    public Server(CommandManager commandManager, int port) throws SocketException {
        this.connection = new DatagramSocket(port);
        this.commandManager = commandManager;
        this.receivingManager = new ReceivingManager(connection);
        this.sendingManager = new SendingManager(connection);
        logger.info("Server started on {} port", port);
    }

    @Override
    public void close() {
        this.connection.close();
        logger.info("Server has been stopped. Connection closed");
    }

    public Response processRequest(Request request) {
        if (request instanceof CommandRequest commandRequest) {
            return commandManager.execute(commandRequest);
        } else if (request instanceof ValidationRequest validationRequest) {
            return commandManager.validate(validationRequest);
        } else if (request instanceof GetCommandsRequest) {
            return new GetCommandsResponse(commandManager.getCommandsDTO());
        } else {
            return new PingResponse();
        }
    }

    public void run() {
        while (true) {
            try {
                var pair = receivingManager.receiveRequest();
                var address = pair.getKey();
                var request = pair.getValue();
                fixedThreadPool.submit(() -> {
                    var response = processRequest(request);
                    new Thread(() -> sendingManager.send(address, response)).start();
                });
            } catch (IOException e) {
                logger.error("Error occurred while I/O.\n{}", e.getMessage());
            } catch (ClassNotFoundException e) {
                logger.error("Error. Invalid response format from client.\n{}", e.getMessage());
            }
        }
    }
}