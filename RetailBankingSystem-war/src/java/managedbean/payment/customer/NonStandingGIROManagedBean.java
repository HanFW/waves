package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.payment.entity.BillingOrganization;
import ejb.payment.entity.GIRO;
import ejb.payment.entity.NonStandingGIRO;
import ejb.payment.session.BillingOrganizationSessionBeanLocal;
import ejb.payment.session.GIROSessionBeanLocal;
import ejb.payment.session.NonStandingGIROSessionBeanLocal;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import ws.client.otherbanks.OtherBankAccount;
import ws.client.otherbanks.OtherBanksWebService_Service;
import ws.client.sach.SACHWebService_Service;

@Named(value = "nonStandingGIROManagedBean")
@RequestScoped

public class NonStandingGIROManagedBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/OtherBanksWebService/OtherBanksWebService.wsdl")
    private OtherBanksWebService_Service service_1;

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/SACHWebService/SACHWebService.wsdl")
    private SACHWebService_Service service;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private BillingOrganizationSessionBeanLocal billingOrganizationSessionBeanLocal;

    @EJB
    private GIROSessionBeanLocal gIROSessionBeanLocal;

    @EJB
    private NonStandingGIROSessionBeanLocal nonStandingGIROSessionBeanLocal;

    private ExternalContext ec;
    private String paymentAmt;
    private Long giroId;

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

    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
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

    public List<NonStandingGIRO> getNonStandingGIROs() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        List<NonStandingGIRO> nonStandingGiros = nonStandingGIROSessionBeanLocal.retrieveNonStandingGIROByCusId(customerBasic.getCustomerBasicId());

        return nonStandingGiros;
    }

    public void nonStandingGIROTransfer() throws IOException {
        
        ec = FacesContext.getCurrentInstance().getExternalContext();

        GIRO giro = gIROSessionBeanLocal.retrieveGIROById(giroId);

        String billingOrganizationName = giro.getBillingOrganization();
        String bankAccountNum = giro.getBankAccountNum();

        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);

        Calendar cal = Calendar.getInstance();
        String transactionDate = cal.getTime().toString();
        String transactionCode = "BILL";
        String transactionRef = "Pay bills to " + billingOrganizationName;

        Long transactionId = transactionSessionBeanLocal.addNewTransaction(transactionDate,
                transactionCode, transactionRef, paymentAmt, " ", cal.getTimeInMillis(), bankAccount.getBankAccountId());

        Double currentBankAccountBalance = Double.valueOf(bankAccount.getBankAccountBalance()) - Double.valueOf(paymentAmt);
        bankAccountSessionBeanLocal.updateBankAccountBalance(bankAccountNum, currentBankAccountBalance.toString());

        BillingOrganization billOrg = billingOrganizationSessionBeanLocal.retrieveBillingOrganizationByName(billingOrganizationName);
        String bankName = billOrg.getBankName();
        String billOrgBankAccountNum = billOrg.getBankAccountNum();

        if (bankName.equals("DBS")) {
            sachNonStandingGIROTransferMTD(bankAccount.getBankAccountNum(), billOrgBankAccountNum, Double.valueOf(paymentAmt));

            OtherBankAccount dbsBankAccount = retrieveBankAccountByNum(billOrg.getBankAccountNum());
            toBankAccountNumWithType = dbsBankAccount.getOtherBankAccountType() + "-" + dbsBankAccount.getOtherBankAccountNum();
            fromBankAccountNumWithType = bankAccount.getBankAccountType()+"-"+bankAccount.getBankAccountNum();
            statusMessage = "Successfully Pay Bills";
            fromAccountBalance = bankAccount.getBankAccountBalance();
            
            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("transactionId", transactionId.toString());
            ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
            ec.getFlash().put("transferAmt", paymentAmt);
            ec.getFlash().put("fromAccountBalance", fromAccountBalance);
            
            ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerNonStandingGIROTransferDone.xhtml?faces-redirect=true");
            
        } else if (bankName.equals("OCBC")) {

        }
    }

    private void sachNonStandingGIROTransferMTD(java.lang.String fromBankAccountNum, java.lang.String toBankAccountNum, java.lang.Double transferAmt) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.sach.SACHWebService port = service.getSACHWebServicePort();
        port.sachNonStandingGIROTransferMTD(fromBankAccountNum, toBankAccountNum, transferAmt);
    }

    private OtherBankAccount retrieveBankAccountByNum(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service_1.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }
}
