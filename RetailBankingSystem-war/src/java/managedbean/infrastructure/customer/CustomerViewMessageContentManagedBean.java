package managedbean.infrastructure.customer;

import ejb.deposit.entity.MessageBox;
import ejb.infrastructure.session.MessageSessionBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named(value = "customerViewMessageContentManagedBean")
@RequestScoped

public class CustomerViewMessageContentManagedBean implements Serializable {
    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    private String fromWhere;
    private String messageType;
    private String subject;
    private String receivedDate;
    private String messageContent;
    
    private Long messageBoxId;
    
    public CustomerViewMessageContentManagedBean() {
    }

    public String getFromWhere() {
        MessageBox message = messageSessionBeanLocal.retrieveMessageBoxById(messageBoxId);
        fromWhere = message.getFromWhere();
        
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getMessageType() {
        MessageBox message = messageSessionBeanLocal.retrieveMessageBoxById(messageBoxId);
        messageType = message.getMessageType();
        
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSubject() {
        MessageBox message = messageSessionBeanLocal.retrieveMessageBoxById(messageBoxId);
        subject = message.getSubject();
        
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReceivedDate() {
        MessageBox message = messageSessionBeanLocal.retrieveMessageBoxById(messageBoxId);
        receivedDate = message.getReceivedDate();
        
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getMessageContent() {
        MessageBox message = messageSessionBeanLocal.retrieveMessageBoxById(messageBoxId);
        messageContent = message.getMessageContent();
        
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getMessageBoxId() {
        return messageBoxId;
    }

    public void setMessageBoxId(Long messageBoxId) {
        this.messageBoxId = messageBoxId;
    }
    
    public void deleteMessage() {
        messageSessionBeanLocal.deleteMessage(messageBoxId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have successfully deleted your message.", "Successfully"));
    }
    
}
