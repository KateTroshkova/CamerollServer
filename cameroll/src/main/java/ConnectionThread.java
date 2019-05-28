import data.Entrance;
import data.Session;
import data.User;
import model.InputValidation;
import request.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionThread extends Thread {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isConnected=true;
    private DataStore store;

    public ConnectionThread(Socket client){
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
                if (request instanceof ChooseRequest){
                    postChoose((ChooseRequest)request);
                }
                if (request instanceof SignInRequest){
                    getSignIn((SignInRequest)request);
                }
                if (request instanceof SignUpRequest){
                    getSignUp((SignUpRequest)request);
                }
            } catch (SocketException e){
                e.printStackTrace();
                isConnected=false;
                Main.serverList.remove(this);
            }
            catch (IOException | ClassNotFoundException e) {
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
        catch (IOException ignored){

        }
    }

    private void getSignIn(SignInRequest request){
        InputValidation validation=new InputValidation();
        if (validation.validName(request.getEntrance().getName()) &&
                validation.validPassword(request.getEntrance().getPassword())){
            User user=store.isUserExist(request);
            if (user!=null){
                try {
                    out.writeInt(3);
                    out.writeObject(user);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                postError(1000);
            }
        }
        else{
           postError(1000);
        }
    }

    private synchronized void getSignUp(SignUpRequest request){
        InputValidation validation=new InputValidation();
        if (validation.validName(request.getRegistration().getName()) &&
                validation.validPassword(request.getRegistration().getPassword()) &&
                validation.validConfirm(request.getRegistration().getPassword(), request.getRegistration().getConfirm())){
            SignInRequest anotherRequest=new SignInRequest(new Entrance(request.getRegistration().getName(), request.getRegistration().getPassword()));
            User user=store.isUserExist(anotherRequest);
            if (user==null){
                store.createNewUser(request);
            }
            getSignIn(anotherRequest);
        }
        else{
            postError(1000);
        }
    }

    private synchronized void postChoose(ChooseRequest request){
        if (validAction(request)) {
            request.updatePattern();
            Session updated = store.getHall(request);
            try {
                out.writeInt(5);
                out.writeObject(updated);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            postError(2000);
        }
    }

    private boolean validAction(ChooseRequest request){
        String pattern=store.getSessionById((int) request.getSession().getId()).getPattern();
        String[] data=pattern.split(";");
        int height=Integer.valueOf(data[1]);
        return data[2+height*request.getY()+request.getX()].equals("0");
    }

    private void postError(int error){
        try {
            out.writeInt(4);
            out.writeInt(error);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
