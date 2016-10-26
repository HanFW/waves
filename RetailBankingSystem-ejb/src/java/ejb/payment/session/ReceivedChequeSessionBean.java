package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.ReceivedCheque;
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
public class ReceivedChequeSessionBean implements ReceivedChequeSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewReceivedCheque(String transactionDate, String transactionAmt,
            String receivedBankAccountNum, String receivedCustomerName, String receivedCustomerMobile,
            String receivedChequeStatus, Long customerBasicId) {

        ReceivedCheque receivedCheque = new ReceivedCheque();

        receivedCheque.setReceivedBankAccountNum(receivedBankAccountNum);
        receivedCheque.setReceivedChequeStatus(receivedChequeStatus);
        receivedCheque.setReceivedCustomerMobile(receivedCustomerMobile);
        receivedCheque.setReceivedCustomerName(receivedCustomerName);
        receivedCheque.setTransactionAmt(transactionAmt);
        receivedCheque.setTransactionDate(transactionDate);
        receivedCheque.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(receivedCheque);
        entityManager.flush();

        return receivedCheque.getChequeId();
    }

    @Override
    public List<ReceivedCheque> getAllNewReceivedCheque() {

        Query query = entityManager.createQuery("SELECT r FROM ReceivedCheque r Where r.receivedChequeStatus=:receivedChequeStatus");
        query.setParameter("receivedChequeStatus", "New");

        return query.getResultList();
    }

    @Override
    public ReceivedCheque retrieveReceivedChequeById(Long chequeId) {
        ReceivedCheque receivedCheque = new ReceivedCheque();

        try {
            Query query = entityManager.createQuery("Select r From ReceivedCheque r Where r.chequeId=:chequeId");
            query.setParameter("chequeId", chequeId);

            if (query.getResultList().isEmpty()) {
                return new ReceivedCheque();
            } else {
                receivedCheque = (ReceivedCheque) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ReceivedCheque();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return receivedCheque;
    }

    @Override
    public List<ReceivedCheque> retrieveReceivedChequeByCusId(String customerIdentificationNum) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<ReceivedCheque>();
        }
        try {
            Query query = entityManager.createQuery("Select r From ReceivedCheque r Where r.customerBasic=:customerBasic");
            query.setParameter("customerBasic", customerBasic);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<ReceivedCheque>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<ReceivedCheque>();
        }
    }

    @Override
    public void updateReceivedChequeStatus(Long chequeId) {
        ReceivedCheque receivedCheque = retrieveReceivedChequeById(chequeId);
        receivedCheque.setReceivedChequeStatus("Approved");
    }
}
