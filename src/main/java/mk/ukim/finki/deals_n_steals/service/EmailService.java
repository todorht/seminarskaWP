package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.Email;

public interface EmailService {
    public void sendSimpleMessage(String to, String subject, String text);
    public void notifyAllEmails();
    public Email subscribe(String email);
    public void sendMessageWithAttachment(String to, String subject, String text, Long orderNumber);
}
