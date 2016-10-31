package ejb.bi.session;

import ejb.bi.entity.Rate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RateSessionBean implements RateSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewRate(Double rateValue, int updateDate, String rateType,
            String openAccountNumOfPeople, String closeAccountNumOfPeople) {

        Rate rate = new Rate();

        rate.setRateType(rateType);
        rate.setRateValue(rateValue);
        rate.setUpdateDate(updateDate);
        rate.setOpenAccountNumOfPeople(openAccountNumOfPeople);
        rate.setCloseAccountNumOfPeople(closeAccountNumOfPeople);

        entityManager.persist(rate);
        entityManager.flush();

        return rate.getRateId();
    }

    @Override
    public Rate retrieveAcquisitionRateByDate(int updateDate) {
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
    public Rate retrieveAttritionRateByDate(int updateDate) {
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
}
