/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import ejb.loan.entity.LoanRepaymentAccount;
import ejb.loan.session.LoanRepaymentSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "customerMakeLoanRepaymentManagedBean")
@ViewScoped
public class CustomerMakeLoanRepaymentManagedBean implements Serializable {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private LoanRepaymentSessionBeanLocal loanRepaymentSessionBeanLocal;
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    String loanAccountNumber;

    private Map<String, String> fromAccounts = new HashMap<String, String>();
    private String fromCurrency;
    private Map<String, String> toAccounts = new HashMap<String, String>();
    private String toCurrency;
    private Double transferAmt;
    private String fromBankAccountNumWithType;
    private String toBankAccountNumWithType;
    private String fromAccount;
    private String toAccount;
    private String loanType;

    private Long newTransactionId;
    private String statusMessage;

    /**
     * Creates a new instance of CustomerMakeLoanRepaymentManagedBean
     */
    public CustomerMakeLoanRepaymentManagedBean() {
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        loanAccountNumber = (String) ec.getFlash().get("loanAccountNumber");
        transferAmt = (double) ec.getFlash().get("amount");
        loanType = (String) ec.getFlash().get("loanType");

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
        fromAccounts = new HashMap<String, String>();

        for (int i = 0; i < bankAccounts.size(); i++) {
            fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
        }

        toAccounts = new HashMap<String, String>();
        toAccounts.put(loanType + " Repayment Account-" + loanAccountNumber, loanType + " Repayment Account-" + loanAccountNumber);
    }

    public void makeMonthlyRepayment() throws IOException {
        System.out.println("=");
        System.out.println("====== loan/CustomerMakeLoanRepaymentManagedBean: makeMonthlyRepayment() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        fromAccount = handleAccountString(fromBankAccountNumWithType);
        toAccount = handleAccountString(toBankAccountNumWithType);
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");

        BankAccount bankAccountFrom = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromAccount);
        LoanRepaymentAccount bankAccountTo = loanRepaymentSessionBeanLocal.getRepaymentAccountByAccountNum(toAccount);

        if (bankAccountFrom.getBankAccountStatus().equals("Inactive")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!You account(from) has not been activated.", "Failed!"));
        } else if (bankAccountFrom.getBankAccountStatus().equals("Active")) {
            Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

            if (diffAmt >= 0) {

                Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                newTransactionId = loanRepaymentSessionBeanLocal.makeMonthlyRepayment(bankAccountFrom, bankAccountTo, transferAmt);
                statusMessage = "Your transaction has been completed.";
                loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "make loan repayment for S$" + transferAmt, "successful", null);

                Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                ec.getFlash().put("statusMessage", statusMessage);
                ec.getFlash().put("newTransactionId", newTransactionId);
                ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                ec.getFlash().put("transferAmt", transferAmt);
                ec.getFlash().put("fromAccount", fromAccount);
                ec.getFlash().put("toAccount", toAccount);
                ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalanceDouble);
                ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalanceDouble);

                ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerTransferDone.xhtml?faces-redirect=true");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Your account balance is insufficient.", "Failed!"));
            }
        }
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public Map<String, String> getFromAccounts() {
        return fromAccounts;
    }

    public void setFromAccounts(Map<String, String> fromAccounts) {
        this.fromAccounts = fromAccounts;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Map<String, String> getToAccounts() {
        return toAccounts;
    }

    public void setToAccounts(Map<String, String> toAccounts) {
        this.toAccounts = toAccounts;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(Double transferAmt) {
        this.transferAmt = transferAmt;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

}
