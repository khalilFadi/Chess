package dataaccess;

import model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;
public class UserDAO {

    public UserDAO() throws DataAccessException {
        configureDatabase();
    }
    private void configureDatabase() throws DataAccessException{
        var createUserTable = """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) NOT NULL PRIMARY KEY,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
            )""";
        try (var conn = DatabaseManager.getConnection()){
            try (var peraparedStatement = conn.prepareStatement(createUserTable))
            {
                peraparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new DataAccessException("unable to configure database", e);
        }
    }
    public void createUser(User user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()); //just hashing
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatemenet = conn.prepareStatement(statement)){
                preparedStatemenet.setString(1, user.getUsername());
                preparedStatemenet.setString(2, hashedPassword);
                preparedStatemenet.setString(3, user.getEmail());
                preparedStatemenet.executeUpdate();
            }
        } catch (SQLException x){
            if(x.getErrorCode() == 1062){
                throw new DataAccessException("User already exists");
            }
            throw new DataAccessException("Unable to create user", x);
        }
    }

    public User getUser(String username) throws DataAccessException {
        var statement = "SELECT username, password, email FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, username);
                try(var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        var user = new User();
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setEmail(rs.getString("email"));
                        return user;
                    }
                }
            }
        }catch (SQLException e){
            throw new DataAccessException("unable to read user", e);

        }
        throw new DataAccessException("User not found");
    }

    public void clear() throws DataAccessException{
        var statement = "DELETE FROM users";
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new DataAccessException("Unable to clear users", e);
        }
    }
}