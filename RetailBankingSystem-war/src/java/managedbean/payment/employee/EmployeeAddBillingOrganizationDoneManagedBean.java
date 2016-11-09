package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.NonStandingGIRO;
import ejb.payment.entity.RegisteredBillingOrganization;
import ejb.payment.session.NonStandingGIROSessionBeanLocal;
import ejb.payment.session.RegisteredBillingOrganizationSessionBeanLocal;
import java.io.IOException;
import java.util.Calendar;
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

@Named(value = "employeeAddBillingOrganizationDoneManagedBean")
@RequestScoped

public class EmployeeAddBillingOrganizationDoneManagedBean {

    @EJB
    private NonStandingGIROSessionBeanLocal nonStandingGIROSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private RegisteredBillingOrganizationSessionBeanLocal registeredBillingOrganizationSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private String billingOrganization;
    private String billReference;
    private Map<String, String> myAccounts = new HashMap<String, String>();
    private Map<String, String> billingOrganizations = new HashMap<String, String>();
    private String paymentAmt;
    private String bankAccountNumWithType;
    private String bankAccountNum;
    private String transferMethod;
    private String transferFrequency;
    private boolean visible = false;
    private boolean transferMethodRender = false;

    private String statusMessage;
    private Long giroId;
    private String updateDate;
    private String giroType;
    private String paymentFrequency;

    private ExternalContext ec;
    private String customerIdentificationNum;

    public EmployeeAddBillingOrganizationDoneManagedBean() {
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

    public void show() {
        if (transferMethod.equals("One Time")) {
            visible = false;
            transferMethodRender = false;
        } else {
            visible = true;
            transferMethodRender = true;
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

    public String getTransferMethod() {
        return transferMethod;
    }

    public void setTransferMethod(String transferMethod) {
        this.transferMethod = transferMethod;
    }

    public String getTransferFrequency() {
        return transferFrequency;
    }

    public void setTransferFrequency(String transferFrequency) {
        this.transferFrequency = transferFrequency;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isTransferMethodRender() {
        return transferMethodRender;
    }

    public void setTransferMethodRender(boolean transferMethodRender) {
        this.transferMethodRender = transferMethodRender;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getGiroType() {
        return giroType;
    }

    public void setGiroType(String giroType) {
        this.giroType = giroType;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public void addNewBillingOrganization() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        giroType = "Non Standing";
        NonStandingGIRO nonStandingGIRO = nonStandingGIROSessionBeanLocal.retrieveNonStandingByBillRef(billReference);

        if (nonStandingGIRO.getGiroId() != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Bill has already existed", ""));
        } else {
            if (transferMethod.equals("One Time")) {
                paymentFrequency = "One Time";
                bankAccountNum = handleAccountString(bankAccountNumWithType);

                giroId = nonStandingGIROSessionBeanLocal.addNewNonStandingGIRO(billingOrganization,
                        billReference, bankAccountNum, bankAccountNumWithType,
                        paymentFrequency, paymentAmt, giroType, "Pending", true,
                        customerBasic.getCustomerBasicId());

                statusMessage = "Your new billing organization has been added!";
                Calendar cal = Calendar.getInstance();
                updateDate = cal.getTime().toString();

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("giroId", giroId.toString());
                ec.getFlash().put("updateDate", updateDate);
                ec.getFlash().put("billingOrganization", billingOrganization);
                ec.getFlash().put("billReference", billReference);

                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/payment/employeeAddBillingOrganizationFinal.xhtml?faces-redirect=true");

            } else {
                bankAccountNum = handleAccountString(bankAccountNumWithType);

                giroId = nonStandingGIROSessionBeanLocal.addNewNonStandingGIRO(billingOrganization, billReference,
                        bankAccountNum, bankAccountNumWithType, transferFrequency,
                        paymentAmt, giroType, "Pending", true, customerBasic.getCustomerBasicId());

                statusMessage = "Your new billing organization has been added!";
                Calendar cal = Calendar.getInstance();
                updateDate = cal.getTime().toString();

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("giroId", giroId.toString());
                ec.getFlash().put("updateDate", updateDate);
                ec.getFlash().put("billingOrganization", billingOrganization);
                ec.getFlash().put("billReference", billReference);

                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/payment/employeeAddBillingOrganizationFinal.xhtml?faces-redirect=true");
            }
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
