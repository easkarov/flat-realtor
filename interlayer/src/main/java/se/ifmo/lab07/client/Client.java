package se.ifmo.lab07.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.dto.response.Response;

import javax.naming.TimeLimitExceededException;
import java.io.*;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class Client implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final int BATCH = 1024;

    private long timeout = 2_000_000_000;

    private final DatagramChannel connection;

    private Client(DatagramChannel connection) {
        this.connection = connection;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public static Client connect() throws IOException {
        var dc = DatagramChannel.open();
        dc.configureBlocking(false);
        return new Client(dc);
    }

    public void send(SocketAddress address, byte[] data) throws IOException {
        int n = (int) Math.ceil((double) data.length / (BATCH - 1));
        for (int i = 0; i < n; i++) {
            byte[] batch = new byte[BATCH];
            System.arraycopy(data, i * (BATCH - 1), batch, 0, Math.min(data.length - i * (BATCH - 1), BATCH - 1));

            batch[BATCH - 1] = (byte) ((i + 1 == n) ? 1 : 0);
            connection.send(ByteBuffer.wrap(batch), address);
            logger.info("Batch {}/{} has been sent", i + 1, n);
        }
    }

    public void send(SocketAddress address, Request request) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        var objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(request);
        send(address, byteStream.toByteArray());
        logger.info("Request <{}> sent to {}", request, address);
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException, TimeLimitExceededException {
        byte[] data = receive();
        var byteStream = new ByteArrayInputStream(data);
        var objectStream = new ObjectInputStream(byteStream);

        return (Response) objectStream.readObject();
    }

    public byte[] receive() throws IOException, TimeLimitExceededException {
        ByteBuffer buffer = ByteBuffer.allocate(BATCH);
        SocketAddress address = null;
        var data = new ByteArrayOutputStream();

        var startTime = System.nanoTime();
        while (true) {
            while (address == null) {
                if ((System.nanoTime() - startTime) > timeout) {
                    throw new TimeLimitExceededException("Server not available. Try later");
                }
                address = connection.receive(buffer);
            }

            data.write(Arrays.copyOf(buffer.array(), BATCH - 1));
            if (buffer.array()[BATCH - 1] == (byte) 1) {
                return data.toByteArray();
            }
            address = null;
            buffer.clear();
        }
    }

    public Response sendAndReceive(SocketAddress address, Request request) throws IOException, ClassNotFoundException, TimeLimitExceededException {
        send(address, request);
        return receiveResponse();
    }

    public void close() throws IOException {
        connection.close();
    }

}