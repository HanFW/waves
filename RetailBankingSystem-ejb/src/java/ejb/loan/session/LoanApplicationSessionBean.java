/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.loan.entity.CarLoanApplication;
import ejb.loan.entity.CashlineApplication;
import ejb.loan.entity.CreditReportAccountStatus;
import ejb.loan.entity.CreditReportBureauScore;
import ejb.loan.entity.CreditReportDefaultRecords;
import ejb.loan.entity.CustomerDebt;
import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.EducationLoanApplication;
import ejb.loan.entity.EducationLoanGuarantor;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanInterestPackage;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentAccount;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.entity.RefinancingApplication;
import ejb.loan.entity.RenovationLoanApplication;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
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
    public void submitLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, Long guarantorBasicId, Long guarantorAdvancedId, ArrayList<CustomerDebt> debts,
            CustomerProperty cp, MortgageLoanApplication mortgage, RefinancingApplication refinancing, String loanType, String interestPackage) {
        System.out.println("****** loan/LoanApplicationSessionBean: submitLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, guarantorBasicId);

        //set debts to guarantorBasic (1-M uni)
        cb.setCustomerDebt(debts);

        //set on both side (1-1 bi)
        cp.setCustomerBasic(cb);
        cb.setCustomerProperty(cp);

        //set on both side (1-1 bi)
        if (!hasCustomerAdvanced) {
            CustomerAdvanced ca = em.find(CustomerAdvanced.class, guarantorAdvancedId);
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
    public void submitCashlineApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, CashlineApplication cashline, Long newCustomerBasicId, Long newCustomerAdvancedId){
        System.out.println("****** loan/LoanApplicationSessionBean: submitCashlineApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, newCustomerBasicId);

        //set on both side (1-1 bi)
        if (!hasCustomerAdvanced) {
            CustomerAdvanced ca = em.find(CustomerAdvanced.class, newCustomerAdvancedId);
            cb.setCustomerAdvanced(ca);
            ca.setCustomerBasic(cb);
        }

        Query query = em.createQuery("SELECT p FROM LoanInterestPackage p WHERE p.packageName = :packageName");
        query.setParameter("packageName", "Cashline");
        List resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            LoanInterestPackage pkg = (LoanInterestPackage) resultList.get(0);
            System.out.println("****** loan/LoanApplicationSessionBean: submitCashlineApplication(): interest package: " + pkg.getPackageName());
            //set on both side
            cashline.setCustomerBasic(cb);
            cb.addCashlineApplication(cashline);
            cashline.setLoanInterestPackage(pkg);
            pkg.addCashlineApplication(cashline);
        } else {
            System.out.println("****** loan/LoanApplicationSessionBean: submitCashlineApplication(): no interest package found");
            cashline.setCustomerBasic(cb);
            cb.addCashlineApplication(cashline);
        }

        em.flush();
    }
    
    @Override
    public void submitEducationLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, EducationLoanApplication application, Long newCustomerBasicId, Long newCustomerAdvancedId, Long guarantorId){
        System.out.println("****** loan/LoanApplicationSessionBean: submitEducationLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, newCustomerBasicId);

        //set on both side (1-1 bi)
        if (!hasCustomerAdvanced) {
            CustomerAdvanced ca = em.find(CustomerAdvanced.class, newCustomerAdvancedId);
            cb.setCustomerAdvanced(ca);
            ca.setCustomerBasic(cb);
        }
        
        Query query = em.createQuery("SELECT p FROM LoanInterestPackage p WHERE p.packageName = :packageName");
        query.setParameter("packageName", "Education Loan");
        List resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            LoanInterestPackage pkg = (LoanInterestPackage) resultList.get(0);
            System.out.println("****** loan/LoanApplicationSessionBean: submitEducationLoanApplication(): interest package: " + pkg.getPackageName());
            application.setCustomerBasic(cb);
            cb.addLoanApplication(application);
            application.setLoanInterestPackage(pkg);
            pkg.addLoanApplication(application);
        } else {
            System.out.println("****** loan/LoanApplicationSessionBean: submitEducationLoanApplication(): no interest package found");
            application.setCustomerBasic(cb);
            cb.addLoanApplication(application);
        }

        em.flush();
        
        //add loan guarantor
        EducationLoanGuarantor guarantor = em.find(EducationLoanGuarantor.class, guarantorId);
        application.setEducationLoanGuarantor(guarantor);
        guarantor.setEducationLoanApplication(application);
        
        em.flush();
    }
    
    @Override
    public void submitCarLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, CarLoanApplication application, Long newCustomerBasicId, Long newCustomerAdvancedId){
        System.out.println("****** loan/LoanApplicationSessionBean: submitCarLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, newCustomerBasicId);

        //set on both side (1-1 bi)
        if (!hasCustomerAdvanced) {
            CustomerAdvanced ca = em.find(CustomerAdvanced.class, newCustomerAdvancedId);
            cb.setCustomerAdvanced(ca);
            ca.setCustomerBasic(cb);
        }

        Query query = em.createQuery("SELECT p FROM LoanInterestPackage p WHERE p.packageName = :packageName");
        query.setParameter("packageName", "Car Loan");
        List resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            LoanInterestPackage pkg = (LoanInterestPackage) resultList.get(0);
            System.out.println("****** loan/LoanApplicationSessionBean: submitCarLoanApplication(): interest package: " + pkg.getPackageName());
            application.setCustomerBasic(cb);
            cb.addLoanApplication(application);
            application.setLoanInterestPackage(pkg);
            pkg.addLoanApplication(application);
        } else {
            System.out.println("****** loan/LoanApplicationSessionBean: submitCarLoanApplication(): no interest package found");
            application.setCustomerBasic(cb);
            cb.addLoanApplication(application);
        }

        em.flush();
    }
    
    @Override
    public void submitRenovationLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, RenovationLoanApplication application, CustomerProperty property, Long newCustomerBasicId, Long newCustomerAdvancedId){
        System.out.println("****** loan/LoanApplicationSessionBean: submitRenovationLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, newCustomerBasicId);

        //set on both side (1-1 bi)
        property.setCustomerBasic(cb);
        cb.setCustomerProperty(property);

        //set on both side (1-1 bi)
        if (!hasCustomerAdvanced) {
            CustomerAdvanced ca = em.find(CustomerAdvanced.class, newCustomerAdvancedId);
            cb.setCustomerAdvanced(ca);
            ca.setCustomerBasic(cb);
        }

        Query query = em.createQuery("SELECT p FROM LoanInterestPackage p WHERE p.packageName = :packageName");
        query.setParameter("packageName", "Renovation Loan");
        List resultList = query.getResultList();
        
        if (!resultList.isEmpty()) {
            LoanInterestPackage pkg = (LoanInterestPackage) resultList.get(0);
            System.out.println("****** loan/LoanApplicationSessionBean: submitRenovationLoanApplication(): interest package: " + pkg.getPackageName());
            application.setCustomerBasic(cb);
            cb.addLoanApplication(application);
            application.setLoanInterestPackage(pkg);
            pkg.addLoanApplication(application);
        } else {
            System.out.println("****** loan/LoanApplicationSessionBean: submitRenovationLoanApplication(): no interest package found");
            application.setCustomerBasic(cb);
            cb.addLoanApplication(application);
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
    public List<LoanApplication> getLoanApplications(ArrayList<String> loans, String loanType) {
        List<LoanApplication> applications = new ArrayList<LoanApplication>();

        if (loanType.equals("all")) {
            for (String loan : loans) {
                switch (loan) {
                    case "waiting for valuation":
                        Query query1 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus");
                        query1.setParameter("applicationStatus", "waiting for valuation");
                        applications.addAll(query1.getResultList());
                        break;
                    case "pending":
                        Query query2 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus");
                        query2.setParameter("applicationStatus", "pending");
                        applications.addAll(query2.getResultList());
                        break;
                    case "in progress":
                        Query query3 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus");
                        query3.setParameter("applicationStatus", "in progress");
                        applications.addAll(query3.getResultList());
                        break;
                    case "approved":
                        Query query4 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus");
                        query4.setParameter("applicationStatus", "approved");
                        applications.addAll(query4.getResultList());
                        break;
                    case "started":
                        Query query5 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus");
                        query5.setParameter("applicationStatus", "started");
                        applications.addAll(query5.getResultList());
                        break;
                }
            }
        }else if(loanType.equals("mortgage")){
            for (String loan : loans) {
                switch (loan) {
                    case "waiting for valuation":
                        Query query1 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus "
                                + "AND (ma.loanType =:loanType1 OR ma.loanType =:loanType2 OR ma.loanType =:loanType3 OR ma.loanType =:loanType4)");
                        query1.setParameter("applicationStatus", "waiting for valuation");
                        query1.setParameter("loanType1", "HDB - New Purchase");
                        query1.setParameter("loanType2", "HDB - Refinancing");
                        query1.setParameter("loanType3", "Private Property - New Purchase");
                        query1.setParameter("loanType4", "Private Property - Refinancing");
                        applications.addAll(query1.getResultList());
                        break;
                    case "pending":
                        Query query2 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus "
                                + "AND (ma.loanType =:loanType1 OR ma.loanType =:loanType2 OR ma.loanType =:loanType3 OR ma.loanType =:loanType4)");
                        query2.setParameter("applicationStatus", "pending");
                        query2.setParameter("loanType1", "HDB - New Purchase");
                        query2.setParameter("loanType2", "HDB - Refinancing");
                        query2.setParameter("loanType3", "Private Property - New Purchase");
                        query2.setParameter("loanType4", "Private Property - Refinancing");
                        applications.addAll(query2.getResultList());
                        break;
                    case "in progress":
                        Query query3 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus "
                                + "AND (ma.loanType =:loanType1 OR ma.loanType =:loanType2 OR ma.loanType =:loanType3 OR ma.loanType =:loanType4)");
                        query3.setParameter("applicationStatus", "in progress");
                        query3.setParameter("loanType1", "HDB - New Purchase");
                        query3.setParameter("loanType2", "HDB - Refinancing");
                        query3.setParameter("loanType3", "Private Property - New Purchase");
                        query3.setParameter("loanType4", "Private Property - Refinancing");
                        applications.addAll(query3.getResultList());
                        break;
                    case "approved":
                        Query query4 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus "
                                + "AND (ma.loanType =:loanType1 OR ma.loanType =:loanType2 OR ma.loanType =:loanType3 OR ma.loanType =:loanType4)");
                        query4.setParameter("applicationStatus", "approved");
                        query4.setParameter("loanType1", "HDB - New Purchase");
                        query4.setParameter("loanType2", "HDB - Refinancing");
                        query4.setParameter("loanType3", "Private Property - New Purchase");
                        query4.setParameter("loanType4", "Private Property - Refinancing");
                        applications.addAll(query4.getResultList());
                        break;
                    case "started":
                        Query query5 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus "
                                + "AND (ma.loanType =:loanType1 OR ma.loanType =:loanType2 OR ma.loanType =:loanType3 OR ma.loanType =:loanType4)");
                        query5.setParameter("applicationStatus", "started");
                        query5.setParameter("loanType1", "HDB - New Purchase");
                        query5.setParameter("loanType2", "HDB - Refinancing");
                        query5.setParameter("loanType3", "Private Property - New Purchase");
                        query5.setParameter("loanType4", "Private Property - Refinancing");
                        applications.addAll(query5.getResultList());
                        break;
                }
            }
        }else{
            for (String loan : loans) {
                switch (loan) {
                    case "waiting for valuation":
                        Query query1 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus AND ma.loanType =:loanType");
                        query1.setParameter("applicationStatus", "waiting for valuation");
                        query1.setParameter("loanType", loanType);
                        applications.addAll(query1.getResultList());
                        break;
                    case "pending":
                        Query query2 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus AND ma.loanType =:loanType");
                        query2.setParameter("applicationStatus", "pending");
                        query2.setParameter("loanType", loanType);
                        applications.addAll(query2.getResultList());
                        break;
                    case "in progress":
                        Query query3 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus AND ma.loanType =:loanType");
                        query3.setParameter("applicationStatus", "in progress");
                        query3.setParameter("loanType", loanType);
                        applications.addAll(query3.getResultList());
                        break;
                    case "approved":
                        Query query4 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus AND ma.loanType =:loanType");
                        query4.setParameter("applicationStatus", "approved");
                        query4.setParameter("loanType", loanType);
                        applications.addAll(query4.getResultList());
                        break;
                    case "started":
                        Query query5 = em.createQuery("SELECT ma FROM LoanApplication ma WHERE ma.applicationStatus = :applicationStatus AND ma.loanType =:loanType");
                        query5.setParameter("applicationStatus", "started");
                        query5.setParameter("loanType", loanType);
                        applications.addAll(query5.getResultList());
                        break;
                }
            }
        }

        return applications;
    }

    @Override
    public MortgageLoanApplication getMortgageLoanApplicationById(Long applicationId) {
        System.out.println("****** loan/LoanApplicationSessionBean: getMortgageLoanApplicationById() ******");
        MortgageLoanApplication application = em.find(MortgageLoanApplication.class, applicationId);
        em.refresh(application);
        return application;
    }

    @Override
    public RefinancingApplication getRefinancingApplicationById(Long applicationId) {
        System.out.println("****** loan/LoanApplicationSessionBean: getRefinancingApplicationById() ******");
        RefinancingApplication application = em.find(RefinancingApplication.class, applicationId);
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
        CustomerBasic guarantor = application.getCustomerBasic();
        CustomerAdvanced ca = guarantor.getCustomerAdvanced();
        CustomerProperty property = guarantor.getCustomerProperty();

        CreditReportBureauScore report = guarantor.getBureauScore();
        for (CustomerDebt debt : guarantor.getCustomerDebt()) {
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
        em.remove(guarantor);
        em.flush();
    }

    @Override
    public void approveRefinancingLoanRequest(Long applicationId, int period, double instalment) {
        System.out.println("****** loan/LoanApplicationSessionBean: approveRefinancingLoanRequest() ******");
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        application.setPeriodSuggested(period * 12);
        application.setInstalment(instalment);
        application.setApplicationStatus("approved");
        application.setFinalActionDate(new Date());
        em.flush();
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
    
    @Override
    public List<CreditReportAccountStatus> getAccountStatusByBureauScoreId(Long id){
        CreditReportBureauScore report = em.find(CreditReportBureauScore.class, id);
        return report.getAccountStatus();
    }
    
    @Override
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
            String guarantorOtherMonthlyIncomeSource){
        EducationLoanGuarantor guarantor = new EducationLoanGuarantor();

        guarantor.setName(guarantorName);
        guarantor.setSalutation(guarantorSalutation);
        guarantor.setIdentificationNum(guarantorIdentificationNum);
        guarantor.setGender(guarantorGender);
        guarantor.setEmail(guarantorEmail);
        guarantor.setMobile(guarantorMobile);
        guarantor.setDateOfBirth(guarantorDateOfBirth);
        guarantor.setNationality(guarantorNationality);
        guarantor.setCountryOfResidence(guarantorCountryOfResidence);
        guarantor.setCompany(guarantorCompany);
        guarantor.setRace(guarantorRace);
        guarantor.setMaritalStatus(guarantorMaritalStatus);
        guarantor.setOccupation(guarantorOccupation);
        guarantor.setAddress(guarantorAddress);
        guarantor.setPostal(guarantorPostal);
        guarantor.setSignature(guarantorSignature);
        guarantor.setAge(getAge(guarantorDateOfBirth));
        
        guarantor.setNumOfDependent(guarantorNumOfDependents);
        guarantor.setEducation(guarantorEducation);
        guarantor.setResidentialStatus(guarantorResidentialStatus);
        guarantor.setYearInResidence(guarantorLengthOfResidence);
        guarantor.setIndustryType(guarantorIndustryType);
        guarantor.setLengthOfCurrentJob(guarantorLengthOfCurrentJob);
        guarantor.setEmploymentStatus(guarantorEmploymentStatus);
        guarantor.setMonthlyFixedIncome(guarantorMonthlyFixedIncome);
        guarantor.setResidentialType(guarantorResidentialType);
        guarantor.setCompanyAddress(guarantorCompanyAddress);
        guarantor.setCompanyPostal(guarantorCompanyPostal);
        guarantor.setCurrentPosition(guarantorCurrentPosition);
        guarantor.setCurrentJobTitle(guarantorCurrentJobTitle);
        guarantor.setPreviousCompanyName(guarantorPreviousCompany);
        guarantor.setLengthOfPreviousJob(guarantorLengthOfPreviousJob);
        guarantor.setOtherMonthlyIncome(guarantorOtherMonthlyIncome);
        guarantor.setOtherMonthlyIncomeSource(guarantorOtherMonthlyIncomeSource);

        em.persist(guarantor);
        em.flush();

        return guarantor.getId();
    }
    
    private String getAge(String guarantorDateOfBirth) {
        String daystr = guarantorDateOfBirth.substring(0, 2);
        String monthstr = guarantorDateOfBirth.substring(3, 6);
        String yearstr = guarantorDateOfBirth.substring(7);
        int month = 0;
        int day = Integer.parseInt(daystr);
        int year = Integer.parseInt(yearstr);
        String guarantorAge = "";

        switch (monthstr) {
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
        }

        LocalDate localBirth = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        Period p = Period.between(localBirth, now);
        guarantorAge = String.valueOf(p.getYears());

        return guarantorAge;
    }

}
