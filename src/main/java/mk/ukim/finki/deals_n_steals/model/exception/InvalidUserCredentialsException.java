package mk.ukim.finki.deals_n_steals.model.exception;



public class InvalidUserCredentialsException extends RuntimeException {
    public InvalidUserCredentialsException(){
        super("Invalid User");
    }
}
