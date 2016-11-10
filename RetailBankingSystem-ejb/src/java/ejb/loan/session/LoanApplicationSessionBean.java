/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.CustomerEmailSessionBeanLocal;
import ejb.infrastructure.session.MessageSessionBeanLocal;
import ejb.loan.entity.CarLoanApplication;
import ejb.loan.entity.CashlineApplication;
import ejb.loan.entity.CreditReportAccountStatus;
import ejb.loan.entity.CreditReportBureauScore;
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
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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

    @EJB
    private LoanPayableAccountSessionBeanLocal loanPayableAccountSessionBeanLocal;

    @EJB
    private LoanStatementSessionBeanLocal loanStatementSessionBeanLocal;

    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @EJB
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;
    
    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void submitLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, Long customerBasicId, Long customerAdvancedId, ArrayList<CustomerDebt> debts,
            boolean hasJoint, boolean jointIsExistingCustomer, boolean jointHasCustomerAdvanced, Long jointBasicId, Long jointAdvancedId, ArrayList<CustomerDebt> jointDebts,
            CustomerProperty cp, MortgageLoanApplication mortgage, RefinancingApplication refinancing, String loanType, String interestPackage) {
        System.out.println("****** loan/LoanApplicationSessionBean: submitLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, customerBasicId);
        CustomerBasic joint = null;

        //set debts to customerBasic (1-M uni)
        cb.setCustomerDebt(debts);

        //set property on both side (1-1 bi)
        cp.addCustomerBasic(cb);
        cb.setCustomerProperty(cp);

        //set customer advanced on both side (1-1 bi)
        if (!hasCustomerAdvanced) {
            CustomerAdvanced ca = em.find(CustomerAdvanced.class, customerAdvancedId);
            cb.setCustomerAdvanced(ca);
            ca.setCustomerBasic(cb);
        }

        if (hasJoint) {
            joint = em.find(CustomerBasic.class, jointBasicId);
            joint.setCustomerDebt(jointDebts);
            cp.addCustomerBasic(joint);
            joint.setCustomerProperty(cp);
            if (!jointHasCustomerAdvanced) {
                CustomerAdvanced ca = em.find(CustomerAdvanced.class, jointAdvancedId);
                joint.setCustomerAdvanced(ca);
                ca.setCustomerBasic(joint);
            }
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
                if (hasJoint) {
                    mortgage.setCustomer(joint);
                    joint.addLoanApplication(mortgage);
                }
                mortgage.setLoanInterestPackage(pkg);
                pkg.addLoanApplication(mortgage);
            } else {
                refinancing.setCustomerBasic(cb);
                cb.addLoanApplication(refinancing);
                if (hasJoint) {
                    refinancing.setCustomer(joint);
                    joint.addLoanApplication(refinancing);
                }
                refinancing.setLoanInterestPackage(pkg);
                pkg.addLoanApplication(refinancing);
            }
        } else {
            System.out.println("****** loan/LoanApplicationSessionBean: submitLoanApplication(): no interest package found");
            if (loanType.equals("purchase")) {
                mortgage.setCustomerBasic(cb);
                cb.addLoanApplication(mortgage);
                if (hasJoint) {
                    mortgage.setCustomer(joint);
                    joint.addLoanApplication(mortgage);
                }
            } else {
                refinancing.setCustomerBasic(cb);
                cb.addLoanApplication(refinancing);
                if (hasJoint) {
                    refinancing.setCustomer(joint);
                    joint.addLoanApplication(refinancing);
                }
            }
        }

        em.flush();
    }

    @Override
    public void submitCashlineApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, CashlineApplication cashline, Long newCustomerBasicId, Long newCustomerAdvancedId) {
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
    public void submitEducationLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, EducationLoanApplication application, Long newCustomerBasicId, Long newCustomerAdvancedId, Long guarantorId) {
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
    public void submitCarLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, CarLoanApplication application, Long newCustomerBasicId, Long newCustomerAdvancedId) {
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
    public void submitRenovationLoanApplication(boolean isExistingCustomer, boolean hasCustomerAdvanced, RenovationLoanApplication application, CustomerProperty property, Long newCustomerBasicId, Long newCustomerAdvancedId) {
        System.out.println("****** loan/LoanApplicationSessionBean: submitRenovationLoanApplication() ******");
        CustomerBasic cb = em.find(CustomerBasic.class, newCustomerBasicId);

        //set on both side (1-1 bi)
        property.addCustomerBasic(cb);
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
    public CustomerDebt addNewCustomerDebt(String facilityType, String financialInstitution, double totalAmount, double monthlyInstalment, String collateral, int tenure, double rate) {
        System.out.println("****** loan/LoanApplicationSessionBean: addNewCustomerDebt() ******");
        CustomerDebt cb = new CustomerDebt();

        cb.setFacilityType(facilityType);
        cb.setFinancialInstitution(financialInstitution);
        cb.setTotalAmount(totalAmount);
        cb.setMonthlyInstalment(monthlyInstalment);
        cb.setCollateralDetails(collateral);
        cb.setRemainingTenure(tenure);
        cb.setCurrentInterest(rate);

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
        } else if (loanType.equals("mortgage")) {
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
        } else {
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
        em.refresh(application);
        return application;
    }

    @Override
    public void approveLoanRequest(Long applicationId, double amount, int period, double instalment, String loanType) {
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        application.setAmountGranted(amount);
        application.setPeriodSuggested(period * 12);
        application.setInstalment(instalment);
        application.setApplicationStatus("approved");
        application.setFinalActionDate(new Date());

        if (loanType.equals("Mortgage Loan")) {
            customerEmailSessionBeanLocal.sendEmail(application.getCustomerBasic(), "approveContractLoanRequest", null);
        } else if (loanType.equals("Renovation Loan")) {
            customerAdminSessionBeanLocal.createOnlineBankingAccount(application.getCustomerBasic().getCustomerBasicId(), "approveRenovationLoanRequest");
        } else if (loanType.equals("Car Loan")) {
            customerEmailSessionBeanLocal.sendEmail(application.getCustomerBasic(), "approveContractLoanRequest", null);
        } else if (loanType.equals("Education Loan")) {
            customerAdminSessionBeanLocal.createOnlineBankingAccount(application.getCustomerBasic().getCustomerBasicId(), "approveEducationLoanRequest");
        }
        em.flush();
    }

    @Override
    public void rejectLoanRequest(Long applicationId) {
        LoanApplication application = em.find(LoanApplication.class, applicationId);
//        CustomerBasic customer = application.getCustomerBasic();
//        CustomerProperty property = customer.getCustomerProperty();
//        if (property != null) {
//            em.remove(property);
//        }
        em.remove(application);
        em.flush();
    }

    @Override
    public void startNewLoan(Long applicationId, String loanType) {
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        application.setApplicationStatus("started");

        LoanPayableAccount loanPayableAccount = new LoanPayableAccount();
        LoanRepaymentAccount loanRepaymentAccount = new LoanRepaymentAccount();

        application.setLoanPayableAccount(loanPayableAccount);
        loanPayableAccount.setLoanApplication(application);

        loanPayableAccount.setLoanRepaymentAccount(loanRepaymentAccount);
        loanRepaymentAccount.setLoanPayableAccount(loanPayableAccount);

        em.flush();

        String payableAccountNumber = generateAccountNumber();
        String repaymentAccountNumber = generateAccountNumber();

        loanPayableAccount.setAccountNumber(payableAccountNumber);
        loanPayableAccount.setInitialAmount(application.getAmountGranted());
        loanPayableAccount.setAccountBalance(application.getAmountGranted());
        loanPayableAccount.setStartDate(new Date());
        loanPayableAccount.setAccountStatus("started");
        loanPayableAccount.setOverdueBalance(0);

        loanRepaymentAccount.setAccountNumber(repaymentAccountNumber);
        loanRepaymentAccount.setDefaultMonths(0);
        
        Calendar cal = Calendar.getInstance();
        Date receivedDate = cal.getTime();
        
        String subject = "Your "+loanType+" has started.";
        String messageContent = "<br/><br/>Your "+loanType+ " has been started by one of our loan managers. <br/><br/>"
                + "Gentle Reminder: Please do remember to make repayment on time. <br/>"
                + "Thank you for subscribing to Merlion Loan Services. <br/>";

        messageSessionBeanLocal.sendMessage("Merlion Bank", "Loan", subject, receivedDate.toString(),
                messageContent, application.getCustomerBasic().getCustomerBasicId());
        
        if (loanType.equals("Mortgage Loan")) {
            customerAdminSessionBeanLocal.createOnlineBankingAccount(application.getCustomerBasic().getCustomerBasicId(), "startMortgageLoan");
        } else if (loanType.equals("Car Loan")) {
            customerAdminSessionBeanLocal.createOnlineBankingAccount(application.getCustomerBasic().getCustomerBasicId(), "startCarLoan");
        } else if (loanType.equals("Renovation Loan")) {
            customerAdminSessionBeanLocal.createOnlineBankingAccount(application.getCustomerBasic().getCustomerBasicId(), "startRenovationLoan");
        } else if (loanType.equals("Education Loan")) {
            customerAdminSessionBeanLocal.createOnlineBankingAccount(application.getCustomerBasic().getCustomerBasicId(), "startEducationLoan");
        }

        String statementType = "Loan Statement";
        String accountDetails = loanType;
        LoanPayableAccount account = loanPayableAccountSessionBeanLocal.retrieveLoanPayableAccountByNum(payableAccountNumber);
        loanStatementSessionBeanLocal.addNewLoanStatement(statementType, accountDetails);

        em.flush();
    }

    private String generateAccountNumber() {
        String bankAccountNum;
        String status;
        SecureRandom random = new SecureRandom();

        bankAccountNum = new BigInteger(23, random).setBit(22).toString(10);
        status = checkAccountDuplication(bankAccountNum);

        while (status.equals("Duplicated")) {
            bankAccountNum = new BigInteger(23, random).setBit(22).toString(10);
            status = checkAccountDuplication(bankAccountNum);
        }

        return bankAccountNum;
    }

    private String checkAccountDuplication(String accountNum) {
        Query query = em.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
        query.setParameter("bankAccountNum", accountNum);

        List bankAccountList = query.getResultList();

        if (bankAccountList.isEmpty()) {
            Query query2 = em.createQuery("Select a From LoanPayableAccount a Where a.accountNumber=:bankAccountNum");
            query2.setParameter("bankAccountNum", accountNum);
            List payableList = query2.getResultList();
            if (payableList.isEmpty()) {
                Query query3 = em.createQuery("Select a From LoanRepaymentAccount a Where a.accountNumber=:bankAccountNum");
                query3.setParameter("bankAccountNum", accountNum);
                List repaymentList = query3.getResultList();
                if (repaymentList.isEmpty()) {
                    return "Success";
                } else {
                    return "Duplicated";
                }
            } else {
                return "Duplicated";
            }
        } else {
            return "Duplicated";
        }

    }

    @Override
    public void updateLoanStatus(String status, Long applicationId) {
        LoanApplication application = em.find(LoanApplication.class, applicationId);
        application.setApplicationStatus(status);
        em.flush();
    }

    @Override
    public void updateCashlineStatus(String status, Long applicationId) {
        CashlineApplication application = em.find(CashlineApplication.class, applicationId);
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
        application.setPropertyAppraisedValue(appraisedValue);
        application.setApplicationStatus("pending");
    }

    @Override
    public List<CreditReportAccountStatus> getAccountStatusByBureauScoreId(Long id) {
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
            String guarantorOtherMonthlyIncomeSource) {
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

    @Override
    public List<CashlineApplication> getCashlineApplications(ArrayList<String> status) {
        List<CashlineApplication> applications = new ArrayList<CashlineApplication>();

        for (String currentStatus : status) {
            switch (currentStatus) {
                case "pending":
                    Query query1 = em.createQuery("SELECT c FROM CashlineApplication c WHERE c.applicationStatus = :applicationStatus");
                    query1.setParameter("applicationStatus", "pending");
                    applications.addAll(query1.getResultList());
                    break;
                case "in progress":
                    Query query2 = em.createQuery("SELECT c FROM CashlineApplication c WHERE c.applicationStatus = :applicationStatus");
                    query2.setParameter("applicationStatus", "in progress");
                    applications.addAll(query2.getResultList());
                    break;
                case "approved":
                    Query query3 = em.createQuery("SELECT c FROM CashlineApplication c WHERE c.applicationStatus = :applicationStatus");
                    query3.setParameter("applicationStatus", "approved");
                    applications.addAll(query3.getResultList());
                    break;
            }
        }
        return applications;
    }

    @Override
    public int getApplicantsAverageAge(CustomerBasic customer, CustomerBasic joint) {
        int averageAge = 0;
        double customerIncome = customer.getCustomerAdvanced().getMonthlyFixedIncome()
                + customer.getCustomerAdvanced().getOtherMonthlyIncome() * 0.7;

        double jointIncome = joint.getCustomerAdvanced().getMonthlyFixedIncome()
                + joint.getCustomerAdvanced().getOtherMonthlyIncome() * 0.7;

        double totalIncome = customerIncome + jointIncome;

        int customerAge = Integer.parseInt(customer.getCustomerAge());
        int jointAge = Integer.parseInt(joint.getCustomerAge());
        Double age = customerAge * customerIncome / totalIncome + jointAge * jointIncome / totalIncome;

        averageAge = age.intValue();
        return averageAge;
    }

    @Override
    public int getLTVRatio(CustomerBasic customer, CustomerBasic joint, LoanApplication application) {
        int numOfMortgage = getAllMortgageDebts(customer, joint).size();

        System.out.println("****** loan/LoanApplicationSessionBean: getLTVRatio(): Number of mortgages: " + numOfMortgage);

        if (numOfMortgage == 0) {
            return 80;
        } else if (numOfMortgage == 1) {
            return 50;
        } else {
            return 40;
        }
    }

    private List<CustomerDebt> getAllMortgageDebts(CustomerBasic customer, CustomerBasic joint) {
        List<CustomerDebt> accountStatus = customer.getCustomerDebt();
        List<CustomerDebt> customerMortgage = new ArrayList<CustomerDebt>();
        List<CustomerDebt> allMortgage = new ArrayList<CustomerDebt>();

        for (CustomerDebt account : accountStatus) {
            if (account.getFacilityType().equals("mortgage")) {
                customerMortgage.add(account);
            }
        }
        allMortgage.addAll(customerMortgage);

        if (joint != null) {
            List<CustomerDebt> jointAccountStatus = joint.getCustomerDebt();
            for (CustomerDebt account : jointAccountStatus) {
                if (account.getFacilityType().equals("mortgage")) {
                    allMortgage.add(account);
                    for (CustomerDebt customerAccount : customerMortgage) {
                        if (account.getCollateralDetails().equals(customerAccount.getCollateralDetails())) {
                            allMortgage.remove(account);
                            break;
                        }
                    }
                }
            }
        }
        return allMortgage;
    }

    @Override
    public int getTDSRRemaining(CustomerBasic customer, CustomerBasic joint) {
        double totalInstalment = 0;
        List<CustomerDebt> allDebts = getAllCustomerDebts(customer, joint);
        for (CustomerDebt debt : allDebts) {
            totalInstalment += debt.getMonthlyInstalment();
        }

        double totalIncome = getGrossIncome(customer, joint);

        Double remaining = totalIncome * 0.6 - totalInstalment;

        return remaining.intValue();
    }

    private List<CustomerDebt> getAllCustomerDebts(CustomerBasic customer, CustomerBasic joint) {
        List<CustomerDebt> allDebts = customer.getCustomerDebt();
        List<CustomerDebt> customerDebts = customer.getCustomerDebt();

        if (joint != null) {
            List<CustomerDebt> jointDebts = joint.getCustomerDebt();
            for (CustomerDebt account : jointDebts) {
                allDebts.add(account);
                for (CustomerDebt customerAccount : customerDebts) {
                    if (account.getCollateralDetails().equals(customerAccount.getCollateralDetails())) {
                        allDebts.remove(account);
                        break;
                    }
                }
            }
        }
        return allDebts;
    }

    @Override
    public int getMSRRemaining(CustomerBasic customer, CustomerBasic joint) {
        double totalInstalment = 0;
        List<CustomerDebt> allMortgage = getAllMortgageDebts(customer, joint);
        for (CustomerDebt debt : allMortgage) {
            totalInstalment += debt.getMonthlyInstalment();
        }
        double totalIncome = getGrossIncome(customer, joint);

        Double remaining = totalIncome * 0.3 - totalInstalment;

        return remaining.intValue();
    }

    @Override
    public double getGrossIncome(CustomerBasic customer, CustomerBasic joint) {
        double customerIncome = customer.getCustomerAdvanced().getMonthlyFixedIncome()
                + customer.getCustomerAdvanced().getOtherMonthlyIncome() * 0.7;

        double jointIncome = 0;
        if (joint != null) {
            jointIncome = joint.getCustomerAdvanced().getMonthlyFixedIncome()
                    + joint.getCustomerAdvanced().getOtherMonthlyIncome() * 0.7;
        }

        double totalIncome = customerIncome + jointIncome;

        return totalIncome;
    }

    @Override
    public double getRiskRatio(CustomerBasic customer, CustomerBasic joint) {
        double customerRisk = 0;
        double jointRisk = 0;

        //applicant 1 risk
        CreditReportBureauScore customerBureauScore = customer.getBureauScore();
        if (customerBureauScore != null) {
            customerRisk = customerBureauScore.getProbabilityOfDefault();
        }
        CustomerAdvanced ca = customer.getCustomerAdvanced();
        if (customer.getCustomerMaritalStatus().equals("Married")) {
            customerRisk += 0.01;
        }
        customerRisk += ca.getNumOfDependent() * 0.03;
        if (ca.getResidentialStatus().equals("Rented")) {
            customerRisk += 0.05;
        }
        if (ca.getEmploymentStatus().equals("Self-Employed") && ca.getLengthOfCurrentJob() < 3) {
            customerRisk += 0.02;
        }
        if (ca.getLengthOfCurrentJob() < 3 && ca.getLengthOfPreviousJob() < 3) {
            customerRisk += 0.01;
        }

        //joint applicant risk
        if (joint != null) {
            CreditReportBureauScore jointBureauScore = joint.getBureauScore();
            CustomerAdvanced jointCA = joint.getCustomerAdvanced();
            if (jointBureauScore != null) {
                jointRisk = jointBureauScore.getProbabilityOfDefault();
            }
            if (joint.getCustomerMaritalStatus().equals("Married")) {
                jointRisk += 0.01;
            }
            jointRisk += jointCA.getNumOfDependent() * 0.03;
            if (jointCA.getResidentialStatus().equals("Rented")) {
                jointRisk += 0.05;
            }
            if (jointCA.getEmploymentStatus().equals("Self-Employed") && jointCA.getLengthOfCurrentJob() < 3) {
                jointRisk += 0.02;
            }
            if (jointCA.getLengthOfCurrentJob() < 3 && jointCA.getLengthOfPreviousJob() < 3) {
                jointRisk += 0.01;
            }
        }

        DecimalFormat df = new DecimalFormat("0.00");
        String risk = df.format(Math.max(customerRisk, jointRisk));

        return Double.parseDouble(risk);
    }

    @Override
    public int calculateMortgageTenure(double amount, double instalment, double interest) {
        Double tenure = Math.log(1 - interest / 12 * amount / instalment) / Math.log(1 + interest / 12) * -1 / 12;
        return tenure.intValue() + 1;
    }

    @Override
    public RenovationLoanApplication getRenovationLoanApplicationById(Long applicationId) {
        RenovationLoanApplication application = em.find(RenovationLoanApplication.class, applicationId);
        em.refresh(application);
        return application;
    }

    @Override
    public CarLoanApplication getCarLoanApplicationById(Long applicationId) {
        CarLoanApplication application = em.find(CarLoanApplication.class, applicationId);
        em.refresh(application);
        return application;
    }

    @Override
    public EducationLoanApplication getEducationLoanApplicationById(Long applicationId) {
        EducationLoanApplication application = em.find(EducationLoanApplication.class, applicationId);
        em.refresh(application);
        return application;
    }

    @Override
    public CashlineApplication getCashlineApplicationById(Long applicationId) {
        CashlineApplication application = em.find(CashlineApplication.class, applicationId);
        em.refresh(application);
        return application;
    }

    @Override
    public void approveCashlineRequest(Long applicationId, double amount) {
        CashlineApplication application = em.find(CashlineApplication.class, applicationId);
        application.setAmountGranted((int) amount);
        application.setApplicationStatus("approved");
        application.setFinalActionDate(new Date());
        em.flush();
    }

    @Override
    public void rejectCashlineRequest(Long applicationId) {
        CashlineApplication application = em.find(CashlineApplication.class, applicationId);

        em.remove(application);
        em.flush();
    }
}
