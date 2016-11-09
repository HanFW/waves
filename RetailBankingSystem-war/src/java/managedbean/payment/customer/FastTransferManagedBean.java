package managedbean.payment.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.payment.session.OtherBankPayeeSessionBeanLocal;
import ejb.payment.entity.OtherBankPayee;
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
import javax.xml.ws.WebServiceRef;
import ws.client.otherbanks.OtherBankAccount;
import ws.client.otherbanks.OtherBanksWebService_Service;
import ws.client.sach.SACHWebService_Service;

@Named(value = "fastTransferManagedBean")
@RequestScoped

public class FastTransferManagedBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/OtherBanksWebService/OtherBanksWebService.wsdl")
    private OtherBanksWebService_Service service_otherBanks;

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/SACHWebService/SACHWebService.wsdl")
    private SACHWebService_Service service_sach;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private OtherBankPayeeSessionBeanLocal otherBankPayeeSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String toBankAccountNumWithType;
    private String toCurrency;
    private Map<String, String> customerOtherBankPayee = new HashMap<String, String>();
    private Map<String, String> fromAccounts = new HashMap<String, String>();
    private String fromBankAccountNumWithType;
    private String fromCurrency;
    private Double transferAmt;
    private String fromBankAccount;
    private String toBankAccount;
    private String fromAccountAvailableBalance;
    private String fromAccountTotalBalance;

    private String statusMessage;

    private ExternalContext ec;

    public FastTransferManagedBean() {
    }

    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (ec.getSessionMap().get("customer") != null) {
            CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
            fromAccounts = new HashMap<String, String>();
            customerOtherBankPayee = new HashMap<String, String>();
            List<OtherBankPayee> otherBankPayees = otherBankPayeeSessionBeanLocal.retrieveOtherBankPayeeByCusId(customerBasic.getCustomerBasicId());

            for (int i = 0; i < bankAccounts.size(); i++) {
                fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }

            for (int j = 0; j < otherBankPayees.size(); j++) {
                customerOtherBankPayee.put(otherBankPayees.get(j).getPayeeAccountType() + "-" + otherBankPayees.get(j).getPayeeAccountNum() + "-" + otherBankPayees.get(j).getOtherBankPayeeName(), otherBankPayees.get(j).getPayeeAccountType() + "-" + otherBankPayees.get(j).getPayeeAccountNum() + "-" + otherBankPayees.get(j).getOtherBankPayeeName());
            }
        }
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Map<String, String> getFromAccounts() {
        return fromAccounts;
    }

    public void setFromAccounts(Map<String, String> fromAccounts) {
        this.fromAccounts = fromAccounts;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Double getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(Double transferAmt) {
        this.transferAmt = transferAmt;
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

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getFromAccountAvailableBalance() {
        return fromAccountAvailableBalance;
    }

    public void setFromAccountAvailableBalance(String fromAccountAvailableBalance) {
        this.fromAccountAvailableBalance = fromAccountAvailableBalance;
    }

    public String getFromAccountTotalBalance() {
        return fromAccountTotalBalance;
    }

    public void setFromAccountTotalBalance(String fromAccountTotalBalance) {
        this.fromAccountTotalBalance = fromAccountTotalBalance;
    }

    public Map<String, String> getCustomerOtherBankPayee() {
        return customerOtherBankPayee;
    }

    public void setCustomerOtherBankPayee(Map<String, String> customerOtherBankPayee) {
        this.customerOtherBankPayee = customerOtherBankPayee;
    }

    public void fastTransfer() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        fromBankAccount = handleAccountString(fromBankAccountNumWithType);
        toBankAccount = handleAccountString(toBankAccountNumWithType);

        BankAccount merlionBankAccountFrom = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromBankAccount);
        OtherBankAccount otherBankAccountTo = retrieveBankAccountByNum(toBankAccount);

        if (transferAmt >= 50000) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Upper limit for FAST transfer is S$50000 per transaction", "Failed!"));
        } else {
            Double diffAmt = Double.valueOf(merlionBankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;
            if (diffAmt >= 0) {

                Double currentAvailableBalance = Double.valueOf(merlionBankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;
                Double currentTotalBalance = Double.valueOf(merlionBankAccountFrom.getTotalBankAccountBalance()) - transferAmt;
                bankAccountSessionBeanLocal.updateBankAccountBalance(fromBankAccount, currentAvailableBalance.toString(), currentTotalBalance.toString());

                Calendar cal = Calendar.getInstance();
                String transactionCode = "ICT";
                String transactionRef = "Transfer to " + otherBankAccountTo.getOtherBankAccountType() + "-" + otherBankAccountTo.getOtherBankAccountNum();
                Long transactionDateMilis = cal.getTimeInMillis();

                Long transactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                        transferAmt.toString(), " ", transactionDateMilis, merlionBankAccountFrom.getBankAccountId());

                sachTransferMTD(fromBankAccount, toBankAccount, transferAmt);

                statusMessage = "Your transaction has been completed.";
                fromAccountAvailableBalance = currentAvailableBalance.toString();
                fromAccountTotalBalance = currentTotalBalance.toString();

                ec.getFlash().put("transactionId", transactionId);
                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                ec.getFlash().put("transferAmt", transferAmt);
                ec.getFlash().put("fromAccount", fromBankAccount);
                ec.getFlash().put("toAccount", toBankAccount);
                ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerTransferFastDone.xhtml?faces-redirect=true");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your account balance is insufficient.", "Failed!"));
            }
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }

    private void sachTransferMTD(java.lang.String fromBankAccountNum, java.lang.String toBankAccountNum, java.lang.Double transferAmt) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.sach.SACHWebService port = service_sach.getSACHWebServicePort();
        port.sachTransferMTD(fromBankAccountNum, toBankAccountNum, transferAmt);
    }

    private OtherBankAccount retrieveBankAccountByNum(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service_otherBanks.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }
}
