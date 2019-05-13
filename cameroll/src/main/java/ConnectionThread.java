import data.Movie;
import request.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionThread extends Thread {

    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isConnected=true;
    private DataStore store;

    public ConnectionThread(Socket client){
        this.client=client;
        store=new DataStore();
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
                Request request= (Request) in.readObject();
                if (request instanceof GetMoviesRequest){
                    getMovie();
                }
                if (request instanceof GetCinemasRequest){
                    getCinema();
                }
                if (request instanceof GetSessionRequest){
                    getSession((GetSessionRequest)request);
                }
            } catch (SocketException e){
                e.printStackTrace();
                isConnected=false;
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void getMovie(){
        try {
            out.writeInt(0);
            out.writeObject(store.getMovies());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCinema(){
        try {
            out.writeInt(1);
            out.writeObject(store.getCinemas());
            out.flush();
            System.out.println("cinemas done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSession(GetSessionRequest request){
        try {
            if (request.getMovie() != null) {
                out.writeInt(2);
                out.writeObject(store.getSessionByMovie(request.getMovie()));
                out.flush();
            }
            if (request.getCinema()!=null){
                out.writeInt(2);
                out.writeObject(store.getSessionByCinema(request.getCinema()));
                out.flush();
            }
            System.out.println("sessions done");
        }
        catch (IOException e){

        }
    }

    private void getSignIn(SignInRequest request){

    }

    private void getSignUp(SignUpRequest request){

    }

    private void postBook(BookRequest request){

    }

    private void postBuy(BuyRequest request){

    }

    private void postRefuse(RefuseRequest request){

    }

    private void getHallChange(){

    }
}
