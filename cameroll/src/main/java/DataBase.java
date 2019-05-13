import java.sql.*;

public class DataBase {

    /**public static void main(String[] args){
        try(Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/cameroll_data", "root", "root");
            Statement statement=connection.createStatement()){
            //statement.execute("insert into user(name, password, manager) values ('vasiutkina', 'ia', '0')");
            ResultSet result = statement.executeQuery("select * from sessions where cinema=1");
            while(result.next()){
                int movieId = result.getInt("movie");
                Statement s1=connection.createStatement();
                ResultSet result1=s1.executeQuery("select * from movies where id="+movieId);
                while(result1.next()){
                    System.out.println(result1.getString("name"));
                }
                s1.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
