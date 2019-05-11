import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Main {

    public static final int PORT = 8080;
    public static LinkedList<Connection> serverList = new LinkedList<>(); // список всех нитей

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();
                serverList.add(new Connection(socket)); // добавить новое соединенние в список
            }
        } finally {
            server.close();
        }
    }
}
