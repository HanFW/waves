package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.FastPayee;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FastPayeeSessionBean implements FastPayeeSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public FastPayee retrieveFastPayeeById(Long payeeId) {
        
        FastPayee fastPayee = new FastPayee();

        try {
            Query query = entityManager.createQuery("Select f From FastPayee f Where f.payeeId=:payeeId");
            query.setParameter("payeeId", payeeId);

            if (query.getResultList().isEmpty()) {
                return new FastPayee();
            } else {
                fastPayee = (FastPayee) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new FastPayee();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return fastPayee;
    }

    @Override
    public List<FastPayee> retrieveFastPayeeByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<FastPayee>();
        }
        try {
            Query query = entityManager.createQuery("Select f From FastPayee f Where f.customerBasic=:customerBasic And f.payeeType=:payeeType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("payeeType", "FAST");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<FastPayee>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<FastPayee>();
        }
    }

    @Override
    public Long addNewFastPayee(String fastPayeeName, String fastPayeeAccountNum, String fasrPayeeAccountType,
            String lastTransactionDate, String payeeType, Long customerBasicId) {
        FastPayee fastPayee = new FastPayee();

        fastPayee.setFastPayeeName(fastPayeeName);
        fastPayee.setLastTransactionDate(lastTransactionDate);
        fastPayee.setPayeeAccountNum(fastPayeeAccountNum);
        fastPayee.setPayeeAccountType(fasrPayeeAccountType);
        fastPayee.setPayeeType(payeeType);
        fastPayee.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(fastPayee);
        entityManager.flush();

        return fastPayee.getPayeeId();
    }

    @Override
    public FastPayee retrieveFastPayeeByNum(String payeeAccountNum) {
        
        FastPayee fastPayee = new FastPayee();

        try {
            Query query = entityManager.createQuery("Select f From FastPayee f Where f.payeeAccountNum=:payeeAccountNum");
            query.setParameter("payeeAccountNum", payeeAccountNum);

            if (query.getResultList().isEmpty()) {
                return new FastPayee();
            } else {
                fastPayee = (FastPayee) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new FastPayee();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return fastPayee;
    }
}
