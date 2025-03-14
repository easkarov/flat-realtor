package se.ifmo.lab07.network;

import se.ifmo.lab07.Configuration;
import se.ifmo.lab07.dto.Credentials;
import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.dto.response.Response;

import javax.naming.TimeLimitExceededException;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class Client implements AutoCloseable {
    private static final int BATCH = Configuration.BATCH;

    private static final long MAX_DELAY = Configuration.MAX_DELAY;

    private final DatagramChannel connection;

    private final InetSocketAddress address;

    private Credentials credentials;


    private Client(DatagramChannel connection, InetSocketAddress address) {
        this.connection = connection;
        this.address = address;
    }

    public static Client connect(String host, int port) throws IOException {
        var dc = DatagramChannel.open();
        dc.configureBlocking(false);
        return new Client(dc, new InetSocketAddress(host, port));
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials credentials() {
        return credentials;
    }

    public void send(byte[] data) throws IOException {
        int n = (int) Math.ceil((double) data.length / (BATCH - 1));
        for (int i = 0; i < n; i++) {
            byte[] batch = new byte[BATCH];
            System.arraycopy(data, i * (BATCH - 1), batch, 0, Math.min(data.length - i * (BATCH - 1), BATCH - 1));

            batch[BATCH - 1] = (byte) ((i + 1 == n) ? 1 : 0);
            connection.send(ByteBuffer.wrap(batch), address);
        }
    }

    public void send(Request request) throws IOException {
        request.setCredentials(credentials);
        var byteStream = new ByteArrayOutputStream();
        var objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(request);
        send(byteStream.toByteArray());
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
                if ((System.nanoTime() - startTime) > MAX_DELAY) {
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

    public Response sendAndReceive(Request request) throws IOException, ClassNotFoundException, TimeLimitExceededException {
        send(request);
        return receiveResponse();
    }

    public void close() throws IOException {
        connection.close();
    }

}