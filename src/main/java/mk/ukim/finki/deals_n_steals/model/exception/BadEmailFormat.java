package mk.ukim.finki.deals_n_steals.model.exception;

public class BadEmailFormat extends RuntimeException{
    public BadEmailFormat(){
        super("Bad Email Format");
    }
}
