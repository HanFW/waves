package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payment.entity.NonStandingGIRO;
import ejb.payment.session.NonStandingGIROSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "nonStandingGIROManagedBean")
@RequestScoped

public class NonStandingGIROManagedBean implements Serializable {

    @EJB
    private NonStandingGIROSessionBeanLocal nonStandingGIROSessionBeanLocal;

    private ExternalContext ec;

    private String paymentAmt;

    private String statusMessage;
    private String toBankAccountNumWithType;
    private String fromBankAccountNumWithType;
    private String fromBankAccount;
    private String toBankAccount;
    private String fromAccountBalance;

    public NonStandingGIROManagedBean() {
    }

    public String getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getFromBankAccount() {
        return fromBankAccount;
    }

    public void setFromBankAccount(String fromBankAccount) {
        this.fromBankAccount = fromBankAccount;
    }

    public String getToBankAccount() {
        return toBankAccount;
    }

    public void setToBankAccount(String toBankAccount) {
        this.toBankAccount = toBankAccount;
    }

    public String getFromAccountBalance() {
        return fromAccountBalance;
    }

    public void setFromAccountBalance(String fromAccountBalance) {
        this.fromAccountBalance = fromAccountBalance;
    }

    public List<NonStandingGIRO> getOneTimeGIROs() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<NonStandingGIRO> oneTimeGiros = nonStandingGIROSessionBeanLocal.retrieveOneTimeGIROByCusId(customerBasic.getCustomerBasicId());

        return oneTimeGiros;
    }

    public List<NonStandingGIRO> getRecurrentGIROs() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<NonStandingGIRO> recurrentTimeGiros = nonStandingGIROSessionBeanLocal.retrieveRecurrentGIROByCusId(customerBasic.getCustomerBasicId());

        return recurrentTimeGiros;
    }
}
