package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.GameDAO;

public class ClearService{
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void clear(){
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }
}