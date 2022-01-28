

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private MimeMessagePreparator messagePreparator;

    @Autowired
    private JavaMailSender javaMailSender;

    public EmailService() {
    }

    public void sendEmail(MimeMessagePreparator messagePreparator) {
        try {
            new Thread(new EmailSender(messagePreparator)).start();
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    class EmailSender implements Runnable {
        MimeMessagePreparator messagePreparator;

        public EmailSender(MimeMessagePreparator messagePreparator) {
            this.messagePreparator = messagePreparator;
        }

        @Override
        public void run() {
            try {
                javaMailSender.send(messagePreparator);
            } catch (MailException e) {
                e.printStackTrace();
            }
        }
    }
}
