package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payment.session.FastPayeeSessionBeanLocal;
import ejb.payment.entity.FastPayee;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "addFastPayeeManagedBean")
@RequestScoped
public class AddFastPayeeManagedBean {

    @EJB
    private FastPayeeSessionBeanLocal fastPayeeSessionBeanLocal;

    private String payeeName;
    private String payeeAccountNum;
    private String payeeAccountType;
    private String lastTransactionDate;
    private Long fastPayeeId;
    private Long customerBasicId;
    private String statusMessage;

    private ExternalContext ec;

    public AddFastPayeeManagedBean() {
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

    public Long getFastPayeeId() {
        return fastPayeeId;
    }

    public void setFastPayeeId(Long fastPayeeId) {
        this.fastPayeeId = fastPayeeId;
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

    public void addFastPayee() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        lastTransactionDate = "";
        customerBasicId = customerBasic.getCustomerBasicId();
        fastPayeeId = fastPayeeSessionBeanLocal.addNewFastPayee(payeeName, payeeAccountNum, payeeAccountType, lastTransactionDate, "FAST", customerBasicId);
        FastPayee fastPayee = fastPayeeSessionBeanLocal.retrieveFastPayeeById(fastPayeeId);

        customerBasic.getFastPayee().add(fastPayee);

        statusMessage = "New Recipient Added Successfully.";

        ec.getFlash().put("statusMessage", statusMessage);
        ec.getFlash().put("fastPayeeId", fastPayeeId);
        ec.getFlash().put("payeeName", payeeName);
        ec.getFlash().put("payeeAccountNum", payeeAccountNum);
        ec.getFlash().put("payeeAccountType", payeeAccountType);

        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerAddFastPayeeDone.xhtml?faces-redirect=true");
    }

}
