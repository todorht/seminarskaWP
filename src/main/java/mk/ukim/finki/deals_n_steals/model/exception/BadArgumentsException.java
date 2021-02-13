package mk.ukim.finki.deals_n_steals.model.exception;

public class BadArgumentsException extends RuntimeException {
    public BadArgumentsException(){
        super("Invalid arguments");
    }
}
