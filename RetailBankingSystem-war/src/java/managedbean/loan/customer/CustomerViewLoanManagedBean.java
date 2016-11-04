/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

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
    private int totalMonths;
    private Date loanStartDate;
    
    private String interestPackage;
    private double interestRate;
    
    
    private double instalment;
    private double overdueBalance;
    private double fees;
    private double totalPayment;
    
    private int remainingyears;
    private int remainingmonths;
    
    private LoanPayableAccount pa;
    private LoanRepaymentAccount ra;
        
    /**
     * Creates a new instance of CustomerViewLoanManagedBean
     */
    public CustomerViewLoanManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        loanId = (Long) ec.getFlash().get("loanId");
        pa = loanManagementSessionBeanLocal.getLoanPayableAccountById(loanId);
        ra = pa.getLoanRepaymentAccount();
    }
    
    public void makeRepaymentByMerlionBankAccount() throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("loanAccountNumber", ra.getAccountNumber());
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/loan/customerMakeLoanRepayment.xhtml?faces-redirect=true");
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public double getTotalAmount() {
        totalAmount = pa.getInitialAmount();
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLoanAccountNumber() {
        loanAccountNumber = pa.getAccountNumber();
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public String getLoanType() {
        loanType = pa.getLoanApplication().getLoanType();
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public double getRemainingBalance() {
        remainingBalance = pa.getAccountBalance();
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public int getTotalMonths() {
        totalMonths = pa.getLoanRepaymentAccount().getRepaymentMonths();
        return totalMonths;
    }

    public void setTotalMonths(int totalMonths) {
        this.totalMonths = totalMonths;
    }

    public Date getLoanStartDate() {
        loanStartDate = pa.getStartDate();
        return loanStartDate;
    }

    public void setLoanStartDate(Date loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public String getInterestPackage() {
        interestPackage = pa.getLoanApplication().getLoanInterestPackage().getPackageName();
        return interestPackage;
    }

    public void setInterestPackage(String interestPackage) {
        this.interestPackage = interestPackage;
    }

    public double getInterestRate() {
        interestRate = pa.getLoanApplication().getLoanInterestPackage().getInterestRate();
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getInstalment() {
        DecimalFormat df = new DecimalFormat("0.00");
        instalment = ra.getInstalment();
        return df.format(instalment);
    }

    public void setInstalment(double instalment) {
        this.instalment = instalment;
    }

    public double getOverdueBalance() {
        overdueBalance = ra.getOverdueBalance();
        return overdueBalance;
    }

    public void setOverdueBalance(double overdueBalance) {
        this.overdueBalance = overdueBalance;
    }

    public double getFees() {
        fees = ra.getFees();
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public String getTotalPayment() {
        DecimalFormat df = new DecimalFormat("0.00");
        instalment = ra.getInstalment();
        fees = ra.getFees();
        totalPayment = instalment + fees;
        return df.format(totalPayment);
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public int getRemainingyears() {
        int allMonths = pa.getLoanApplication().getPeriodSuggested();
        int finishedMonths = ra.getRepaymentMonths();
        remainingyears = (allMonths - finishedMonths) / 12;
        return remainingyears;
    }

    public void setRemainingyears(int remainingyears) {
        this.remainingyears = remainingyears;
    }

    public int getRemainingmonths() {
        int allMonths = pa.getLoanApplication().getPeriodSuggested();
        int finishedMonths = ra.getRepaymentMonths();
        remainingmonths = (allMonths - finishedMonths) % 12;
        return remainingmonths;
    }

    public void setRemainingmonths(int remainingmonths) {
        this.remainingmonths = remainingmonths;
    }

    public LoanPayableAccount getPa() {
        return pa;
    }

    public void setPa(LoanPayableAccount pa) {
        this.pa = pa;
    }
    
    
}
