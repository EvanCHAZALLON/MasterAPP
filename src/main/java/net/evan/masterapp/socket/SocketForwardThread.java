package net.evan.masterapp.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SocketForwardThread extends Thread {

    private static final int READ_BUFFER_SIZE = 8192;

    private InputStream inputStream;
    private OutputStream outputStream;
    private SocketServerClient parent;

    public SocketForwardThread(SocketServerClient parent, InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.parent = parent;
    }

    public void run() {
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        try {
            while (true) {
                int bytesRead = inputStream.read(buffer);
                if (bytesRead == -1)
                    break;
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {}
        parent.connectionBroken();
    }

}
