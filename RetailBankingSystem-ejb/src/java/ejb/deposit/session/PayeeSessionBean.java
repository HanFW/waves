package ejb.deposit.session;

import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.Payee;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import javax.ejb.LocalBean;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@Stateless
@LocalBean
public class PayeeSessionBean implements PayeeSessionBeanLocal {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewPayee(String payeeName, String payeeAccountNum, String payeeAccountType,
            String lastTransactionDate, Long customerBasicId) {
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: addNewPayee() ******");
        Payee payee = new Payee();

        payee.setPayeeName(payeeName);
        payee.setPayeeAccountNum(payeeAccountNum);
        payee.setPayeeAccountType(payeeAccountType);
        payee.setLastTransactionDate(lastTransactionDate);
        payee.setCustomerBasic(bankAccountSessionLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(payee);
        entityManager.flush();

        return payee.getPayeeId();
    }

    @Override
    public String deletePayee(String payeeAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: deletePayee() ******");
        Payee payee = retrievePayeeByNum(payeeAccountNum);

        entityManager.remove(payee);
        entityManager.flush();

        return "Successfully deleted!";
    }

    @Override
    public Payee retrievePayeeById(Long payeeId) {
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: retrievePayeeById() ******");
        Payee payee = new Payee();

        try {
            Query query = entityManager.createQuery("Select p From Payee p Where p.payeeId=:payeeId");
            query.setParameter("payeeId", payeeId);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/InterestSessionBean: retrievePayeeById(): invalid payee Id: no result found, return new payee");
                return new Payee();
            } else {
                System.out.println("****** deposit/InterestSessionBean: retrievePayeeById(): valid payee Id: return payee");
                payee = (Payee) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/InterestSessionBean: retrievePayeeById(): Entity not found error: " + enfe.getMessage());
            return new Payee();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/InterestSessionBean: retrievePayeeById(): Non unique result error: " + nure.getMessage());
        }

        return payee;
    }

    @Override
    public Payee retrievePayeeByName(String payeeName) {
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: retrievePayeeByName() ******");
        Payee payee = new Payee();

        try {
            Query query = entityManager.createQuery("Select p From Payee p Where p.payeeName=:payeeName");
            query.setParameter("payeeName", payeeName);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/InterestSessionBean: retrievePayeeByName(): invalid payee name: no result found, return new payee");
                return new Payee();
            } else {
                System.out.println("****** deposit/InterestSessionBean: retrievePayeeByName(): valid payee name: return payee");
                payee = (Payee) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/InterestSessionBean: retrievePayeeByName(): Entity not found error: " + enfe.getMessage());
            return new Payee();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/InterestSessionBean: retrievePayeeByName(): Non unique result error: " + nure.getMessage());
        }

        return payee;
    }

    @Override
    public Payee retrievePayeeByNum(String payeeAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: retrievePayeeByNum() ******");
        Payee payee = new Payee();

        try {
            Query query = entityManager.createQuery("Select p From Payee p Where p.payeeAccountNum=:payeeAccountNum");
            query.setParameter("payeeAccountNum", payeeAccountNum);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/InterestSessionBean: retrievePayeeByNum(): invalid payee account number: no result found, return new payee");
                return new Payee();
            } else {
                System.out.println("****** deposit/InterestSessionBean: retrievePayeeByNum(): valid payee account number: return payee");
                payee = (Payee) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/InterestSessionBean: retrievePayeeByNum(): Entity not found error: " + enfe.getMessage());
            return new Payee();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/InterestSessionBean: retrievePayeeByNum(): Non unique result error: " + nure.getMessage());
        }

        return payee;
    }

    @Override
    public List<Payee> retrievePayeeByCusId(Long customerBasicId) {
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: retrievePayeeByCusId() ******");
        CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            System.out.println("****** deposit/InterestSessionBean: retrievePayeeByCusId(): invalid customer Id: no result found, return new payee");
            return new ArrayList<Payee>();
        }
        try {
            Query query = entityManager.createQuery("Select p From Payee p Where p.customerBasic=:customerBasic");
            query.setParameter("customerBasic", customerBasic);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/InterestSessionBean: retrievePayeeByCusId(): wrong customer Id: no result found, return new payee");
                return new ArrayList<Payee>();
            } else {
                System.out.println("****** deposit/InterestSessionBean: retrievePayeeByCusId(): correct customer Id: return payee");
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/InterestSessionBean: Entity not found error: " + enfe.getMessage());
            return new ArrayList<Payee>();
        }
    }

    @Override
    public void updateLastTransactionDate(String bankAccountNum) {

        Payee customerPayee = retrievePayeeByNum(bankAccountNum);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        String lastTransactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;

        customerPayee.setLastTransactionDate(lastTransactionDate);
    }
}
