package managedbean.payment.customer;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.GIRO;
import ejb.payment.entity.NonStandingGIRO;
import ejb.payment.entity.RegisteredBillingOrganization;
import ejb.payment.session.GIROSessionBeanLocal;
import ejb.payment.session.NonStandingGIROSessionBeanLocal;
import ejb.payment.session.RegisteredBillingOrganizationSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import ws.client.otherbanks.OtherBankAccount;
import ws.client.otherbanks.OtherBanksWebService_Service;
import ws.client.sach.SACHWebService_Service;

@Named(value = "nonStandingGIROTransferDoneManagedBean")
@SessionScoped

public class NonStandingGIROTransferDoneManagedBean implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/OtherBanksWebService/OtherBanksWebService.wsdl")
    private OtherBanksWebService_Service service_otherBanks;

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/SACHWebService/SACHWebService.wsdl")
    private SACHWebService_Service service_sach;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private RegisteredBillingOrganizationSessionBeanLocal registeredBillingOrganizationSessionBeanLocal;

    @EJB
    private GIROSessionBeanLocal gIROSessionBeanLocal;

    @EJB
    private NonStandingGIROSessionBeanLocal nonStandingGIROSessionBeanLocal;

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

    private ExternalContext ec;

    public String getBillingOrganizationName() {
        NonStandingGIRO nonStandingGiro = nonStandingGIROSessionBeanLocal.retrieveNonStandingGIROById(giroId);
        billingOrganizationName = nonStandingGiro.getBillingOrganizationName();

        return billingOrganizationName;
    }

    public void setBillingOrganizationName(String billingOrganizationName) {
        this.billingOrganizationName = billingOrganizationName;
    }

    public String getBillReference() {
        NonStandingGIRO nonStandingGiro = nonStandingGIROSessionBeanLocal.retrieveNonStandingGIROById(giroId);
        billReference = nonStandingGiro.getBillReference();

        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getTransferFrequency() {
        NonStandingGIRO nonStandingGiro = nonStandingGIROSessionBeanLocal.retrieveNonStandingGIROById(giroId);
        transferFrequency = nonStandingGiro.getPaymentFrequency();

        return transferFrequency;
    }

    public void setTransferFrequency(String transferFrequency) {
        this.transferFrequency = transferFrequency;
    }

    public String getBankAccountNumWithType() {
        NonStandingGIRO nonStandingGiro = nonStandingGIROSessionBeanLocal.retrieveNonStandingGIROById(giroId);
        bankAccountNumWithType = nonStandingGiro.getBankAccountNumWithType();

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

    public void nonStandingGIROTransfer() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        NonStandingGIRO nonStandingGiro = nonStandingGIROSessionBeanLocal.retrieveNonStandingGIROById(giroId);

        if (nonStandingGiro.getNonStandingStatus().equals("Approved")) {

            String billingOrganizationName = nonStandingGiro.getBillingOrganizationName();
            String bankAccountNum = nonStandingGiro.getBankAccountNum();

            BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);

            Double currentAvailableBankAccountBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) - paymentAmt;
            bankAccountSessionBeanLocal.updateBankAccountAvailableBalance(bankAccountNum, currentAvailableBankAccountBalance.toString());

            RegisteredBillingOrganization billOrg = registeredBillingOrganizationSessionBeanLocal.retrieveRegisteredBillingOrganizationByName(billingOrganizationName);
            String bankName = billOrg.getBankName();
            String billOrgBankAccountNum = billOrg.getBankAccountNum();

            if (bankName.equals("DBS") && nonStandingGiro.getPaymentFrequency().equals("One Time")) {

                sachNonStandingGIROTransferMTD(bankAccount.getBankAccountNum(), billOrgBankAccountNum, paymentAmt);

                OtherBankAccount dbsBankAccount = retrieveBankAccountByNum(billOrg.getBankAccountNum());
                toBankAccountNumWithType = dbsBankAccount.getOtherBankAccountType() + "-" + dbsBankAccount.getOtherBankAccountNum();
                fromBankAccountNumWithType = bankAccount.getBankAccountType() + "-" + bankAccount.getBankAccountNum();
                statusMessage = "Successfully Pay Bills";

                fromBankAccountAvailableBalance = currentAvailableBankAccountBalance;
                fromBankAccountTotalBalance = Double.valueOf(bankAccount.getTotalBankAccountBalance());

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                ec.getFlash().put("transferAmt", paymentAmt);
                ec.getFlash().put("fromBankAccountAvailableBalance", fromBankAccountAvailableBalance.toString());
                ec.getFlash().put("fromBankAccountTotalBalance", fromBankAccountTotalBalance.toString());

                paymentAmt = null;

                ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerNonStandingGIROTransferFinished.xhtml?faces-redirect=true");

            } else if (bankName.equals("DBS") && !nonStandingGiro.getPaymentFrequency().equals("One Time")) {

                sachNonStandingGIROTransferMTD(bankAccount.getBankAccountNum(), billOrgBankAccountNum, paymentAmt);

                nonStandingGIROSessionBeanLocal.updatePaymentAmt(giroId, paymentAmt.toString());

                OtherBankAccount dbsBankAccount = retrieveBankAccountByNum(billOrg.getBankAccountNum());
                toBankAccountNumWithType = dbsBankAccount.getOtherBankAccountType() + "-" + dbsBankAccount.getOtherBankAccountNum();
                fromBankAccountNumWithType = bankAccount.getBankAccountType() + "-" + bankAccount.getBankAccountNum();
                statusMessage = "Successfully Pay Bills";

                fromBankAccountAvailableBalance = currentAvailableBankAccountBalance;
                fromBankAccountTotalBalance = Double.valueOf(bankAccount.getTotalBankAccountBalance());

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                ec.getFlash().put("transferAmt", paymentAmt);
                ec.getFlash().put("fromBankAccountAvailableBalance", fromBankAccountAvailableBalance.toString());
                ec.getFlash().put("fromBankAccountTotalBalance", fromBankAccountTotalBalance.toString());

                paymentAmt = null;

                ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerNonStandingGIROTransferFinished.xhtml?faces-redirect=true");
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Now your are not allowed to pay bill", ""));
        }
    }

    public void delete() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        GIRO giro = gIROSessionBeanLocal.retrieveGIROById(giroId);

        if (giro.getGiroId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! GIRO does not exist.", "Failed!"));
        } else {
            gIROSessionBeanLocal.deleteGIRO(giroId);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully! GIRO deleted Successfully.", "Successfuly!"));
        }
    }

    public void changeToRecurrentPayment() {
        nonStandingGIROSessionBeanLocal.updateNonStandingPaymentFrequency(giroId, transactionFrequencyForOneTime);
        transactionFrequencyForOneTime = null;
    }

    private OtherBankAccount retrieveBankAccountByNum(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service_otherBanks.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }

    private void sachNonStandingGIROTransferMTD(java.lang.String fromBankAccountNum, java.lang.String toBankAccountNum, java.lang.Double transferAmt) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.sach.SACHWebService port = service_sach.getSACHWebServicePort();
        port.sachNonStandingGIROTransferMTD(fromBankAccountNum, toBankAccountNum, transferAmt);
    }
}
