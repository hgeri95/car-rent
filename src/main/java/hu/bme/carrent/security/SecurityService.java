package hu.bme.carrent.security;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
