package mk.ukim.finki.deals_n_steals.service;

import com.stripe.exception.*;
import com.stripe.model.Charge;
import mk.ukim.finki.deals_n_steals.model.ChargeRequest;

public interface PaymentService {
    Charge pay(ChargeRequest chargeRequest) throws CardException,
            APIException,
            AuthenticationException,
            InvalidRequestException,
            APIConnectionException;

}
