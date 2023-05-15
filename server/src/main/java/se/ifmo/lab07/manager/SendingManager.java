package se.ifmo.lab07.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.dto.response.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class SendingManager {
    private static final Logger logger = LoggerFactory.getLogger(SendingManager.class);
    private static final int BATCH = 1024;

    private final DatagramSocket connection;

    public SendingManager(DatagramSocket connection) {
        this.connection = connection;
    }

    public void send(SocketAddress address, byte[] data) throws IOException {
        int n = (int) Math.ceil((double) data.length / (BATCH - 1));

        for (int i = 0; i < n; i++) {
            byte[] batch = new byte[BATCH];
            System.arraycopy(data, i * (BATCH - 1), batch, 0, Math.min(data.length - i * (BATCH - 1), BATCH - 1));
            batch[BATCH - 1] = (byte) ((i + 1 == n) ? 1 : 0);
            DatagramPacket dp = new DatagramPacket(batch, batch.length, address);
            connection.send(dp);
            logger.info("Batch {}/{} has been sent", i + 1, n);
        }
    }

    public void send(SocketAddress address, Response response) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        var objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(response);
        send(address, byteStream.toByteArray());
        logger.info("Response <{}> sent to {}", response.getClass().getSimpleName(), address);
    }
}
