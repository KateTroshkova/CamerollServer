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
            //statement.execute("insert into user(name, password, manager) values ('vasiutkina', 'ia', '0')");
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
        /**Cinema[] result=new Cinema[4];
        result[0]=avrora;
        result[1]=park;
        result[2]=kino;
        result[3]=victory;
        return result;*/
        return null;
    }

    public Session[] getSessionByMovie(Movie movie){
        /**ArrayList<Session> result=new ArrayList<>();
        for(Session session:sessions){
            if (session.getMovie().equals(movie)){
                result.add(session);
            }
        }
        Session[] r=new Session[result.size()];
        for(int i=0; i<result.size(); i++){
            r[i]=result.get(i);
         }
         return r;*/
        return null;
    }

    public Session[] getSessionByCinema(Cinema cinema){
        /**ArrayList<Session> result=new ArrayList<>();
        for(Session session:sessions){
            if (session.getCinema().equals(cinema)){
                result.add(session);
            }
        }
        Session[] r=new Session[result.size()];
        for(int i=0; i<result.size(); i++){
            r[i]=result.get(i);
        }
        return r;*/
        return null;
    }

}
