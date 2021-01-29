package mk.ukim.finki.deals_n_steals.model.exception;

public class PasswordDoNotMatchException extends RuntimeException{
    public PasswordDoNotMatchException(){
        super("Passwords do not match");
    }
}
