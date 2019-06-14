package sk.dudak.eduport.security.jwt;

public class NonExistentUsernameException extends Exception {
    public NonExistentUsernameException(String message) {
        super(message);
    }
}
