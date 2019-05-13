import data.Cinema;
import data.Hall;
import data.Movie;
import data.Session;

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
                    sessionHall.setPlacePattern(hall.getString("pattern"));
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
                    session.setMovie(sessionMovie);
                }
                Statement hStatement=connection.createStatement();
                ResultSet hall=hStatement.executeQuery("select * from halls where id="+result.getInt("hall"));
                if (hall.next()){
                    Hall sessionHall=new Hall();
                    sessionHall.setId(hall.getInt("id"));
                    sessionHall.setName(hall.getString("name"));
                    sessionHall.setPlacePattern(hall.getString("pattern"));
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

}
