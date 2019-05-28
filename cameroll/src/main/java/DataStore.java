import data.*;
import request.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class DataStore {

    public DataStore(){

    }

    public Movie[] getMovies(){
        try(Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            ArrayList<Movie> movies=new ArrayList<>();
            ResultSet result = statement.executeQuery("select * from movies");
            while(result.next()){
                Movie movie=new Movie();
                movie.setId(result.getInt("id"));
                movie.setName(result.getString("name"));
                movie.setDescription(result.getString("description"));
                movie.setActors(result.getString("actors"));
                movie.setCountry(result.getString("country"));
                movie.setGenre(result.getString("genre"));
                movie.setUrl(result.getString("url"));
                movies.add(movie);
            }
            Movie[] response=new Movie[movies.size()];
            for(int i=0; i<movies.size(); i++){
                response[i]=movies.get(i);
            }
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Session getSessionById(int id){
        try(Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            ResultSet result = statement.executeQuery("select * from sessions where id="+id);
            while(result.next()){
                Session session=new Session();
                session.setPattern(result.getString("pattern"));
                return session;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cinema[] getCinemas(){
        try(Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            ArrayList<Cinema> cinemas=new ArrayList<>();
            ResultSet result = statement.executeQuery("select * from cinemas");
            while(result.next()){
                Cinema cinema=new Cinema();
                cinema.setId(result.getInt("id"));
                cinema.setName(result.getString("name"));
                cinema.setAddress(result.getString("address"));
                cinemas.add(cinema);
            }
            Cinema[] response=new Cinema[cinemas.size()];
            for(int i=0; i<cinemas.size(); i++){
                response[i]=cinemas.get(i);
            }
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Session[] getSessionByMovie(Movie movie){
        try(Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            ArrayList<Session> sessions=new ArrayList<>();
            ResultSet result = statement.executeQuery("select * from sessions where movie="+movie.getId());
            while(result.next()){
                Session session=new Session();
                session.setId(result.getInt("id"));
                session.setMovie(movie);
                session.setTime(result.getString("time"));
                session.setDate(result.getString("date"));
                session.setPrice(result.getInt("price"));
                session.setPattern(result.getString("pattern"));
                Statement cStatement=connection.createStatement();
                ResultSet cinema=cStatement.executeQuery("select * from cinemas where id="+result.getInt("cinema"));
                if (cinema.next()){
                    Cinema sessionCinema=new Cinema();
                    sessionCinema.setId(cinema.getInt("id"));
                    sessionCinema.setName(cinema.getString("name"));
                    sessionCinema.setAddress(cinema.getString("address"));
                    session.setCinema(sessionCinema);
                }
                Statement hStatement=connection.createStatement();
                ResultSet hall=hStatement.executeQuery("select * from halls where id="+result.getInt("hall"));
                if (hall.next()){
                    Hall sessionHall=new Hall();
                    sessionHall.setId(hall.getInt("id"));
                    sessionHall.setName(hall.getString("name"));
                    session.setHall(sessionHall);
                }
                sessions.add(session);
            }
            Session[] response=new Session[sessions.size()];
            for(int i=0; i<sessions.size(); i++){
                response[i]=sessions.get(i);
            }
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Session[] getSessionByCinema(Cinema cinema){
        try(Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            ArrayList<Session> sessions=new ArrayList<>();
            ResultSet result = statement.executeQuery("select * from sessions where cinema="+cinema.getId());
            while(result.next()){
                Session session=new Session();
                session.setId(result.getInt("id"));
                session.setCinema(cinema);
                session.setTime(result.getString("time"));
                session.setDate(result.getString("date"));
                session.setPrice(result.getInt("price"));
                session.setPattern(result.getString("pattern"));
                Statement mStatement=connection.createStatement();
                ResultSet movie=mStatement.executeQuery("select * from movies where id="+result.getInt("movie"));
                if (movie.next()){
                    Movie sessionMovie=new Movie();
                    sessionMovie.setId(movie.getInt("id"));
                    sessionMovie.setName(movie.getString("name"));
                    sessionMovie.setDescription(movie.getString("description"));
                    sessionMovie.setActors(movie.getString("actors"));
                    sessionMovie.setCountry(movie.getString("country"));
                    sessionMovie.setGenre(movie.getString("genre"));
                    sessionMovie.setUrl(movie.getString("url"));
                    session.setMovie(sessionMovie);
                }
                Statement hStatement=connection.createStatement();
                ResultSet hall=hStatement.executeQuery("select * from halls where id="+result.getInt("hall"));
                if (hall.next()){
                    Hall sessionHall=new Hall();
                    sessionHall.setId(hall.getInt("id"));
                    sessionHall.setName(hall.getString("name"));
                    session.setHall(sessionHall);
                }
                sessions.add(session);
            }
            Session[] response=new Session[sessions.size()];
            for(int i=0; i<sessions.size(); i++){
                response[i]=sessions.get(i);
            }
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User isUserExist(SignInRequest request){
        try(Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            ArrayList<User> users=new ArrayList<>();
            ResultSet result = statement.executeQuery(
                    "select * from user where name='"+request.getEntrance().getName()+"' and password='"+request.getEntrance().getPassword()+"'");
            while(result.next()){
                User user = new User();
                user.setId(result.getInt("id"));
                user.setName(result.getString("name"));
                user.setPassword(result.getString("password"));
                user.setManager(result.getInt("manager")==1);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createNewUser(SignUpRequest request){
        try(Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            ArrayList<User> users=new ArrayList<>();
            int manager=request.getRegistration().isManager()?1:0;
            statement.execute("insert into user (name, password, manager) values ('"+request.getRegistration().getName().trim()+"', '"+request.getRegistration().getPassword().trim()+"', "+manager+")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Session getHall(ChooseRequest request){
        return getHall(request.getSession().getHall(), request.getSession ().getPattern(), (int)request.getSession().getId());
    }

    private Session getHall(Hall sessionHall, String sessionPattern, int sessionId){
        try(Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            int affected = statement.executeUpdate("update sessions set pattern='"+sessionPattern+"' where id="+sessionId);
            ResultSet result=connection.createStatement().executeQuery("select * from sessions where id="+sessionId);
            while(result.next()) {
                Session session = new Session();
                session.setId(result.getInt("id"));
                session.setHall(sessionHall);
                session.setTime(result.getString("time"));
                session.setDate(result.getString("date"));
                session.setPrice(result.getInt("price"));
                session.setPattern(result.getString("pattern"));
                Statement cStatement = connection.createStatement();
                ResultSet cinema = cStatement.executeQuery("select * from cinemas where id=" + result.getInt("cinema"));
                if (cinema.next()) {
                    Cinema sessionCinema = new Cinema();
                    sessionCinema.setId(cinema.getInt("id"));
                    sessionCinema.setName(cinema.getString("name"));
                    sessionCinema.setAddress(cinema.getString("address"));
                    session.setCinema(sessionCinema);
                }
                Statement mStatement=connection.createStatement();
                ResultSet movie=mStatement.executeQuery("select * from movies where id="+result.getInt("movie"));
                if (movie.next()){
                    Movie sessionMovie=new Movie();
                    sessionMovie.setId(movie.getInt("id"));
                    sessionMovie.setName(movie.getString("name"));
                    sessionMovie.setDescription(movie.getString("description"));
                    sessionMovie.setActors(movie.getString("actors"));
                    sessionMovie.setCountry(movie.getString("country"));
                    sessionMovie.setGenre(movie.getString("genre"));
                    sessionMovie.setUrl(movie.getString("url"));
                    session.setMovie(sessionMovie);
                }
                return session;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
