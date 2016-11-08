package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payment.session.OtherBankPayeeSessionBeanLocal;
import ejb.payment.entity.OtherBankPayee;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "addOtherBankPayeeManagedBean")
@RequestScoped
public class AddOtherBankPayeeManagedBean {

    @EJB
    private OtherBankPayeeSessionBeanLocal otherBankPayeeSessionBeanLocal;

    private String payeeName;
    private String payeeAccountNum;
    private String payeeAccountType;
    private String lastTransactionDate;
    private Long otherBankPayeeId;
    private Long customerBasicId;
    private String statusMessage;

    private ExternalContext ec;

    public AddOtherBankPayeeManagedBean() {
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

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Long getOtherBankPayeeId() {
        return otherBankPayeeId;
    }

    public void setOtherBankPayeeId(Long otherBankPayeeId) {
        this.otherBankPayeeId = otherBankPayeeId;
    }

    public Long getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void addOtherBankPayee() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        lastTransactionDate = "";
        customerBasicId = customerBasic.getCustomerBasicId();
        otherBankPayeeId = otherBankPayeeSessionBeanLocal.addNewOtherBankPayee(payeeName,
                payeeAccountNum, payeeAccountType, lastTransactionDate, "Other Bank",
                customerBasicId);
        OtherBankPayee otherBankPayee = otherBankPayeeSessionBeanLocal.retrieveOtherBankPayeeById(otherBankPayeeId);

        customerBasic.getOtherBankPayee().add(otherBankPayee);

        statusMessage = "New Recipient Added Successfully.";

        ec.getFlash().put("statusMessage", statusMessage);
        ec.getFlash().put("otherBankPayeeId", otherBankPayeeId);
        ec.getFlash().put("payeeName", payeeName);
        ec.getFlash().put("payeeAccountNum", payeeAccountNum);
        ec.getFlash().put("payeeAccountType", payeeAccountType);

        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerAddOtherBankPayeeDone.xhtml?faces-redirect=true");
    }
}
