/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.loan.session.LoanRepaymentSessionBeanLocal;
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

/**
 *
 * @author hanfengwei
 */
@Named(value = "customerMakeLoanRepaymentManagedBean")
@RequestScoped
public class CustomerMakeLoanRepaymentManagedBean {
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
    private String fromAccount;
    private String toAccount;
    

    /**
     * Creates a new instance of CustomerMakeLoanRepaymentManagedBean
     */
    public CustomerMakeLoanRepaymentManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        loanAccountNumber = (String) ec.getFlash().get("loanAccountNumber");
        System.out.println("====== loan/CustomerMakeLoanRepaymentManagedBean: init: get loan account number: " + loanAccountNumber);
        
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

            List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
            fromAccounts = new HashMap<String, String>();
            
            for (int i = 0; i < bankAccounts.size(); i++) {
                fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
            }
            
            toAccounts.put(loanAccountNumber, loanAccountNumber);
    }
    
    public void makePayment(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        
        fromAccount = handleAccountString(fromBankAccountNumWithType);
        toAccount = loanAccountNumber;
        
        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");
        
        loanRepaymentSessionBeanLocal.makeMonthlyRepayment(fromAccount, toAccount, transferAmt);
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction successful", null);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);
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

    
    
}
