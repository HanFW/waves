/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.loan.entity.CreditReportAccountStatus;
import ejb.loan.entity.CreditReportBureauScore;
import ejb.loan.entity.CreditReportDefaultRecords;
import ejb.loan.entity.CustomerDebt;
import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanInterestPackage;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentAccount;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.entity.RefinancingApplication;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class LoanApplicationSessionBean implements LoanApplicationSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void submitLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, Long customerBasicId, Long customerAdvancedId, ArrayList<CustomerDebt> debts,
            CustomerProperty cp, MortgageLoanApplication mortgage, RefinancingApplication refinancing, String loanType, String interestPackage) {
        System.out.println("****** loan/LoanApplicationSessionBean: submitLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, customerBasicId);

        //set debts to customerBasic (1-M uni)
        cb.setCustomerDebt(debts);

        //set on both side (1-1 bi)
        cp.setCustomerBasic(cb);
        cb.setCustomerProperty(cp);

        //set on both side (1-1 bi)
        if (!hasCustomerAdvanced) {
            CustomerAdvanced ca = em.find(CustomerAdvanced.class, customerAdvancedId);
            cb.setCustomerAdvanced(ca);
            ca.setCustomerBasic(cb);
        }

        Query query = em.createQuery("SELECT p FROM LoanInterestPackage p WHERE p.packageName = :packageName");
        query.setParameter("packageName", interestPackage);
        List resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            LoanInterestPackage pkg = (LoanInterestPackage) resultList.get(0);
            System.out.println("****** loan/LoanApplicationSessionBean: submitLoanApplication(): interest package: " + pkg.getPackageName());
            //set on both side
            if (loanType.equals("purchase")) {
                mortgage.setCustomerBasic(cb);
                cb.addLoanApplication(mortgage);
                mortgage.setLoanInterestPackage(pkg);
                pkg.addLoanApplication(mortgage);
            } else {
                refinancing.setCustomerBasic(cb);
                cb.addLoanApplication(refinancing);
                refinancing.setLoanInterestPackage(pkg);
                pkg.addLoanApplication(refinancing);
            }
        } else {
            System.out.println("****** loan/LoanApplicationSessionBean: submitLoanApplication(): no interest package found");
            if (loanType.equals("purchase")) {
                mortgage.setCustomerBasic(cb);
                cb.addLoanApplication(mortgage);
            } else {
                refinancing.setCustomerBasic(cb);
                cb.addLoanApplication(refinancing);
            }
        }

        em.flush();
    }

    @Override
    public CustomerDebt addNewCustomerDebt(String facilityType, String financialInstitution, double totalAmount, double monthlyInstalment) {
        System.out.println("****** loan/LoanApplicationSessionBean: addNewCustomerDebt() ******");
        CustomerDebt cb = new CustomerDebt();

        cb.setFacilityType(facilityType);
        cb.setFinancialInstitution(financialInstitution);
        cb.setTotalAmount(totalAmount);
        cb.setMonthlyInstalment(monthlyInstalment);

        return cb;
    }

    @Override
    public List<LoanApplication> getAllLoanApplications() {
        Query query = em.createQuery("SELECT la FROM LoanApplication la WHERE la.applicationStatus = :applicationStatus1");
        query.setParameter("applicationStatus1", "pending");
        return query.getResultList();
    }

    @Override
    public MortgageLoanApplication getMortgageLoanApplicationById(Long applicationId) {
        System.out.println("****** loan/LoanApplicationSessionBean: getMortgageLoanApplicationById() ******");
        MortgageLoanApplication application = em.find(MortgageLoanApplication.class, applicationId);
        return application;
    }

    @Override
    public RefinancingApplication getRefinancingApplicationById(Long applicationId) {
        System.out.println("****** loan/LoanApplicationSessionBean: getRefinancingApplicationById() ******");
        RefinancingApplication application = em.find(RefinancingApplication.class, applicationId);
        return application;
    }

    @Override
    public LoanApplication getLoanApplicationById(Long applicationId) {
        System.out.println("****** loan/LoanApplicationSessionBean: getLoanApplicationById() ******");
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        return application;
    }

    @Override
    public double[] getMortgagePurchaseLoanMaxInterval() {
        System.out.println("****** loan/LoanApplicationSessionBean: getMortgagePurchaseLoanMaxInterval() ******");
        double[] maxInterval = new double[2];
        maxInterval[0] = 460000;
        maxInterval[1] = 510000;
        return maxInterval;
    }

    @Override
    public double getMortgagePurchaseLoanRiskRatio() {
        System.out.println("****** loan/LoanApplicationSessionBean: getMortgagePurchaseLoanRiskRatio() ******");
        double ratio = 0;
        return ratio;
    }

    @Override
    public double[] getMortgagePurchaseLoanSuggestedInterval() {
        System.out.println("****** loan/LoanApplicationSessionBean: getMortgagePurchaseLoanSuggestedInterval() ******");
        double[] interval = new double[2];
        interval[0] = 460000;
        interval[1] = 510000;
        return interval;
    }

    @Override
    public void approveMortgageLoanRequest(Long applicationId, double amount, int period, double instalment) {
        System.out.println("****** loan/LoanApplicationSessionBean: approveMortgageLoanRequest() ******");
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        application.setAmountGranted(amount);
        application.setPeriodSuggested(period * 12);
        application.setInstalment(instalment);
        application.setApplicationStatus("approved");
        application.setFinalActionDate(new Date());
        em.flush();
    }

    @Override
    public void rejectMortgageLoanRequest(Long applicationId) {
        System.out.println("****** loan/LoanApplicationSessionBean: rejectMortgageLoanRequest() ******");
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        CustomerBasic customer = application.getCustomerBasic();
        CustomerAdvanced ca = customer.getCustomerAdvanced();
        CustomerProperty property = customer.getCustomerProperty();

        CreditReportBureauScore report = customer.getBureauScore();
        for (CustomerDebt debt : customer.getCustomerDebt()) {
            em.remove(debt);
        }
        for (CreditReportAccountStatus as : report.getAccountStatus()) {
            em.remove(as);
        }
        for (CreditReportDefaultRecords dr : report.getDefaultRecords()) {
            em.remove(dr);
        }
        em.remove(report);

        em.remove(application);
        em.remove(property);
        em.remove(ca);
        em.remove(customer);
        em.flush();
    }

    @Override
    public void approveRefinancingLoanRequest(Long applicationId, int period, double instalment) {

    }

    @Override
    public void rejectRefinancingLoanRequest(Long applicationId) {

    }

    @Override
    public List<LoanApplication> getAllApprovedLoans() {
        Query query = em.createQuery("SELECT la FROM LoanApplication la WHERE la.applicationStatus = :applicationStatus");
        query.setParameter("applicationStatus", "approved");
        return query.getResultList();
    }

    @Override
    public List<LoanApplication> getAllStartedLoans() {
        Query query = em.createQuery("SELECT la FROM LoanApplication la WHERE la.applicationStatus = :applicationStatus");
        query.setParameter("applicationStatus", "started");
        return query.getResultList();
    }

    @Override
    public List<LoanApplication> getAllInProgressLoans() {
        Query query = em.createQuery("SELECT la FROM LoanApplication la WHERE la.applicationStatus = :applicationStatus");
        query.setParameter("applicationStatus", "in progress");
        return query.getResultList();
    }

    @Override
    public void startNewLoan(Long applicationId) {
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        application.setApplicationStatus("started");

        LoanPayableAccount loanPayableAccount = new LoanPayableAccount();
        LoanRepaymentAccount loanRepaymentAccount = new LoanRepaymentAccount();

        application.setLoanPayableAccount(loanPayableAccount);
        loanPayableAccount.setLoanApplication(application);

        loanPayableAccount.setLoanRepaymentAccount(loanRepaymentAccount);
        loanRepaymentAccount.setLoanPayableAccount(loanPayableAccount);

        em.flush();

        DecimalFormat df = new DecimalFormat("000000");

        loanPayableAccount.setAccountNumber("6000" + df.format(loanPayableAccount.getId()));
        loanPayableAccount.setInitialAmount(application.getAmountGranted());
        loanPayableAccount.setAccountBalance(application.getAmountGranted());
        loanPayableAccount.setStartDate(new Date());
        loanPayableAccount.setAccountStatus("started");
        loanPayableAccount.setOverdueBalance(0);

        loanRepaymentAccount.setAccountNumber("7000" + df.format(loanRepaymentAccount.getId()));

        em.flush();
    }

    @Override
    public void updateLoanStatus(String status, Long applicationId) {
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        application.setApplicationStatus(status);
        em.flush();
    }

    @Override
    public List<MortgageLoanApplication> getAllMortgageApplicationsPendingAppraisal() {
        Query query = em.createQuery("SELECT ma FROM MortgageLoanApplication ma WHERE ma.applicationStatus = :applicationStatus");
        query.setParameter("applicationStatus", "waiting for valuation");
        return query.getResultList();
    }

    @Override
    public void submitAppraisal(double appraisedValue, Long applicationId) {
        System.out.println("****** loan/LoanApplicationSessionBean: submitAppraisal() ******");
        MortgageLoanApplication application = em.find(MortgageLoanApplication.class, applicationId);
        application.setApplicationStatus("pending");
    }

}
