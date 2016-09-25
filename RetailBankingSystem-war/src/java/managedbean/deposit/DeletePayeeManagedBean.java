package managedbean.deposit;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import ejb.deposit.session.PayeeSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;

@Named(value = "deletePayeeManagedBean")
@RequestScoped

public class DeletePayeeManagedBean implements Serializable {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private PayeeSessionBeanLocal payeeSessionLocal;

    private String payeeAccountNum;
    private String payeeAccountType;
    private String statusMessage;
    private Long payeeId;

    private ExternalContext ec;

    public DeletePayeeManagedBean() {
    }

    public String getPayeeAccountNum() {
        return payeeAccountNum;
    }

    public void setPayeeAccountNum(String payeeAccountNum) {
        this.payeeAccountNum = payeeAccountNum;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public void deletePayee() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/DeletePayeeManagedBean: deletePayee() ======");
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        Payee payee = payeeSessionLocal.retrievePayeeByNum(payeeAccountNum);

        if (payee.getPayeeId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Recipient does not exist.", "Failed!"));
            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "delete recipient", "failed", "Recipient does not exist");
        } else {
            String delete = payeeSessionLocal.deletePayee(payeeAccountNum);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfuly! Recipient deleted Successfully.", "Successfuly!"));
            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "delete recipient", "successful", null);
        }
    }

}
