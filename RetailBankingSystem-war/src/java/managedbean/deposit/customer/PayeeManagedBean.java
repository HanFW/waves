package managedbean.deposit.customer;

import ejb.deposit.entity.BankAccount;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import ejb.deposit.entity.RegularPayee;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.PayeeSessionBeanLocal;
import ejb.deposit.session.RegularPayeeSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;

@Named(value = "payeeManagedBean")
@RequestScoped

public class PayeeManagedBean implements Serializable {

    @EJB
    private PayeeSessionBeanLocal payeeSessionBeanLocal;

    @EJB
    private RegularPayeeSessionBeanLocal regularPayeeSessionBeanLocal;

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    private Long payeeId;
    private String payeeName;
    private String payeeAccountNum;
    private String payeeAccountType;
    private Long newPayeeId;
    private String lastTransactionDate;
    private String statusMessage;
    private Long customerBasicId;
    private Payee customerPayee;

    private ExternalContext ec;

    public PayeeManagedBean() {
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
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

    public Long getNewPayeeId() {
        return newPayeeId;
    }

    public void setNewPayeeId(Long newPayeeId) {
        this.newPayeeId = newPayeeId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public Payee getCustomerPayee() {
        return customerPayee;
    }

    public void setCustomerPayee(Payee customerPayee) {
        this.customerPayee = customerPayee;
    }

    public void addPayee() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/PayeeManagedBean: addPayee() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(payeeAccountNum);
        boolean payeeExist = false;

        if (bankAccount.getBankAccountId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Your recipient account does not exist.", "Failed!"));
        } else if ((bankAccount.getCustomerBasic().getCustomerBasicId()).equals(customerBasic.getCustomerBasicId())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You cannot add your own account as recipient.", "Failed!"));
        } else if (bankAccount.getBankAccountType().equals("Fixed Deposit Account")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You cannot add fixed deposit account user as recipient.", "Failed!"));
        } else {

            if (!bankAccount.getBankAccountType().equals(payeeAccountType)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Your recipient account type is wrong.", "Failed!"));
            } else {

                List<Payee> payees = customerBasic.getPayee();

                for (int i = 0; i < payees.size(); i++) {
                    if (payees.get(i).getPayeeAccountNum().equals(payeeAccountNum)) {
                        payeeExist = true;
                    } else {
                        payeeExist = false;
                    }
                }
                if (!payeeExist) {

                    if (payees.size() >= 2) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You already have 20 recipients.", "Failed!"));
                    } else {

                        lastTransactionDate = "";
                        customerBasicId = customerBasic.getCustomerBasicId();
                        newPayeeId = regularPayeeSessionBeanLocal.addNewRegularPayee(payeeName, payeeAccountNum, payeeAccountType, lastTransactionDate, "Regular", customerBasicId);
                        Payee payee = payeeSessionBeanLocal.retrievePayeeById(newPayeeId);

                        customerBasic.getPayee().add(payee);
                        statusMessage = "New Recipient Added Successfully.";
                        loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "add payee", "successful", null);

                        ec.getFlash().put("statusMessage", statusMessage);
                        ec.getFlash().put("newPayeeId", newPayeeId);
                        ec.getFlash().put("payeeName", payeeName);
                        ec.getFlash().put("payeeAccountNum", payeeAccountNum);
                        ec.getFlash().put("payeeAccountType", payeeAccountType);

                        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerAddRecipientDone.xhtml?faces-redirect=true");

                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Recipient has existed.", "Failed!"));
                }
            }
        }
    }

    public List<RegularPayee> getRegularPayee() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<RegularPayee> regularPayee = regularPayeeSessionBeanLocal.retrieveRegularPayeeByCusId(customerBasic.getCustomerBasicId());

        return regularPayee;
    }
}
