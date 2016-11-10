package ejb.bi.session;

import ejb.bi.entity.AccountClosureReason;
import ejb.bi.entity.DepositAccountClosure;
import ejb.bi.entity.DepositAccountOpen;
import ejb.bi.entity.NumOfExistingCustomer;
import ejb.bi.entity.Rate;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RateSessionBean implements RateSessionBeanLocal {

    @EJB
    private AccountClosureReasonSessionBeanLocal accountClosureReasonSessionBeanLocal;

    @EJB
    private NumOfExistingCustomerSessionBeanLocal numOfExistingCustomerSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewRate(Double rateValue, String rateType, String rateStatus,
            Integer updateYear, Integer updateMonth, String currentYear) {

        Rate rate = new Rate();

        rate.setCurrentYear(currentYear);
        rate.setRateStatus(rateStatus);
        rate.setRateType(rateType);
        rate.setRateValue(rateValue);
        rate.setUpdateMonth(updateMonth);
        rate.setUpdateYear(updateYear);

        entityManager.persist(rate);
        entityManager.flush();

        return rate.getRateId();
    }

    @Override
    public Rate retrieveAcquisitionRateByMonth(Integer updateMonth) {
        Rate rate = new Rate();

        try {
            Query query = entityManager.createQuery("Select r From Rate r Where r.updateMonth=:updateMonth And r.rateType=:rateType And r.currentYear=:currentYear");
            query.setParameter("updateMonth", updateMonth);
            query.setParameter("rateType", "Acquisition");
            query.setParameter("currentYear", "Yes");

            if (query.getResultList().isEmpty()) {
                return new Rate();
            } else {
                rate = (Rate) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Rate();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return rate;
    }

    @Override
    public Rate retrieveAttritionRateByMonth(Integer updateMonth) {
        Rate rate = new Rate();

        try {
            Query query = entityManager.createQuery("Select r From Rate r Where r.updateMonth=:updateMonth And r.rateType=:rateType And r.currentYear=:currentYear");
            query.setParameter("updateMonth", updateMonth);
            query.setParameter("rateType", "Attrition");
            query.setParameter("currentYear", "Yes");

            if (query.getResultList().isEmpty()) {
                return new Rate();
            } else {
                rate = (Rate) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Rate();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return rate;
    }

    @Override
    public Rate retrieveAcquisitionRateById(Long rateId) {
        Rate rate = new Rate();

        try {
            Query query = entityManager.createQuery("Select r From Rate r Where r.rateId=:rateId And r.rateType=:rateType");
            query.setParameter("rateId", rateId);
            query.setParameter("rateType", "Acquisition");

            if (query.getResultList().isEmpty()) {
                return new Rate();
            } else {
                rate = (Rate) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Rate();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return rate;
    }

    @Override
    public Rate retrieveAttritionRateById(Long rateId) {
        Rate rate = new Rate();

        try {
            Query query = entityManager.createQuery("Select r From Rate r Where r.rateId=:rateId And r.rateType=:rateType");
            query.setParameter("rateId", rateId);
            query.setParameter("rateType", "Attrition");

            if (query.getResultList().isEmpty()) {
                return new Rate();
            } else {
                rate = (Rate) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Rate();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return rate;
    }

    @Override
    public Rate retrieveRateById(Long rateId) {
        Rate rate = new Rate();

        try {
            Query query = entityManager.createQuery("Select r From Rate r Where r.rateId=:rateId");
            query.setParameter("rateId", rateId);

            if (query.getResultList().isEmpty()) {
                return new Rate();
            } else {
                rate = (Rate) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Rate();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return rate;
    }

    @Override
    public Rate getAcqRate() {
        Query query = entityManager.createQuery("SELECT r FROM Rate r Where r.rateType=:rateType And r.rateStatus=:rateStatus");
        query.setParameter("rateType", "Acquisition");
        query.setParameter("rateStatus", "New");

        return (Rate) query.getSingleResult();
    }

    @Override
    public Rate getAttRate() {
        Query query = entityManager.createQuery("SELECT r FROM Rate r Where r.rateType=:rateType And r.rateStatus=:rateStatus");
        query.setParameter("rateType", "Attrition");
        query.setParameter("rateStatus", "New");

        return (Rate) query.getSingleResult();
    }

    @Override
    public List<Rate> getCurrentYearAcqRate() {
        Query query = entityManager.createQuery("SELECT r FROM Rate r Where r.rateType=:rateType And r.currentYear=:currentYear");
        query.setParameter("rateType", "Acquisition");
        query.setParameter("currentYear", "Yes");

        return query.getResultList();
    }

    @Override
    public List<Rate> getCurrentYearAttRate() {
        Query query = entityManager.createQuery("SELECT r FROM Rate r Where r.rateType=:rateType And r.currentYear=:currentYear");
        query.setParameter("rateType", "Attrition");
        query.setParameter("currentYear", "Yes");

        return query.getResultList();
    }

    @Override
    public void monthlyDashboardRate() {

        DecimalFormat df = new DecimalFormat("#.0000000");

        Integer acqUpdateYear = 0;
        Integer attUpdateYear = 0;

        Rate acqRate = getAcqRate();
        Integer acqUpdateMonth = acqRate.getUpdateMonth();
        acqUpdateYear = acqRate.getUpdateYear();
        acqRate.setRateStatus("Done");

        if (acqUpdateMonth == 12) {
            acqUpdateYear = acqRate.getUpdateYear() + 1;
            acqUpdateMonth = 0;
            List<Rate> acqRates = getCurrentYearAcqRate();

            for (int i = 0; i < acqRates.size(); i++) {
                acqRates.get(i).setCurrentYear("No");
            }
        }

        Rate attRate = getAttRate();
        Integer attUpdateMonth = attRate.getUpdateMonth();
        attUpdateYear = attRate.getUpdateYear();
        attRate.setRateStatus("Done");

        NumOfExistingCustomer numOfExistingCustomer = numOfExistingCustomerSessionBeanLocal.getNumOfExistingCustomer();
        numOfExistingCustomer.setStatus("Done");

        if (attUpdateMonth == 12) {
            attUpdateYear = attRate.getUpdateYear() + 1;
            attUpdateMonth = 0;
            List<Rate> attRates = getCurrentYearAttRate();
            List<NumOfExistingCustomer> numOfExistingCustomers = numOfExistingCustomerSessionBeanLocal.getCurrentYearNumOfExistingCustomer();

            for (int i = 0; i < attRates.size(); i++) {
                attRates.get(i).setCurrentYear("No");
                numOfExistingCustomers.get(i).setCurrentYear("No");
            }
        }

        Calendar cal = Calendar.getInstance();
        Long endTime = cal.getTimeInMillis();
        Long startTime = endTime - 300000;

        Integer currentAcqUpdateMonth = acqUpdateMonth + 1;
        Integer currentAttUpdateMonth = attUpdateMonth + 1;

        NumOfExistingCustomer numOfCustomer = new NumOfExistingCustomer();
        if (acqUpdateMonth == 0) {
            Integer updateYear = acqUpdateYear - 1;
            numOfCustomer = numOfExistingCustomerSessionBeanLocal.retrievePreviousNumOfExistingCustomerByMonth(12, updateYear);
        } else {
            numOfCustomer = numOfExistingCustomerSessionBeanLocal.retrieveNumOfExistingCustomerByMonth(currentAcqUpdateMonth - 1);
        }
        String numberOfCustomer = numOfCustomer.getNumOfExistingCustomer();

        Query queryOpenAccount = entityManager.createQuery("SELECT d FROM DepositAccountOpen d WHERE d.currentTimeMilis >= :startTime And d.currentTimeMilis<=:endTime");
        queryOpenAccount.setParameter("startTime", startTime);
        queryOpenAccount.setParameter("endTime", endTime);
        List<DepositAccountOpen> depositAccountOpens = queryOpenAccount.getResultList();

        Query queryCloseActiveAccount = entityManager.createQuery("SELECT d FROM DepositAccountClosure d WHERE d.currentTimeMilis >= :startTime And d.currentTimeMilis<=:endTime And d.accountStatus=:accountStatus");
        queryCloseActiveAccount.setParameter("startTime", startTime);
        queryCloseActiveAccount.setParameter("endTime", endTime);
        queryCloseActiveAccount.setParameter("accountStatus", "Active");
        List<DepositAccountClosure> depositActiveAccountClosures = queryCloseActiveAccount.getResultList();

        Query queryCloseInactiveAccount = entityManager.createQuery("SELECT d FROM DepositAccountClosure d WHERE d.currentTimeMilis >= :startTime And d.currentTimeMilis<=:endTime And d.accountStatus=:accountStatus");
        queryCloseInactiveAccount.setParameter("startTime", startTime);
        queryCloseInactiveAccount.setParameter("endTime", endTime);
        queryCloseInactiveAccount.setParameter("accountStatus", "Inactive");
        List<DepositAccountClosure> depositInactiveAccountClosures = queryCloseInactiveAccount.getResultList();

        Integer numberOfOpenAccounts = depositAccountOpens.size();
        Integer numberOfCloseActiveAccounts = depositActiveAccountClosures.size();
        Integer numberOfCloseInactiveAccounts = depositInactiveAccountClosures.size();

        if (depositAccountOpens.isEmpty()) {
            numberOfOpenAccounts = 0;
        }

        if (depositActiveAccountClosures.isEmpty()) {
            numberOfCloseActiveAccounts = 0;
        }

        Integer newNumOfExistingCustomer = Integer.valueOf(numberOfCustomer) + numberOfOpenAccounts - numberOfCloseActiveAccounts;

        String acqRateValue = df.format(depositAccountOpens.size() / Double.valueOf(numberOfCustomer));
        String attRateValue = df.format(depositActiveAccountClosures.size() / Double.valueOf(numberOfCustomer));

        Long newAcqRateId = addNewRate(Double.valueOf(acqRateValue), "Acquisition",
                "New", acqUpdateYear, currentAcqUpdateMonth, "Yes");
        Long newAttRateId = addNewRate(Double.valueOf(attRateValue), "Attrition",
                "New", attUpdateYear, currentAttUpdateMonth, "Yes");

        Integer numOfOpeningAccounts = depositAccountOpens.size();
        Integer numOfClosingAccounts = depositActiveAccountClosures.size();

        Long newNumOfExistingCustomerId = numOfExistingCustomerSessionBeanLocal.addNewNumOfExistingCustomer(
                newNumOfExistingCustomer.toString(), numOfOpeningAccounts.toString(),
                numOfClosingAccounts.toString(), numberOfCloseInactiveAccounts.toString(),
                currentAcqUpdateMonth, attUpdateYear, "New", "Yes");
    }

    @Override
    public void generateMonthlyAccountClosureReason() {

        DecimalFormat df = new DecimalFormat("#.000000");

        AccountClosureReason interestAccountClosureReason = accountClosureReasonSessionBeanLocal.getNewInterestAccountClosureReason();
        AccountClosureReason serviceChargeAccountClosureReason = accountClosureReasonSessionBeanLocal.getNewServiceChargeAccountClosureReason();
        AccountClosureReason customerServiceAccountClosureReason = accountClosureReasonSessionBeanLocal.getNewCustomerServiceAccountClosureReason();
        AccountClosureReason dontNeedAccountClosureReason = accountClosureReasonSessionBeanLocal.getNewDontNeedAccountClosureReason();
        AccountClosureReason appliedAnotherAccountClosureReason = accountClosureReasonSessionBeanLocal.getNewAppliedAnotherAccountClosureReason();
        AccountClosureReason otherReasonsAccountClosureReason = accountClosureReasonSessionBeanLocal.getNewOtherReasonsAccountClosureReason();

        Integer updateMonth = interestAccountClosureReason.getUpdateMonth();
        Integer updateYear = interestAccountClosureReason.getUpdateYear();
        interestAccountClosureReason.setAccountClosureReasonStatus("Done");
        serviceChargeAccountClosureReason.setAccountClosureReasonStatus("Done");
        customerServiceAccountClosureReason.setAccountClosureReasonStatus("Done");
        dontNeedAccountClosureReason.setAccountClosureReasonStatus("Done");
        appliedAnotherAccountClosureReason.setAccountClosureReasonStatus("Done");
        otherReasonsAccountClosureReason.setAccountClosureReasonStatus("Done");

        if (updateMonth == 12) {
            updateYear = interestAccountClosureReason.getUpdateYear() + 1;
            updateMonth = 0;
            List<AccountClosureReason> accountClosureReasons = accountClosureReasonSessionBeanLocal.getCurrentYearAccountClosureReason();

            for (int i = 0; i < accountClosureReasons.size(); i++) {
                accountClosureReasons.get(i).setCurrentYear("No");
            }
        }

        Calendar cal = Calendar.getInstance();
        Long endTime = cal.getTimeInMillis();
        Long startTime = endTime - 300000;

        Query queryCloseAccount = entityManager.createQuery("SELECT d FROM DepositAccountClosure d WHERE d.currentTimeMilis >= :startTime And d.currentTimeMilis<=:endTime");
        queryCloseAccount.setParameter("startTime", startTime);
        queryCloseAccount.setParameter("endTime", endTime);
        List<DepositAccountClosure> depositAccountClosures = queryCloseAccount.getResultList();

        Integer numOfCustomerCloseAccount = depositAccountClosures.size();
        Integer countInterest = 0;
        Integer countServiceCharge = 0;
        Integer countCustomerService = 0;
        Integer countDontNeed = 0;
        Integer countAppliedAnother = 0;
        Integer countOtherReasons = 0;

        for (int i = 0; i < depositAccountClosures.size(); i++) {
            String eachAccountClosureReason = depositAccountClosures.get(i).getAccountClosureReason();

            if (eachAccountClosureReason.equals("Interest")) {
                countInterest++;
            } else if (eachAccountClosureReason.equals("Service Charge")) {
                countServiceCharge++;
            } else if (eachAccountClosureReason.equals("Customer Service")) {
                countCustomerService++;
            } else if (eachAccountClosureReason.equals("Dont Need")) {
                countDontNeed++;
            } else if (eachAccountClosureReason.equals("Applied Another")) {
                countAppliedAnother++;
            } else if (eachAccountClosureReason.equals("Other Reasons")) {
                countOtherReasons++;
            }
        }

        String rateInterest = "0";
        String rateServiceCharge = "0";
        String rateCustomerService = "0";
        String rateDontNeed = "0";
        String rateAppliedAnother = "0";
        String rateOtherReasons = "0";

        if (numOfCustomerCloseAccount != 0) {
            rateInterest = df.format(countInterest / numOfCustomerCloseAccount);
            rateServiceCharge = df.format(countServiceCharge / numOfCustomerCloseAccount);
            rateCustomerService = df.format(countCustomerService / numOfCustomerCloseAccount);
            rateDontNeed = df.format(countDontNeed / numOfCustomerCloseAccount);
            rateAppliedAnother = df.format(countAppliedAnother / numOfCustomerCloseAccount);
            rateOtherReasons = df.format(countOtherReasons / numOfCustomerCloseAccount);
        }

        Integer currentUpdateMonth = updateMonth + 1;

        Long reasonInterestId = accountClosureReasonSessionBeanLocal.addNewAccountClosureReason(rateInterest,
                "Interest", currentUpdateMonth, updateYear, "New", "Yes");
        Long reasonServiceChargeId = accountClosureReasonSessionBeanLocal.addNewAccountClosureReason(rateServiceCharge,
                "Service Charge", currentUpdateMonth, updateYear, "New", "Yes");
        Long reasonCustomerService = accountClosureReasonSessionBeanLocal.addNewAccountClosureReason(rateCustomerService,
                "Customer Service", currentUpdateMonth, updateYear, "New", "Yes");
        Long reasonDontNeed = accountClosureReasonSessionBeanLocal.addNewAccountClosureReason(rateDontNeed,
                "Dont Need", currentUpdateMonth, updateYear, "New", "Yes");
        Long reasonAppliedAnother = accountClosureReasonSessionBeanLocal.addNewAccountClosureReason(rateAppliedAnother,
                "Applied Another", currentUpdateMonth, updateYear, "New", "Yes");
        Long reasonOtherReasons = accountClosureReasonSessionBeanLocal.addNewAccountClosureReason(rateOtherReasons,
                "Other Reasons", currentUpdateMonth, updateYear, "New", "Yes");
    }
}
