/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.loan.entity.CarLoanApplication;
import ejb.loan.entity.CashlineApplication;
import ejb.loan.entity.CreditReportAccountStatus;
import ejb.loan.entity.CustomerDebt;
import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.EducationLoanApplication;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.entity.RefinancingApplication;
import ejb.loan.entity.RenovationLoanApplication;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface LoanApplicationSessionBeanLocal {
    public void submitLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, Long customerBasicId, Long customerAdvancedId, ArrayList<CustomerDebt> debts, 
            CustomerProperty cp, MortgageLoanApplication mortgage, RefinancingApplication refinancing, String loanType, String interestPackage);
    public void submitCashlineApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, CashlineApplication cashline, Long newCustomerBasicId, Long newCustomerAdvancedId);
    public void submitEducationLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, EducationLoanApplication application, Long newCustomerBasicId, Long newCustomerAdvancedId);
    public void submitCarLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, CarLoanApplication application, Long newCustomerBasicId, Long newCustomerAdvancedId);
    public void submitRenovationLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, RenovationLoanApplication application, MortgageLoanApplication mortgage, Long newCustomerBasicId, Long newCustomerAdvancedId);
    public CustomerDebt addNewCustomerDebt(String facilityType, String financialInstitution, double totalAmount, double monthlyInstalment);
    public List<LoanApplication> getLoanApplications(ArrayList<String> loans, String loanType);
    public MortgageLoanApplication getMortgageLoanApplicationById(Long applicationId);
    public RefinancingApplication getRefinancingApplicationById(Long applicationId);
    public double[] getMortgagePurchaseLoanMaxInterval();
    public double getMortgagePurchaseLoanRiskRatio();
    public double[] getMortgagePurchaseLoanSuggestedInterval();
    public void approveMortgageLoanRequest(Long applicationId, double amount, int period, double instalment);
    public void rejectMortgageLoanRequest(Long applicationId);
    public void approveRefinancingLoanRequest(Long applicationId, int period, double instalment);
    public void startNewLoan(Long applicationId);
    public void updateLoanStatus(String status, Long applicationId);
    public List<MortgageLoanApplication> getAllMortgageApplicationsPendingAppraisal();
    public void submitAppraisal(double appraisedValue, Long applicationId);
    public List<CreditReportAccountStatus> getAccountStatusByBureauScoreId(Long id);
}
