/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import ejb.loan.entity.LoanInterestPackage;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentAccount;
import ejb.loan.session.LoanManagementSessionBeanLocal;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hanfengwei
 */
@Named(value = "customerViewLoanManagedBean")
@RequestScoped
public class CustomerViewLoanManagedBean {

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

    /**
     * Creates a new instance of CustomerViewLoanManagedBean
     */
    public CustomerViewLoanManagedBean() {
    }

    @PostConstruct
    public void init() {
        DecimalFormat df = new DecimalFormat("0.00");

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
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
            } else{
                interestRate = pkg.getInterestRate() * 100 + 1.2;
            }
        } else if(interestPackage.equals("Private Property-Floating")){
            interestRate = pkg.getInterestRate() * 100 + 1.25;
        } else{
            interestRate = pkg.getInterestRate() * 100;
        }
        
        instalment = ra.getInstalment();
        fees = ra.getFees();
        overdueBalance = ra.getOverdueBalance();
        totalPayment = instalment + fees + overdueBalance;
    }

    public void makeRepaymentByMerlionBankAccount() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("loanAccountNumber", ra.getAccountNumber());
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/loan/customerMakeLoanRepayment.xhtml?faces-redirect=true");
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

}
