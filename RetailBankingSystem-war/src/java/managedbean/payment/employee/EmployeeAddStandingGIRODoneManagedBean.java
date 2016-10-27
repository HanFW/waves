package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.RegisteredBillingOrganization;
import ejb.payment.session.RegisteredBillingOrganizationSessionBeanLocal;
import ejb.payment.session.StandingGIROSessionBeanLocal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;

@Named(value = "employeeAddStandingGIRODoneManagedBean")
@RequestScoped

public class EmployeeAddStandingGIRODoneManagedBean {

    @EJB
    private StandingGIROSessionBeanLocal standingGIROSessionBeanLocal;

    @EJB
    private RegisteredBillingOrganizationSessionBeanLocal registeredBillingOrganizationSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String billingOrganization;
    private String billReference;
    private Double paymentLimit;
    private String customerName;
    private String customerMobile;
    private String bankAccountNum;
    private String bankAccountNumWithType;
    private Map<String, String> myAccounts = new HashMap<String, String>();
    private Map<String, String> billingOrganizations = new HashMap<String, String>();
    private String standingGiroStatus;
    private String giroType;
    private String customerIdentificationNum;

    private ExternalContext ec;

    public EmployeeAddStandingGIRODoneManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        myAccounts = new HashMap<String, String>();
        List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());

        for (int i = 0; i < bankAccounts.size(); i++) {
            myAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
        }

        billingOrganizations = new HashMap<String, String>();
        List<RegisteredBillingOrganization> billOrgans = registeredBillingOrganizationSessionBeanLocal.getAllRegisteredBillingOrganization();

        for (int j = 0; j < billOrgans.size(); j++) {
            billingOrganizations.put(billOrgans.get(j).getBillingOrganizationName(), billOrgans.get(j).getBillingOrganizationName());
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

    public Double getPaymentLimit() {
        return paymentLimit;
    }

    public void setPaymentLimit(Double paymentLimit) {
        this.paymentLimit = paymentLimit;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCustomerName() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        return customerBasic.getCustomerName();
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        return customerBasic.getCustomerMobile();
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
    }

    public Map<String, String> getMyAccounts() {
        return myAccounts;
    }

    public void setMyAccounts(Map<String, String> myAccounts) {
        this.myAccounts = myAccounts;
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public String getStandingGiroStatus() {
        return standingGiroStatus;
    }

    public void setStandingGiroStatus(String standingGiroStatus) {
        this.standingGiroStatus = standingGiroStatus;
    }

    public Map<String, String> getBillingOrganizations() {
        return billingOrganizations;
    }

    public void setBillingOrganizations(Map<String, String> billingOrganizations) {
        this.billingOrganizations = billingOrganizations;
    }

    public String getGiroType() {
        return giroType;
    }

    public void setGiroType(String giroType) {
        this.giroType = giroType;
    }

    public void addNewStandingGiro() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        standingGiroStatus = "Active";
        giroType = "Standing";

        bankAccountNum = handleAccountString(bankAccountNumWithType);

        standingGIROSessionBeanLocal.addNewStandingGIRO(billingOrganization, billReference, paymentLimit.toString(),
                customerName, customerMobile, bankAccountNum, standingGiroStatus,
                bankAccountNumWithType, giroType, customerBasic.getCustomerBasicId());

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Add GIRO Arrangement Successfully", ""));
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }

}
