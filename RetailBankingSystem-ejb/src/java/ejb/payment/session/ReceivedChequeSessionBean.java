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
            String receivedChequeStatus, String chequeNum, String chequeType, 
            String otherBankAccountNum, Long customerBasicId) {

        ReceivedCheque receivedCheque = new ReceivedCheque();

        receivedCheque.setReceivedBankAccountNum(receivedBankAccountNum);
        receivedCheque.setReceivedChequeStatus(receivedChequeStatus);
        receivedCheque.setReceivedCustomerMobile(receivedCustomerMobile);
        receivedCheque.setReceivedCustomerName(receivedCustomerName);
        receivedCheque.setTransactionAmt(transactionAmt);
        receivedCheque.setTransactionDate(transactionDate);
        receivedCheque.setChequeNum(chequeNum);
        receivedCheque.setChequeType(chequeType);
        receivedCheque.setOtherBankAccountNum(otherBankAccountNum);
        receivedCheque.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(receivedCheque);
        entityManager.flush();

        return receivedCheque.getChequeId();
    }

    @Override
    public List<ReceivedCheque> getAllNewReceivedCheque() {

        Query query = entityManager.createQuery("SELECT r FROM ReceivedCheque r Where r.receivedChequeStatus=:receivedChequeStatus And r.chequeType=:chequeType");
        query.setParameter("receivedChequeStatus", "New");
        query.setParameter("chequeType", "Received");

        return query.getResultList();
    }

    @Override
    public ReceivedCheque retrieveReceivedChequeById(Long chequeId) {
        ReceivedCheque receivedCheque = new ReceivedCheque();

        try {
            Query query = entityManager.createQuery("Select r From ReceivedCheque r Where r.chequeId=:chequeId And r.chequeType=:chequeType");
            query.setParameter("chequeId", chequeId);
            query.setParameter("chequeType", "Received");

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
    public ReceivedCheque retrieveReceivedChequeByNum(String chequeNum) {
        ReceivedCheque receivedCheque = new ReceivedCheque();

        try {
            Query query = entityManager.createQuery("Select r From ReceivedCheque r Where r.chequeNum=:chequeNum And r.chequeType=:chequeType");
            query.setParameter("chequeNum", chequeNum);
            query.setParameter("chequeType", "Received");

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
    public List<ReceivedCheque> retrieveReceivedChequeByCusIC(String customerIdentificationNum) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<ReceivedCheque>();
        }
        try {
            Query query = entityManager.createQuery("Select r From ReceivedCheque r Where r.customerBasic=:customerBasic And r.chequeType=:chequeType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("chequeType", "Received");

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
    public void updateReceivedChequeStatus(String chequeNum) {
        ReceivedCheque receivedCheque = retrieveReceivedChequeByNum(chequeNum);
        receivedCheque.setReceivedChequeStatus("Approved");
    }
}
