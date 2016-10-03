package managedbean.payment;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import ejb.payement.session.FastPayeeSessionBeanLocal;
import ejb.payment.entity.FastPayee;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "deleteFastPayeeManagedBean")
@RequestScoped

public class DeleteFastPayeeManagedBean {
    
    @EJB
    private FastPayeeSessionBeanLocal fastPayeeSessionBeanLocal;

    private String payeeAccountNum;
    
    private ExternalContext ec;
    
    public DeleteFastPayeeManagedBean() {
    }

    public String getPayeeAccountNum() {
        return payeeAccountNum;
    }

    public void setPayeeAccountNum(String payeeAccountNum) {
        this.payeeAccountNum = payeeAccountNum;
    }
    
    public List<FastPayee> getFastPayees() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<FastPayee> fastPayee = fastPayeeSessionBeanLocal.retrieveFastPayeeByCusId(customerBasic.getCustomerBasicId());

        return fastPayee;
    }
    
    public void deletePayee() throws IOException {
        
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        FastPayee fastPayee = fastPayeeSessionBeanLocal.retrieveFastPayeeByNum(payeeAccountNum);

        if (fastPayee.getFastPayeeId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Recipient does not exist.", "Failed!"));
        } else {
            String delete = fastPayeeSessionBeanLocal.deleteFastPayee(payeeAccountNum);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfuly! Recipient deleted Successfully.", "Successfuly!"));
        }
    }
}
