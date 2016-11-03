package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.IssuedCheque;
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
public class IssuedChequeSessionBean implements IssuedChequeSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewIssuedCheque(String transactionDate, String transactionAmt, String chequeNum,
            String issuedChequeStatus, String chequeType, Long customerBasicId) {

        IssuedCheque issuedCheque = new IssuedCheque();

        issuedCheque.setChequeNum(chequeNum);
        issuedCheque.setIssuedChequeStatus(issuedChequeStatus);
        issuedCheque.setTransactionAmt(transactionAmt);
        issuedCheque.setTransactionDate(transactionDate);
        issuedCheque.setChequeType(chequeType);
        issuedCheque.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(issuedCheque);
        entityManager.flush();

        return issuedCheque.getChequeId();
    }

    @Override
    public IssuedCheque retrieveIssuedChequeById(Long chequeId) {
        IssuedCheque issuedCheque = new IssuedCheque();

        try {
            Query query = entityManager.createQuery("Select i From IssuedCheque i Where i.chequeId=:chequeId And i.chequeType=:chequeType");
            query.setParameter("chequeId", chequeId);
            query.setParameter("chequeType", "Issued");

            if (query.getResultList().isEmpty()) {
                return new IssuedCheque();
            } else {
                issuedCheque = (IssuedCheque) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new IssuedCheque();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return issuedCheque;
    }

    @Override
    public IssuedCheque retrieveIssuedChequeByNum(String chequeNum) {
        IssuedCheque issuedCheque = new IssuedCheque();

        try {
            Query query = entityManager.createQuery("Select i From IssuedCheque i Where i.chequeNum=:chequeNum And i.chequeType=:chequeType");
            query.setParameter("chequeNum", chequeNum);
            query.setParameter("chequeType", "Issued");

            if (query.getResultList().isEmpty()) {
                return new IssuedCheque();
            } else {
                issuedCheque = (IssuedCheque) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new IssuedCheque();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return issuedCheque;
    }

    @Override
    public List<IssuedCheque> retrieveIssuedChequeByCusIC(String customerIdentificationNum) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<IssuedCheque>();
        }
        try {
            Query query = entityManager.createQuery("Select i From IssuedCheque i Where i.customerBasic=:customerBasic And i.chequeType=:chequeType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("chequeType", "Issued");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<IssuedCheque>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<IssuedCheque>();
        }
    }
    
    @Override
    public void updateIssuedChequeStatus(String chequeNum) {
        IssuedCheque issuedCheque = retrieveIssuedChequeByNum(chequeNum);
        issuedCheque.setIssuedChequeStatus("Approved");
    }
}
