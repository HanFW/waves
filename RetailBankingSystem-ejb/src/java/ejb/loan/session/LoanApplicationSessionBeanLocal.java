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
            boolean hasJoint, boolean jointIsExistingCustomer, boolean jointHasCustomerAdvanced, Long jointBasicId, Long jointAdvancedId, ArrayList<CustomerDebt> jointDebts,
            CustomerProperty cp, MortgageLoanApplication mortgage, RefinancingApplication refinancing, String loanType, String interestPackage);
    public void submitCashlineApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, CashlineApplication cashline, Long newCustomerBasicId, Long newCustomerAdvancedId);
    public void submitEducationLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, EducationLoanApplication application, Long newCustomerBasicId, Long newCustomerAdvancedId, Long guarantorId);
    public void submitCarLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, CarLoanApplication application, Long newCustomerBasicId, Long newCustomerAdvancedId);
    public void submitRenovationLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, RenovationLoanApplication application, CustomerProperty property, Long newCustomerBasicId, Long newCustomerAdvancedId);
    public CustomerDebt addNewCustomerDebt(String facilityType, String financialInstitution, double totalAmount, double monthlyInstalment, String collateral, int tenure, double rate);
    public List<LoanApplication> getLoanApplications(ArrayList<String> loans, String loanType);
    public MortgageLoanApplication getMortgageLoanApplicationById(Long applicationId);
    public RefinancingApplication getRefinancingApplicationById(Long applicationId);
    public double[] getMortgagePurchaseLoanMaxInterval();
    public double getMortgagePurchaseLoanRiskRatio();
    public double[] getMortgagePurchaseLoanSuggestedInterval();
    public void approveMortgageLoanRequest(Long applicationId, double amount, int period, double instalment);
    public void rejectMortgageLoanRequest(Long applicationId);
    public void approveRefinancingLoanRequest(Long applicationId, int period, double instalment);
    public void startNewMortgageLoan(Long applicationId);
    public void updateLoanStatus(String status, Long applicationId);
    public List<MortgageLoanApplication> getAllMortgageApplicationsPendingAppraisal();
    public void submitAppraisal(double appraisedValue, Long applicationId);
    public List<CreditReportAccountStatus> getAccountStatusByBureauScoreId(Long id);
    public Long createLoanGuarantor(String guarantorName, String guarantorSalutation,
            String guarantorIdentificationNum, String guarantorGender,
            String guarantorEmail, String guarantorMobile, String guarantorDateOfBirth,
            String guarantorNationality, String guarantorCountryOfResidence, String guarantorRace,
            String guarantorMaritalStatus, String guarantorOccupation, String guarantorCompany,
            String guarantorAddress, String guarantorPostal, byte[] guarantorSignature, 
            int guarantorNumOfDependents, String guarantorEducation, String guarantorResidentialStatus,
            int guarantorLengthOfResidence, String guarantorIndustryType, int guarantorLengthOfCurrentJob, String guarantorEmploymentStatus,
            double guarantorMonthlyFixedIncome, String guarantorResidentialType, String guarantorCompanyAddress,
            String guarantorCompanyPostal, String guarantorCurrentPosition, String guarantorCurrentJobTitle,
            String guarantorPreviousCompany, int guarantorLengthOfPreviousJob, double guarantorOtherMonthlyIncome,
            String guarantorOtherMonthlyIncomeSource);
    
    public List<CashlineApplication> getCashlineApplications(ArrayList<String> status);
}
