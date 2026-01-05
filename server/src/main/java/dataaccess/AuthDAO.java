package dataaccess;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO {

    public AuthDAO() throws DataAccessException{
        configureDatabase();
    }
    public void configureDatabase() throws DataAccessException {
        var createAuthTable = """
                CREATE TABLE IF NOT EXISTS auth (
                    authToken VARCHAR(255) NOT NULL PRIMARY KEY,
                    username VARCHAR(255) NOT NULL
                )""";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createAuthTable)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException x) {
            throw new DataAccessException("Unable to configure database", x);
        }
    }
    public String createAuth(String username) throws DataAccessException{
        String authToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try(var  conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new DataAccessException("Unable to create auth token", e);
        }
        return authToken;
    }

    public String getUsername(String authToken) throws DataAccessException {
        var statement = "SELECT username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("username");
                    }
                }
            }
        }catch (SQLException e){
            throw new DataAccessException("Unable to read auth token", e);
        }
        throw new DataAccessException("Invalid auth token");
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, authToken);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected == 0){
                    throw new DataAccessException("Invalid auth token");
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Unable to delete auth token", e);
        }
    }

    public void clear() throws DataAccessException{
        var statement = "DELETE FROM auth";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatemnt = conn.prepareStatement(statement)){
                preparedStatemnt.executeUpdate();
            }
        }catch (SQLException e){
            throw new DataAccessException("Unable to clear auth token", e);
        }
    }
}