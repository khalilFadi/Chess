package service;
import  dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.User;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.LoginResponse;
import org.mindrot.jbcrypt.BCrypt;
public class UserService{
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResponse register(RegisterRequest request) throws DataAccessException{
        if(request.getUsername() == null || request.getPassword() == null || request.getEmail() == null){
            throw new DataAccessException("bad request");
        }
        try{
            userDAO.getUser(request.getUsername());
            throw new DataAccessException("already taken");
        }catch (DataAccessException e){
            if(e.getMessage().equals("User not found")){
                //maybe create user directly or redirect to the sign up page
            }else {
                throw new DataAccessException("already taken");
            }
        }
        User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
        userDAO.createUser(user);
        String authToken = authDAO.createAuth(user.getUsername());
        return new LoginResponse(user.getUsername(), authToken);
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException{
        if(request.getUsername() == null || request.getPassword() == null){
            throw new DataAccessException("bad request");
        }
        User user = userDAO.getUser(request.getUsername());
        if(!BCrypt.checkpw(request.getPassword(), user.getPassword())){
            throw new DataAccessException("unauthorized");
        }
        String authToken = authDAO.createAuth(user.getUsername());
        return new LoginResponse(user.getUsername(), authToken);
    }
    public void logout(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }
}