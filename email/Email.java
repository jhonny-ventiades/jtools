package com.avatech.msfalcos.tools.email;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email {


    String emailPort = "465";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    String emailHost = "smtp.gmail.com";

    private String fromEmail;
    private String fromPassword;
    private List<String> toEmailList;
    private String emailSubject;
    private String emailBody;
    private String urlFile;
    private String nameFile;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;


    public Email() {

    }

    public Email(
            String fromEmail,
            String fromPassword,
            String emailPort,
            String emailHost,
            List<String> toEmailList,
            String emailSubject,
            String emailBody,
            String urlFile,
            String nameFile) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        this.urlFile = urlFile;
        this.nameFile = nameFile;
        this.emailHost=emailHost;
        this.emailPort=emailPort;

        emailProperties = System.getProperties();
//			emailProperties.put("mail.smtp.user", this.fromEmail);
        emailProperties.put("mail.smtp.host", this.emailHost);
        emailProperties.put("mail.smtp.port", this.emailPort);
        emailProperties.put("mail.smtp.starttls.enable",this.starttls);
        emailProperties.put("mail.smtp.debug", "true");
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.socketFactory.port", this.emailPort);
        emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        emailProperties.put("mail.smtp.socketFactory.fallback", "false");
        System.out.println("Mail server properties set.");
    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);
        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        for (String toEmail : toEmailList) {
            Log.i("GMail","toEmail: "+toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));
        }
        emailMessage.setSubject(emailSubject);
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message
        messageBodyPart.setText(emailBody);

        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(urlFile);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(nameFile);
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        emailMessage.setContent(multipart );

        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(this.emailHost, this.fromEmail, this.fromPassword);
        Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }


}