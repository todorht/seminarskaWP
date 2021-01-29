package mk.ukim.finki.deals_n_steals.model.exception;


public class InvalidArgumentsException extends RuntimeException{
    public InvalidArgumentsException(){
        super("Invalid username or password.");
    }
}
