package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.payment.entity.NonStandingGIRO;
import ejb.payment.session.NonStandingGIROSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeNonStandingGIROTransferDoneManagedBean")
@RequestScoped

public class EmployeeNonStandingGIROTransferDoneManagedBean {

    @EJB
    private NonStandingGIROSessionBeanLocal nonStandingGIROSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private String billingOrganizationName;
    private String billReference;
    private String transferFrequency;
    private String bankAccountNumWithType;
    private Double paymentAmt;

    private String statusMessage;
    private String toBankAccountNumWithType;
    private String fromBankAccountNumWithType;
    private Double fromBankAccountAvailableBalance;
    private Double fromBankAccountTotalBalance;
    private Long giroId;
    private String transactionFrequencyForOneTime;

    private String customerIdentificationNum;

    private ExternalContext ec;

    public EmployeeNonStandingGIROTransferDoneManagedBean() {
    }

    public NonStandingGIROSessionBeanLocal getNonStandingGIROSessionBeanLocal() {
        return nonStandingGIROSessionBeanLocal;
    }

    public void setNonStandingGIROSessionBeanLocal(NonStandingGIROSessionBeanLocal nonStandingGIROSessionBeanLocal) {
        this.nonStandingGIROSessionBeanLocal = nonStandingGIROSessionBeanLocal;
    }

    public CRMCustomerSessionBeanLocal getCustomerSessionBeanLocal() {
        return customerSessionBeanLocal;
    }

    public void setCustomerSessionBeanLocal(CRMCustomerSessionBeanLocal customerSessionBeanLocal) {
        this.customerSessionBeanLocal = customerSessionBeanLocal;
    }

    public String getBillingOrganizationName() {
        return billingOrganizationName;
    }

    public void setBillingOrganizationName(String billingOrganizationName) {
        this.billingOrganizationName = billingOrganizationName;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getTransferFrequency() {
        return transferFrequency;
    }

    public void setTransferFrequency(String transferFrequency) {
        this.transferFrequency = transferFrequency;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
    }

    public Double getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(Double paymentAmt) {
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

    public Double getFromBankAccountAvailableBalance() {
        return fromBankAccountAvailableBalance;
    }

    public void setFromBankAccountAvailableBalance(Double fromBankAccountAvailableBalance) {
        this.fromBankAccountAvailableBalance = fromBankAccountAvailableBalance;
    }

    public Double getFromBankAccountTotalBalance() {
        return fromBankAccountTotalBalance;
    }

    public void setFromBankAccountTotalBalance(Double fromBankAccountTotalBalance) {
        this.fromBankAccountTotalBalance = fromBankAccountTotalBalance;
    }

    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public String getTransactionFrequencyForOneTime() {
        return transactionFrequencyForOneTime;
    }

    public void setTransactionFrequencyForOneTime(String transactionFrequencyForOneTime) {
        this.transactionFrequencyForOneTime = transactionFrequencyForOneTime;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public List<NonStandingGIRO> getOneTimeGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<NonStandingGIRO> oneTimeGiros = nonStandingGIROSessionBeanLocal.retrieveOneTimeGIROByCusId(customerBasic.getCustomerBasicId());

        return oneTimeGiros;
    }

    public List<NonStandingGIRO> getRecurrentGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        List<NonStandingGIRO> recurrentTimeGiros = nonStandingGIROSessionBeanLocal.retrieveRecurrentGIROByCusId(customerBasic.getCustomerBasicId());

        return recurrentTimeGiros;
    }
}
