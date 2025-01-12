package com.example.gestion_activityrecognition.Utils;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String recipient, String subject, String body) throws MessagingException {
        // Paramètres SMTP (remplacez avec votre propre fournisseur SMTP)
        String host = "smtp.mailtrap.io";  // Par exemple, vous pouvez utiliser Mailtrap pour le test
        String username = "2f050ab03cc908";  // Remplacez par votre nom d'utilisateur SMTP
        String password = "6137f3c3965e8c";  // Remplacez par votre mot de passe SMTP

        // Propriétés de la connexion SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "2525");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        // Crée une session SMTP avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Crée le message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("Majdoub.Syrine@esprit.tn"));  // Remplacez par votre email
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(body);

        // Envoi du message
        Transport.send(message);
    }
}
