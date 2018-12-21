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
    private List<String> recipients;

    public EmailDTO() {
    }

    public EmailDTO(String userEmail, String password, String subject, String body, List<String> recipients) {
        this.userEmail = userEmail;
        this.password = password;
        this.subject = subject;
        this.body = body;
        this.recipients = recipients;
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

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }
}