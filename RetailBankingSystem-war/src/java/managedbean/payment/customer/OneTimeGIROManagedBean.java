package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payement.session.BillingOrganizationSessionBeanLocal;
import ejb.payment.entity.BillingOrganization;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "oneTimeGIROManagedBean")
@RequestScoped

public class OneTimeGIROManagedBean {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private BillingOrganizationSessionBeanLocal billingOrganizationSessionBeanLocal;

    private String billingOrganization;
    private String billReference;
    private Map<String, String> myAccounts = new HashMap<String, String>();
    private Map<String, String> billingOrganizations = new HashMap<String, String>();
    private String paymentAmt;
    private String bankAccountNumWithType;

    private ExternalContext ec;

    public OneTimeGIROManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            myAccounts = new HashMap<String, String>();
            List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());

            for (int i = 0; i < bankAccounts.size(); i++) {
                myAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }

            billingOrganizations = new HashMap<String, String>();
            List<BillingOrganization> billOrgans = billingOrganizationSessionBeanLocal.getAllBillingOrganization();

            for (int j = 0; j < billOrgans.size(); j++) {
                billingOrganizations.put(billOrgans.get(j).getBillingOrganizationName(), billOrgans.get(j).getBillingOrganizationName());
            }
        }
    }

    public String getBillingOrganization() {
        return billingOrganization;
    }

    public void setBillingOrganization(String billingOrganization) {
        this.billingOrganization = billingOrganization;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public Map<String, String> getMyAccounts() {
        return myAccounts;
    }

    public void setMyAccounts(Map<String, String> myAccounts) {
        this.myAccounts = myAccounts;
    }

    public String getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public Map<String, String> getBillingOrganizations() {
        return billingOrganizations;
    }

    public void setBillingOrganizations(Map<String, String> billingOrganizations) {
        this.billingOrganizations = billingOrganizations;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
    }

    public void addNewOneTimeBilling() {
        
    }
}
