import data.Movie;
import request.GetMoviesRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Thread {

    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isConnected=true;

    public Connection(Socket client){
        this.client=client;
        try {
            in=new ObjectInputStream(client.getInputStream());
            out=new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        while(isConnected){
            try {
                GetMoviesRequest request= (GetMoviesRequest) in.readObject();
                out.writeObject(new Movie("Lion King", "fuck", "", "family", "USA"));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
