package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.OtherBankPayee;
import java.util.ArrayList;
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
public class OtherBankPayeeSessionBean implements OtherBankPayeeSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OtherBankPayee retrieveOtherBankPayeeById(Long payeeId) {
        
        OtherBankPayee otherBankPayee = new OtherBankPayee();

        try {
            Query query = entityManager.createQuery("Select o From OtherBankPayee o Where o.payeeId=:payeeId");
            query.setParameter("payeeId", payeeId);

            if (query.getResultList().isEmpty()) {
                return new OtherBankPayee();
            } else {
                otherBankPayee = (OtherBankPayee) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new OtherBankPayee();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return otherBankPayee;
    }

    @Override
    public List<OtherBankPayee> retrieveOtherBankPayeeByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<OtherBankPayee>();
        }
        try {
            Query query = entityManager.createQuery("Select o From OtherBankPayee o Where o.customerBasic=:customerBasic And o.payeeType=:payeeType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("payeeType", "Other Bank");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<OtherBankPayee>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<OtherBankPayee>();
        }
    }

    @Override
    public Long addNewOtherBankPayee(String otherBankPayeeName, String otherBankPayeeAccountNum, 
            String otherBankPayeeAccountType, String lastTransactionDate, String payeeType, 
            Long customerBasicId) {
        OtherBankPayee otherBankPayee = new OtherBankPayee();

        otherBankPayee.setPayeeAccountNum(otherBankPayeeAccountNum);
        otherBankPayee.setPayeeAccountType(otherBankPayeeAccountType);
        otherBankPayee.setPayeeType(payeeType);
        otherBankPayee.setLastTransactionDate(lastTransactionDate);
        otherBankPayee.setOtherBankPayeeName(otherBankPayeeName);
        otherBankPayee.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(otherBankPayee);
        entityManager.flush();

        return otherBankPayee.getPayeeId();
    }

    @Override
    public OtherBankPayee retrieveOtherBankPayeeByNum(String payeeAccountNum) {
        
        OtherBankPayee otherBankPayee = new OtherBankPayee();

        try {
            Query query = entityManager.createQuery("Select o From OtherBankPayee o Where o.payeeAccountNum=:payeeAccountNum");
            query.setParameter("payeeAccountNum", payeeAccountNum);

            if (query.getResultList().isEmpty()) {
                return new OtherBankPayee();
            } else {
                otherBankPayee = (OtherBankPayee) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new OtherBankPayee();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return otherBankPayee;
    }
    
    @Override
    public void updateLastTransactionDate(String payeeAccountNum) {

        OtherBankPayee otherBankPayee = retrieveOtherBankPayeeByNum(payeeAccountNum);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        String lastTransactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;

        otherBankPayee.setLastTransactionDate(lastTransactionDate);
    }
}
