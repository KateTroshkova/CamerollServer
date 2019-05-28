import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Main {

    public static final int PORT = 8331;
    public static LinkedList<ConnectionThread> serverList = new LinkedList<>();
    public static boolean isRunning=true;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (isRunning) {
                Socket socket = server.accept();
                serverList.add(new ConnectionThread(socket));
            }
        } finally {
            server.close();
        }
    }
}
