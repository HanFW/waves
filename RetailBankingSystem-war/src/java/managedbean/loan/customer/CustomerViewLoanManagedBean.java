/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.loan.entity.LoanInterestPackage;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentAccount;
import ejb.loan.entity.LoanRepaymentTransaction;
import ejb.loan.session.LoanManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author hanfengwei
 */
@Named(value = "customerViewLoanManagedBean")
@ViewScoped
public class CustomerViewLoanManagedBean implements Serializable {
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private LoanManagementSessionBeanLocal loanManagementSessionBeanLocal;

    private Long loanId;
    private double totalAmount;
    private String loanAccountNumber;
    private String loanType;
    private double remainingBalance;
    private int loanTenure;
    private Date loanStartDate;
    private int remainingyears;
    private int remainingmonths;

    private String interestPackage;
    private double interestRate;

    private double instalment;
    private double overdueBalance;
    private double fees;
    private double totalPayment;

    private LoanPayableAccount pa;
    private LoanRepaymentAccount ra;

    private boolean noDepositAccount;
    private boolean hasRecurringRepayment;
    private String recurringAccountNum;

    private List<LoanRepaymentTransaction> repaymentHistory;

    private Map<String, String> depositAccounts = new HashMap<String, String>();
    private String loanServingAccount;

    private CustomerBasic customer;
    private double totalInterest;
    private double totalPrincipal;

    private double currentPrincipal;
    private double currentInterest;
    /**
     * Creates a new instance of CustomerViewLoanManagedBean
     */
    public CustomerViewLoanManagedBean() {
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        customer = (CustomerBasic) ec.getSessionMap().get("customer");
        noDepositAccount = false;
        DecimalFormat df = new DecimalFormat("0.00");

        loanId = (Long) ec.getFlash().get("loanId");
        pa = loanManagementSessionBeanLocal.getLoanPayableAccountById(loanId);
        ra = pa.getLoanRepaymentAccount();
        loanTenure = pa.getLoanApplication().getPeriodSuggested() / 12;

        int allMonths = pa.getLoanApplication().getPeriodSuggested();
        int finishedMonths = ra.getRepaymentMonths();
        remainingyears = (allMonths - finishedMonths) / 12;
        remainingmonths = (allMonths - finishedMonths) % 12;

        LoanInterestPackage pkg = pa.getLoanApplication().getLoanInterestPackage();
        interestPackage = pkg.getPackageName();
        if (interestPackage.equals("HDB-Fixed")) {
            if (finishedMonths <= 36) {
                interestRate = 1.8;
            } else {
                interestRate = pkg.getInterestRate() * 100 + 1.2;
            }
        } else if (interestPackage.equals("HDB-Floating")) {
            interestRate = pkg.getInterestRate() * 100 + 1.3;
        } else if (interestPackage.equals("Private Property-Fixed")) {
            if (finishedMonths <= 36) {
                interestRate = 1.8;
            } else {
                interestRate = pkg.getInterestRate() * 100 + 1.2;
            }
        } else if (interestPackage.equals("Private Property-Floating")) {
            interestRate = pkg.getInterestRate() * 100 + 1.25;
        } else {
            interestRate = pkg.getInterestRate() * 100;
        }

        interestRate = Math.round(interestRate * 100.0) / 100.0;
        instalment = ra.getCurrentInstalment();
        fees = ra.getFees();
        fees = Math.round(fees * 100.0) / 100.0;
        overdueBalance = ra.getOverdueBalance();
        overdueBalance = Math.round(overdueBalance * 100.0) / 100.0;
        totalAmount = ra.getAccountBalance();
        totalAmount = Math.round(totalAmount * 100.0) / 100.0;

        recurringAccountNum = ra.getDepositAccountNumber();
        if (recurringAccountNum != null) {
            hasRecurringRepayment = true;
        } else {
            hasRecurringRepayment = false;
        }
        
        totalInterest = ra.getTotalInterest();
        totalInterest = Math.round(totalInterest * 100.0) / 100.0;
        totalPrincipal = ra.getTotalPrincipal();
        totalPrincipal = Math.round(totalPrincipal * 100.0) / 100.0;
        currentInterest = ra.getCurrentInterest();
        currentInterest = Math.round(currentInterest * 100.0) / 100.0;
        currentPrincipal = ra.getCurrentPrincipal();
        currentPrincipal = Math.round(currentPrincipal * 100.0) / 100.0;
    }

