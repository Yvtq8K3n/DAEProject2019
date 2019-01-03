package ejbs;

import dtos.EmailDTO;
import java.security.Security;
import java.util.List;
import java.util.Properties;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
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
    
    @POST
    @RolesAllowed({"Administrator","Client"})
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public synchronized Response send(EmailDTO email) throws AddressException, MessagingException 
    {
        String userEmail = email.getUserEmail();
        String password = email.getPassword();
        String subject = email.getSubject();
        String body = email.getBody();
        List<String> recipients = email.getRecipients();
        
        
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", true);
        props.setProperty("mail.smtp.quitwait", "false");
        
        //Whitout Authentication
        Session session = Session.getDefaultInstance(props);
        
        //Whit Authentication
        /*Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication()
              { return new PasswordAuthentication(userEmail,password);}     
        });*/
        
        MimeMessage message = new MimeMessage(session);
        message.setSender(new InternetAddress(userEmail));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");
        

        for(String recipient:recipients){
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }
     
        Transport.send(message);   
        
        return Response.status(Response.Status.CREATED).entity("Email successfully sent.").build();
    }
}