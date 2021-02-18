package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Email;
import mk.ukim.finki.deals_n_steals.model.exception.BadEmailFormat;
import mk.ukim.finki.deals_n_steals.repository.jpa.EmailRepository;
import mk.ukim.finki.deals_n_steals.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;

    @Autowired
    private JavaMailSender emailSender;

    public EmailServiceImpl(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public Email subscribe(String email){
        if(email == null || email.isEmpty()){
            throw new BadEmailFormat();
        }
        return emailRepository.save(new Email(email, true));
    }

    public void sendSimpleMessage(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dealss.n.stealss@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void notifyAllEmails() {
        List<Email> emails = emailRepository.findAll();
        emails.forEach(email -> sendSimpleMessage(email.getEmail(), "New Product", "We have a new product on our website, you can check it if you like"));
    }


}
