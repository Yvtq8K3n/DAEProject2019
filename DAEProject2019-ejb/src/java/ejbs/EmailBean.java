package ejbs;

import dtos.EmailDTO;
import java.security.Security;
import java.util.List;
import java.util.Properties;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/email")
public class EmailBean {

    //@Resource(name="mail/gmail")  //I did not use, to not cause problems to all
    //private Session session;      //This session is based on the glashfish version
                                    //Basicaly it sets the property i set as Properties
    private String mailhost = "smtp.gmail.com";
    //private String mailhost= "mail.ipleiria.pt"; 
    
    /*
    @Resource(name = "mail/dae")
    private Session mailSession;
    */
    
    @POST
    @Path("/send")
    @PermitAll
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response send(EmailDTO email) throws AddressException, MessagingException 
    {
        /*//Não funcionou
        Message message = new MimeMessage(mailSession);

        try {
            // Adjust the recipients. Here we have only one recipient.
            // The recipient's address must be an object of the InternetAddress class.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getRecipients().get(0), false));

            // Set the message's subject
            message.setSubject(email.getSubject());

            // Insert the message's body
            message.setText(email.getBody());

            // Adjust the date of sending the message
            Date timeStamp = new Date();
            message.setSentDate(timeStamp);

            // Use the 'send' static method of the Transport class to send the message
            Transport.send(message);

        } catch (MessagingException e) {
            throw e;
        }*/
        
        String userEmail = email.getUserEmail();
        String password = email.getPassword();
        String subject = email.getSubject();
        String body = email.getBody();
        String recipient = email.getRecipient();
        
        
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", true);
        props.setProperty("mail.smtp.quitwait", "false");
        
        Session session = Session.getInstance(props,
                   new javax.mail.Authenticator() 
        {
              protected PasswordAuthentication getPasswordAuthentication()
              {
                  return new PasswordAuthentication(userEmail,"secret123456789");
              }     
        });
        
        MimeMessage message = new MimeMessage(session);
        message.setSender(new InternetAddress(userEmail));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");
        
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
     
        Transport.send(message);   
        
        return Response.status(Response.Status.OK).entity("Mail was successfully sent").build();
    }
}