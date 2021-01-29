package mk.ukim.finki.deals_n_steals.model.exception;


public class UserNameExistsException extends RuntimeException{
    public UserNameExistsException(String username){
        super(String.format("User with username: %s already exists", username));
    }
}
