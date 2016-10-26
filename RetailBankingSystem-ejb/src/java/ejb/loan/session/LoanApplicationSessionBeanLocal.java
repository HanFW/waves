/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.loan.entity.CustomerDebt;
import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.entity.RefinancingApplication;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface LoanApplicationSessionBeanLocal {
    public void submitLoanApplication(Long customerBasicId, Long customerAdvancedId, ArrayList<CustomerDebt> debts, 
            CustomerProperty cp, MortgageLoanApplication mortgage, RefinancingApplication refinancing, String loanType, String interestPackage);
    public CustomerDebt addNewCustomerDebt(String facilityType, String financialInstitution, double totalAmount, double monthlyInstalment);
    public List<LoanApplication> getAllLoanApplications();
    public MortgageLoanApplication getMortgageLoanApplicationById(Long applicationId);
    public RefinancingApplication getRefinancingApplicationById(Long applicationId);
    public LoanApplication getLoanApplicationById(Long applicationId);
    public double[] getMortgagePurchaseLoanMaxInterval();
    public double getMortgagePurchaseLoanRiskRatio();
    public double[] getMortgagePurchaseLoanSuggestedInterval();
    public void approveMortgageLoanRequest(Long applicationId, double amount, int period, double instalment);
    public void rejectMortgageLoanRequest(Long applicationId);
    public void approveRefinancingLoanRequest(Long applicationId, int period, double instalment);
    public void rejectRefinancingLoanRequest(Long applicationId);
    public List<LoanApplication> getAllApprovedLoans();
    public void startNewLoan(Long applicationId);
    public List<LoanApplication> getAllStartedLoans();
    public List<LoanApplication> getAllInProgressLoans();
    public void updateLoanStatus(String status, Long applicationId);
}
