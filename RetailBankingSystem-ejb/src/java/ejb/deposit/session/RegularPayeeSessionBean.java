package ejb.deposit.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import ejb.deposit.entity.RegularPayee;
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

public class RegularPayeeSessionBean implements RegularPayeeSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewRegularPayee(String payeeName, String payeeAccountNum, String payeeAccountType,
            String lastTransactionDate, String payeeType, Long customerBasicId) {

        RegularPayee regularPayee = new RegularPayee();

        regularPayee.setPayeeAccountNum(payeeAccountNum);
        regularPayee.setPayeeAccountType(payeeAccountType);
        regularPayee.setLastTransactionDate(lastTransactionDate);
        regularPayee.setPayeeType(payeeType);
        regularPayee.setPayeeName(payeeName);
        regularPayee.setCustomerBasic(bankAccountSessionLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(regularPayee);
        entityManager.flush();

        return regularPayee.getPayeeId();
    }

    @Override
    public List<RegularPayee> retrieveRegularPayeeByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<RegularPayee>();
        }
        try {
            Query query = entityManager.createQuery("Select r From RegularPayee r Where r.customerBasic=:customerBasic And r.payeeType=:payeeType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("payeeType", "Regular");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<RegularPayee>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<RegularPayee>();
        }
    }
    
    @Override
    public Payee retrieveRegularPayeeByName(String payeeName) {
        System.out.println("*");
        System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByName() ******");
        Payee payee = new Payee();

        try {
            Query query = entityManager.createQuery("Select r From RegularPayee r Where r.payeeName=:payeeName");
            query.setParameter("payeeName", payeeName);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByName(): invalid payee name: no result found, return new payee");
                return new Payee();
            } else {
                System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByName(): valid payee name: return payee");
                payee = (Payee) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByName(): Entity not found error: " + enfe.getMessage());
            return new Payee();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/PayeeSessionBean: retrievePayeeByName(): Non unique result error: " + nure.getMessage());
        }

        return payee;
    }
}
