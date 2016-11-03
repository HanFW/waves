package ejb.bi.session;

import ejb.bi.entity.DepositAccountClosure;
import ejb.bi.entity.NumOfExistingCustomer;
import ejb.bi.entity.Rate;
import ejb.deposit.entity.BankAccount;
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
    private NumOfExistingCustomerSessionBeanLocal numOfExistingCustomerSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewRate(Double rateValue, Integer updateDate, String rateType,
            String rateStatus) {

        Rate rate = new Rate();

        rate.setRateType(rateType);
        rate.setRateValue(rateValue);
        rate.setUpdateDate(updateDate);
        rate.setRateStatus(rateStatus);

        entityManager.persist(rate);
        entityManager.flush();

        return rate.getRateId();
    }

    @Override
    public Rate retrieveAcquisitionRateByDate(Integer updateDate) {
        Rate rate = new Rate();

        try {
            Query query = entityManager.createQuery("Select r From Rate r Where r.updateDate=:updateDate And r.rateType=:rateType");
            query.setParameter("updateDate", updateDate);
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
    public Rate retrieveAttritionRateByDate(Integer updateDate) {
        Rate rate = new Rate();

        try {
            Query query = entityManager.createQuery("Select r From Rate r Where r.updateDate=:updateDate And r.rateType=:rateType");
            query.setParameter("updateDate", updateDate);
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
    public Rate retrieveAcquisitionRateByDate(Long rateId) {
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
    public void monthlyDashboardRate() {

        DecimalFormat df = new DecimalFormat("#.000000");

        Rate acqRate = getAcqRate();
        Integer acqUpdateDate = acqRate.getUpdateDate();
        acqRate.setRateStatus("Done");

        Rate attRate = getAttRate();
        Integer attUpdateDate = attRate.getUpdateDate();
        attRate.setRateStatus("Done");

        Calendar cal = Calendar.getInstance();
        Long endTime = cal.getTimeInMillis();
        Long startTime = endTime - 100010;

        Query queryOpenAccount = entityManager.createQuery("SELECT b FROM BankAccount b WHERE b.currentTimeMilis >= :startTime And b.currentTimeMilis<=:endTime And b.bankAccountStatus=:bankAccountStatus");
        queryOpenAccount.setParameter("startTime", startTime);
        queryOpenAccount.setParameter("endTime", endTime);
        queryOpenAccount.setParameter("bankAccountStatus", "Active");
        List<BankAccount> bankAccounts = queryOpenAccount.getResultList();

        Query queryCloseAccount = entityManager.createQuery("SELECT d FROM DepositAccountClosure d WHERE d.currentTimeMilis >= :startTime And d.currentTimeMilis<=:endTime");
        queryCloseAccount.setParameter("startTime", startTime);
        queryCloseAccount.setParameter("endTime", endTime);
        List<DepositAccountClosure> depositAccountClosures = queryCloseAccount.getResultList();

        NumOfExistingCustomer numOfCustomer = numOfExistingCustomerSessionBeanLocal.retrieveNumOfExistingCustomerByDate(acqUpdateDate);
        String numberOfCustomer = numOfCustomer.getNumOfExistingCustomer();

        Integer newNumOfExistingCustomer = Integer.valueOf(numberOfCustomer) + bankAccounts.size() - depositAccountClosures.size();
        Integer currentAcqUpdateDate = acqUpdateDate + 1;
        Integer currentAttUpdateDate = attUpdateDate + 1;

        String acqRateValue = df.format(bankAccounts.size() / Double.valueOf(numberOfCustomer));
        String attRateValue = df.format(depositAccountClosures.size() / Double.valueOf(numberOfCustomer));

        Long newAcqRateId = addNewRate(Double.valueOf(acqRateValue), currentAcqUpdateDate, "Acquisition",
                "New");
        Long newAttRateId = addNewRate(Double.valueOf(attRateValue), currentAttUpdateDate, "Attrition",
                "New");
        Long newNumOfExistingCustomerId = numOfExistingCustomerSessionBeanLocal.addNewNumOfExistingCustomer(
                newNumOfExistingCustomer.toString(), currentAcqUpdateDate);

    }
}
