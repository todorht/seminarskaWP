package mk.ukim.finki.deals_n_steals.service.impl;

import com.itextpdf.text.pdf.codec.Base64;
import com.sun.istack.ByteArrayDataSource;
import mk.ukim.finki.deals_n_steals.model.Email;
import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.exception.BadEmailFormat;
import mk.ukim.finki.deals_n_steals.repository.EmailRepository;
import mk.ukim.finki.deals_n_steals.service.EmailService;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    static String FILEPATH = "/static/prazen";
    static File file = new File(FILEPATH);

    private final EmailRepository emailRepository;
    private final OrderService orderService;

    @Autowired
    private JavaMailSender emailSender;

    public EmailServiceImpl(EmailRepository emailRepository, OrderService orderService) {
        this.emailRepository = emailRepository;
        this.orderService = orderService;
    }

    public Email subscribe(String email){
        if(email == null || email.isEmpty()){
            throw new BadEmailFormat();
        }
        return emailRepository.save(new Email(email, true));
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, Long orderNumber) {
//
        MimeMessagePreparator preparator = new MimeMessagePreparator()
        {
            public void prepare(MimeMessage mimeMessage) throws Exception
            {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom("dealss.n.stealss@gmail.com");
                mimeMessage.setSubject(subject);
                mimeMessage.setText(text);
                Order order = orderService.findByOrderNumber(orderNumber);
                OutputStream os = new FileOutputStream(file);
                os.write(order.getPdf());
                os.flush();
                os.close();
//                FileUtils.writeByteArrayToFile(file, order.getPdf());
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.addAttachment("order" + orderNumber + ".pdf", file);
            }
        };

        try {
            emailSender.send(preparator);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
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
