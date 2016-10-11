package managedbean.payment;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.payement.session.DBSBankAccountSessionBeanLocal;
import ejb.payement.session.FastPayeeSessionBeanLocal;
import ejb.payement.session.SACHSessionBeanLocal;
import ejb.payment.entity.DBSBankAccount;
import ejb.payment.entity.FastPayee;
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

@Named(value = "fastTransferManagedBean")
@RequestScoped

public class FastTransferManagedBean {
    
    @EJB
    private DBSBankAccountSessionBeanLocal dBSBankAccountSessionBeanLocal;

    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private FastPayeeSessionBeanLocal fastPayeeSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String toBankAccountNumWithType;
    private String toCurrency;
    private Map<String, String> customerFastPayee = new HashMap<String, String>();
    private Map<String, String> fromAccounts = new HashMap<String, String>();
    private String fromBankAccountNumWithType;
    private String fromCurrency;
    private Double transferAmt;
    private String fromBankAccount;
    private String toBankAccount;
    private String fromAccountBalance;

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
            customerFastPayee = new HashMap<String, String>();
            List<FastPayee> fastPayees = fastPayeeSessionBeanLocal.retrieveFastPayeeByCusId(customerBasic.getCustomerBasicId());

            for (int i = 0; i < bankAccounts.size(); i++) {
                fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }

            for (int j = 0; j < fastPayees.size(); j++) {
                customerFastPayee.put(fastPayees.get(j).getFastPayeeAccountType() + "-" + fastPayees.get(j).getFastPayeeAccountNum() + "-" + fastPayees.get(j).getFastPayeeName(), fastPayees.get(j).getFastPayeeAccountType() + "-" + fastPayees.get(j).getFastPayeeAccountNum() + "-" + fastPayees.get(j).getFastPayeeName());
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

    public Map<String, String> getCustomerFastPayee() {
        return customerFastPayee;
    }

    public void setCustomerFastPayee(Map<String, String> customerFastPayee) {
        this.customerFastPayee = customerFastPayee;
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

    public String getFromAccountBalance() {
        return fromAccountBalance;
    }

    public void setFromAccountBalance(String fromAccountBalance) {
        this.fromAccountBalance = fromAccountBalance;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void fastTransfer() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        fromBankAccount = handleAccountString(fromBankAccountNumWithType);
        toBankAccount = handleAccountString(toBankAccountNumWithType);

        BankAccount merlionBankAccountFrom = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromBankAccount);
        DBSBankAccount dbsBankAccountTo = dBSBankAccountSessionBeanLocal.retrieveBankAccountByNum(toBankAccount);

        Double diffAmt = Double.valueOf(merlionBankAccountFrom.getBankAccountBalance()) - transferAmt;
        if (diffAmt >= 0) {

            Double currentBalance = Double.valueOf(merlionBankAccountFrom.getBankAccountBalance()) - transferAmt;
            bankAccountSessionBeanLocal.updateBankAccountBalance(fromBankAccount, currentBalance.toString());

            Calendar cal = Calendar.getInstance();
            String transactionCode = "ICT";
            String transactionRef = dbsBankAccountTo.getDbsBankAccountType() + "-" + dbsBankAccountTo.getDbsBankAccountNum();
            Long transactionDateMilis = cal.getTimeInMillis();

            Long transactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                    transferAmt.toString(), " ", transactionDateMilis, merlionBankAccountFrom.getBankAccountId());

            sACHSessionBeanLocal.SACHTransferMTD(fromBankAccount, toBankAccount, transferAmt);

            statusMessage = "Your transaction has been completed.";
            fromAccountBalance = currentBalance.toString();

            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("transactionId", transactionId);
            ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
            ec.getFlash().put("transferAmt", transferAmt);
            ec.getFlash().put("fromAccount", fromBankAccount);
            ec.getFlash().put("toAccount", toBankAccount);
            ec.getFlash().put("fromAccountBalance", fromAccountBalance);

            ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/payment/customerTransferFastDone.xhtml?faces-redirect=true");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Your account balance is insufficient.", "Failed!"));
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
