package managedbean.infrastructure.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.MessageBox;
import ejb.infrastructure.session.MessageSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "customerViewMessageManagedBean")
@RequestScoped

public class CustomerViewMessageManagedBean implements Serializable{
    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    private ExternalContext ec;
    
    public CustomerViewMessageManagedBean() {
    }
 
    public List<MessageBox> getMessageBox() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        String customerIC = customerBasic.getCustomerIdentificationNum();

        List<MessageBox> messageBox = messageSessionBeanLocal.retrieveMessageBoxByCusIC(customerIC.toUpperCase());
        System.out.println("////////message number//////" + messageBox.size() + "///////message content//" + messageBox.get(messageBox.size()- 1).getMessageContent());
        if (messageBox.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your identification is invalid", "Failed!"));
        }

        return messageBox;
    }
    
}
