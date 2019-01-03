package dtos;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Email")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailDTO  implements Serializable{

    private String userEmail;
    private String password;
    private String subject;
    private String body;
    private String recipient;

    public EmailDTO() {
    }

    public EmailDTO(String userEmail, String password, String subject, String body, String recipient) {
        this.userEmail = userEmail;
        this.password = password;
        this.subject = subject;
        this.body = body;
        this.recipient = recipient;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}