    public void makeRepaymentByMerlionBankAccount() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        List<BankAccount> accounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customer.getCustomerIdentificationNum());
        if (accounts.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "No existing deposit account found.", null);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, message);
            noDepositAccount = true;
        } else {
            noDepositAccount = false;
            ec.getFlash().put("loanAccountNumber", ra.getAccountNumber());
            ec.getFlash().put("amount", totalAmount);
            ec.getFlash().put("loanType", pa.getLoanApplication().getLoanType());
            ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/loan/customerMakeLoanRepayment.xhtml?faces-redirect=true");
        }
    }

    public void applyDepositAccount() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerOpenAccount.xhtml?faces-redirect=true");
    }

    public void makeRecurringRepayment() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        customer = (CustomerBasic) ec.getSessionMap().get("customer");
        
        List<BankAccount> accounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customer.getCustomerIdentificationNum());
        if (accounts.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "No existing deposit account found.", null);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, message);
            noDepositAccount = true;
        } else {
            noDepositAccount = false;
            for (BankAccount account : accounts) {
                depositAccounts.put(account.getBankAccountNum(), account.getBankAccountNum());
                System.out.println("****** " + depositAccounts);
            }
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('recurringDialog').show();");
        }
    }

    public void confirmRecurringPayment() {
        loanManagementSessionBeanLocal.setRecurringLoanServingAccount(loanServingAccount, ra.getId());
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('recurringDialog').hide();");
    }

    public void deleteRecurringPayment() {
        loanManagementSessionBeanLocal.deleteRecurringLoanServingAccount(ra.getId());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Repayment plan updated successfully.", null);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);
    }

    public BankAccountSessionBeanLocal getBankAccountSessionBeanLocal() {
        return bankAccountSessionBeanLocal;
    }

    public void setBankAccountSessionBeanLocal(BankAccountSessionBeanLocal bankAccountSessionBeanLocal) {
        this.bankAccountSessionBeanLocal = bankAccountSessionBeanLocal;
    }

    public Map<String, String> getDepositAccounts() {
        return depositAccounts;
    }

    public void setDepositAccounts(Map<String, String> depositAccounts) {
        this.depositAccounts = depositAccounts;
    }

    public String getLoanServingAccount() {
        return loanServingAccount;
    }

    public void setLoanServingAccount(String loanServingAccount) {
        this.loanServingAccount = loanServingAccount;
    }

    public List<LoanRepaymentTransaction> getRepaymentHistory() {
        repaymentHistory = loanManagementSessionBeanLocal.getRepaymentHistory(ra.getId());
        return repaymentHistory;
    }

    public LoanManagementSessionBeanLocal getLoanManagementSessionBeanLocal() {
        return loanManagementSessionBeanLocal;
    }

    public void setLoanManagementSessionBeanLocal(LoanManagementSessionBeanLocal loanManagementSessionBeanLocal) {
        this.loanManagementSessionBeanLocal = loanManagementSessionBeanLocal;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public int getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(int loanTenure) {
        this.loanTenure = loanTenure;
    }

    public Date getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(Date loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public int getRemainingyears() {
        return remainingyears;
    }

    public void setRemainingyears(int remainingyears) {
        this.remainingyears = remainingyears;
    }

    public int getRemainingmonths() {
        return remainingmonths;
    }

    public void setRemainingmonths(int remainingmonths) {
        this.remainingmonths = remainingmonths;
    }

    public String getInterestPackage() {
        return interestPackage;
    }

    public void setInterestPackage(String interestPackage) {
        this.interestPackage = interestPackage;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInstalment() {
        return instalment;
    }

    public void setInstalment(double instalment) {
        this.instalment = instalment;
    }

    public double getOverdueBalance() {
        return overdueBalance;
    }

    public void setOverdueBalance(double overdueBalance) {
        this.overdueBalance = overdueBalance;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public LoanPayableAccount getPa() {
        return pa;
    }

    public void setPa(LoanPayableAccount pa) {
        this.pa = pa;
    }

    public LoanRepaymentAccount getRa() {
        return ra;
    }

    public void setRa(LoanRepaymentAccount ra) {
        this.ra = ra;
    }

    public boolean isNoDepositAccount() {
        return noDepositAccount;
    }

    public void setNoDepositAccount(boolean noDepositAccount) {
        this.noDepositAccount = noDepositAccount;
    }

    public boolean isHasRecurringRepayment() {
        return hasRecurringRepayment;
    }

    public void setHasRecurringRepayment(boolean hasRecurringRepayment) {
        this.hasRecurringRepayment = hasRecurringRepayment;
    }

    public String getRecurringAccountNum() {
        return recurringAccountNum;
    }

    public void setRecurringAccountNum(String recurringAccountNum) {
        this.recurringAccountNum = recurringAccountNum;
    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public double getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(double totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public double getCurrentPrincipal() {
        return currentPrincipal;
    }

    public void setCurrentPrincipal(double currentPrincipal) {
        this.currentPrincipal = currentPrincipal;
    }

    public double getCurrentInterest() {
        return currentInterest;
    }

    public void setCurrentInterest(double currentInterest) {
        this.currentInterest = currentInterest;
    }

}
