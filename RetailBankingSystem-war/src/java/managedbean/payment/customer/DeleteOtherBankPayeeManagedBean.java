package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.PayeeSessionBeanLocal;
import ejb.payment.session.OtherBankPayeeSessionBeanLocal;
import ejb.payment.entity.OtherBankPayee;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "deleteOtherBankPayeeManagedBean")
@RequestScoped

public class DeleteOtherBankPayeeManagedBean {

    @EJB
    private PayeeSessionBeanLocal payeeSessionBeanLocal;

    @EJB
    private OtherBankPayeeSessionBeanLocal otherBankPayeeSessionBeanLocal;

    private String payeeAccountNum;

    private ExternalContext ec;

    public DeleteOtherBankPayeeManagedBean() {
    }

    public String getPayeeAccountNum() {
        return payeeAccountNum;
    }

    public void setPayeeAccountNum(String payeeAccountNum) {
        this.payeeAccountNum = payeeAccountNum;
    }

    public List<OtherBankPayee> getOtherBankPayees() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<OtherBankPayee> otherBankPayee = otherBankPayeeSessionBeanLocal.retrieveOtherBankPayeeByCusId(customerBasic.getCustomerBasicId());

        return otherBankPayee;
    }

    public void deletePayee() throws IOException {

        OtherBankPayee otherBankPayee = otherBankPayeeSessionBeanLocal.retrieveOtherBankPayeeByNum(payeeAccountNum);

        if (otherBankPayee.getPayeeId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Recipient does not exist.", "Failed!"));
        } else {
            payeeSessionBeanLocal.deletePayee(payeeAccountNum);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully! Recipient deleted Successfully.", "Successfuly!"));
        }
    }
}
