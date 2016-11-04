package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.payment.entity.SWIFTPayee;
import ejb.payment.session.SWIFTPayeeSessionBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "addSWIFTPayeeManagedBean")
@RequestScoped

public class AddSWIFTPayeeManagedBean {

    @EJB
    private SWIFTPayeeSessionBeanLocal sWIFTPayeeSessionBeanLocal;

    private String payeeInstitution;
    private String payeeAccountNum;
    private String payeeAccountType;
    private String payeeSWIFTCode;
    private String payeeBank;
    private String payeeCountry;

    private String statusMessage;

    private ExternalContext ec;

    public AddSWIFTPayeeManagedBean() {
    }

    public String getPayeeInstitution() {
        return payeeInstitution;
    }

    public void setPayeeInstitution(String payeeInstitution) {
        this.payeeInstitution = payeeInstitution;
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

    public String getPayeeSWIFTCode() {
        return payeeSWIFTCode;
    }

    public void setPayeeSWIFTCode(String payeeSWIFTCode) {
        this.payeeSWIFTCode = payeeSWIFTCode;
    }

    public String getPayeeBank() {
        return payeeBank;
    }

    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }

    public String getPayeeCountry() {
        return payeeCountry;
    }

    public void setPayeeCountry(String payeeCountry) {
        this.payeeCountry = payeeCountry;
    }

    public void addSWIFTPayee() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        String lastTransactionDate = "";

        Long swiftPayeeId = sWIFTPayeeSessionBeanLocal.addNewSWIFTPayee(payeeInstitution,
                payeeAccountNum, payeeAccountType, payeeSWIFTCode, lastTransactionDate,
                payeeCountry, payeeBank, "SWIFT", customerBasic.getCustomerBasicId());
        SWIFTPayee swiftPayee = sWIFTPayeeSessionBeanLocal.retrieveSWIFTPayeeById(swiftPayeeId);

        customerBasic.getSwiftPayee().add(swiftPayee);

        statusMessage = "New Recipient Added Successfully.";

        ec.getFlash().put("statusMessage", statusMessage);
        ec.getFlash().put("payeeInstitution", payeeInstitution);
        ec.getFlash().put("payeeAccountNum", payeeAccountNum);
        ec.getFlash().put("payeeAccountType", payeeAccountType);
        ec.getFlash().put("payeeSWIFTCode", payeeSWIFTCode);
        ec.getFlash().put("payeeCountry", payeeCountry);
        ec.getFlash().put("payeeBank", payeeBank);

        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerAddSWIFTPayeeDone.xhtml?faces-redirect=true");
    }
}
